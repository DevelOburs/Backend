package com.fridgify.recipe_api.repository;

import com.fridgify.recipe_api.model.UserSavedRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserSavedRecipeRepository extends JpaRepository<UserSavedRecipe, UserSavedRecipe.UserSavedRecipeId> {
    Optional<UserSavedRecipe> findByIdRecipeIdAndIdUserId(long recipeId, long userId);

    long countByIdRecipeId(long recipeId);

    @Query("SELECT us.id.recipeId FROM UserSavedRecipe us WHERE us.id.userId = :userId")
    List<Long> findRecipeIdsByUserId(@Param("userId") Long userId);
}
