package com.example.app.config;

import com.alibaba.fastjson.JSON;
import com.example.module.entity.Sign;
import com.example.module.user.service.UserService;
import com.example.module.utils.Response;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class AppLoggedInValidationInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!validateAppLoggedIn(request)) {
            // 验证失败时，返回一个 Response 对象
            Response<Void> errorResponse = new Response<>(1002);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(JSON.toJSONString(errorResponse));
            return false;
        }
        return true;
    }

    private boolean validateAppLoggedIn(HttpServletRequest request) {
        String sign = request.getParameter("sign");
        if (sign == null) {
            return false; // 如果没有签名参数，则认为未登录
        }
        byte[] decodedBytes = Base64.decodeBase64(sign);
        String jsonSignString = new String(decodedBytes, StandardCharsets.UTF_8);
        Sign userSign = JSON.parseObject(jsonSignString, Sign.class);
        Long userId = userSign.getUserId();
        if (userSign.getExpireTime().after(new Date()) && userService.getById(userId) != null) {
            return true; // 如果签名未过期且用户存在，则认为已登录
        }
        return false; // 其他情况认为未登录
    }
}
