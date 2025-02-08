package com.example.module.service;

import com.example.module.entity.User;
import com.example.module.mapper.UserMapper;
import com.example.module.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.regex.Pattern;


@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    public User findById(Long id) {
        return userMapper.findById(id);
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
    public User login(String phone, String password) {
        return userMapper.login(phone, password);
    }
    public User findByPhone(String phone) {
        return userMapper.findByPhone(phone);
    }
    public boolean isValidPhone(String phone) {
        Pattern phonePattern = Pattern.compile("^\\d{13}$");
        return phonePattern.matcher(phone).matches();
    }

    public String findSaltByPhone(String phone) {
        return userMapper.findSaltByPhone(phone);
    }
    public String register(String phone, String password,String nickName){
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
        return encodedPassword;
    }

}



