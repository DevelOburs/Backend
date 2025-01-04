package com.fridgify.recipe_api.service;

import com.fridgify.recipe_api.common.exception.ResourceNotFoundException;
import com.fridgify.recipe_api.dto.IngredientDTO;
import com.fridgify.recipe_api.model.Ingredient;
import com.fridgify.recipe_api.model.IngredientCategory;
import com.fridgify.recipe_api.model.Recipe;
import com.fridgify.recipe_api.model.User;
import com.fridgify.recipe_api.repository.IngredientRepository;
import com.fridgify.recipe_api.repository.RecipeIngredientRepository;
import com.fridgify.recipe_api.repository.RecipeRepository;
import com.fridgify.recipe_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTests {

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

    private Recipe recipe;
    private Ingredient ingredient;
    private User user;

    @BeforeEach
    void setUp() {
        recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("Pasta");

        ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setName("Tomato");

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
    }

    @Test
    void testGetAllRecipes() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Recipe> recipePage = new PageImpl<>(List.of(recipe));

        ArgumentCaptor<Specification<Recipe>> specificationCaptor = ArgumentCaptor.forClass(Specification.class);
        when(recipeRepository.findAll(specificationCaptor.capture(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(recipe)));

        List<Recipe> result = recipeService.getAllRecipes(10, 0, null, null, null, null, null, null, null);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Pasta", result.get(0).getName());
    }

    @Test
    void testGetRecipeById_RecipeFound() {
        when(recipeRepository.findRecipeById(1L)).thenReturn(Optional.of(recipe));
        Recipe result = recipeService.getRecipeById(1L);
        assertNotNull(result);
        assertEquals("Pasta", result.getName());
    }

    @Test
    void testGetRecipeById_RecipeNotFound() {
        when(recipeRepository.findRecipeById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> recipeService.getRecipeById(1L));
    }

    @Test
    void testCreateRecipe() {
        IngredientDTO ingredientDTO = new IngredientDTO(1L, "Tomato", IngredientCategory.PRODUCE, null);
        List<IngredientDTO> ingredients = List.of(ingredientDTO);

        when(recipeRepository.save(any())).thenReturn(recipe);
        when(ingredientRepository.findByNameCaseSensitive("Tomato")).thenReturn(Optional.of(ingredient));

        Recipe result = recipeService.createRecipe(recipe, ingredients);
        assertNotNull(result);
        assertEquals("Pasta", result.getName());
        verify(recipeRepository, times(1)).save(any());
    }

    @Test
    void testUpdateRecipe_RecipeFound() {
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        when(recipeRepository.save(any())).thenReturn(recipe);

        Recipe updatedRecipe = new Recipe();
        updatedRecipe.setName("Pizza");

        Recipe result = recipeService.updateRecipe(1L, updatedRecipe, null);
        assertNotNull(result);
        assertEquals("Pizza", result.getName());
    }

    @Test
    void testDeleteRecipe() {
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        doNothing().when(recipeIngredientRepository).deleteByRecipeId(1L);
        doNothing().when(recipeRepository).delete(recipe);

        assertDoesNotThrow(() -> recipeService.deleteRecipe(1L));
        verify(recipeRepository, times(1)).delete(recipe);
    }

    @Test
    void testGetRecipesByUserId() {
        Page<Recipe> recipePage = new PageImpl<>(List.of(recipe));
        when(recipeRepository.findRecipesByUserId(1L, Pageable.unpaged())).thenReturn(recipePage);

        List<Recipe> result = recipeService.getRecipesByUserId(1L, null, null);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void testIsUserOwnerOfRecipe_True() {
        recipe.setUser(user);
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));

        boolean isOwner = recipeService.isUserOwnerOfRecipe(1L, "testuser");
        assertTrue(isOwner);
    }
}