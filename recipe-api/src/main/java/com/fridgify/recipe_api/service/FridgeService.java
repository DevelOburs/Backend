package com.fridgify.recipe_api.service;

import com.fridgify.recipe_api.dto.FridgeDTO;
import com.fridgify.recipe_api.dto.IngredientDTO;
import com.fridgify.recipe_api.model.Ingredient;
import com.fridgify.recipe_api.model.User;
import com.fridgify.recipe_api.repository.IngredientRepository;
import com.fridgify.recipe_api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FridgeService {

    private final UserRepository userRepository;
    private final IngredientRepository ingredientRepository;

    public FridgeService(UserRepository userRepository, IngredientRepository ingredientRepository) {
        this.userRepository = userRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public FridgeDTO getAllIngredientsInFridge(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getFridgeIngredients().stream()
                .map(ingredient -> IngredientDTO.builder()
                        .id(ingredient.getId())
                        .name(ingredient.getName())
                        .category(ingredient.getCategory())
                        .imageUrl(ingredient.getImageUrl())
                        .build())
                .collect(Collectors.collectingAndThen(Collectors.toList(), ingredients -> FridgeDTO.builder()
                        .userId(userId)
                        .ingredients(ingredients)
                        .build()));
    }

    public List<IngredientDTO> getIngredientsNotInFridge(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        List<Ingredient> fridgeIngredients = user.getFridgeIngredients();

        List<Ingredient> allIngredients = ingredientRepository.findAll();

        List<Ingredient> notInFridgeIngredients = allIngredients.stream()
                .filter(ingredient -> !fridgeIngredients.contains(ingredient))
                .toList();

        return notInFridgeIngredients.stream()
                .map(IngredientDTO::new)
                .toList();
    }

    public FridgeDTO addIngredientsToFridge(Long userId, List<IngredientDTO> ingredientDTOs) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        List<Ingredient> ingredientsToAdd = ingredientDTOs.stream()
                .map(dto -> ingredientRepository.findById(dto.getId())
                        .orElseThrow(() -> new EntityNotFoundException("Ingredient not found with id: " + dto.getId())))
                .filter(ingredient -> !user.getFridgeIngredients().contains(ingredient))
                .toList();

        user.getFridgeIngredients().addAll(ingredientsToAdd);

        userRepository.save(user);

        return FridgeDTO.builder()
                .userId(user.getId())
                .ingredients(
                        user.getFridgeIngredients().stream()
                                .map(IngredientDTO::new)
                                .toList()
                )
                .build();
    }

    public FridgeDTO removeIngredientsFromFridge(Long userId, List<IngredientDTO> ingredientDTOs) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        List<Ingredient> ingredientsToRemove = ingredientDTOs.stream()
                .map(dto -> ingredientRepository.findById(dto.getId())
                        .orElseThrow(() -> new EntityNotFoundException("Ingredient not found with id: " + dto.getId())))
                .filter(user.getFridgeIngredients()::contains)
                .toList();

        user.getFridgeIngredients().removeAll(ingredientsToRemove);

        userRepository.save(user);

        return FridgeDTO.builder()
                .userId(user.getId())
                .ingredients(
                        user.getFridgeIngredients().stream()
                                .map(IngredientDTO::new)
                                .toList()
                )
                .build();
    }
}
