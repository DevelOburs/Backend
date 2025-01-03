package com.fridgify.recipe_api.service;

import com.fridgify.recipe_api.common.exception.ResourceNotFoundException;
import com.fridgify.recipe_api.dto.IngredientDTO;
import com.fridgify.recipe_api.model.*;
import com.fridgify.recipe_api.repository.IngredientRepository;
import com.fridgify.recipe_api.repository.RecipeIngredientRepository;
import com.fridgify.recipe_api.repository.RecipeRepository;
import com.fridgify.recipe_api.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.ws.rs.NotFoundException;
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
    private final UserRepository userRepository;

    public RecipeService(RecipeRepository recipeRepository, IngredientRepository ingredientRepository, RecipeIngredientRepository recipeIngredientRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.userRepository = userRepository;
    }

    public List<Recipe> getAllRecipes(Integer limit, Integer pageNumber, RecipeCategory category,
                                      Integer minCookingTime, Integer maxCookingTime,
                                      Integer minCalories, Integer maxCalories, String recipeName, Long userId) {
        Pageable pageable = (limit != null && pageNumber != null) ? PageRequest.of(pageNumber, limit) : Pageable.unpaged();

        Specification<Recipe> spec = filterByCriteria(
                category, minCookingTime, maxCookingTime, minCalories, maxCalories, recipeName, userId);

        Page<Recipe> recipePage = recipeRepository.findAll(spec, pageable);

        return recipePage.getContent();
    }

    public Recipe getRecipeById(Long id) {
        return recipeRepository.findRecipeById(id).orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id " + id));
    }

    @Transactional
    public Recipe createRecipe(Recipe recipe, List<IngredientDTO> ingredients) {
        Recipe savedRecipe = recipeRepository.save(recipe);

        List<RecipeIngredient> recipeIngredients = ingredients.stream()
                .map(ingredient_ -> {
                    Ingredient ingredient = ingredientRepository.findByNameCaseSensitive(ingredient_.getName())
                            .orElseThrow(() -> new RuntimeException("Ingredient not found: " + ingredient_.getName()));
                    return new RecipeIngredient(
                            recipe.getId(),
                            ingredient.getId(),
                            recipe,
                            ingredient,
                            ingredient_.getQuantity());
                })
                .collect(Collectors.toList());

        recipeRepository.saveAndFlush(savedRecipe);
        savedRecipe.updateIngredients(recipeIngredients);
        return savedRecipe;
    }

    @Transactional
    public Recipe updateRecipe(Long id, Recipe updatedRecipe, List<IngredientDTO> ingredients) {
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
                    .map(ingredient_ -> {
                        Ingredient ingredient = ingredientRepository.findByNameCaseSensitive(ingredient_.getName())
                                .orElseThrow(() -> new RuntimeException("Ingredient not found: " + ingredient_.getName()));
                        return new RecipeIngredient(
                                existingRecipe.getId(),
                                ingredient.getId(),
                                existingRecipe,
                                ingredient,
                                ingredient_.getQuantity());
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

    public List<Recipe> getRecipesByUserId(Long userId, Integer limit, Integer pageNumber) {

        Pageable pageable = (limit != null && pageNumber != null) ? PageRequest.of(pageNumber, limit) : Pageable.unpaged();

        Page<Recipe> recipePage = recipeRepository.findRecipesByUserId(userId, pageable);

                log.info("Recipes fetching by user id");
        return recipePage.getContent();
    }

    public List<RecipeCategory> getAllCategories() {
        return List.of(RecipeCategory.values());
    }

    public Specification<Recipe> filterByCriteria(
            RecipeCategory category, Integer minCookingTime, Integer maxCookingTime,
            Integer minCalories, Integer maxCalories, String recipeName, Long userId) {
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

            if (userId != null) {
                predicates.add(filterByFridge(userId).toPredicate(root, query, criteriaBuilder));
            }

            if (recipeName != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(
                        root.get("name")), "%" + recipeName.toLowerCase() + "%")
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Specification<Recipe> filterByFridge(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        List<Long> fridgeIngredientIds = user.getFridgeIngredients().stream()
                .map(Ingredient::getId)
                .collect(Collectors.toList());

        return (root, query, criteriaBuilder) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<RecipeIngredient> recipeIngredientRoot = subquery.from(RecipeIngredient.class);

            subquery.select(recipeIngredientRoot.get("recipe").get("id"))
                    .where(criteriaBuilder.not(recipeIngredientRoot.get("ingredient").get("id").in(fridgeIngredientIds)));

            return criteriaBuilder.not(root.get("id").in(subquery));
        };
    }

    public Optional<Long> getCountRecipesOfUser(Long userId) {
        return recipeRepository.countRecipesByUserId(userId);
    }

    public boolean isUserOwnerOfRecipe(Long recipeId, String username) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new NotFoundException("Recipe not found"));
        return recipe.getUser().getUsername().equals(username);
    }
}