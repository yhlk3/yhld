package com.example.module.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    private Long id;
    private String name;
    private String image;
    private Integer createTime;
    private Integer updateTime;
    private Integer isDeleted;
    private Long parentId;
    private List<Category> children;
    private List<Commodity> commodities;
}