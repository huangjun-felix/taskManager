package com.huangjun.taskmanager.utils;

import com.huangjun.taskmanager.entity.User;

public class ThreadLocalUtils {

    private static final ThreadLocal<User> local = ThreadLocal.withInitial(()->null);

    public static User getUser(){
        return local.get();
    }
    public static void setUser(User user){
        local.set(user);
    }
    public static void remove(){
        local.remove();
    }
}
