package com.fridgify.auth_api.dto;

import com.fridgify.auth_api.common.UserDTO;
import lombok.Data;

@Data
public class RegisterRequest {
    // DTO for registration requests
    private UserDTO user;

}
