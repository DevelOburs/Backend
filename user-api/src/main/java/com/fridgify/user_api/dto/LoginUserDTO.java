package com.fridgify.user_api.dto;

import lombok.Data;

@Data
public class LoginUserDTO {

    private String username;
    private String email;
    private String password;
}
