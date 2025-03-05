package com.example.module.commodity.service;

import com.example.module.commodity.entity.Label;
import com.example.module.commodity.mapper.LabelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class LabelService {
    @Autowired
    private LabelMapper labelMapper;
    public Label getById(Long id) {
        return labelMapper.getById(id);
    }
    public Label extractById(Long id) {
        return labelMapper.extractById(id);
    }
    public Long insert(String name){
        int timestamp = (int) (System.currentTimeMillis() / 1000);
        Label label = new Label();
        label.setName(name);
        label.setCreateTime(timestamp);
        label.setUpdateTime(timestamp);
        label.setIsDeleted(0);
        labelMapper.insert(label);
        return label.getId();
    }
    public int update(Label label) {
        return labelMapper.update(label);
    }
    public int delete(Long id) {
        return labelMapper.delete(id);
    }

    //通过标签名获取id
    public Long getIdByName(String name){
        return labelMapper.getIdByName(name);
    }

    public List<Long> processLabels(String[] names){
        List<Long> ids = new ArrayList<>();
        for (String name : names) {
            Long id = getIdByName(name);
            if (id != null) {
                ids.add(id);
            }
            if (id == null) {
                insert(name);
                ids.add(getIdByName(name));
            }
        }
        return ids;
    }
}
