package com.fridgify.recipe_api;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fridgify.recipe_api.common.exception.ResourceNotFoundException;
import com.fridgify.recipe_api.controller.RecipeController;
import com.fridgify.recipe_api.dto.RecipeDTO;
import com.fridgify.recipe_api.model.Ingredient;
import com.fridgify.recipe_api.model.IngredientCategory;
import com.fridgify.recipe_api.model.Recipe;
import com.fridgify.recipe_api.model.RecipeCategory;
import com.fridgify.recipe_api.model.RecipeIngredient;
import com.fridgify.recipe_api.model.User;
import com.fridgify.recipe_api.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author daryl
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest({RecipeController.class})
@AutoConfigureMockMvc(addFilters = false)
public class RecipeApiApplicationMvcTests {

    private final String BASE_URL = "/recipe-api";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RecipeService recipeService;

    @MockBean
    private RecipeLikeService recipeLikeService;

    @SpyBean
    private BCryptPasswordEncoder passwordEncoder;

    @MockBean
    private UserService userService;

    @MockBean
    private RecipeCommentService recipeCommentService;

    @MockBean
    private UserSavedRecipeService userSavedRecipeService;


    @Test
    public void getAllRecipes() throws Exception {
        /* Mock create user */
        User mockUser = User.builder()
                .id(1L)
                .username("testUser")
                .email("test@example.com")
                .build();

        Recipe recipe1 = Recipe.builder()
                .id(1L)
                .name("Spaghetti Carbonara")
                .description("A classic Italian pasta dish")
                .createdAt(LocalDateTime.now())
                .likeCount(10L)
                .commentCount(5L)
                .saveCount(7L)
                .imageUrl("https://ex.com/carbonara.jpg")
                .category(RecipeCategory.MAIN_DISHES)
                .calories(500L)
                .cookingTime(20L)
                .user(mockUser)
                .build();

        /* Create ingredients */
        Ingredient ingredient1 = Ingredient.builder()
                .id(1L)
                .name("Spaghetti")
                .category(IngredientCategory.BAKING_AND_PANTRY)
                .build();

        Ingredient ingredient2 = Ingredient.builder()
                .id(2L)
                .name("Eggs")
                .category(IngredientCategory.BAKING_AND_PANTRY)
                .build();

        /* Create recipe ingredients */
        RecipeIngredient recipeIngredient1 = new RecipeIngredient(1L, 1L, recipe1, ingredient1, "500g");
        RecipeIngredient recipeIngredient2 = new RecipeIngredient(1L, 2L, recipe1, ingredient2, "4 pieces");

        List<RecipeIngredient> ingredients = List.of(recipeIngredient1, recipeIngredient2);
        recipe1.setIngredients(ingredients);

        List<Recipe> recipes = List.of(recipe1);
        when(recipeService.getAllRecipes(null, null, null, null, null, null, null, null, null))
                .thenReturn(recipes);

        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "mockJwt"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Spaghetti Carbonara"));


    }

    @Test
    public void testGetRecipesByUserIdUnauthorized() throws Exception {
        when(recipeService.getRecipeById(anyLong())).thenThrow(new AccessDeniedException("Access is denied"));
        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testUpdateRecipe() throws Exception {
        Long recipeId = 1L;
        Long userId = 1L;

        User mockUser = User.builder()
                .id(userId)
                .username("testUser")
                .email("test@example.com")
                .build();

        Recipe updatedRecipe = Recipe.builder()
                .id(recipeId)
                .name("Updated Recipe")
                .description("Updated Description")
                .createdAt(LocalDateTime.now())
                .user(mockUser)
                .likeCount(0L)
                .commentCount(0L)
                .saveCount(0L)
                .category(RecipeCategory.MAIN_DISHES)
                .calories(600L)
                .cookingTime(45L)
                .build();

        Ingredient ingredient1 = Ingredient.builder()
                .id(1L)
                .name("Updated Ingredient")
                .category(IngredientCategory.BAKING_AND_PANTRY)
                .build();

        RecipeIngredient recipeIngredient1 = new RecipeIngredient(1L, 1L, updatedRecipe, ingredient1, "600g");
        updatedRecipe.setIngredients(List.of(recipeIngredient1));

        RecipeDTO recipeDTO = RecipeDTO.toResponse(updatedRecipe);

        when(recipeService.updateRecipe(eq(recipeId), any(Recipe.class), anyList())).thenReturn(updatedRecipe);

        mockMvc.perform(put(BASE_URL + "/{id}", recipeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipeDTO))
                        .header("Authorization", "mockJwt"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Recipe"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.calories").value(600))
                .andExpect(jsonPath("$.cookingTime").value(45))
                .andExpect(jsonPath("$.category").value("MAIN_DISHES"));

        verify(recipeService).updateRecipe(eq(recipeId), any(Recipe.class), anyList());
    }

    @Test
    public void testUpdateRecipeNotFound() throws Exception {
        Long recipeId = 999L;
        RecipeDTO recipeDTO = new RecipeDTO();
        recipeDTO.setName("Updated Recipe");
        recipeDTO.setDescription("Updated Description");

        when(recipeService.updateRecipe(eq(recipeId), any(Recipe.class), anyList()))
                .thenThrow(new ResourceNotFoundException("Recipe not found with id " + recipeId));

        mockMvc.perform(put(BASE_URL + "/{id}", recipeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipeDTO))
                        .header("Authorization", "mockJwt"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteRecipe() throws Exception {
        Long recipeId = 1L;

        mockMvc.perform(delete(BASE_URL + "/{id}", recipeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "mockJwt"))
                .andExpect(status().isNoContent());

        verify(recipeService).deleteRecipe(recipeId);
    }

    @Test
    public void testDeleteRecipeNotFound() throws Exception {
        Long recipeId = 999L;

        doThrow(new ResourceNotFoundException("Recipe not found with id " + recipeId))
                .when(recipeService).deleteRecipe(recipeId);

        mockMvc.perform(delete(BASE_URL + "/{id}", recipeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "mockJwt"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteRecipeUnauthorized() throws Exception {
        Long recipeId = 1L;

        doThrow(new AccessDeniedException("Access is denied"))
                .when(recipeService).deleteRecipe(recipeId);

        mockMvc.perform(delete(BASE_URL + "/{id}", recipeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}