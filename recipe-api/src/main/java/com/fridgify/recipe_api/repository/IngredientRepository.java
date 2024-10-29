package com.fridgify.recipe_api.repository;

import com.fridgify.recipe_api.model.Ingredient;
import com.fridgify.recipe_api.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    @Override
    List<Ingredient> findAll();

    Ingredient findIngredientById(Long id);

    List<Ingredient> findIngredientsByName(String name);
}
