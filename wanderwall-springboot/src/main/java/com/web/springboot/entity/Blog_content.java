package com.web.springboot.entity;
import lombok.Data;

@Data
public class Blog_content {
    private Integer Paragraph_id;
    private String type;
    private String content;
    private String url;
    private Integer Blog_id;
}
