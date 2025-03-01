package com.example.console.controller;

import com.alibaba.fastjson.JSON;
import com.example.console.domain.BaseContentValueVo;
import com.example.module.utils.Response;
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

public class CommodityController {
    @Autowired
    private CommodityService commodityService;
    @RequestMapping("/commodity/insert")
    public Response insertCommodity(@RequestParam("title") String title,
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
            commodityService.edit(id, title, price, location, introduction,images,categoryId);
            return new Response(1001);
            } catch (Exception e) {
            return new Response(3055,null);
        }

    }
    @RequestMapping("/commodity/update")
    public Response updateCommodity(@RequestParam("id") Long id,
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
            commodityService.edit(id, title, price, location, introduction, images, categoryId);
            return new Response(1001);

        } catch (Exception e) {
            return new Response(3056);
        }
    }
    @RequestMapping("/commodity/delete")
    public Response deleteCommodity(@RequestParam Long id) {
        try {
            commodityService.delete(id);
            return new Response(1001);
        } catch (RuntimeException e) {
            return new Response(3057);
        }
    }
    @RequestMapping("/commodity/list")
    public Response list(@RequestParam("page") int page) {
        int pageSize = 5;
        List<Commodity> commodities = null;
        try {
            commodities = commodityService.getCommoditiesByPage(page, pageSize);
        } catch (Exception e) {
            return new Response(3053);
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
        return new Response(1001, response);
    }
    @RequestMapping("/commodity/info")
    public Response info(@RequestParam Long id) {
        Commodity commodity = null;
        try {
            commodity = commodityService.getById(id);
        } catch (Exception e) {
            return new Response(3052);
        }

        CommodityInfoVO commodityInfoVO = new CommodityInfoVO();
        List<BaseContentValueVo> contents = JSON.parseArray(commodity.getDetails(), BaseContentValueVo.class);
        commodityInfoVO.setDetails(contents);
        commodityInfoVO.setPrice(commodity.getPrice());
        commodityInfoVO.setTitle(commodity.getTitle());
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
        return new Response(1001, commodityInfoVO);
    }

    @RequestMapping(value = "/commodity/extract")
    public Response extractCommodityById(@RequestParam Long id) {
        try {
           Commodity commodity =  commodityService.extractById(id);
           return new Response(1001,commodity);
        } catch (Exception e) {
            return null;
        }
    }
}
