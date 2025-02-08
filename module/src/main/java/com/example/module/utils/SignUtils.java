package com.example.module.utils;

import com.alibaba.fastjson.JSON;
import com.example.module.entity.Sign;
import com.example.module.entity.User;
import com.example.module.service.UserService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class SignUtils {
    public String createSign(User user){
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
