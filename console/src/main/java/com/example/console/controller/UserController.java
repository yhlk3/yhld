package com.example.console.controller;

import com.alibaba.fastjson.JSON;
import com.example.console.domain.SignVO;
import com.example.module.entity.Sign;
import com.example.module.entity.User;
import com.example.module.service.UserService;
import com.example.module.utils.PasswordUtils;
import com.example.module.utils.SignUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;


@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private SignUtils signUtils;

    @RequestMapping("/user/login")
    public SignVO login( @RequestParam("phone") String phone,
                         @RequestParam("password") String password) {
        String salt =userService.findSaltByPhone(phone);
        PasswordUtils passwordUtils = new PasswordUtils();
        String encodedPassword = passwordUtils.md5WithSalt(password,salt);
        User user = userService.login(phone, encodedPassword);
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
                String encodedPassword =userService.register(phone, password, nickName);
                User user = userService.login(phone, encodedPassword);
                SignVO signVO = new SignVO(signUtils.createSign(user));
                return signVO;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}








