package com.example.module.utils;

import com.example.module.user.entity.User;
import com.alibaba.fastjson.JSON;
import com.example.module.entity.Sign;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;


import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Service
public class SignUtils {
    public String createSign(User user){
        Sign sign = new Sign();
        sign.setUserId(user.getId());
        // 使用 ZonedDateTime 处理时区
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        ZonedDateTime expireTime = now.plusSeconds(3600); // 1小时后过期
        sign.setExpireTime(Date.from(expireTime.toInstant()));

        String jsonWpString = JSON.toJSONString(sign);
        String encodedSignString = Base64.encodeBase64URLSafeString(jsonWpString.getBytes(StandardCharsets.UTF_8));
        return encodedSignString;

    }

}
