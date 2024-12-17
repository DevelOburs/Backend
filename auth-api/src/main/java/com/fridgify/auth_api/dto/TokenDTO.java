package com.fridgify.auth_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDTO {
    private String token;
    private Long userId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private List<String> roles;
    private String error;
}
