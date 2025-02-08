package com.example.module.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Commodity {
    private Long id;
    private String title;
    private Integer price;
    private String location;
    private String introduction;
    private String images;
    private Integer createTime;
    private Integer updateTime;
    private Integer isDeleted;
    private Long categoryId;
}
