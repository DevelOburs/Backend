package com.fridgify.recipe_api.repository;

import com.fridgify.recipe_api.model.RecipeComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecipeCommentRepository extends JpaRepository<RecipeComment, Long> {
    List<RecipeComment> findByRecipeIdOrderByCreatedAtDesc(Long recipeId);

    Optional<RecipeComment> findById(Long id);
}
