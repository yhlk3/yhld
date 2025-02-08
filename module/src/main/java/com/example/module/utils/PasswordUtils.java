package com.example.module.utils;
import com.example.module.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
public class PasswordUtils {
    @Autowired
    private UserService userService;

    public String md5WithSalt(String password, String salt) {
        try {

            // 拼接密码和盐
            String saltedPassword = password + salt;

            // 使用 MD5 算法计算哈希值
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(saltedPassword.getBytes(StandardCharsets.UTF_8));

            // 将字节数组转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating MD5 hash", e);
        }
    }

}
