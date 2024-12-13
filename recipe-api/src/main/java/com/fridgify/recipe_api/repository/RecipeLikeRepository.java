package com.fridgify.recipe_api.repository;

import com.fridgify.recipe_api.model.RecipeLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecipeLikeRepository extends JpaRepository<RecipeLike, RecipeLike.RecipeLikeId> {
    Optional<RecipeLike> findByIdRecipeIdAndIdUserId(Long recipeId, Long userId);

    long countByIdRecipeId(Long recipeId);

    long countByIdUserId(Long userId);
}
