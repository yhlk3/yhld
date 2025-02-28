package com.example.app.controller;

import com.alibaba.fastjson.JSON;
import com.example.module.utils.Response;
import com.example.module.user.entity.User;
import com.example.module.user.service.UserService;
import com.example.module.utils.SignUtils;
import com.example.app.domain.SignVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private SignUtils signUtils;

    @RequestMapping("/user/register")
    public Response register(@RequestParam("phone") String phone,
                           @RequestParam("password") String password,
                           @RequestParam("nickName") String nickName) {
        // 校验手机号是否为13位
        if(!userService.isValidPhone(phone))
            return new Response(1013,null);
        try {
            userService.register(phone, password, nickName);
            User user = userService.login(phone, password);
            SignVO signVO = new SignVO(signUtils.createSign(user));
            return new Response(1001,signVO);
        } catch (Exception e) {
            return null;
        }
    }
    @RequestMapping("/user/login")
    public Response login( @RequestParam("phone") String phone,
                         @RequestParam("password") String password) {
        User user = userService.login(phone, password);
        if (user == null) {
            return new Response(1014, null);
        }
        SignVO signVO = new SignVO(signUtils.createSign(user));
        return new Response(1001, signVO);
    }

}
