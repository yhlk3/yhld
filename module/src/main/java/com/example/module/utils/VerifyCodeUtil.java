package com.example.module.utils;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

/**
 * 验证码生成工具类
 */
@Service
public class VerifyCodeUtil {
    private static final String NUMBERS = "0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    public static String generateNumericCode(int length) {
        return generateCode(NUMBERS, length);
    }
    private static String generateCode(String baseString, int length) {
        if (length < 1) throw new IllegalArgumentException("验证码长度不能小于1");

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(baseString.length());
            sb.append(baseString.charAt(index));
        }
        return sb.toString();
    }
}
