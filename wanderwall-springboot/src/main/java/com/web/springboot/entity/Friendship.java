package com.web.springboot.entity;
import lombok.Data;

@Data
public class Friendship {
    private Integer friendship_id;
    private Integer userid;
    private String username;
    private String friend_username;
    private Integer friend_id;
}
