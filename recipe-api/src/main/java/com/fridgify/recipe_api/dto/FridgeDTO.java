package com.fridgify.recipe_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FridgeDTO {
    private Long userId;
    private List<IngredientDTO> ingredients;
}
