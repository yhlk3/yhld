package com.example.module.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String phone;
    private String nickName;
    private String password;
    private String avatar;
    private String salt;
    private Integer createTime;
    private Integer updateTime;
    private Integer isDeleted;
}
