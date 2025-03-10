package com.example.app.controller;

import com.example.module.sms.service.SmsService;
import com.example.module.utils.VerifyCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;
    @Autowired
    private VerifyCodeUtil verifyCodeUtil;

    // 单发
    @PostMapping("/send")
    public void send(@RequestParam("phone") String phone) {
        String code = VerifyCodeUtil.generateNumericCode(6);
        smsService.sendSms(phone, code);
    }
    // 批量
    @PostMapping("/batchSend")
    public void batchSend(@RequestParam("phones") List<String> phones) {
        smsService.sendBatchSms(phones);
    }

}

