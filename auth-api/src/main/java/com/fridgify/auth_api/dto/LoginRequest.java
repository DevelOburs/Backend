package com.fridgify.auth_api.dto;

import com.fridgify.auth_api.common.UserDTO;
import lombok.Getter;

@Getter
public class LoginRequest {
    private String username;
    private String email;
    private String password;
}

