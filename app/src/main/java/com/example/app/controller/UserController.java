package com.example.app.controller;

import com.example.module.utils.SignUtils;
import com.example.app.domain.SignVO;
import com.example.module.entity.User;
import com.example.module.service.UserService;
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
    public SignVO register(@RequestParam("phone") String phone,
                           @RequestParam("password") String password,
                           @RequestParam("nickName") String nickName){
        userService.register(phone, password, nickName);
        User user = userService.login(phone, password);
        SignVO signVO = new SignVO(signUtils.createSign(user));
        return signVO;
    }
    @RequestMapping("/user/login")
    public SignVO login( @RequestParam("phone") String phone,
                         @RequestParam("password") String password) {
        User user = userService.login(phone, password);
        if (user == null) {
            return null;
        }
        SignVO signVO = new SignVO(signUtils.createSign(user));
        return signVO;
    }

}
