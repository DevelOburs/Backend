package com.fridgify.recipe_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fridgify.recipe_api.dto.RecipeCommentDTO;
import com.fridgify.recipe_api.dto.RecipeCreateCommentDTO;
import com.fridgify.recipe_api.service.RecipeCommentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RecipeCommentController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RecipeCommentControllerTests {

    private final String BASE_URL = "/recipe-api/comment";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RecipeCommentService recipeCommentService;

    @Test
    public void testGetComments() throws Exception {
        Long recipeId = 1L;

        RecipeCommentDTO comment1 = RecipeCommentDTO.builder()
                .id(1L)
                .comment("Great recipe!")
                .userId(1L)
                .username("user1")
                .build();

        List<RecipeCommentDTO> comments = List.of(comment1);
        when(recipeCommentService.getComments(recipeId)).thenReturn(comments);

        mockMvc.perform(get(BASE_URL + "/{recipeId}", recipeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "mockJwt"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].comment").value("Great recipe!"));

        verify(recipeCommentService).getComments(recipeId);
    }

    @Test
    public void testAddComment() throws Exception {
        Long recipeId = 1L;
        Long userId = 1L;
        String commentText = "Great recipe!";

        RecipeCreateCommentDTO commentDTO = new RecipeCreateCommentDTO();
        commentDTO.setComment(commentText);

        RecipeCommentDTO savedComment = RecipeCommentDTO.builder()
                .id(1L)
                .recipeId(recipeId)
                .userId(userId)
                .comment(commentText)
                .username("testUser")
                .build();

        when(recipeCommentService.addComment(recipeId, userId, commentText)).thenReturn(savedComment);

        mockMvc.perform(post(BASE_URL + "/{recipeId}", recipeId)
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDTO))
                        .header("Authorization", "mockJwt"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment").value(commentText));

        verify(recipeCommentService).addComment(recipeId, userId, commentText);
    }
}
