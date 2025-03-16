package com.example.console.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryExportVO {
    @ExcelProperty("分类ID")
    private Long id;

    @ExcelProperty("分类路径")
    private String path;

    @ExcelProperty("层级名称")
    private String nameWithIndent;

    @ExcelProperty("完整名称")
    private String fullName;


}