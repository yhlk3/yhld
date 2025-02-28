package com.example.module.details.mapper;

import com.example.module.details.entity.CommodityDetails;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CommodityDetailsMapper {

    @Select("select * from commodity_details where commodity_id = #{commodityId} and is_deleted = 0 order by sequence")
    List<CommodityDetails> getByCommodityId(@Param("commodityId") Long commodityId);

    @Select("select * from commodity_details where id = #{id} and is_deleted = 0")
    CommodityDetails getById(@Param("id")Long id);

    @Select("select * from commodity_details where id = #{id}")
    CommodityDetails extractById(@Param("id")Long id);

    Integer insert(@Param("commodityDetails") CommodityDetails commodityDetails);

    Integer update(@Param("commodityDetails") CommodityDetails commodityDetails);

    @Update("update commodity_details set  is_deleted = 1 where id = #{id}")
    Integer delete(@Param("id")Long id);
}
