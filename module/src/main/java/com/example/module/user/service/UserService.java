package com.example.module.user.service;

import com.example.module.user.entity.User;
import com.example.module.user.mapper.UserMapper;
import com.example.module.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.regex.Pattern;


@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    public User getById(Long id) {
        return userMapper.getById(id);
    }
    public User extractById(Long id) {
        return userMapper.extractById(id);
    }
    public Long insert(String phone, String nickName, String password) {
        int timestamp = (int) (System.currentTimeMillis() / 1000);
        User user = new User();
        user.setPhone(phone);
        user.setNickName(nickName);
        user.setPassword(password);
        user.setCreateTime(timestamp);
        user.setUpdateTime(timestamp);
        user.setIsDeleted(0);
        userMapper.insert(user);
        return user.getId();
    }
    public int update(User user) {
        return userMapper.update(user);
    }
    public int delete(Long id) {
        return userMapper.delete(id);
    }
    public User login(String phone, String password) {
        String salt =userMapper.findSaltByPhone(phone);
        PasswordUtils passwordUtils = new PasswordUtils();
        String encodedPassword = passwordUtils.md5WithSalt(password, salt);
        return userMapper.login(phone, encodedPassword);
    }
    public User findByPhone(String phone) {
        return userMapper.findByPhone(phone);
    }
    public boolean isValidPhone(String phone) {
        Pattern phonePattern = Pattern.compile("^\\d{13}$");
        return phonePattern.matcher(phone).matches();
    }

    public void register(String phone, String password,String nickName){
        //验证手机号是否被注册过，如果注册过，抛出异常已注册
        if (userMapper.findByPhone(phone) != null) {
            throw new RuntimeException("手机号已被注册");
        }
        int timestamp = (int) (System.currentTimeMillis() / 1000);
        String salt = UUID.randomUUID().toString();
        User user = new User();
        PasswordUtils passwordUtils = new PasswordUtils();
        String encodedPassword = passwordUtils.md5WithSalt(password, salt);
        user.setPhone(phone);
        user.setNickName(nickName);
        user.setPassword(encodedPassword);
        user.setSalt(salt);
        user.setCreateTime(timestamp);
        user.setUpdateTime(timestamp);
        user.setIsDeleted(0);
        userMapper.insert(user);
    }

}



