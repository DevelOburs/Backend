package com.fridgify.user_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseUserDTO {

    private String username;
    private String email;
    private String firstName;
    private String lastName;
}
