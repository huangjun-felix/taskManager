package com.huangjun.taskmanager.config;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {

//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        redisTemplate.setKeySerializer(RedisSerializer.string());
//        redisTemplate.setHashKeySerializer(RedisSerializer.string());
//        Jackson2JsonRedisSerializer<String> redisSerializer = new Jackson2JsonRedisSerializer<>(String.class);
//        redisTemplate.setValueSerializer(redisSerializer);
//        redisTemplate.setHashValueSerializer(redisSerializer);
//        redisTemplate.afterPropertiesSet();
//        return redisTemplate;
//    }

}
