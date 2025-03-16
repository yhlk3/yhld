package com.example.module.utils;

import java.util.HashMap;
import java.util.Map;

public class ResponseCode {
    private static final Map<Integer, String> statusMap = new HashMap<Integer, String>();

    static {
        //user error
        statusMap.put(1001, "OK");
        statusMap.put(1002, "没有登录");
        statusMap.put(1003, "已被登陆");
        statusMap.put(1010, "账号密码不匹配或账号不存在");
        statusMap.put(1011, "账号已存在");
        statusMap.put(1012, "密码错误");
        statusMap.put(1013, "手机号输入错误");
        statusMap.put(1014, "用户不存在");
        statusMap.put(1015, "手机号已存在");
        statusMap.put(1016, "注册失败");

        //create user and forget password
        statusMap.put(2014, "账号尚未注册");

        //file upload error
        statusMap.put(3011, "文件上传失败");
        statusMap.put(3012, "文件为空");
        statusMap.put(3013, "文件生成失败");

        //commodity and category error
        statusMap.put(3052, "ID不正确");
        statusMap.put(3053, "页码不正确，商品列表获取失败");
        statusMap.put(3054, "商品不存在");
        statusMap.put(3055, "新增失败");
        statusMap.put(3056, "修改失败");
        statusMap.put(3057, "删除失败");
        //发送短信异常
        statusMap.put(4001, "发送短信失败");

        //other error
        statusMap.put(4003, "没有权限");
        statusMap.put(4004, "链接超时");
    }

    public static String getMsg(Integer code) {
        return statusMap.get(code);
    }
}
