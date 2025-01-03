package com.fridgify.recipe_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fridgify.recipe_api.controller.RecipeLikeController;
import com.fridgify.recipe_api.service.RecipeLikeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RecipeLikeController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RecipeLikeControllerTests {

    private final String BASE_URL = "/recipe-api/like";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RecipeLikeService recipeLikeService;

    @Test
    public void testLikeRecipe() throws Exception {
        Long recipeId = 1L;
        Long userId = 1L;

        mockMvc.perform(post(BASE_URL + "/{recipeId}", recipeId)
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "mockJwt"))
                .andExpect(status().isOk());

        verify(recipeLikeService, times(1)).likeRecipe(recipeId, userId);
    }

    @Test
    public void testLikeRecipeUnauthorized() throws Exception {
        Long recipeId = 1L;
        Long userId = 1L;

        doThrow(new AccessDeniedException("Access is denied"))
                .when(recipeLikeService).likeRecipe(recipeId, userId);

        mockMvc.perform(post(BASE_URL + "/{recipeId}", recipeId)
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
