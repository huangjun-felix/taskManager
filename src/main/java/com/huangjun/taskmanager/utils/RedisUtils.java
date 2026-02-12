package com.huangjun.taskmanager.utils;

import com.huangjun.taskmanager.constants.RedisConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.lang.String;

@Component
public class RedisUtils {

    private RedisUtils(){}

    private static final long BEGIN_TIMESTAMP = 1640995200L;
    private static final long COUNT_BITS = 32;
    private static RedisTemplate<String, String> redisTemplate;
    private static final ThreadLocal<String> threadLocal = ThreadLocal.withInitial(() -> null);
    @Autowired
    private void setRedisTemplate(RedisTemplate<String, String> template) {
        redisTemplate = template;
    }
    public static void set(String key, Object value, Long expireTime) {
        if (Objects.isNull(key)) {
            throw new RuntimeException("没有传递对应参数");
        }
        String json = StringUtils.toJson(value);
        redisTemplate.opsForValue().set(key, json, expireTime, TimeUnit.MILLISECONDS);
    }
    public static void set(String key, Object value) {
        set(key, value, -1L);
    }
    public static String get(String key) {
        String s = redisTemplate.opsForValue().get(key);
        if (s != null && s.isEmpty()) {
            return null;
        }
        return s;
    }
    public static <T> T get(String key ,Class<T> clazz) {
        String object = redisTemplate.opsForValue().get(key);
        return StringUtils.fromJson(object, clazz);
    }
    public static void delete(String key) {
        redisTemplate.delete(key);
    }
    public static void expire(String key,Long expireTime){
        redisTemplate.expire(key,expireTime,TimeUnit.MILLISECONDS);
    }
    public static void setHash(String key,String field,Object value){
        String json = StringUtils.toJson(value);
        redisTemplate.opsForHash().put(key,field,json);
    }
    public static void setHash(String key,String field,Object value,Long expireTime){
        setHash(key, field, value);
        expire(key, expireTime);
    }
    public static void deleteHashField(String key, String field){
        redisTemplate.opsForHash().delete(key,field);
    }
    public static <T> T getFieldHashValue(String key,String field,Class<T> clazz){
        Object o = redisTemplate.opsForHash().get(key, field);
        return StringUtils.fromJson((String) o, clazz);
    }
    public static <T> List<T> getHashAllValue(String key, Class<T> clazz) {
        List<Object> values = redisTemplate.opsForHash().values(key);
        if (values.isEmpty()) {
            return null;
        }
        List<String> hashJsons = values.stream()
                .map(obj->obj!=null ? obj.toString():null)
                .toList();
        return StringUtils.fromListJson(hashJsons,clazz);
    }
    public static boolean tryLock(String key,Long expireTime){
        String uuid = String.valueOf(nextId(RedisConstants.TRYLOCK+key));
        Boolean b = redisTemplate.opsForValue()
                .setIfAbsent(key, uuid, expireTime, TimeUnit.MILLISECONDS);
        if (Boolean.FALSE.equals(b)) {
            return false;
        }
        threadLocal.set(uuid);
        return true;
    }
    public static void unlock(String key){
        String uuid = threadLocal.get();
        if (uuid==null) {
            threadLocal.remove();
            return;
        }
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(RedisConstants.UNLOCK_LUA);
        redisScript.setResultType(Long.class);
        redisTemplate.execute(redisScript, Collections.singletonList(RedisConstants.TRYLOCK+key), uuid);
        threadLocal.remove();
    }
    public static long nextId(String key){
        LocalDateTime time=LocalDateTime.now();
        long second = time.toEpochSecond(ZoneOffset.UTC) - BEGIN_TIMESTAMP;
        String date = time.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        Long increment = redisTemplate.opsForValue().increment(RedisConstants.INCR_KEY + key + date);
        long timeTemp = 0;
        if (increment != null) {
            timeTemp = increment;
        }
        return second << COUNT_BITS | timeTemp;
    }
}
