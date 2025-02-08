package com.example.console.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryTreeNode {
    private Long id;
    private String name;
    private List<CategoryTreeNode> children;

    public CategoryTreeNode(Long id, String name) {
        this.id = id;
        this.name = name;
        this.children = new ArrayList<>();
    }

}
