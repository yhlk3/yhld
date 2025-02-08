package com.example.console.domain;

import lombok.Data;

@Data
public class CommodityListVO {
    private Long id;
    private String image;  // 假设这里使用第一个图片作为展示
    private Integer price;
    private String title;
    private String location;

}