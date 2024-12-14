package com.fridgify.recipe_api.repository;

import com.fridgify.recipe_api.model.Ingredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    Page<Ingredient> findAll(Specification<Ingredient> spec, Pageable pageable);

    @Query("SELECT i FROM Ingredient i WHERE i.id NOT IN " +
            "(SELECT ri.ingredient.id FROM RecipeIngredient ri WHERE ri.recipe.id = :recipeId)")
    Page<Ingredient> findIngredientsNotInRecipe(@Param("recipeId") Long recipeId, Pageable pageable);

    @Query("SELECT i FROM Ingredient i WHERE i.id NOT IN :ingredientIds")
    List<Ingredient> findNotInFridge(@Param("ingredientIds") List<Long> ingredientIds);

    @Query("SELECT i FROM Ingredient i WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Ingredient> findByNameContainingIgnoreCase(@Param("name") String name);

    List<Ingredient> findByIdIn(List<Long> ingredientIds);

    @Query("SELECT i FROM Ingredient i JOIN RecipeIngredient ri ON i.id = ri.ingredient.id WHERE ri.recipe.id = :recipeId")
    Page<Ingredient> findIngredientsByRecipeId(Long recipeId, Pageable pageable);
}
