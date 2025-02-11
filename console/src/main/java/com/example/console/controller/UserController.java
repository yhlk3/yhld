package com.example.console.controller;

import com.example.console.domain.SignVO;
import com.example.module.user.entity.User;
import com.example.module.user.service.UserService;
import com.example.module.utils.SignUtils;
import javax.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletResponse;


@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private SignUtils signUtils;

    @RequestMapping("/user/login")
    public String login( @RequestParam("phone") String phone,
                         @RequestParam("password") String password,
                         HttpServletResponse response) {
        User user = userService.login(phone,password);
        if (user == null) {
            return "用户不存在";
        }
        SignVO signVO = new SignVO(signUtils.createSign(user));
        Cookie cookie = new Cookie("sign", signVO.getSign());
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
        return "登陆成功";
    }
    @RequestMapping("/user/register")
    public String register(@RequestParam("phone") String phone,
                           @RequestParam("password") String password,
                           @RequestParam("nickName") String nickName,
                           HttpServletResponse response) {
        // 校验手机号是否为13位
        if(!userService.isValidPhone(phone))
            return null;
        try {
            userService.register(phone, password, nickName);
            User user = userService.login(phone, password);
            SignVO signVO = new SignVO(signUtils.createSign(user));
            Cookie cookie = new Cookie("sign", signVO.getSign());
            cookie.setMaxAge(3600);
            response.addCookie(cookie);
            return "注册成功";
        } catch (Exception e) {
            return "注册失败";
        }
    }
}








