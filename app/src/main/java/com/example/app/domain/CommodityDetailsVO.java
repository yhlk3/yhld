package com.example.app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommodityDetailsVO {
    private String contentType;
    private String contentValue;
    private int sequence;
}
