package com.example.console.config;

import com.alibaba.fastjson.JSON;
import com.example.module.entity.Sign;
import com.example.module.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class ConsoleLoginValidationInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return validateConsoleLogin(request);
    }

    private boolean validateConsoleLogin(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("sign".equals(cookie.getName())) {
                    String encodedSignString = cookie.getValue();
                    byte[] decodedBytes = java.util.Base64.getDecoder().decode(encodedSignString);
                    String jsonSignString = new String(decodedBytes, StandardCharsets.UTF_8);
                    Sign sign = JSON.parseObject(jsonSignString, Sign.class);
                    Long userId = sign.getUserId();
                    if (sign.getExpireTime().before(new Date()) || userService.getById(userId) == null) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }
}