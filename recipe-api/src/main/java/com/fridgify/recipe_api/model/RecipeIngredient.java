package com.fridgify.recipe_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RecipeIngredient {

    @EmbeddedId
    private RecipeIngredientId id;

    @ManyToOne
    @MapsId("recipeId")
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private Recipe recipe;

    @ManyToOne
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredient_id", referencedColumnName = "id")
    private Ingredient ingredient;

    @Column(nullable = false)
    private String quantity;

    public RecipeIngredient(Long recipeId, Long ingredientId, Recipe recipe, Ingredient ingredient, String quantity) {
        this.id = new RecipeIngredientId(recipeId, ingredientId);
        this.recipe = recipe;
        this.ingredient = ingredient;
        this.quantity = quantity;
    }

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecipeIngredientId implements Serializable {

        @Column(name = "recipe_id")
        private Long recipeId;

        @Column(name = "ingredient_id")
        private Long ingredientId;
    }
}
