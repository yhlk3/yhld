package com.example.module.mapper;

import com.example.module.entity.Category;
import com.example.module.entity.Commodity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CategoryMapper {

    // 根据ID查询操作
    @Select("SELECT * FROM category WHERE id =  #{id} AND is_deleted=0")
    Category getById(@Param("id")Long id);

    // 根据ID提取操作
    @Select("SELECT * FROM category WHERE id =  #{id}")
    Category extractById(@Param("id")Long id);

    @Select("SELECT * FROM category WHERE parent_id =  #{parentId} AND is_deleted=0")
    List<Category> getChildByParentId(@Param("parentId")Long parentId);

    @Select("SELECT * FROM category WHERE is_deleted=0")
    List<Category> findAll();
    @Select("SELECT * FROM category WHERE is_deleted=0 ORDER BY id LIMIT #{offset}, #{pageSize}")
    List<Category> findByPage(@Param("offset") int offset, @Param("pageSize") int pageSize);

    @Select("SELECT * FROM category WHERE is_deleted=0 AND parent_id IS NULL ORDER BY id")
    List<Category> getTopCategories();

    // 插入操作
    Integer insert(@Param("category")Category category);

    // 更新操作
    Integer update(@Param("category")Category category);

    // 删除操作
    @Update("UPDATE category SET update_time = #{updateTime} , is_deleted = 1 WHERE id = #{id}")
    Integer delete(@Param("id")Long id,@Param("updateTime") Integer updateTime);

    List<Category> getCategoriesByIds(@Param("ids") String ids);






}