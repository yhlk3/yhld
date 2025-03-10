package com.example.module.sms.task.service;

import com.example.module.sms.task.entity.SmsTask;
import com.example.module.sms.task.mapper.SmsTaskMapper;
import com.example.module.utils.VerifyCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.module.utils.Response;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SmsTaskService {
    @Autowired
    SmsTaskMapper smsTaskMapper;

    public SmsTask getById(Long id){
        return smsTaskMapper.getById(id);
    }
    public Response insert(String phone){
        int timestamp = (int) (System.currentTimeMillis() / 1000);
        SmsTask smsTask = new SmsTask();
        smsTask.setPhone(phone);
        String uniqueCode = VerifyCodeUtil.generateNumericCode(6);
        smsTask.setContent(uniqueCode);
        smsTask.setSendTime(timestamp);
        smsTask.setStatus(0);
        try {
            smsTaskMapper.insert(smsTask);
        } catch (Exception e) {
            return new Response(3055);
        }
        return new Response(1001);
    }
    public Response update(SmsTask smsTask){
        try {
            smsTaskMapper.update(smsTask);
        } catch (Exception e) {
            return new Response(3055);
        }
        return new Response(1001);
    }
    public List<SmsTask> getByStatus(){
        return smsTaskMapper.getByStatus();
    }
    public int setStatus(String ids){
        return smsTaskMapper.setStatus(ids);
    }

}
