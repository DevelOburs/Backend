package com.fridgify.user_api.dto;

import lombok.*;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponseUserDTO {
    private Long userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private List<String> roles;
}
