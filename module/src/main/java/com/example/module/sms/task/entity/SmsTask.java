package com.example.module.sms.task.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsTask {
    private Long id;
    private String phone;
    private String content;
    private Integer sendTime;
    private Integer status;
}
