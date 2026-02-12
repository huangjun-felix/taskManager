package com.huangjun.taskmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huangjun.taskmanager.constants.RedisConstants;
import com.huangjun.taskmanager.constants.ResultConstants;
import com.huangjun.taskmanager.constants.TimeConstants;
import com.huangjun.taskmanager.entity.Result;
import com.huangjun.taskmanager.entity.User;
import com.huangjun.taskmanager.mapper.UserMapper;
import com.huangjun.taskmanager.service.UserService;
import com.huangjun.taskmanager.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result registry(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,user.getUsername());
        User user1 = userMapper.selectOne(wrapper);
        if(user1==null){
            String uuid = UUID.randomUUID().toString();
            user.setId(uuid);
            user.setCreateTime(LocalDateTime.now());
            int result = userMapper.insert(user);
            if(result<0){
                return Result.fail(ResultConstants.REGISTRY_ERROR);
            }
//            RedisUtils.set(RedisConstants.USER_LOGIN + uuid,user,TimeConstants.SEVEN_MINUTE);
        }
        if (user.getPassword().equals(user.getUsername())) {
            return Result.fail(ResultConstants.REGISTRY_BEFORE);
        }
        return Result.ok(ResultConstants.REGISTRY_SUCCESS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result login(User user) {
//        User user1 = RedisUtils.get(RedisConstants.USER_LOGIN + userId, User.class);
//        //如果redis里面有用户信息
//        if(user1!=null){
//            return Result.ok(userId);
//        }
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,user.getUsername());
        User user2 = userMapper.selectOne(wrapper);
        if(user2==null){
            return Result.fail(ResultConstants.LOGIN_NOT_INFO);
        }
        if (!user.getPassword().equals(user2.getPassword())) {
            return Result.fail(ResultConstants.LOGIN_ERROR_PASSWD);
        }
        RedisUtils.set(RedisConstants.USER_LOGIN + user2.getId(),user2, TimeConstants.SEVEN_MINUTE);
        return Result.ok(user2.getId());
    }
}
