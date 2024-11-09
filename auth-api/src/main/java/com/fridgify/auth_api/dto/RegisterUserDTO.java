package com.fridgify.auth_api.dto;

import lombok.Data;

@Data
public class RegisterUserDTO {

    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
