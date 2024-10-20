package com.fridgify.recipe_api.controller;

import com.fridgify.recipe_api.dto.RecipeDTO;
import com.fridgify.recipe_api.model.Recipe;
import com.fridgify.recipe_api.service.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recipe-api")
public class RecipeController {

    private final RecipeService recipeService;

    // Inject the RecipeService using constructor-based dependency injection
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    // Get all recipes
    @GetMapping
    public ResponseEntity<List<RecipeDTO>> getAllRecipes() {
        List<Recipe> recipes = recipeService.getAllRecipes();
        List<RecipeDTO> recipeDTOs = recipes.stream()
                .map(recipe -> RecipeDTO.builder()
                        .id(recipe.getId())
                        .name(recipe.getName())
                        .description(recipe.getDescription())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(recipeDTOs);
    }

    // Get recipe by ID
    @GetMapping("/{id}")
    public ResponseEntity<RecipeDTO> getRecipeById(@PathVariable Long id) {
        Recipe recipe = recipeService.getRecipeById(id);
        RecipeDTO recipeDTO = RecipeDTO.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .description(recipe.getDescription())
                .build();
        return ResponseEntity.ok(recipeDTO);
    }

    // Create a new recipe
    @PostMapping
    public ResponseEntity<RecipeDTO> createRecipe(@RequestBody RecipeDTO recipeDTO) {
        Recipe newRecipe = recipeDTO.toModel(); // Convert RecipeDTO to Recipe
        Recipe savedRecipe = recipeService.createRecipe(newRecipe);

        RecipeDTO responseDTO = RecipeDTO.builder()
                .id(savedRecipe.getId())
                .name(savedRecipe.getName())
                .description(savedRecipe.getDescription())
                .build();

        return ResponseEntity.ok(responseDTO);
    }

    // Update an existing recipe
    @PutMapping("/{id}")
    public ResponseEntity<RecipeDTO> updateRecipe(@PathVariable Long id, @RequestBody RecipeDTO recipeDTO) {
        Recipe updatedRecipe = Recipe.builder()
                .id(id)
                .name(recipeDTO.getName())
                .description(recipeDTO.getDescription())
                .build();
        Recipe savedRecipe = recipeService.updateRecipe(id, updatedRecipe);

        RecipeDTO responseDTO = RecipeDTO.builder()
                .id(savedRecipe.getId())
                .name(savedRecipe.getName())
                .description(savedRecipe.getDescription())
                .build();

        return ResponseEntity.ok(responseDTO);
    }

    // Delete a recipe by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }
}
