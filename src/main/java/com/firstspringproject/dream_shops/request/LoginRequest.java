package com.firstspringproject.dream_shops.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
