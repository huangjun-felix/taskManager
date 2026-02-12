package com.huangjun.taskmanager.controller;

import com.huangjun.taskmanager.entity.Result;
import com.huangjun.taskmanager.entity.User;
import com.huangjun.taskmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody @Validated(User.LoginGroup.class)User user) {
        return userService.login(user);
    }

    @PostMapping("/registry")
    public Result registry(@RequestBody @Validated(User.RegisterGroup.class) User user) {
        return userService.registry(user);
    }
}
