package com.web.springboot.entity;

import lombok.Data;

@Data
public class Blog {
    private Integer blog_id;
    private Integer id;
    private String cover;
    private String username;
    private String province;
    private String city;
    private String area;
    private String title;
    private String date;
}