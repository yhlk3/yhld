package com.example.app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryListVO {
    private Long id;
    private String name;
    private String image;
    private List<CategoryChildrenVO> children;

}

