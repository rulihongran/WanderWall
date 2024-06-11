package com.web.springboot.controler.dto;

import lombok.Data;

@Data
public class userdto 
{
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String avatarUrl;
    private String token;
    private String role;
}
