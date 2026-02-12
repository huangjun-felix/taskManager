package com.huangjun.taskmanager.interceptor;

import com.huangjun.taskmanager.constants.RedisConstants;
import com.huangjun.taskmanager.constants.TimeConstants;
import com.huangjun.taskmanager.entity.User;
import com.huangjun.taskmanager.utils.RedisUtils;
import com.huangjun.taskmanager.utils.ThreadLocalUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class GlobalInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("authorization");
        User user = RedisUtils.get(RedisConstants.USER_LOGIN+token, User.class);
        if(user!=null){
            ThreadLocalUtils.setUser(user);
            RedisUtils.set(RedisConstants.USER_LOGIN+user.getId(),user, TimeConstants.SEVEN_MINUTE);
        }
        return true;
    }
}
