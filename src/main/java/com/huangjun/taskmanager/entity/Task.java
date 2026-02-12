package com.huangjun.taskmanager.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_task")
public class Task {
    private String id;
    private String userId;
    private String title;
    private String description;
    private int status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
