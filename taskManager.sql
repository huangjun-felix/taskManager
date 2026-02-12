create database taskmanager;

use taskmanager;

CREATE TABLE if not exists t_user (
                                      id varchar(100)  PRIMARY KEY COMMENT '主键',
                                      username VARCHAR(255) NOT NULL UNIQUE COMMENT '用户名（唯一）',
                                      password VARCHAR(255) NOT NULL COMMENT '密码（明文即可，不强制加密）',
                                      create_time DATETIME not null  COMMENT '创建时间'
);

CREATE TABLE if not exists t_task (
                                      id varchar(100)  PRIMARY KEY COMMENT '主键',
                                      user_id varchar(100) NOT NULL COMMENT '所属用户',
                                      title VARCHAR(255) NOT NULL COMMENT '任务标题',
                                      description TEXT COMMENT '任务描述',
                                      status int DEFAULT 0 COMMENT '任务状态（0-待办，1-完成）',
                                      create_time DATETIME not null COMMENT '创建时间',
                                      update_time DATETIME not null COMMENT '更新时间'
);