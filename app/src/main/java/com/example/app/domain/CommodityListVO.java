package com.example.app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CommodityListVO {
    private Long id;
    private ImageInfoVO image;  // 假设这里使用第一个图片作为展示
    private Integer price;
    private String title;
    private String location;
    private String category;
}
