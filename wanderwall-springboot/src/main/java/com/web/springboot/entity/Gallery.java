package com.web.springboot.entity;
import lombok.Data;

@Data
public class Gallery {
    private String username;
    private String dsc;
    private String src;
    private Boolean isediting;
}