package com.fridgify.recipe_api.repository;

import com.fridgify.recipe_api.model.Recipe;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    @Override
    List<Recipe> findAll();

    // Find all recipes by name (Spring Data JPA will automatically implement this)
    List<Recipe> findByName(String name);

    // Example: Find recipes that have a specific ingredient
    List<Recipe> findByDescriptionContaining(String descriptionWords);

    List<Recipe> findRecipesByUserId(Long userId);
}
