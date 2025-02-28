package com.example.module.details.service;

import com.example.module.details.entity.CommodityDetails;
import com.example.module.details.mapper.CommodityDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommodityDetailsService {

    @Autowired
    private CommodityDetailsMapper commodityDetailsMapper;

    public CommodityDetails getById(Long id) {
        return  commodityDetailsMapper.getById(id);
    }
    public CommodityDetails extractById(Long id) {
        return  commodityDetailsMapper.extractById(id);
    }
    public int insert(CommodityDetails commodityDetails) {
        return commodityDetailsMapper.insert(commodityDetails);
    }
    public int update(CommodityDetails commodityDetails) {
        return commodityDetailsMapper.update(commodityDetails);
    }
    public int delete(Long id) {
        return commodityDetailsMapper.delete(id);
    }
    public List<CommodityDetails> getByCommodityId(Long commodityId) {
        return commodityDetailsMapper.getByCommodityId(commodityId);
    }
}
