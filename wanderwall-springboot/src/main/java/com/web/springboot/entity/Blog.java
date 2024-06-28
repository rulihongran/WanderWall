package com.web.springboot.entity;

import lombok.Data;

import java.util.List;

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

    /**
     * 额外返回
     */
    private List<Blog_content> paragh;
    private String content;

    /**
     * 额外查询如此
     */
    private String time;
    private String location;
}