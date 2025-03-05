package com.example.module.commodity.mapper;

import com.example.module.commodity.entity.CommodityLabelRelationship;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CommodityLabelMapper {

    @Select("select * from commodity_label_relationship where id = #{id} and is_deleted=0 ")
    CommodityLabelRelationship getById(@Param("id")Long id);

    @Select("select * from commodity_label_relationship where id = #{id}")
    CommodityLabelRelationship extractById(@Param("id")Long id);

    int insert(@Param("commodityLabelRelationship")CommodityLabelRelationship commodityLabelRelationship);

    int update(@Param("commodityLabelRelationship")CommodityLabelRelationship commodityLabelRelationship);

    @Update("update commodity_label_relationship set is_deleted = 1 ")
    int delete(Long id);
    //通过商品id和标签id查询关系id
    @Select("select id from commodity_label_relationship where commodity_id = #{commodityId} and label_id = #{labelId} and is_deleted=0 ")
    Long getIdByCommodityIdAndLabelId(@Param("commodityId")Long commodityId,@Param("labelId")Long labelId);
    //通过商品id查找标签ids
    @Select("select label_id from commodity_label_relationship where commodity_id = #{commodityId} and is_deleted=0 ")
    List<Long> getLabelIdsByCommodityId(@Param("commodityId")Long commodityId);
}
