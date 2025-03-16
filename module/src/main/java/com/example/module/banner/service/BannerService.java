package com.example.module.banner.service;

import org.springframework.stereotype.Service;
import com.example.module.banner.entity.Banner;

import java.util.Arrays;
import java.util.List;

@Service
public class BannerService {
    public List<Banner> getBanners() {
        return Arrays.asList(
                new Banner("https://example.com/banner1.jpg", "/product/1"),
                new Banner("https://example.com/banner2.jpg", "/event/spring")
        );
    }
}