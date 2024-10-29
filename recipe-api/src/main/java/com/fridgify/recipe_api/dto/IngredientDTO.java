package com.fridgify.recipe_api.dto;

import com.fridgify.recipe_api.model.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDTO {
    private long id;
    private String name;

    public Ingredient toModel() {
        return Ingredient.builder()
                .name(this.name)
                .build();
    }
}
