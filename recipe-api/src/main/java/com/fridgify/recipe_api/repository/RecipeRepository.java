package com.fridgify.recipe_api.repository;

import com.fridgify.recipe_api.model.Recipe;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Modifying
    @Query("UPDATE Recipe r SET r.likeCount = :likeCount WHERE r.id = :recipeId")
    void updateLikeCount(@Param("recipeId") Long recipeId, @Param("likeCount") Long likeCount);

    @Modifying
    @Query("UPDATE Recipe r SET r.commentCount = :commentCount WHERE r.id = :recipeId")
    void updateCommentCount(@Param("recipeId") Long recipeId, @Param("commentCount") Long commentCount);
}
