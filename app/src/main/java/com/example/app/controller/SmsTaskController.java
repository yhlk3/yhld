package com.example.app.controller;


import com.example.module.sms.service.SmsService;
import com.example.module.sms.task.entity.SmsTask;
import com.example.module.sms.task.service.SmsTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.module.utils.Response;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SmsTaskController {
    @Autowired
    private SmsTaskService smsTaskService;
    @Autowired
    private SmsService smsService;
    @RequestMapping("/send/sms")
    public Response sendSms(@RequestParam("phone") String phone){
        try {
            smsTaskService.insert(phone);
        } catch (Exception e) {
            return new Response(3055);
        }
        return new Response(1001);
    }
    @RequestMapping("/sms/task/scan")
    @Scheduled(fixedRate = 2000)
    public Response scan(){
        List<SmsTask> smsTasks = smsTaskService.getByStatus();
        List<String> phoneNumbers = new ArrayList<>();
        StringBuilder idsStringBuilder = new StringBuilder();
        for (SmsTask smsTask : smsTasks) {
            String phone = smsTask.getPhone();
            if (idsStringBuilder.length() > 0) {
                idsStringBuilder.append(",");
            }
            phoneNumbers.add(phone);
            idsStringBuilder.append(smsTask.getId());
        }
        String ids = idsStringBuilder.toString();
        try {
            smsService.sendBatchSms(phoneNumbers);
            smsTaskService.setStatus(ids);
        } catch (Exception e) {
            return new Response(4001);
        }
        return new Response(1001);
    }
}
