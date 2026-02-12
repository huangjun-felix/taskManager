package com.huangjun.taskmanager.tranctionalEvent;

import com.huangjun.taskmanager.entity.Task;
import lombok.Data;

import java.util.List;

@Data
public class TaskUpdateEvent {
    private List<Task> taskList;

    public TaskUpdateEvent(List<Task> tasks) {
        this.taskList = tasks;
    }

}
