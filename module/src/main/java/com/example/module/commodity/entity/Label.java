package com.example.module.commodity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Label {
    private Long id;
    private String name;
    private Integer createTime;
    private Integer updateTime;
    private Integer isDeleted;
}
