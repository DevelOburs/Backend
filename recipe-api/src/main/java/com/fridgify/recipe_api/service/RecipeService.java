package com.fridgify.recipe_api.service;

import com.fridgify.recipe_api.common.exception.ResourceNotFoundException;
import com.fridgify.recipe_api.dto.RecipeDTO;
import com.fridgify.recipe_api.model.Recipe;
import com.fridgify.recipe_api.repository.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
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
        if (updatedRecipe.getName() != null) {
            existingRecipe.setName(updatedRecipe.getName());
        }
        if (updatedRecipe.getDescription() != null) {
            existingRecipe.setDescription(updatedRecipe.getDescription());
        }
        if (updatedRecipe.getImageUrl() != null) {
            existingRecipe.setImageUrl(updatedRecipe.getImageUrl());

        }
        return recipeRepository.save(existingRecipe);
    }

    public void deleteRecipe(Long id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id " + id));
        recipeRepository.delete(recipe);
    }

    public List<Recipe> getRecipesByUserId(Long userId) {
        log.info("Recipes fetching by user id");
        return recipeRepository.findRecipesByUserId(userId);
    }

}