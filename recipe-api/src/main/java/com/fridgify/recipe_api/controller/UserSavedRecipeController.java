package com.fridgify.recipe_api.controller;

import com.fridgify.recipe_api.dto.RecipeDTO;
import com.fridgify.recipe_api.model.Recipe;
import com.fridgify.recipe_api.model.UserSavedRecipe;
import com.fridgify.recipe_api.service.UserSavedRecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recipe-api/save")
public class UserSavedRecipeController {
    private final UserSavedRecipeService userSavedRecipeService;

    public UserSavedRecipeController(UserSavedRecipeService userSavedRecipeService) {
        this.userSavedRecipeService = userSavedRecipeService;
    }

    @GetMapping("/{recipeId}/{userId}")
    public ResponseEntity<String> toggleSaveRecipe(@PathVariable Long recipeId,
                                                   @PathVariable Long userId) {
        try {
            userSavedRecipeService.toggleSaveRecipe(recipeId, userId);
            return ResponseEntity.ok("Recipe save status toggled successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to toggle recipe save status: " + e.getMessage());
        }
    }

    @GetMapping("/{recipeId}/count")
    public ResponseEntity<Long> getSaveCount(@PathVariable Long recipeId) {
        try {
            long saveCount = userSavedRecipeService.getSaveCount(recipeId);
            return ResponseEntity.ok(saveCount);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(0L);
        }
    }

    @GetMapping("/list/{userId}")
    public ResponseEntity<?> getSavedRecipes(@PathVariable Long userId) {
        try {
            List<Recipe> savedRecipes = userSavedRecipeService.getSavedRecipesByUser(userId);
            List<RecipeDTO> result = savedRecipes.stream()
                    .map(recipe -> RecipeDTO.builder()
                            .id(recipe.getId())
                            .name(recipe.getName())
                            .description(recipe.getDescription())
                            .likeCount(recipe.getLikeCount())
                            .commentCount(recipe.getCommentCount())
                            .saveCount(recipe.getSaveCount())
                            .build())
                    .toList();
            return ResponseEntity.ok(result);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Saved recipes could not fetch!");
        }
    }
}
