package com.fridgify.recipe_api.service;

import com.fridgify.recipe_api.common.exception.ResourceNotFoundException;
import com.fridgify.recipe_api.dto.RecipeDTO;
import com.fridgify.recipe_api.model.Recipe;
import com.fridgify.recipe_api.repository.RecipeRepository;
import com.fridgify.recipe_api.repository.UserSavedRecipeRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<Recipe> getAllRecipes(Integer limit, Integer pageNumber) {
        if (limit != null && pageNumber != null) {
            Pageable pageable = PageRequest.of(pageNumber, limit);
            return recipeRepository.findAll(pageable).getContent();
        }
        else {
            return recipeRepository.findAll();
        }
    }

    public Recipe getRecipeById(Long id) {
        return recipeRepository.findRecipeById(id).orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id " + id));
    }

    public Recipe createRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
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