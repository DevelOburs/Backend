package com.fridgify.recipe_api.controller;

import com.fridgify.recipe_api.dto.RecipeDTO;
import com.fridgify.recipe_api.model.Recipe;
import com.fridgify.recipe_api.model.User;
import com.fridgify.recipe_api.service.RecipeService;
import com.fridgify.recipe_api.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber) {

        // Fetch recipes with pagination
        List<Recipe> recipes = recipeService.getAllRecipes(limit, pageNumber);

        // Convert Recipe to RecipeDTO
        List<RecipeDTO> recipeDTOs = recipes.stream()
                .map(recipe -> RecipeDTO.builder()
                        .id(recipe.getId())
                        .name(recipe.getName())
                        .description(recipe.getDescription())
                        .likeCount(recipe.getLikeCount())
                        .commentCount(recipe.getCommentCount())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(recipeDTOs);
    }

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

    @PostMapping
    public ResponseEntity<RecipeDTO> createRecipe(@RequestBody RecipeDTO recipeDTO) {
        if(recipeDTO.getUserId() == null) {recipeDTO.setUserId((long)3);}
        Recipe newRecipe = recipeDTO.toModel(); // Convert RecipeDTO to Recipe
        newRecipe.setUser(userService.getUserById(recipeDTO.getUserId()));
        Recipe savedRecipe = recipeService.createRecipe(newRecipe);

        RecipeDTO responseDTO = RecipeDTO.builder()
                .id(savedRecipe.getId())
                .name(savedRecipe.getName())
                .description(savedRecipe.getDescription())
                .user(User.builder().id(savedRecipe.getUser().getId()).build())
                .build();

        return ResponseEntity.ok(responseDTO);
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/getRecipes/{userId}")
    public ResponseEntity<List<RecipeDTO>> getRecipesByUserId(@PathVariable Long userId) {
        List<RecipeDTO> recipes = recipeService.getRecipesByUserId(userId);
        if (recipes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(recipes);
    }
}
