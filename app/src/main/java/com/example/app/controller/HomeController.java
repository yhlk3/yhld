package com.example.app.controller;

import com.example.app.domain.CategoryVO;
import com.example.app.domain.CommodityListVO;
import com.example.app.domain.HomeResponseVO;
import com.example.app.domain.ImageInfoVO;
import com.example.module.banner.service.BannerService;
import com.example.module.category.service.CategoryService;
import com.example.module.commodity.entity.Commodity;
import com.example.module.event.entity.Event;
import com.example.module.banner.entity.Banner;
import com.example.module.commodity.service.CommodityService;
import com.example.module.event.service.EventService;
import com.example.module.utils.Response;
import com.example.module.category.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class HomeController {

    @Autowired
    private BannerService bannerService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private EventService eventService;

    @Autowired
    private CommodityService commodityService;

    @RequestMapping("/home")
    public Response getHomeData() throws ExecutionException, InterruptedException {
        HomeResponseVO response = new HomeResponseVO();

        // 并行获取基础数据
        CompletableFuture<List<Banner>> bannerFuture = CompletableFuture.supplyAsync(() -> bannerService.getBanners());
        CompletableFuture<List<Event>> eventFuture = CompletableFuture.supplyAsync(() -> eventService.getEvents());
        CompletableFuture<List<Category>> categoryFuture = CompletableFuture.supplyAsync(() -> categoryService.getTopCategories());
        CompletableFuture<List<Commodity>> commodityFuture = CompletableFuture.supplyAsync(() ->
                commodityService.getCommoditiesByPage(1, 5));

        // 等待所有任务完成
        CompletableFuture.allOf(bannerFuture, eventFuture, categoryFuture, commodityFuture).join();

        // 设置基础数据
        response.setBanners(bannerFuture.get());
        response.setEvents(eventFuture.get());

        // 分类数据转换
        List<Category> categoryList = categoryFuture.get();
        List<CategoryVO> categoryVOList = new ArrayList<>();
        for (Category category : categoryList) {
            CategoryVO categoryVO = new CategoryVO();
            categoryVO.setId(category.getId());
            categoryVO.setName(category.getName());
            categoryVO.setImage(category.getImage());
            categoryVOList.add(categoryVO);
        }
        response.setCategories(categoryVOList);

        // 商品数据处理
        List<Commodity> commodities = commodityFuture.get();
        StringBuilder idsStringBuilder = new StringBuilder();
        for (Commodity commodity : commodities) {
            if (idsStringBuilder.length() > 0) {
                idsStringBuilder.append(",");
            }
            idsStringBuilder.append(commodity.getCategoryId());
        }
        String ids = idsStringBuilder.toString();

        CompletableFuture<Map<Long, String>> categoryMapFuture = CompletableFuture.supplyAsync(() -> {
            List<Category> categories = categoryService.getCategoriesByIds(ids);
            Map<Long, String> map = new HashMap<>();
            for (Category category : categories) {
                map.put(category.getId(), category.getName());
            }
            return map;
        });

        // 保持原有商品VO转换逻辑
        Map<Long, String> categoryMap = categoryMapFuture.get();
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
            } else {
                continue;
            }
            voList.add(vo);
        }
        response.setCommodities(voList);

        return new Response(1001, response);
    }
}