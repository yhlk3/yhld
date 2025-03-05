package com.example.module.commodity.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.example.module.commodity.entity.Label;

import java.util.List;

@Mapper
public interface LabelMapper {

    @Select("select * from label where id = #{id} and is_deleted=0 ")
    Label getById(@Param("id")Long id);

    @Select("select * from label where id = #{id}")
    Label extractById(@Param("id")Long id);

    int insert(@Param("label")Label label);

    int update(@Param("label")Label label);

    @Update("update label set is_deleted = 1 ")
    int delete(Long id);
    //通过标签名获取标签id
    @Select("select id from label where name = #{name} and is_deleted=0 ")
    Long getIdByName(@Param("name")String name);


}
