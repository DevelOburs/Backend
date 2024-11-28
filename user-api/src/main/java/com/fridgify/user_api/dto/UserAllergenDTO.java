package com.fridgify.user_api.dto;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserAllergenDTO {
    private Long userId;
    private Long allergenId;
}
