package com.web.springboot.entity;
import java.util.Date;
import lombok.Data;
@Data
public class Blog {
    private Integer Blog_id;
    private Integer id;
    private String cover;
    private String province;
    private String city;
    private String area;
    private String title;
    private Date date;
}
