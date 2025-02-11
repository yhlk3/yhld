package com.example.module.config;

import com.alibaba.fastjson.JSON;
import com.example.module.entity.Sign;
import javax.servlet.http.Cookie;
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
public class LoginValidationInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        int serverPort = request.getServerPort();

        // 根据端口号判断请求来源
        if (serverPort == 8082) { // app端
            return validateAppLogin(request);
        } else if (serverPort == 8081) { // console端
            return validateConsoleLogin(request);
        }
        // 如果端口既不是8082也不是8081，则返回false拒绝访问
        return false;
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

    private boolean validateConsoleLogin(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if ("sign".equals(cookie.getName())) {
                    String encodedSignString = cookie.getValue();
                    byte[] decodedBytes = java.util.Base64.getDecoder().decode(encodedSignString);
                    String jsonSignString = new String(decodedBytes, StandardCharsets.UTF_8);
                    Sign sign = JSON.parseObject(jsonSignString, Sign.class);
                    Long userId = sign.getUserId();
                    if (sign.getExpireTime().before(new Date()) || userService.getById(userId) ==null) {
                        return true;
                    }
                }
                else {
                    return true;
                }
            }
            return false;
    }
}