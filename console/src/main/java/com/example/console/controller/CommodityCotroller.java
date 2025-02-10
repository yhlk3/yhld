package com.example.console.controller;

import com.example.console.domain.CommodityInfoVO;
import com.example.console.domain.CommodityListResponse;
import com.example.console.domain.CommodityListVO;
import com.example.module.commodity.entity.Commodity;
import com.example.module.commodity.service.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController

public class CommodityCotroller {
    @Autowired
    private CommodityService commodityService;

    @RequestMapping("/commodity/insert")
    public Long insertCommodity(@RequestParam("title") String title,
                                @RequestParam(required = false) Long id,
                                @RequestParam("price") Integer price,
                                @RequestParam("location") String location,
                                @RequestParam("introduction") String introduction,
                                @RequestParam("images") String images,
                                @RequestParam("categoryId") Long categoryId) {
        title = title.trim();
        location = location.trim();
        introduction = introduction.trim();

        try {
            return commodityService.edit(id, title, price, location, introduction,images,categoryId);
        } catch (Exception e) {
            return null;
        }
    }
    @RequestMapping("/commodity/update")
    public Long updateCommodity(@RequestParam("id") Long id,
                                @RequestParam("title") String title,
                                @RequestParam( "price") Integer price,
                                @RequestParam("location") String location,
                                @RequestParam("introduction") String introduction,
                                @RequestParam("images") String images,
                                @RequestParam("categoryId") Long categoryId) {
        title = title.trim();
        location = location.trim();
        introduction = introduction.trim();
        try {
            return commodityService.edit(id, title, price, location, introduction, images, categoryId);

        } catch (Exception e) {
            return null;
        }
    }
    @RequestMapping("/commodity/delete")
    public String deleteCommodity(@RequestParam Long id) {
        try {
            int result = commodityService.delete(id);
            return result == 1 ? "成功" : "失败";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
    @RequestMapping("/commodity/list")
    public CommodityListResponse list(@RequestParam("page") int page) {
        int pageSize = 5;
        List<Commodity> commodities = null;
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
            vo.setImage(imagesArray[0]);
            vo.setPrice(commodity.getPrice());
            vo.setTitle(commodity.getTitle());
            vo.setLocation(commodity.getLocation());
            voList.add(vo);
        }
        CommodityListResponse response = new CommodityListResponse();
        response.setList(voList);
        response.setTotal(commodityService.getTotalCommodities());
        response.setPageSize(pageSize);
        return response;
    }
    @RequestMapping("/commodity/info")
    public CommodityInfoVO info(@RequestParam Long id) {
        Commodity commodity = null;
        try {
            commodity = commodityService.getById(id);
        } catch (Exception e) {
            return null;
        }
        CommodityInfoVO commodityInfoVO = new CommodityInfoVO();
        commodityInfoVO.setPrice(commodity.getPrice());
        commodityInfoVO.setTitle(commodity.getTitle());
        commodityInfoVO.setIntroduction(commodity.getIntroduction());
        commodityInfoVO.setImages(Arrays.asList(commodity.getImages().split("\\$")));
        Integer timestamp = commodity.getCreateTime();
        long createTimeMillis = timestamp.longValue() * 1000L;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(new Date(createTimeMillis));
        commodityInfoVO.setCreateTime(formattedDate);
        Integer timestamp0 = commodity.getUpdateTime();
        long updateTimeMillis = timestamp0.longValue() * 1000L;
        SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate0 = sdf0.format(new Date(updateTimeMillis));
        commodityInfoVO.setUpdateTime(formattedDate0);
        return commodityInfoVO;
    }

    @RequestMapping(value = "/commodity/extractbyid")
    public Commodity extractCommodityById(@RequestParam Long id) {
        try {
            return commodityService.extractCommodityById(id);
        } catch (Exception e) {
            return null;
        }
    }
}
