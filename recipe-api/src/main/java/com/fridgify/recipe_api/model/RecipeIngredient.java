package com.fridgify.recipe_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(RecipeIngredient.RecipeIngredientId.class)
public class RecipeIngredient {

    @Id
    @Column(name = "recipe_id")
    private Long recipeId;

    @Id
    @Column(name = "ingredient_id")
    private Long ingredientId;

    @ManyToOne
    @JoinColumn(name = "recipe_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "ingredient_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Ingredient ingredient;

    @Column(nullable = false)
    private String quantity;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecipeIngredientId implements Serializable {
        private Long recipeId;
        private Long ingredientId;
    }
}