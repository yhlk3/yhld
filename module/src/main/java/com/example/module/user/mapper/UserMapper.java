package com.example.module.user.mapper;

import com.example.module.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
    @Select("select * from user where id = #{id} and is_deleted = 0")
    User getById(@Param("id")Long id);

    int insert(@Param("user")User user);

    int update(@Param("user")User user);
    @Select("select * from user where phone = #{phone} and password = #{password}")
    User login(@Param("phone")String phone, @Param("password")String password);

    @Select("select * from user where phone = #{phone}")
    User findByPhone(@Param("phone")String phone);

    @Select("select salt from user where phone = #{phone}")
    String findSaltByPhone(@Param("phone")String phone);

    @Update("update user set is_deleted = 1 where id = #{id}")
    int delete(Long id);
    @Select("select * from user where id = #{id}")
    User extractById(Long id);

}
