package com.example.app.controller;

import com.alibaba.fastjson.JSON;
import com.example.app.domain.*;
import com.example.module.category.entity.Category;
import com.example.module.category.service.CategoryService;
import com.example.module.commodity.entity.Commodity;
import com.example.module.commodity.service.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.commons.codec.binary.Base64;


import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
public class CommodityController {

    @Autowired
    private CommodityService commodityService;
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/commodity/search")
    public CommodityListResponse search(@RequestParam(value = "keyword", required = false) String keyword,
                                        @RequestParam(value = "wp", required = false) String encodedWpString) {
        int pageSize = 5;
        CommodityWp wp = null;
        if (encodedWpString != null && !encodedWpString.isEmpty()) {
            // 解码
            byte[] decodedBytes = Base64.decodeBase64(encodedWpString);
            String jsonWpString = new String(decodedBytes, StandardCharsets.UTF_8);
            wp = JSON.parseObject(jsonWpString, CommodityWp.class);
        }
        if (wp == null || wp.getPage() == null) {
            wp = new CommodityWp();
            // 默认值
            wp.setPage(1);
            wp.setKeyword(keyword);
        }
        List<Commodity> commodities = commodityService.getCommoditiesByKeywordAndPage(wp.getKeyword(), wp.getPage(), pageSize);
        StringBuilder idsStringBuilder = new StringBuilder();
        for (Commodity commodity : commodities) {
            if (idsStringBuilder.length() > 0) {
                idsStringBuilder.append(",");
            }
            idsStringBuilder.append(commodity.getCategoryId());
        }
        String ids = idsStringBuilder.toString();
        List<Category> categories = categoryService.getCategoriesByIds(ids);
        Map<Long, String> categoryMap = new HashMap<>();
        for (Category category : categories) {
            categoryMap.put(category.getId(), category.getName());
        }
        List<CommodityListVO> voList = new ArrayList<>();
        for (Commodity commodity : commodities) {
            CommodityListVO vo = new CommodityListVO();
            vo.setId(commodity.getId());
            String[] imagesArray = commodity.getImages().split("\\$");
            try {
                double ar = commodityService.calculateAspectRatioFromRegex(imagesArray[0]);
                vo.setImage(new ImageInfoVO(imagesArray[0], ar));
            } catch (Exception e) {
                e.printStackTrace();
            }
            vo.setPrice(commodity.getPrice());
            vo.setTitle(commodity.getTitle());
            vo.setLocation(commodity.getLocation());
            String categoryName = categoryMap.get(commodity.getCategoryId());
            if (categoryName != null) {
                vo.setCategory(categoryName);
            }
            else {continue;}
            voList.add(vo);
        }
        boolean isEnd = commodities.size() < pageSize;
        CommodityListResponse response = new CommodityListResponse();
        response.setList(voList);
        response.setIsEnd(isEnd);
        wp.setPage(wp.getPage() + 1);
        // 编码
        String jsonWpString = JSON.toJSONString(wp);
        String encodedWpStringResponse = Base64.encodeBase64URLSafeString(jsonWpString.getBytes(StandardCharsets.UTF_8));
        response.setWp(encodedWpStringResponse);
        return response;
    }
    @RequestMapping("/commodity/list")
    public CommodityListResponse list(@RequestParam("page") int page) {
        int pageSize = 5;
        List<Commodity> commodities;
        try {
            commodities = commodityService.getCommoditiesByPage(page, pageSize);
        } catch (Exception e) {
            return null;
        }
        List<CommodityListVO> voList = new ArrayList<>();
        for (Commodity commodity : commodities) {
            CommodityListVO vo = new CommodityListVO();
            vo.setId(commodity.getId());
            String[] imagesArray = commodity.getImages().split("\\$");
            double ar = 1.0; // 默认值
            if (imagesArray != null && imagesArray.length > 0) {
                ar = commodityService.calculateAspectRatioFromRegex(imagesArray[0]);
            }
            vo.setImage(new ImageInfoVO(imagesArray[0], ar));
            vo.setPrice(commodity.getPrice());
            vo.setTitle(commodity.getTitle());
            vo.setLocation(commodity.getLocation());
            Category category = categoryService.getById(commodity.getCategoryId());
            if (category != null) {
                vo.setCategory(category.getName());
            } else {
                break;
            }
            voList.add(vo);
        }
        boolean isEnd = commodities.size() < pageSize;
        CommodityListResponse response = new CommodityListResponse();
        response.setList(voList);
        response.setIsEnd(isEnd);
        return response;
    }

    @RequestMapping("/commodity/info")
    public CommodityInfoVO info(@RequestParam Long id) {
        Commodity commodity =  commodityService.getById(id);
        if (commodity == null ) {
            return null;
        }
        Category category = categoryService.getById(commodity.getCategoryId());
        CommodityInfoVO vo = new CommodityInfoVO();
        vo.setPrice(commodity.getPrice());
        vo.setTitle(commodity.getTitle());
        vo.setIntroduction(commodity.getIntroduction());
        vo.setImages(Arrays.asList(commodity.getImages().split("\\$")));
        vo.setCategory(category.getName());
        vo.setCategoryImage(category.getImage());
        return vo;
    }
}