package com.fridgify.recipe_api.controller;

import com.fridgify.recipe_api.dto.RecipeDTO;
import com.fridgify.recipe_api.model.Recipe;
import com.fridgify.recipe_api.model.RecipeCategory;

import com.fridgify.recipe_api.service.RecipeService;
import com.fridgify.recipe_api.service.UserService;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@RestController
@RequestMapping("/recipe-api")
public class RecipeController {

    private final RecipeService recipeService;
    private final UserService userService;

    public RecipeController(RecipeService recipeService, UserService userService) {
        this.recipeService = recipeService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<RecipeDTO>> getAllRecipes(
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "category", required = false) RecipeCategory category,
            @RequestParam(value = "minCookingTime", required = false) Integer minCookingTime,
            @RequestParam(value = "maxCookingTime", required = false) Integer maxCookingTime,
            @RequestParam(value = "minCalories", required = false) Integer minCalories,
            @RequestParam(value = "maxCalories", required = false) Integer maxCalories) {
        // Fetch recipes with pagination
        List<Recipe> recipes = recipeService.getAllRecipes(
                limit, pageNumber, category, minCookingTime, maxCookingTime, minCalories, maxCalories
        );
        // Convert Recipe to RecipeDTO
        List<RecipeDTO> recipeDTOs = recipes.stream()
                .map(RecipeDTO::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(recipeDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeDTO> getRecipeById(@PathVariable Long id) {
        Recipe recipe = recipeService.getRecipeById(id);
        RecipeDTO recipeDTO = RecipeDTO.toResponse(recipe);
        return ResponseEntity.ok(recipeDTO);
    }

    @PostMapping
    public ResponseEntity<RecipeDTO> createRecipe(@RequestBody RecipeDTO recipeDTO) {
        if(recipeDTO.getUserId() == null) {recipeDTO.setUserId((long)3);}
        Recipe newRecipe = recipeDTO.toModel(); // Convert RecipeDTO to Recipe
        newRecipe.setUser(userService.getUserById(recipeDTO.getUserId()));
        Recipe savedRecipe = recipeService.createRecipe(newRecipe, recipeDTO.getIngredients());

        RecipeDTO responseDTO = RecipeDTO.toResponse(savedRecipe);

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecipeDTO> updateRecipe(@PathVariable Long id, @RequestBody RecipeDTO recipeDTO) {
        Recipe updatedRecipe = Recipe.builder()
                .id(id)
                .name(recipeDTO.getName())
                .description(recipeDTO.getDescription())
                .imageUrl(recipeDTO.getImageUrl())
                .cookingTime(recipeDTO.getCookingTime())
                .calories(recipeDTO.getCalories())
                .category(recipeDTO.getCategory())
                .build();
        Recipe savedRecipe = recipeService.updateRecipe(id, updatedRecipe, recipeDTO.getIngredients());

        RecipeDTO responseDTO = RecipeDTO.toResponse(savedRecipe);

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getRecipes/{userId}")
    public ResponseEntity<List<RecipeDTO>> getRecipesByUserId(@PathVariable Long userId) {
        List<Recipe> recipes = recipeService.getRecipesByUserId(userId);
        if (recipes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(recipes.stream()
                .map(RecipeDTO::toResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<RecipeCategory>> getAllRecipeCategories() {
        List<RecipeCategory> categories = recipeService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/userRecipeCount")
    public ResponseEntity<?> getUserRecipeCount(@RequestParam Long userId) {
        try {
            // Validate userId
            if (userId == null || userId <= 0) {
                log.info("Invalid user ID: {}", userId);
                return ResponseEntity.badRequest().body("Invalid user ID");
            }

            // Check if user exists
            if (!userService.userExists(userId)) {
                log.info("User not found for ID: {}", userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            // Fetch recipe count
            long recipeCount = recipeService.getCountRecipesOfUser(userId)
                    .orElseThrow(() -> new RuntimeException("No recipes found for user ID: " + userId));

            return ResponseEntity.ok(recipeCount);
        } catch (RuntimeException e) {
            log.error("Error occurred while fetching recipe count for user ID {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error occurred for user ID {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

}
