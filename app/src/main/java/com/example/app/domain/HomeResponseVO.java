package com.example.app.domain;

import com.example.module.banner.entity.Banner;
import com.example.module.event.entity.Event;
import lombok.Data;

import java.util.List;
@Data

public class HomeResponseVO {
    private List<Banner> banners;
    private List<CategoryVO> categories;
    private List<Event> events;
    private List<CommodityListVO> commodities;
}
