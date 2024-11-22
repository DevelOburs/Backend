package com.fridgify.recipe_api.service;

import com.fridgify.recipe_api.model.Recipe;
import com.fridgify.recipe_api.model.UserSavedRecipe;
import com.fridgify.recipe_api.repository.RecipeRepository;
import com.fridgify.recipe_api.repository.UserSavedRecipeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserSavedRecipeService {

    private final UserSavedRecipeRepository userSavedRecipeRepository;
    private final RecipeRepository recipeRepository;

    public UserSavedRecipeService(UserSavedRecipeRepository userSavedRecipeRepository,
                                  RecipeRepository recipeRepository) {
        this.userSavedRecipeRepository = userSavedRecipeRepository;
        this.recipeRepository = recipeRepository;
    }

    @Transactional
    public void toggleSaveRecipe(Long recipeId, Long userId) {
        Optional<UserSavedRecipe> existingSavedRecipe = userSavedRecipeRepository.findByIdRecipeIdAndIdUserId(recipeId, userId);

        if (existingSavedRecipe.isPresent()) {
            userSavedRecipeRepository.delete(existingSavedRecipe.get()); // Unsave
            updateSaveCount(recipeId, -1);
        } else {
            UserSavedRecipe.UserSavedRecipeId savedId = new UserSavedRecipe.UserSavedRecipeId();
            savedId.setRecipeId(recipeId);
            savedId.setUserId(userId);

            UserSavedRecipe savedRecipe = new UserSavedRecipe();
            savedRecipe.setId(savedId);
            userSavedRecipeRepository.save(savedRecipe); // Save
            updateSaveCount(recipeId, 1);
        }
    }

    private void updateSaveCount(Long recipeId, int change) {
        Recipe recipe = recipeRepository.findRecipeById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("Recipe not found"));
        long updatedCount = Math.max(0, recipe.getSaveCount() + change); // Assuming `saveCount` is the column for saves
        recipeRepository.updateSaveCount(recipeId, updatedCount);
    }

    public long getSaveCount(Long recipeId) {
        return userSavedRecipeRepository.countByIdRecipeId(recipeId);
    }

    public List<Recipe> getSavedRecipesByUser(Long userId) {
        List<Long> savedRecipeIds = userSavedRecipeRepository.findRecipeIdsByUserId(userId);

        return recipeRepository.findAllById(savedRecipeIds);
    }
}

