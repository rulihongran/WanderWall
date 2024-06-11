package com.web.springboot.entity;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String username;
    private String nickname;
    private String password;
    private String sex;
    private String phone;
    private String email;
    private String address;
    private String signature;
    private String birsday;
    private String work;
    private String habit;
    private String bucket;
    private String object;
}