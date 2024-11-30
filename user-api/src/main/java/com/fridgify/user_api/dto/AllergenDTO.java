package com.fridgify.user_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllergenDTO {
    private Long userId;
    private Long allergenId;
}
