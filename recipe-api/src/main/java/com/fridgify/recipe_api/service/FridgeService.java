package com.fridgify.recipe_api.service;

import com.fridgify.recipe_api.dto.IngredientDTO;
import com.fridgify.recipe_api.model.Ingredient;
import com.fridgify.recipe_api.model.IngredientCategory;
import com.fridgify.recipe_api.model.User;
import com.fridgify.recipe_api.repository.IngredientRepository;
import com.fridgify.recipe_api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FridgeService {

    private final UserRepository userRepository;
    private final IngredientRepository ingredientRepository;

    public FridgeService(UserRepository userRepository, IngredientRepository ingredientRepository) {
        this.userRepository = userRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public List<IngredientDTO> getAllIngredientsInFridge(Long userId,
                                               String nameFilter,
                                               List<IngredientCategory> categoryFilters) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getFridgeIngredients().stream()
                .filter(ingredient -> nameFilter == null || nameFilter.isEmpty()
                        || ingredient.getName().toLowerCase().contains(nameFilter.toLowerCase()))

                .filter(ingredient -> categoryFilters == null || categoryFilters.isEmpty() ||
                        categoryFilters.contains(ingredient.getCategory()))
                .map(ingredient -> IngredientDTO.builder()
                        .id(ingredient.getId())
                        .name(ingredient.getName())
                        .category(ingredient.getCategory())
                        .imageUrl(ingredient.getImageUrl())
                        .build())
                .collect(Collectors.toList());
    }


    public Page<IngredientDTO> getAllIngredientsInFridge(Long userId,
                                                         String nameFilter,
                                                         List<IngredientCategory> categoryFilters,
                                                         int page,
                                                         int size) {

        List<IngredientDTO> filteredIngredients = getAllIngredientsInFridge(userId, nameFilter, categoryFilters);

        int start = Math.min(page * size, filteredIngredients.size());
        int end = Math.min(start + size, filteredIngredients.size());

        List<IngredientDTO> paginatedIngredients = filteredIngredients.subList(start, end);

        return new PageImpl<>(paginatedIngredients, PageRequest.of(page, size), filteredIngredients.size());
    }


    public List<IngredientDTO> getIngredientsNotInFridge(Long userId,
                                                         String nameFilter,
                                                         List<IngredientCategory> categoryFilters) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        List<Ingredient> fridgeIngredients = user.getFridgeIngredients();

        List<Ingredient> allIngredients = ingredientRepository.findAll();

        List<Ingredient> notInFridgeIngredients = allIngredients.stream()
                .filter(ingredient -> !fridgeIngredients.contains(ingredient))
                .collect(Collectors.toList());

        if (nameFilter != null && !nameFilter.isEmpty()) {
            notInFridgeIngredients = notInFridgeIngredients.stream()
                    .filter(ingredient -> ingredient.getName().toLowerCase().contains(nameFilter.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (categoryFilters != null && !categoryFilters.isEmpty()) {
            notInFridgeIngredients = notInFridgeIngredients.stream()
                    .filter(ingredient -> categoryFilters.contains(ingredient.getCategory()))
                    .collect(Collectors.toList());
        }

        return notInFridgeIngredients.stream()
                .map(ingredient -> new IngredientDTO(
                        ingredient.getId(),
                        ingredient.getName(),
                        ingredient.getCategory(),
                        ingredient.getImageUrl()
                ))
                .toList();
    }


    public Page<IngredientDTO> getIngredientsNotInFridge(Long userId,
                                                         String nameFilter,
                                                         List<IngredientCategory> categoryFilters,
                                                         int page,
                                                         int size) {

        List<IngredientDTO> notInFridgeIngredients = getIngredientsNotInFridge(userId, nameFilter, categoryFilters);

        // Calculate start and end indices for pagination
        int start = Math.min(page * size, notInFridgeIngredients.size());
        int end = Math.min(start + size, notInFridgeIngredients.size());

        List<IngredientDTO> paginatedIngredients = notInFridgeIngredients.subList(start, end).stream()
                .map(ingredient -> new IngredientDTO(
                        ingredient.getId(),
                        ingredient.getName(),
                        ingredient.getCategory(),
                        ingredient.getImageUrl()
                ))
                .collect(Collectors.toList());

        return new PageImpl<>(paginatedIngredients, PageRequest.of(page, size), notInFridgeIngredients.size());
    }


    public List<IngredientDTO> addIngredientsToFridge(Long userId, List<Long> ingredientIds) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        Set<Long> uniqueIngredientIds = new HashSet<>(ingredientIds);

        List<Ingredient> ingredientsToAdd = ingredientRepository.findAllById(uniqueIngredientIds);

        if (ingredientsToAdd.size() != uniqueIngredientIds.size()) {
            Set<Long> missingIds = uniqueIngredientIds.stream()
                    .filter(id -> ingredientsToAdd.stream().noneMatch(ingredient -> ingredient.getId() == id))
                    .collect(Collectors.toSet());
            throw new EntityNotFoundException("Ingredients not found with ids: " + missingIds);
        }

        Set<Ingredient> existingIngredients = new HashSet<>(user.getFridgeIngredients());
        ingredientsToAdd.stream()
                .filter(ingredient -> !existingIngredients.contains(ingredient))
                .forEach(user.getFridgeIngredients()::add);

        userRepository.save(user);

        return user.getFridgeIngredients().stream()
                .map(ingredient -> new IngredientDTO(
                        ingredient.getId(),
                        ingredient.getName(),
                        ingredient.getCategory(),
                        ingredient.getImageUrl()
                ))
                .toList();
    }

    public List<IngredientDTO> removeIngredientsFromFridge(Long userId, List<Long> ingredientIds) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        Set<Long> uniqueIngredientIds = new HashSet<>(ingredientIds);

        List<Ingredient> ingredientsToRemove = ingredientRepository.findAllById(uniqueIngredientIds);

        if (ingredientsToRemove.size() != uniqueIngredientIds.size()) {
            Set<Long> missingIds = uniqueIngredientIds.stream()
                    .filter(id -> ingredientsToRemove.stream().noneMatch(ingredient -> ingredient.getId() == id))
                    .collect(Collectors.toSet());
            throw new EntityNotFoundException("Ingredients not found with ids: " + missingIds);
        }

        Set<Ingredient> existingIngredients = new HashSet<>(user.getFridgeIngredients());
        ingredientsToRemove.stream()
                .filter(existingIngredients::contains)
                .forEach(user.getFridgeIngredients()::remove);

        userRepository.save(user);

        return user.getFridgeIngredients().stream()
                .map(ingredient -> new IngredientDTO(
                        ingredient.getId(),
                        ingredient.getName(),
                        ingredient.getCategory(),
                        ingredient.getImageUrl()
                ))
                .toList();
    }
}