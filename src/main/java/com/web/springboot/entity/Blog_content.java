package com.web.springboot.entity;
import lombok.Data;

@Data
public class Blog_content {
    private Integer Paragraph_id;
    private String type;
    private String content;
    private String bucket;
    private String object;
    private Integer Blog_id;
}
