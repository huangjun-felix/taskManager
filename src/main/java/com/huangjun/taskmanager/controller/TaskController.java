package com.huangjun.taskmanager.controller;

import com.huangjun.taskmanager.entity.Result;
import com.huangjun.taskmanager.entity.Task;
import com.huangjun.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @PostMapping("/insert")
    public Result insert(@RequestBody Task task){
        return taskService.insert(task);
    }
    @PostMapping("/update")
    public Result update(@RequestBody Task task){
        return taskService.update(task);
    }
    @PostMapping("/delete")
    public Result delete(@RequestParam("id") String id){
        return taskService.delete(id);
    }
    @PostMapping("/queryById")
    public Result queryById(@RequestParam("id") String id){
        return taskService.queryById(id);
    }
    @PostMapping("/findByUserId")
    public Result findAll(@RequestParam("userId") String userId){
        return taskService.findByUserId(userId);
    }
}
