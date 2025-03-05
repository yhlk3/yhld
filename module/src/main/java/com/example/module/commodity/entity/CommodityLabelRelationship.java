package com.example.module.commodity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommodityLabelRelationship {
    private Long id;
    private Long commodityId;
    private Long labelId;
    private Integer createTime;
    private Integer updateTime;
    private Integer isDeleted;
}
