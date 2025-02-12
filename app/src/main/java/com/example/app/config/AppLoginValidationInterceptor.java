package com.example.app.config;

import com.alibaba.fastjson.JSON;
import com.example.module.entity.Sign;
import com.example.module.user.service.UserService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class AppLoginValidationInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return validateAppLogin(request);
    }
    private boolean validateAppLogin(HttpServletRequest request) {
        String sign = request.getParameter("sign");
        if (sign == null)
            return true;
        byte[] decodedBytes = Base64.decodeBase64(sign);
        String jsonSignString = new String(decodedBytes, StandardCharsets.UTF_8);
        Sign userSign = JSON.parseObject(jsonSignString, Sign.class);
        Long userId = userSign.getUserId();
        if (userSign.getExpireTime().before(new Date()) || userService.getById(userId) == null) {
            return true;
        }
        return false;
    }
}