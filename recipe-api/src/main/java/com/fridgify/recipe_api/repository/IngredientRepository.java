package com.fridgify.recipe_api.repository;

import com.fridgify.recipe_api.model.Ingredient;
import com.fridgify.recipe_api.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    @Override
    List<Ingredient> findAll();

    Ingredient findIngredientById(Long id);

    List<Ingredient> findIngredientsByName(String name);
    List<Ingredient> findByNameIn(List<String> names);

    Optional<Ingredient> findByName(String name);

    @Query(value = "SELECT * FROM ingredient WHERE name = :ingredientName COLLATE utf8mb4_bin", nativeQuery = true)
    Optional<Ingredient> findByNameCaseSensitive(@Param("ingredientName") String ingredientName);
}
