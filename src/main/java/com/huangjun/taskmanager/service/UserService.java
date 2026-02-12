package com.huangjun.taskmanager.service;

import com.huangjun.taskmanager.entity.Result;
import com.huangjun.taskmanager.entity.User;

public interface UserService {
    Result registry(User user);
    Result login(User user);
}
