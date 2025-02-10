package com.example.module.category.service;

import com.example.module.category.entity.Category;
import com.example.module.category.mapper.CategoryMapper;
import com.example.module.commodity.entity.Commodity;
import com.example.module.commodity.mapper.CommodityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CommodityMapper commodityMapper;

    public Category getById(Long id) {
        Category category = categoryMapper.getById(id);
        return category;
    }
    public List<Category> getAllCategories(){
        return categoryMapper.findAll();
    }
    public List<Category> getChildByParentId(Long parentId) {
        return categoryMapper.getChildByParentId(parentId);
    }
    public List<Category> getTopCategories() {
        return categoryMapper.getTopCategories();
    }

    public Long edit(Long id, String name, String image) {
        int timestamp = (int) (System.currentTimeMillis() / 1000);
        Category category = new Category();
        category.setName(name);
        category.setImage(image);
        category.setIsDeleted(0);
        if (id != null) {
            Category existingCategory = categoryMapper.getById(id);
            if (existingCategory == null) {
                throw new RuntimeException("ID不存在");
            }
            category.setId(id);
            category.setUpdateTime(timestamp);
            categoryMapper.update(category);
            return id;
        } else {
            category.setCreateTime(timestamp);
            category.setUpdateTime(timestamp);
            categoryMapper.insert(category);
            return category.getId();
        }
    }

    public int delete(Long id) {
        Category existingCategory = categoryMapper.getById(id);
        if (existingCategory == null) {
            throw new RuntimeException("商品ID不存在");
        }

        int timestamp = (int) (System.currentTimeMillis() / 1000);
        return categoryMapper.delete(id, timestamp);
    }

    public Category extractCategoryById(Long id) {
        Category category = categoryMapper.extractById(id);
        if (category == null) {
            throw new RuntimeException("商品ID不存在");
        }
        return category;
    }
    public List<Category> getCategoriesByIds(String ids) {
        return categoryMapper.getCategoriesByIds(ids);
    }
    public List<Commodity> getLeafCategoriesWithCommodities(Long parentId, int page, int pageSize) {

        List<Category> categories = categoryMapper.getChildByParentId(parentId);
        List<Long> id = new ArrayList<>();
        StringBuilder idsStringBuilder = new StringBuilder();
        for (Category category : categories) {
            // 判断是否有子分类
            if (categoryMapper.getChildByParentId(category.getId()).isEmpty()) {
                id.add(category.getId());
            } else {
                List<Commodity> leafCategoriesWithCommodities = getLeafCategoriesWithCommodities(category.getId(),page,pageSize);
                for (Commodity commodity : leafCategoriesWithCommodities) {
                    id.add(commodity.getId());
                }
            }
        }
        for (int i = 0; i < id.size(); i++) {
            idsStringBuilder.append(id.get(i));
            if (i < id.size() - 1) {
                idsStringBuilder.append(",");
            }
        }
        String ids = idsStringBuilder.toString();
        return commodityMapper.findCommoditiesByCategoryIds(ids, (page-1)*pageSize, pageSize);
    }
    public List<Category> getFinalCategories(Long parentId) {
        List<Category> leafCategories = new ArrayList<>();
        List<Category> categories = categoryMapper.getChildByParentId(parentId);
        for (Category category : categories) {
            // 判断是否有子分类
            if (categoryMapper.getChildByParentId(category.getId()).isEmpty()) {
                leafCategories.add(category);
            } else {
                // 递归获取子类目的最后一级类目
                leafCategories.addAll(getFinalCategories(category.getId()));
            }
        }
        return leafCategories;
    }


}


