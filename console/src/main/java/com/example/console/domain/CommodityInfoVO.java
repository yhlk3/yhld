package com.example.console.domain;

import lombok.Data;

import java.util.List;

@Data
public class CommodityInfoVO {
    private Integer price;
    private String title;
    private List<BaseContentValueVo> details;
    private List<String> images;
    private String createTime;
    private String updateTime;
}