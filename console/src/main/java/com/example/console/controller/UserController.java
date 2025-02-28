package com.example.console.controller;

import com.example.console.domain.SignVO;
import com.example.module.user.entity.User;
import com.example.module.utils.Response;
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
    public Response login( @RequestParam("phone") String phone,
                         @RequestParam("password") String password,
                         HttpServletResponse response) {
        User user = userService.login(phone,password);
        if (user == null) {
            return new Response(1014);
        }
        SignVO signVO = new SignVO(signUtils.createSign(user));
        Cookie cookie = new Cookie("sign", signVO.getSign());
        cookie.setMaxAge(3600);
        cookie.setPath("/");
        response.addCookie(cookie);
        return new Response(1001);
    }
    @RequestMapping("/user/register")
    public Response register(@RequestParam("phone") String phone,
                           @RequestParam("password") String password,
                           @RequestParam("nickName") String nickName,
                           HttpServletResponse response) {
        // 校验手机号是否为13位
        if(!userService.isValidPhone(phone))
            return new Response(1013);
        try {
            userService.register(phone, password, nickName);
            User user = userService.login(phone, password);
            SignVO signVO = new SignVO(signUtils.createSign(user));
            Cookie cookie = new Cookie("sign", signVO.getSign());
            cookie.setMaxAge(3600);
            cookie.setPath("/");
            response.addCookie(cookie);
            return new Response(1001);
        } catch (Exception e) {
            return new Response(1016);
        }
    }
}








