package com.example.module.mapper;

import com.example.module.entity.Category;
import com.example.module.entity.Commodity;
import com.example.module.entity.CommodityCategoryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CommodityMapper {
    @Select("select * from commodity where id = #{id} and is_deleted=0 " )
    Commodity getById(@Param("id")Long id);

    @Select("select * from commodity where id = #{id}")
    Commodity extractById(Long id);

    int update(@Param("commodity")Commodity commodity);

    int insert(@Param("commodity")Commodity commodity);

    @Update("update commodity set is_deleted = 1 update_time = #{update_time}")
    int delete(Long id, Integer updateTime);

    @Select("select * from commodity where is_deleted=0 order by id limit #{offset}, #{pageSize} ")
    List<Commodity> findPage(@Param("offset") int offset, @Param("pageSize") int pageSize);

    @Select("select count(*) from commodity where is_deleted=0")
    int count();

    @Select("select * from commodity where is_deleted=0")
    List<Commodity> findAll();

    List<Commodity> findByKeywordAndPage(@Param("keyword") String keyword,
                                         @Param("offset") int offset,
                                         @Param("pageSize") int pageSize,
                                         @Param("ids") String ids);

    @Select("SELECT id FROM category WHERE is_deleted = 0 AND name LIKE CONCAT('%', #{keyword}, '%')")
    List<Long> getIdsByKeyword(@Param("keyword") String keyword);

    @Update("update commodity set is_deleted = 1 where category_id = #{categoryId}")
    int deleteByCategoryId(Long categoryId);

    List<CommodityCategoryDTO> getCommoditiesWithCategory(@Param("keyword") String keyword,
                                                          @Param("offset") int offset,
                                                          @Param("pageSize") int pageSize);

    @Select("SELECT * FROM commodity WHERE category_id = #{categoryId} AND is_deleted=0")
    List<Commodity> findByCategoryId(@Param("categoryId") Long categoryId);

    List<Commodity> findCommoditiesByCategoryIds(@Param("categoryIds") String categoryIds,
                                                 @Param("offset") int offset,
                                                 @Param("pageSize") int pageSize);
}



