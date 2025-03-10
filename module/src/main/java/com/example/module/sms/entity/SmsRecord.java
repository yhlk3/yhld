package com.example.module.sms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsRecord {
    private Long id;
    private String phoneNumbers;
    private String content;
    private Integer sendTime;
    private Integer status;
}

