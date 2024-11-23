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
    private String category;
    private String imageUrl;

    public IngredientDTO(Ingredient ingredient) {
        if (ingredient == null) {
            return;
        }
        this.id = ingredient.getId();
        this.name = ingredient.getName();
        this.category = ingredient.getCategory();
        this.imageUrl = ingredient.getImageUrl();
    }

    public Ingredient toModel() {
        return Ingredient.builder()
                .name(this.name)
                .category(this.category)
                .imageUrl(this.imageUrl)
                .build();
    }
}
