package com.mock.mocktopus.payload;

import com.mock.mocktopus.model.Role;
import lombok.Data;

@Data
public class SignupRequest {
    private String username;
    private String email;
    private String password;
    private Role role;
}
