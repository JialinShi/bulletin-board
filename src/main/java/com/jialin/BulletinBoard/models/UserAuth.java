package com.jialin.BulletinBoard.models;

import lombok.Data;

@Data
public class UserAuth {
    private String userName;
    private String email;
    private String password;
}
