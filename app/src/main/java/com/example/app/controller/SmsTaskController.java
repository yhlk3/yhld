package com.example.app.controller;

import com.example.module.sms.task.service.SmsTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.module.utils.Response;


@RestController
public class SmsTaskController {
    @Autowired
    private SmsTaskService smsTaskService;
    @RequestMapping("/send/sms")
    public Response sendSms(@RequestParam("phone") String phone){
        try {
            smsTaskService.insert(phone);
        } catch (Exception e) {
            return new Response(3055);
        }
        return new Response(1001);
    }

}
