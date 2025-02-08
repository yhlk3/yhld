package com.example.app.controller;


import com.alibaba.fastjson.JSON;
import com.example.module.entity.Sign;
import com.example.module.entity.User;
import com.example.module.service.UserService;
import com.example.module.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.commons.codec.binary.Base64;
import java.util.Date;
import java.nio.charset.StandardCharsets;
import java.util.UUID;


@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/user/register")
    public Long register(@RequestParam("phone") String phone,
                           @RequestParam("password") String password,
                           @RequestParam("nickName") String nickName){
        String encodedPassword =userService.register(phone, password, nickName);
        User user =userService.login(phone, encodedPassword);
        return user.getId();
    }
    @RequestMapping("/user/login")
    public String login( @RequestParam("phone") String phone,
                         @RequestParam("password") String password) {
        String salt = userService.findSaltByPhone(phone);
        PasswordUtils passwordUtils = new PasswordUtils();
        String encodedPassword = passwordUtils.md5WithSalt(password, salt);
        User user = userService.login(phone, encodedPassword);
        if (user == null) {
            return "用户名或密码错误";
        }
        Sign sign = new Sign();
        sign.setUserId(user.getId());
        Date time = new Date();
        Date expireTime = new Date(time.getTime() + 1000 * 60 * 60 );
        sign.setExpireTime(expireTime);
        String jsonWpString = JSON.toJSONString(sign);
        String encodedSignString = Base64.encodeBase64URLSafeString(jsonWpString.getBytes(StandardCharsets.UTF_8));
        return encodedSignString;

    }

}
