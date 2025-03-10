package com.example.module.sms.task.mapper;

import com.example.module.sms.task.entity.SmsTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SmsTaskMapper {
    //getById
    @Select("select * from sms_task where id = #{id}")
    SmsTask getById(@Param("id") Long id);

    int insert(@Param("smsTask") SmsTask smsTask);

    int update(@Param("smsTask") SmsTask smsTask);

    @Select("select * from sms_task where status = 0")
    List<SmsTask> getByStatus();

    int setStatus(@Param("ids") String ids);

}
