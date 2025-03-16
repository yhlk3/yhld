package com.example.console.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @ExcelProperty("用户ID")
    private Long id;
    @ExcelProperty("用户手机")
    private String phone;
    @ExcelProperty("用户名")
    private String nickName;
    @ExcelProperty("用户注册时间")
    private Integer createTime;
}
