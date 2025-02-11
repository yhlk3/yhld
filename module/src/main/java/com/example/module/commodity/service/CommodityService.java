package com.example.module.commodity.service;

import com.example.module.commodity.entity.Commodity;
import com.example.module.commodity.mapper.CommodityMapper;
import com.example.module.entity.CommodityCategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CommodityService {
    @Autowired
    private CommodityMapper commodityMapper;

    public Commodity getById(Long id) {
        if (id == null) {
            throw new RuntimeException("商品ID不能为空");
        }
        Commodity commodity = commodityMapper.getById(id);
        if (commodity == null) {
            throw new RuntimeException("商品ID不存在");
        }
        return commodity;
    }

    public Long edit(Long id, String title, Integer price, String location, String introduction, String images,Long categoryId) {
        if (title == null || title.isEmpty()) {
            throw new RuntimeException("标题不能为空");
        }
        if (price == null || price <= 0) {
            throw new RuntimeException("价格必须大于0");
        }
        if (location == null || location.isEmpty()) {
            throw new RuntimeException("位置不能为空");
        }
        if (introduction == null || introduction.isEmpty()) {
            throw new RuntimeException("介绍不能为空");
        }
        if (images == null || images.isEmpty()) {
            throw new RuntimeException("图片不能为空");
        }
        if (categoryId == null) {
            throw new RuntimeException("分类ID不能为空");
        }

        int timestamp = (int) (System.currentTimeMillis() / 1000);
        Commodity commodity = new Commodity();
        commodity.setTitle(title);
        commodity.setPrice(price);
        commodity.setLocation(location);
        commodity.setIntroduction(introduction);
        commodity.setImages(images);
        commodity.setIsDeleted(0);
        commodity.setCategoryId(categoryId);

        if (id != null) {
            Commodity existingCommodity = commodityMapper.getById(id);
            if (existingCommodity == null) {
                throw new RuntimeException("商品ID不存在");
            }
            commodity.setId(id);
            commodity.setUpdateTime(timestamp);
            commodityMapper.update(commodity);
            return id;
        } else {
            commodity.setCreateTime(timestamp);
            commodity.setUpdateTime(timestamp);
            commodityMapper.insert(commodity);
            return commodity.getId();
        }
    }
    public int delete(Long id) {
        if (id == null) {
            throw new RuntimeException("商品ID不能为空");
        }
        Commodity existingCommodity = commodityMapper.getById(id);
        if (existingCommodity == null) {
            throw new RuntimeException("商品ID不存在");
        }

        int timestamp = (int) (System.currentTimeMillis() / 1000);
        return commodityMapper.delete(id, timestamp);
    }
    public List<Commodity> getCommoditiesByPage(int page,int pageSize) {

        int offset = (page - 1) * pageSize;
        return commodityMapper.findPage(offset, pageSize);
    }

    public int getTotalCommodities() {
        return commodityMapper.count();
    }
    public List<Commodity> getCommoditiesByKeywordAndPage(String keyword, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<Long> id = commodityMapper.getIdsByKeyword(keyword);
        StringBuilder idsStringBuilder = new StringBuilder();
        for (int i = 0; i < id.size(); i++) {
            idsStringBuilder.append(id.get(i));
            if (i < id.size() - 1) {
                idsStringBuilder.append(",");
            }
        }
        String ids = idsStringBuilder.toString();
        return commodityMapper.findByKeywordAndPage(keyword, offset, pageSize, ids);
    }

    public Commodity extractById(Long id) {
        if (id == null) {
            throw new RuntimeException("商品ID不能为空");
        }
        Commodity commodity = commodityMapper.extractById(id);
        if (commodity == null) {
            throw new RuntimeException("商品ID不存在");
        }
        return commodity;
    }
    public int deleteByCategoryId(Long categoryId) {
        if (categoryId == null) {
            throw new RuntimeException("分类ID不能为空");
        }
        return commodityMapper.deleteByCategoryId(categoryId);
    }

    public static double calculateAspectRatioFromRegex(String imageUrl) {
        // 正则表达式匹配图片的高和宽
        String regex = "_(\\d+)x(\\d+)\\.";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(imageUrl);

        if (matcher.find()) {
            int width = Integer.parseInt(matcher.group(1));
            int height = Integer.parseInt(matcher.group(2));

            // 避免除数为0的情况
            if (height == 0) {
                return 1.0; // 默认值
            }

            // 计算宽高比
            return (double) width / height;
        } else {
            return 1.0; // 默认值
        }
    }

}