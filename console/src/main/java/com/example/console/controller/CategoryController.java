package com.example.console.controller;

import com.example.console.domain.CategoryDTO;
import com.example.module.utils.Response;
import com.example.module.category.entity.Category;
import com.example.console.domain.CategoryTreeNode;
import com.example.module.category.service.CategoryService;
import com.example.module.commodity.service.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CommodityService commodityService;

    @RequestMapping("/category/delete")
    public Response delete(@RequestParam Long id) {
        try {
            categoryService.delete(id);
            commodityService.deleteByCategoryId(id);
        } catch (Exception e) {
            return new Response(3057,null);
        }
        return new Response(1001,null);
    }
    @RequestMapping("/category/tree")
    public Response tree() {
        List<Category> categories =categoryService.getAllCategories();
        Map<Long, CategoryTreeNode> nodeMap = new HashMap<>();
        List<CategoryTreeNode> rootNodes = new ArrayList<>();
        for (Category category : categories) {
            CategoryTreeNode node = new CategoryTreeNode(category.getId(), category.getName());
            nodeMap.put(category.getId(), node);
        }
        for (Category category : categories) {
            CategoryTreeNode node = nodeMap.get(category.getId());
            if (category.getParentId() == null) {
                rootNodes.add(node);
            } else {
                CategoryTreeNode parentNode = nodeMap.get(category.getParentId());
                if (parentNode != null) {
                    parentNode.getChildren().add(node);
                }
            }
        }
        return new Response(1001, rootNodes);
    }
    @RequestMapping("/category/list")
    public Response list() {
        List<Category> categories =categoryService.getAllCategories();
        List<CategoryDTO> voList = new ArrayList<>();
        for (Category category : categories) {
            CategoryDTO vo = new CategoryDTO();
            vo.setId(category.getId());
            vo.setName(category.getName());
            vo.setParentId(category.getParentId());
            voList.add(vo);
        }
        return new Response(1001,voList);
    }
}
