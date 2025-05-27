package com.mock.mocktopus.payload;
import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}