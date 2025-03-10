package com.example.module.sms.mapper;

import com.example.module.sms.entity.SmsRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SmsMapper {
    @Select("select * from sms where id = #{id}")
    SmsRecord getById(@Param("id") Long id);

    int insert(@Param("sms") SmsRecord smsRecord);

    int update(@Param("sms") SmsRecord smsRecord);
}
