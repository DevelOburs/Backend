package com.fridgify.recipe_api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSavedRecipeDTO {
    private long RecipeId;
    private long UserId;
}
