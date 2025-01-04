package com.fridgify.recipe_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fridgify.recipe_api.service.UserSavedRecipeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserSavedRecipeController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserSavedRecipeControllerTests {

    private final String BASE_URL = "/recipe-api/save";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserSavedRecipeService userSavedRecipeService;

    @Test
    public void testToggleSaveRecipe() throws Exception {
        Long recipeId = 1L;
        Long userId = 1L;

        mockMvc.perform(get(BASE_URL + "/{recipeId}/{userId}", recipeId, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "mockJwt"))
                .andExpect(status().isOk())
                .andExpect(content().string("Recipe save status toggled successfully."));

        verify(userSavedRecipeService, times(1)).toggleSaveRecipe(recipeId, userId);
    }

    @Test
    public void testToggleSaveRecipeError() throws Exception {
        Long recipeId = 1L;
        Long userId = 1L;

        doThrow(new RuntimeException("Database error"))
                .when(userSavedRecipeService).toggleSaveRecipe(recipeId, userId);

        mockMvc.perform(get(BASE_URL + "/{recipeId}/{userId}", recipeId, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "mockJwt"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Failed to toggle recipe save status: Database error"));
    }

    @Test
    public void testGetSaveCount() throws Exception {
        Long recipeId = 1L;
        Long expectedCount = 5L;

        when(userSavedRecipeService.getSaveCount(recipeId)).thenReturn(expectedCount);

        mockMvc.perform(get(BASE_URL + "/{recipeId}/count", recipeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "mockJwt"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(expectedCount));
    }

    @Test
    public void testGetSaveCountError() throws Exception {
        Long recipeId = 1L;

        when(userSavedRecipeService.getSaveCount(recipeId))
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get(BASE_URL + "/{recipeId}/count", recipeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "mockJwt"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value(0L));
    }
}
