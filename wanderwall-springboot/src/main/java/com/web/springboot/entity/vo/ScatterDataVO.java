package com.web.springboot.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class ScatterDataVO {

    private String name;

    private List<Float> value;
}
