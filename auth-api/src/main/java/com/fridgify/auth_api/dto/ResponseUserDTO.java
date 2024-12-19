package com.fridgify.auth_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseUserDTO {
    private Long userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private List<String> roles;
}
