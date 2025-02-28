package com.example.app.domain;

import lombok.Data;
import java.util.List;
@Data
public class  CommodityInfoVO {
    private Integer price;
    private String title;
    private List<CommodityDetailsVO> introduction;
    private List<String> images;
    private String category;
    private String categoryImage;


}
