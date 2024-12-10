package com.fridgify.recipe_api.repository;

import com.fridgify.recipe_api.model.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
    void deleteByRecipeId(Long recipeId);

}