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
        return categoryMapper.getById(id);
    }
    public Category extractById(Long id) {
        return categoryMapper.extractById(id);
    }
    public int insert(Category category) {
        return categoryMapper.insert(category);
    }
    public int update(Category category) {
        return categoryMapper.update(category);
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

    public int delete(Long id) {
        Category existingCategory = categoryMapper.getById(id);
        if (existingCategory == null) {
            throw new RuntimeException("商品ID不存在");
        }

        int timestamp = (int) (System.currentTimeMillis() / 1000);
        return categoryMapper.delete(id, timestamp);
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



}


