package com.fridgify.user_api.dto;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponseUserDTO {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
}
