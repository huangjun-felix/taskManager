package com.huangjun.taskmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.huangjun.taskmanager.constants.RedisConstants;
import com.huangjun.taskmanager.constants.ResultConstants;
import com.huangjun.taskmanager.constants.TimeConstants;
import com.huangjun.taskmanager.entity.Result;
import com.huangjun.taskmanager.entity.Task;
import com.huangjun.taskmanager.mapper.TaskMapper;
import com.huangjun.taskmanager.service.TaskService;
import com.huangjun.taskmanager.utils.RedisUtils;
import com.huangjun.taskmanager.utils.ThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result insert(Task task) {
        String uuid = UUID.randomUUID().toString();
        task.setId(uuid);
        task.setUserId(ThreadLocalUtils.getUser().getId());
        task.setCreateTime(LocalDateTime.now());
        task.setUpdateTime(LocalDateTime.now());
        int result = taskMapper.insert(task);
        if (result<0){
            return Result.fail(ResultConstants.TASK_INSERT_ERROR);
        }
        RedisUtils.setHash(RedisConstants.TASK+ ThreadLocalUtils.getUser().getId(),uuid,task);
        return Result.ok(uuid);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(Task task) {
        LambdaUpdateWrapper<Task> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Task::getId,task.getId());
        int result = taskMapper.update(task,updateWrapper);
        if (result<0){
            return Result.fail(ResultConstants.TASK_UPDATE_ERROR);
        }
        RedisUtils.deleteHashField(RedisConstants.TASK+ ThreadLocalUtils.getUser().getId(),task.getId());
        return Result.ok();
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(String id) {
        int delete = taskMapper.deleteById(id);
        if (delete<0){
            return Result.fail(ResultConstants.TASK_DELETE_ERROR);
        }
        RedisUtils.deleteHashField(RedisConstants.TASK+ ThreadLocalUtils.getUser().getId(),id);
        return Result.ok();
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result queryById(String id) {
        Task task = RedisUtils.getFieldHashValue(RedisConstants.TASK + ThreadLocalUtils.getUser().getId(), id, Task.class);
        if (task != null){
            return Result.ok(task);
        }
        Task task2 = taskMapper.selectById(id);
        if (task2 == null){
            Task task1 = new Task();
            RedisUtils.setHash(RedisConstants.TASK+ ThreadLocalUtils.getUser().getId(),id,task1, TimeConstants.TWO_MINUTE);
            return Result.fail(ResultConstants.TASK_NOT_INFO);
        }
        RedisUtils.setHash(RedisConstants.TASK+ ThreadLocalUtils.getUser().getId(),id,task2);
        return Result.ok(task2);
//        boolean b = RedisUtils.tryLock(RedisConstants.TRYLOCK + id, TimeConstants.SEVEN_MINUTE);
//        try {
//            if (b){
//                Task task1 = RedisUtils.getFieldHashValue(RedisConstants.TASK + ThreadLocalUtils.getUser().getId(), id, Task.class);
//                if (task1 != null){
//                    return Result.ok(task1);
//                }
//                Task task2 = taskMapper.selectById(id);
//                if (task2 == null){
//                    return Result.fail(ResultConstants.TASK_NOT_INFO);
//                }
//                RedisUtils.setHash(RedisConstants.TASK+ ThreadLocalUtils.getUser().getId(),id,task2);
//                return Result.ok(task2);
//            }
//        }finally {
//            RedisUtils.unlock(RedisConstants.TRYLOCK + id);
//        }
//        return Result.fail(ResultConstants.TASK_FIND_ERROR);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result findByUserId(String userId) {
        List<Task> taskList = RedisUtils.getHashAllValue(RedisConstants.TASK + (userId == null ? ThreadLocalUtils.getUser().getId() : userId), Task.class);
        if (taskList != null && !taskList.isEmpty()){
            return Result.ok(taskList);
        }
        LambdaQueryWrapper<Task> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Task::getUserId,userId);
        List<Task> tasks = taskMapper.selectList(queryWrapper);
        if (tasks == null || tasks.isEmpty()){
            return Result.fail(ResultConstants.TASK_FIND_ERROR);
        }
//        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
//            @Override
//            public void afterCommit() {
//
//            }
//        });
//        applicationContext.publishEvent(new TaskUpdateEvent(tasks));
        for (Task task : tasks){
            RedisUtils.setHash(RedisConstants.TASK+ ThreadLocalUtils.getUser().getId(),task.getId(),task);
        }
        return Result.ok(tasks);
    }

}
