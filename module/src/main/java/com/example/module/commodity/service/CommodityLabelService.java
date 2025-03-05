package com.example.module.commodity.service;

import com.example.module.commodity.entity.CommodityLabelRelationship;
import com.example.module.commodity.mapper.CommodityLabelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommodityLabelService {
    @Autowired
    private CommodityLabelMapper commodityLabelMapper;
    public CommodityLabelRelationship getById(Long id) {
        return commodityLabelMapper.getById(id);
    }
    public CommodityLabelRelationship extractById(Long id) {
        return commodityLabelMapper.extractById(id);
    }
    public Long insert(Long commodityId,Long labelId) {
        int timestamp = (int) (System.currentTimeMillis() / 1000);
        CommodityLabelRelationship commodityLabelRelationship = new CommodityLabelRelationship();
        commodityLabelRelationship.setCommodityId(commodityId);
        commodityLabelRelationship.setLabelId(labelId);
        commodityLabelRelationship.setCreateTime(timestamp);
        commodityLabelRelationship.setUpdateTime(timestamp);
        commodityLabelRelationship.setIsDeleted(0);
        commodityLabelMapper.insert(commodityLabelRelationship);
        return commodityLabelRelationship.getId();
    }
    public int update(CommodityLabelRelationship commodityLabelRelationship) {
        return commodityLabelMapper.update(commodityLabelRelationship);
    }
    public int delete(Long id) {
        return commodityLabelMapper.delete(id);
    }
    @Transactional
    public void checkCommodityIdAndLabelIdS(Long commodityId, List<Long> labelIds) {
        for (Long labelId : labelIds) {
            Long id = commodityLabelMapper.getIdByCommodityIdAndLabelId(commodityId, labelId);
            if (id == null) {
                insert(commodityId, labelId);
            }
        }
        List<Long> ids = commodityLabelMapper.getLabelIdsByCommodityId(commodityId);
        for (Long id : ids) {
            if (!labelIds.contains(id)) {
                commodityLabelMapper.delete(id);
            }
        }
    }
}
