package com.fridgify.auth_api.dto;

import lombok.Data;

@Data
public class ChangePasswordUserDTO {
    private String username;
    private String password;
    private String newPassword;
}
