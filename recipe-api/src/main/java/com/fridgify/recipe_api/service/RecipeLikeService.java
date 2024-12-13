package com.fridgify.recipe_api.service;

import com.fridgify.recipe_api.model.Recipe;
import com.fridgify.recipe_api.model.RecipeLike;
import com.fridgify.recipe_api.repository.RecipeLikeRepository;
import com.fridgify.recipe_api.repository.RecipeRepository;
import com.fridgify.recipe_api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Slf4j
@Service
public class RecipeLikeService {

    private final RecipeLikeRepository recipeLikeRepository;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public RecipeLikeService(RecipeLikeRepository recipeLikeRepository,
                             RecipeRepository recipeRepository,
                             UserRepository userRepository) {
        this.recipeLikeRepository = recipeLikeRepository;
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void likeRecipe(Long recipeId, Long userId) {
        Optional<RecipeLike> existingLike = recipeLikeRepository.findByIdRecipeIdAndIdUserId(recipeId, userId);

        if (existingLike.isPresent()) {
            recipeLikeRepository.delete(existingLike.get()); // Unlike
            updateLikeCount(recipeId, -1);
        } else {
            RecipeLike.RecipeLikeId likeId = new RecipeLike.RecipeLikeId();
            likeId.setRecipeId(recipeId);
            likeId.setUserId(userId);

            RecipeLike like = new RecipeLike();
            like.setId(likeId);
            recipeLikeRepository.save(like); // Like
            updateLikeCount(recipeId, 1);

        }
    }

    public long countLikes(Long recipeId) {
        return recipeLikeRepository.countByIdRecipeId(recipeId);
    }

    private void updateLikeCount(Long recipeId, int change) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("Recipe not found"));
        long updatedCount = Math.max(0, recipe.getLikeCount() + change);
        recipeRepository.updateLikeCount(recipeId, updatedCount);
    }

    public long totalCountLikes(Long userId) {
        try {
            // Check if the user exists in the database (optional, based on your use case)
            if (!userRepository.existsById(userId)) {
                throw new IllegalArgumentException("User with ID " + userId + " does not exist.");
            }

            return recipeLikeRepository.countByIdUserId(userId);
        } catch (Exception e) {
            // Log the exception and rethrow or handle it
            throw new RuntimeException("Failed to fetch total like count for user ID: " + userId, e);
        }
    }
}
