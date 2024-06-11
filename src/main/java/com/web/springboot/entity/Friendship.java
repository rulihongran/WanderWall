package com.web.springboot.entity;
import lombok.Data;

@Data
public class Friendship {
    private String username;
    private String friend_username;
    private Integer friend_id;
}
