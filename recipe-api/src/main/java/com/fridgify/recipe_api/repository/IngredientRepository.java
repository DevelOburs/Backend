package com.fridgify.recipe_api.repository;

import com.fridgify.recipe_api.model.Ingredient;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    @Override
    List<Ingredient> findAll();

    Ingredient findIngredientById(Long id);

    List<Ingredient> findIngredientsByName(String name);
}
