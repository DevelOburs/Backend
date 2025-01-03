package com.fridgify.recipe_api.controller;

import com.fridgify.recipe_api.common.exception.ResourceNotFoundException;
import com.fridgify.recipe_api.model.Ingredient;
import com.fridgify.recipe_api.model.Recipe;
import com.fridgify.recipe_api.model.User;
import com.fridgify.recipe_api.repository.IngredientRepository;
import com.fridgify.recipe_api.repository.RecipeIngredientRepository;
import com.fridgify.recipe_api.repository.RecipeRepository;
import com.fridgify.recipe_api.repository.UserRepository;
import com.fridgify.recipe_api.service.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FilterFridgeTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private RecipeIngredientRepository recipeIngredientRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RecipeService recipeService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(10L);
        ingredient1.setName("Tomato");

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(20L);
        ingredient2.setName("Onion");

        List<Ingredient> ingredients = Set.of(ingredient1).stream().toList();
        user.setFridgeIngredients(ingredients);
    }

    @Test
    void testFilterByFridge_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Specification<Recipe> spec = recipeService.filterByFridge(1L);

        assertNotNull(spec);

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testFilterByFridge_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> recipeService.filterByFridge(1L));

        assertEquals("User not found with id 1", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
    }
}
