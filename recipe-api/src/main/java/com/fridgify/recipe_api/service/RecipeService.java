package com.fridgify.recipe_api.service;

import com.fridgify.recipe_api.common.exception.ResourceNotFoundException;
import com.fridgify.recipe_api.dto.RecipeDTO;
import com.fridgify.recipe_api.model.Ingredient;
import com.fridgify.recipe_api.model.Recipe;
import com.fridgify.recipe_api.model.RecipeIngredient;
import com.fridgify.recipe_api.repository.IngredientRepository;
import com.fridgify.recipe_api.repository.RecipeIngredientRepository;
import com.fridgify.recipe_api.repository.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;

    public RecipeService(RecipeRepository recipeRepository, IngredientRepository ingredientRepository, RecipeIngredientRepository recipeIngredientRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
    }

    public List<Recipe> getAllRecipes(Integer limit, Integer pageNumber) {
        if (limit != null && pageNumber != null) {
            Pageable pageable = PageRequest.of(pageNumber, limit);
            return recipeRepository.findAll(pageable).getContent();
        } else {
            return recipeRepository.findAll();
        }
    }

    public Recipe getRecipeById(Long id) {
        return recipeRepository.findRecipeById(id).orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id " + id));
    }

    public Recipe createRecipe(Recipe recipe, List<String> ingredients) {
        Recipe savedRecipe = recipeRepository.save(recipe);

        List<RecipeIngredient> recipeIngredients = ingredients.stream()
                .map(ingredientName -> {
                    Ingredient ingredient = ingredientRepository.findByName(ingredientName)
                            .orElseThrow(() -> new RuntimeException("Ingredient not found: " + ingredientName));
                    return RecipeIngredient.builder()
                            .recipe(savedRecipe)
                            .ingredientId(ingredient.getId())
                            .recipeId(savedRecipe.getId())
                            .quantity("default quantity") // Set default quantity or get from request
                            .build();
                })
                .collect(Collectors.toList());

        recipeIngredientRepository.saveAll(recipeIngredients);

        return savedRecipe;
    }

    public Recipe updateRecipe(Long id, Recipe updatedRecipe) {
        Recipe existingRecipe = recipeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id " + id));
        existingRecipe.setName(updatedRecipe.getName());
        existingRecipe.setDescription(updatedRecipe.getDescription());
        return recipeRepository.save(existingRecipe);
    }

    public void deleteRecipe(Long id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id " + id));
        recipeRepository.delete(recipe);
    }

    public List<RecipeDTO> getRecipesByUserId(Long userId) {
        List<Recipe> recipes = recipeRepository.findRecipesByUserId(userId);
        log.info("Recipes fetching by user id");
        return recipes.stream()
                .map(recipe -> RecipeDTO.builder()
                        .id(recipe.getId())
                        .name(recipe.getName())
                        .description((recipe.getDescription()))
                        .user(recipe.getUser())
                        .build())
                .toList();
    }
}