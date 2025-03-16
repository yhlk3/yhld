package com.example.app.crond;

import com.example.module.sms.service.SmsService;
import com.example.module.sms.task.entity.SmsTask;
import com.example.module.sms.task.service.SmsTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SmsTaskCrond {
    @Autowired
    private SmsTaskService smsTaskService;
    @Autowired
    private SmsService smsService;

    @Scheduled(fixedRate = 2000)
    public void scan(){
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
            e.printStackTrace();
        }
    }
}
