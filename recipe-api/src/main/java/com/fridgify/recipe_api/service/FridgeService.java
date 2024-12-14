package com.fridgify.recipe_api.service;

import com.fridgify.recipe_api.model.Ingredient;
import com.fridgify.recipe_api.model.IngredientCategory;
import com.fridgify.recipe_api.model.User;
import com.fridgify.recipe_api.repository.IngredientRepository;
import com.fridgify.recipe_api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class FridgeService {

    private final UserRepository userRepository;
    private final IngredientRepository ingredientRepository;

    public FridgeService(UserRepository userRepository, IngredientRepository ingredientRepository) {
        this.userRepository = userRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public List<Ingredient> getInFridge(Long userId, Integer limit, Integer pageNumber,
                                        IngredientCategory category, String nameFilter) {

        Pageable pageable = (limit != null && pageNumber != null)
                ? PageRequest.of(pageNumber, limit)
                : Pageable.unpaged();

        User user = userRepository.findByIdWithFridgeIngredients(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        List<Long> fridgeIngredientIds = user.getFridgeIngredients().stream()
                .map(Ingredient::getId)
                .toList();

        Specification<Ingredient> spec = Specification.where(inFridge(fridgeIngredientIds))
                .and(byCategoryIfProvided(category))
                .and(byNameFilterIfProvided(nameFilter));

        return ingredientRepository.findAll(spec, pageable).getContent();
    }

    public List<Ingredient> getNotInFridge(Long userId, Integer limit, Integer pageNumber,
                                           IngredientCategory category, String nameFilter) {

        Pageable pageable = (limit != null && pageNumber != null)
                ? PageRequest.of(pageNumber, limit)
                : Pageable.unpaged();

        User user = userRepository.findByIdWithFridgeIngredients(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        List<Long> fridgeIngredientIds = user.getFridgeIngredients().stream()
                .map(Ingredient::getId)
                .toList();

        Specification<Ingredient> spec = Specification.where(notInFridge(fridgeIngredientIds))
                .and(byCategoryIfProvided(category))
                .and(byNameFilterIfProvided(nameFilter));

        return ingredientRepository.findAll(spec, pageable).getContent();
    }


    @Transactional
    public List<Ingredient> addToFridge(Long userId, List<Long> ingredientIds) {
        User user = userRepository.findByIdWithFridgeIngredients(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        List<Ingredient> ingredientsToAdd = ingredientRepository.findByIdIn(ingredientIds);

        if (ingredientsToAdd.isEmpty()) {
            throw new IllegalArgumentException("No valid ingredients found with the provided IDs.");
        }

        List<Ingredient> fridgeIngredients = user.getFridgeIngredients();
        ingredientsToAdd.forEach(ingredient -> {
            if (!fridgeIngredients.contains(ingredient)) {
                fridgeIngredients.add(ingredient);
            }
        });

        user.setFridgeIngredients(fridgeIngredients);
        userRepository.save(user);

        return fridgeIngredients;
    }

    @Transactional
    public List<Ingredient> removeFromFridge(Long userId, List<Long> ingredientIds) {
        User user = userRepository.findByIdWithFridgeIngredients(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        List<Ingredient> ingredientsToRemove = ingredientRepository.findByIdIn(ingredientIds);

        if (ingredientsToRemove.isEmpty()) {
            throw new IllegalArgumentException("No valid ingredients found with the provided IDs.");
        }

        user.getFridgeIngredients().removeAll(ingredientsToRemove);
        userRepository.save(user);

        return user.getFridgeIngredients();
    }

    private Specification<Ingredient> inFridge(List<Long> fridgeIngredientIds) {
        return (root, query, cb) -> root.get("id").in(fridgeIngredientIds);
    }

    private Specification<Ingredient> notInFridge(List<Long> fridgeIngredientIds) {
        return (root, query, cb) -> cb.not(root.get("id").in(fridgeIngredientIds));
    }

    private Specification<Ingredient> byCategoryIfProvided(IngredientCategory category) {
        return (root, query, cb) -> category != null
                ? cb.equal(root.get("category"), category)
                : cb.conjunction();
    }

    private Specification<Ingredient> byNameFilterIfProvided(String nameFilter) {
        return (root, query, cb) -> nameFilter != null && !nameFilter.isEmpty()
                ? cb.like(cb.lower(root.get("name")), "%" + nameFilter.toLowerCase() + "%")
                : cb.conjunction();
    }
}