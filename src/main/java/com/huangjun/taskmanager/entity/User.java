package com.huangjun.taskmanager.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_user")
public class User {
//    @NotBlank(message = "ID不能为空",groups = {LoginGroup.class})
    private String id;
    @NotBlank(message = "用户名不能为空",groups = {LoginGroup.class,RegisterGroup.class})
    private String username;
    @NotBlank(message = "密码不能为空",groups = {LoginGroup.class,RegisterGroup.class})
    private String password;
    private LocalDateTime createTime;

    public interface LoginGroup {}
    public interface RegisterGroup {}
}
