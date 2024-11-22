package com.fridgify.recipe_api.service;

import com.fridgify.recipe_api.dto.FridgeDTO;
import com.fridgify.recipe_api.dto.IngredientDTO;
import com.fridgify.recipe_api.model.User;
import com.fridgify.recipe_api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class FridgeService {

    private final UserRepository userRepository;

    public FridgeService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

}
