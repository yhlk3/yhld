package com.example.console.controller;

import com.alibaba.fastjson.JSON;
import com.example.console.domain.SignVO;
import com.example.module.entity.Sign;
import com.example.module.user.entity.User;
import com.example.module.user.service.UserService;
import com.example.module.utils.SignUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;


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
                           @RequestParam("nickName") String nickName, HttpServletRequest request) {
        // 校验手机号是否为13位
        if(!userService.isValidPhone(phone))
            return null;

        Cookie[] cookies = request.getCookies();
        boolean isLoggedIn = true;
        for (Cookie cookie : cookies) {
            if ("sign".equals(cookie.getName())) {
                String encodedSignString = cookie.getValue();
                byte[] decodedBytes = Base64.decodeBase64(encodedSignString);
                String jsonSignString = new String(decodedBytes, StandardCharsets.UTF_8);
                Sign sign = JSON.parseObject(jsonSignString, Sign.class);
                Long userId = sign.getUserId();
                if (sign.getExpireTime().before(new Date()) || userService.findById(userId) ==null || userService.findByPhone(phone) == null) {
                    isLoggedIn = false;
                } else {
                    return null;
                }
            }
            else {
                isLoggedIn = false;
            }
        }
        if (!isLoggedIn) {
            //对密码加密，并把用户信息注册到数据库中
            try {
                userService.register(phone, password, nickName);
                User user = userService.login(phone, password);
                SignVO signVO = new SignVO(signUtils.createSign(user));
                return signVO;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}








