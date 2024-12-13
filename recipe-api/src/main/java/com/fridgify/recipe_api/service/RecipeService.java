package com.fridgify.recipe_api.service;

import com.fridgify.recipe_api.common.exception.ResourceNotFoundException;
import com.fridgify.recipe_api.model.Ingredient;
import com.fridgify.recipe_api.model.Recipe;
import com.fridgify.recipe_api.model.RecipeIngredient;
import com.fridgify.recipe_api.repository.IngredientRepository;
import com.fridgify.recipe_api.repository.RecipeIngredientRepository;
import com.fridgify.recipe_api.model.RecipeCategory;
import com.fridgify.recipe_api.repository.RecipeRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    public List<Recipe> getAllRecipes(Integer limit, Integer pageNumber, RecipeCategory category,
                                      Integer minCookingTime, Integer maxCookingTime,
                                      Integer minCalories, Integer maxCalories) {
        Pageable pageable = (limit != null && pageNumber != null) ? PageRequest.of(pageNumber, limit) : Pageable.unpaged();

        Specification<Recipe> spec = filterByCriteria(category, minCookingTime, maxCookingTime, minCalories, maxCalories);

        Page<Recipe> recipePage = recipeRepository.findAll(spec, pageable);

        return recipePage.getContent();
    }

    public Recipe getRecipeById(Long id) {
        return recipeRepository.findRecipeById(id).orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id " + id));
    }

    @Transactional
    public Recipe createRecipe(Recipe recipe, List<String> ingredients) {
        Recipe savedRecipe = recipeRepository.save(recipe);

        List<RecipeIngredient> recipeIngredients = ingredients.stream()
                .map(ingredientName -> {
                    Ingredient ingredient = ingredientRepository.findByNameCaseSensitive(ingredientName)
                            .orElseThrow(() -> new RuntimeException("Ingredient not found: " + ingredientName));
                    return new RecipeIngredient(
                            recipe.getId(),
                            ingredient.getId(),
                            recipe,
                            ingredient,
                            "default quantity");
                })
                .collect(Collectors.toList());

        recipeRepository.saveAndFlush(savedRecipe);
        savedRecipe.updateIngredients(recipeIngredients);
        return savedRecipe;
    }

    @Transactional
    public Recipe updateRecipe(Long id, Recipe updatedRecipe, List<String> ingredients) {
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
        if (updatedRecipe.getCategory() != null) {
            existingRecipe.setCategory(updatedRecipe.getCategory());
        }
        if (updatedRecipe.getCalories() != null) {
            existingRecipe.setCalories(updatedRecipe.getCalories());
        }
        if (updatedRecipe.getCookingTime() != null) {
            existingRecipe.setCookingTime(updatedRecipe.getCookingTime());
        }

        if (ingredients != null) {
            List<RecipeIngredient> recipeIngredients = ingredients.stream()
                    .map(ingredientName -> {
                        Ingredient ingredient = ingredientRepository.findByNameCaseSensitive(ingredientName)
                                .orElseThrow(() -> new RuntimeException("Ingredient not found: " + ingredientName));
                        return new RecipeIngredient(
                                existingRecipe.getId(),
                                ingredient.getId(),
                                existingRecipe,
                                ingredient,
                                "default quantity");
                    })
                    .toList();
            existingRecipe.updateIngredients(recipeIngredients);
        }
        return recipeRepository.save(existingRecipe);
    }

    @Transactional
    public void deleteRecipe(Long id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id " + id));
        recipeIngredientRepository.deleteByRecipeId(id);
        recipeRepository.delete(recipe);
    }

    public List<Recipe> getRecipesByUserId(Long userId) {
        log.info("Recipes fetching by user id");
        return recipeRepository.findRecipesByUserId(userId);
    }

    public List<RecipeCategory> getAllCategories() {
        return List.of(RecipeCategory.values());
    }

    public static Specification<Recipe> filterByCriteria(RecipeCategory category, Integer minCookingTime, Integer maxCookingTime, Integer minCalories, Integer maxCalories) {
        return (root, query, criteriaBuilder) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();

            if (category != null) {
                predicates.add(criteriaBuilder.equal(root.get("category"), category));
            }
            if (minCookingTime != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("cookingTime"), minCookingTime));
            }
            if (maxCookingTime != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("cookingTime"), maxCookingTime));
            }
            if (minCalories != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("calories"), minCalories));
            }
            if (maxCalories != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("calories"), maxCalories));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Optional<Long> getCountRecipesOfUser(Long userId) {
        return recipeRepository.countRecipesByUserId(userId);
    }
}