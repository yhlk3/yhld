package com.example.app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommodityListResponse {
    private List<CommodityListVO> list;
    private Boolean isEnd;
    private String wp;
}
