package com.example.console.domain;

import lombok.Data;

@Data

public class CommodityVO {
    private Long id;
    private String title;
    private Integer price;
    private String location;
    private String introduction;
    private String images;
    private Integer createTime;
    private Integer updateTime;
    private Integer isDeleted;
}