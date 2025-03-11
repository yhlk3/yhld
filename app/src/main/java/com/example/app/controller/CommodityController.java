package com.example.app.controller;

import com.alibaba.fastjson.JSON;
import com.example.app.domain.*;
import com.example.module.utils.Response;
import com.example.module.category.entity.Category;
import com.example.module.category.service.CategoryService;
import com.example.module.commodity.entity.Commodity;
import com.example.module.commodity.service.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.commons.codec.binary.Base64;


import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
public class CommodityController {

    @Autowired
    private CommodityService commodityService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @RequestMapping("/commodity/search")
    public Response search(@RequestParam(value = "keyword", required = false) String keyword,
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
        // 构建缓存键
        String cacheKey = "commodity:search:" + wp.getKeyword() + ":" + wp.getPage();
        // 检查缓存
        CommodityListResponse cachedResponse = (CommodityListResponse) redisTemplate.opsForValue().get(cacheKey);
        if (cachedResponse != null) {
            Response result = new Response(1001, cachedResponse);
            return result;
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
        redisTemplate.opsForValue().set(cacheKey, response, 3600, TimeUnit.SECONDS);
        Response result = new Response(1001, response);
        return result;
    }
    @RequestMapping("/commodity/list")
    public Response list(@RequestParam("page") int page) {
        int pageSize = 5;
        List<Commodity> commodities;
        try {
            commodities = commodityService.getCommoditiesByPage(page, pageSize);
        } catch (Exception e) {
            Response result = new Response(3053, null);
            return result;
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
        Response result = new Response(1001, response);
        return result;
    }

    @RequestMapping("/commodity/info")
    public Response info(@RequestParam Long id) {
        Commodity commodity =  commodityService.getById(id);
        if (commodity == null ) {
            Response result = new Response(3054, null);
            return result;
        }
        Category category = categoryService.getById(commodity.getCategoryId());
        CommodityInfoVO vo = new CommodityInfoVO();
        try {
            List<BaseContentValueVo> contents = JSON.parseArray(commodity.getDetails(), BaseContentValueVo.class);
            vo.setDetails(contents);
        } catch (Exception e) {
            return new Response(4004);
        }
        vo.setPrice(commodity.getPrice());
        vo.setTitle(commodity.getTitle());
        vo.setImages(Arrays.asList(commodity.getImages().split("\\$")));
        vo.setCategory(category.getName());
        vo.setCategoryImage(category.getImage());
        Response result = new Response(1001, vo);
        return result;
    }
}