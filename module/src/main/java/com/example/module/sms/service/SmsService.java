package com.example.module.sms.service;

import com.example.module.sms.entity.SmsRecord;
import com.example.module.sms.mapper.SmsMapper;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.example.module.utils.VerifyCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@Service
public class SmsService {
    @Autowired
    private SmsMapper smsRecordMapper;

    @Value("${aliyun.sms.access-key-id}") private String accessKeyId;
    @Value("${aliyun.sms.access-secret}") private String accessSecret;
    @Value("${aliyun.sms.sign-name}") private String signName;
    public void sendSms(String phoneNumbers, String code) {
        // 3. 创建短信记录对象
        int timestamp = (int) (System.currentTimeMillis() / 1000);
        SmsRecord record = new SmsRecord();
        record.setPhoneNumbers(phoneNumbers);
        record.setContent(code);
        record.setSendTime(timestamp); // 使用时间戳记录发送时间
        try {
            // 4. 初始化阿里云客户端
            Config config = new Config()
                    .setAccessKeyId(accessKeyId)
                    .setAccessKeySecret(accessSecret)
                    .setEndpoint("dysmsapi.aliyuncs.com");
            Client client = new Client(config);
            // 5. 构建请求对象
            SendSmsRequest request = new SendSmsRequest()
                    .setPhoneNumbers(phoneNumbers)
                    .setSignName(signName)
                    .setTemplateCode("SMS") // 模板
                    .setTemplateParam("{\"code\":\"" + code + "\"}");
            // 6. 发送短信并获取响应
            SendSmsResponse response = client.sendSms(request);
            record.setStatus(1); // 标记发送成功
        } catch (Exception e) {
            record.setStatus(0);
        } finally {
            // 8. 无论成功失败都记录到数据库
            smsRecordMapper.insert(record);
        }
    }
    // 批量发送
    public void sendBatchSms(List<String> phoneNumbers) {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (String phone : phoneNumbers) {
            executor.submit(() -> {
                String uniqueCode = VerifyCodeUtil.generateNumericCode(6);
                SmsRecord record = new SmsRecord();
                record.setPhoneNumbers(phone);
                record.setContent(uniqueCode); // 存储独立验证码
                record.setSendTime((int)(System.currentTimeMillis() / 1000));
                try {
                    // 复用原有发送逻辑（但使用 uniqueCode）
                    Config config = new Config()
                            .setAccessKeyId(accessKeyId)
                            .setAccessKeySecret(accessSecret)
                            .setEndpoint("dysmsapi.aliyuncs.com");
                    Client client = new Client(config);
                    SendSmsRequest request = new SendSmsRequest()
                            .setPhoneNumbers(phone)
                            .setSignName(signName)
                            .setTemplateCode("SMS")
                            .setTemplateParam("{\"code\":\"" + uniqueCode + "\"}");
                    client.sendSms(request);
                    record.setStatus(1);
                } catch (Exception e) {
                    record.setStatus(0);
                } finally {
                    smsRecordMapper.insert(record);
                }
            });
        }
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
