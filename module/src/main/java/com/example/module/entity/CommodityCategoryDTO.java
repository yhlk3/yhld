package com.example.module.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommodityCategoryDTO {
    private Long id;
    private String title;
    private String images;
    private Integer price;
    private String location;
    private String categoryName;
}
