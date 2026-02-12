package com.huangjun.taskmanager.constants;

public class RedisConstants {

    public static final String USER_LOGIN = "user:login:";
    public static final String TASK = "task:";
    public static final String INCR_KEY = "incr:id:";
    public static final String UNLOCK_LUA = "if redis.call('get',key[1]) == ARGV[1] " +
            "then " +
            "return redis.call('del',key[1]) " +
            "else " +
            "return 0 " +
            "end";
    public static final String TRYLOCK = "lock:id:";
}
