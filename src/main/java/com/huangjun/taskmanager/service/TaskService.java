package com.huangjun.taskmanager.service;

import com.huangjun.taskmanager.entity.Result;
import com.huangjun.taskmanager.entity.Task;

public interface TaskService {
    Result insert(Task task);
    Result update(Task task);
    Result delete(String id);
    Result queryById(String id);
    Result findByUserId(String userId);
}
