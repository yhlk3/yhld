package com.example.app.controller;


import com.example.app.domain.*;
import com.example.module.utils.Response;
import com.example.module.category.entity.Category;
import com.example.module.category.service.CategoryService;
import com.example.module.commodity.entity.Commodity;
import com.example.module.commodity.service.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CommodityService commodityService;

    @RequestMapping("/category/list")
    public Response list() {
        List<Category> categories = categoryService.getTopCategories();
        List<CategoryListVO> voList = new ArrayList<>();
        for (Category category : categories) {
            CategoryListVO vo = new CategoryListVO();
            vo.setId(category.getId());
            vo.setName(category.getName());
            vo.setImage(category.getImage());
            List<Category> children = categoryService.getChildByParentId(category.getId());
            List<CategoryChildrenVO> childrenVO = new ArrayList<>();
            for (Category child : children) {
                CategoryChildrenVO childrenList = new CategoryChildrenVO();
                childrenList.setId(child.getId());
                childrenList.setName(child.getName());
                childrenList.setImage(child.getImage());
                childrenVO.add(childrenList);
            }
            vo.setChildren(childrenVO);
            voList.add(vo);
        }
        CategoryListResponse response = new CategoryListResponse();
        response.setList(voList);
        Response result = new Response(1001, response);
        return result;
    }
    @RequestMapping("/category/list/info")
    public Response listInfo(@RequestParam("id") Long id, @RequestParam("page") int page) {
        double ar = 1.0; // 默认值
        int pageSize = 5;
        List<Category> Children =categoryService.getChildByParentId(id);
        List<CategoryChildrenVO> vo  = new ArrayList<>();
        for (Category category : Children){
            CategoryChildrenVO childrenVO = new CategoryChildrenVO();
            childrenVO.setId(category.getId());
            childrenVO.setName(category.getName());
            childrenVO.setImage(category.getImage());
            vo.add(childrenVO);
        }
        CategoryWithCommodityVO CategoryWithCommodity = new CategoryWithCommodityVO();
        List<CommodityListVO> commodityList = new ArrayList<>();
        List<Commodity> Commodities = categoryService.getLeafCategoriesWithCommodities(id, page, pageSize);
        for (Commodity commodity : Commodities) {
            CommodityListVO commodityListVO = new CommodityListVO();
            commodityListVO.setId(commodity.getId());
            String[] imagesArray = commodity.getImages().split("\\$");
            if (imagesArray != null && imagesArray.length > 0) {
                ar = commodityService.calculateAspectRatioFromRegex(imagesArray[0]);
            }
            commodityListVO.setImage(new ImageInfoVO(imagesArray[0], ar));
            commodityListVO.setPrice(commodity.getPrice());
            commodityListVO.setTitle(commodity.getTitle());
            commodityListVO.setLocation(commodity.getLocation());
            Category category = categoryService.getById(commodity.getCategoryId());
            commodityListVO.setCategory(category.getName());
            commodityList.add(commodityListVO);
        }
        CategoryWithCommodity.setCategoryList(vo);
        CategoryWithCommodity.setCommodityList(commodityList);
        Response result = new Response(1001, CategoryWithCommodity);
        return result;
    }
}