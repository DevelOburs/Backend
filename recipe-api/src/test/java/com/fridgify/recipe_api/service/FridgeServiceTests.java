package com.fridgify.recipe_api.service;

import com.fridgify.recipe_api.model.Ingredient;
import com.fridgify.recipe_api.model.IngredientCategory;
import com.fridgify.recipe_api.model.User;
import com.fridgify.recipe_api.repository.IngredientRepository;
import com.fridgify.recipe_api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FridgeServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private FridgeService fridgeService;

    private User user;
    private Ingredient ingredient1;
    private Ingredient ingredient2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setFridgeIngredients(new ArrayList<>());

        ingredient1 = new Ingredient(1L, "Apple", IngredientCategory.PRODUCE, "apple.jpg");
        ingredient2 = new Ingredient(2L, "Milk", IngredientCategory.DAIRY_AND_EGGS, "milk.jpg");
    }

    @Test
    void getInFridge_ShouldReturnIngredientsInFridge() {
        user.getFridgeIngredients().addAll(List.of(ingredient1, ingredient2));
        when(userRepository.findByIdWithFridgeIngredients(1L)).thenReturn(Optional.of(user));
        when(ingredientRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(ingredient1, ingredient2)));

        List<Ingredient> result = fridgeService.getInFridge(1L, 10, 0, null, null);

        assertEquals(2, result.size());
        assertTrue(result.contains(ingredient1));
        assertTrue(result.contains(ingredient2));
    }

    @Test
    void getNotInFridge_ShouldReturnIngredientsNotInFridge() {
        user.getFridgeIngredients().add(ingredient1);
        when(userRepository.findByIdWithFridgeIngredients(1L)).thenReturn(Optional.of(user));
        when(ingredientRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(ingredient2)));

        List<Ingredient> result = fridgeService.getNotInFridge(1L, 10, 0, null, null);

        assertEquals(1, result.size());
        assertTrue(result.contains(ingredient2));
    }

    @Test
    void addToFridge_ShouldAddIngredientsToFridge() {
        when(userRepository.findByIdWithFridgeIngredients(1L)).thenReturn(Optional.of(user));
        when(ingredientRepository.findByIdIn(List.of(1L, 2L))).thenReturn(List.of(ingredient1, ingredient2));

        List<Ingredient> result = fridgeService.addToFridge(1L, List.of(1L, 2L));

        assertEquals(2, result.size());
        assertTrue(result.contains(ingredient1));
        assertTrue(result.contains(ingredient2));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void addToFridge_ShouldThrowExceptionWhenNoValidIngredients() {
        when(userRepository.findByIdWithFridgeIngredients(1L)).thenReturn(Optional.of(user));
        when(ingredientRepository.findByIdIn(List.of(3L))).thenReturn(List.of());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                fridgeService.addToFridge(1L, List.of(3L)));

        assertEquals("No valid ingredients found with the provided IDs.", exception.getMessage());
    }

    @Test
    void removeFromFridge_ShouldRemoveIngredientsFromFridge() {
        user.getFridgeIngredients().addAll(List.of(ingredient1, ingredient2));
        when(userRepository.findByIdWithFridgeIngredients(1L)).thenReturn(Optional.of(user));
        when(ingredientRepository.findByIdIn(List.of(1L))).thenReturn(List.of(ingredient1));

        List<Ingredient> result = fridgeService.removeFromFridge(1L, List.of(1L));

        assertEquals(1, result.size());
        assertFalse(result.contains(ingredient1));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void removeFromFridge_ShouldThrowExceptionWhenNoValidIngredients() {
        when(userRepository.findByIdWithFridgeIngredients(1L)).thenReturn(Optional.of(user));
        when(ingredientRepository.findByIdIn(List.of(3L))).thenReturn(List.of());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                fridgeService.removeFromFridge(1L, List.of(3L)));

        assertEquals("No valid ingredients found with the provided IDs.", exception.getMessage());
    }

    @Test
    void getInFridge_ShouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findByIdWithFridgeIngredients(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                fridgeService.getInFridge(1L, 10, 0, null, null));

        assertEquals("User not found with id: 1", exception.getMessage());
    }

    @Test
    void getNotInFridge_ShouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findByIdWithFridgeIngredients(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                fridgeService.getNotInFridge(1L, 10, 0, null, null));

        assertEquals("User not found with id: 1", exception.getMessage());
    }
}
