package com.example.console.controller;

import com.example.console.domain.SignVO;
import com.example.module.user.entity.User;
import com.example.module.user.service.UserService;
import com.example.module.utils.SignUtils;
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

    @RequestMapping("/user/login")
    public SignVO login( @RequestParam("phone") String phone,
                         @RequestParam("password") String password) {
        User user = userService.login(phone,password);
        if (user == null) {
            return null;
        }
        SignVO signVO = new SignVO(signUtils.createSign(user));
        return signVO;
    }
    @RequestMapping("/user/register")
    public SignVO register(@RequestParam("phone") String phone,
                           @RequestParam("password") String password,
                           @RequestParam("nickName") String nickName) {
        // 校验手机号是否为13位
        if(!userService.isValidPhone(phone))
            return null;
        try {
            userService.register(phone, password, nickName);
            User user = userService.login(phone, password);
            SignVO signVO = new SignVO(signUtils.createSign(user));
            return signVO;
        } catch (Exception e) {
            return null;
        }
    }
}








