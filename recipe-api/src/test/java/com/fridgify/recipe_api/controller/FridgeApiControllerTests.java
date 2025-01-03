package com.fridgify.recipe_api.controller;

import com.fridgify.recipe_api.dto.IngredientIdsDTO;
import com.fridgify.recipe_api.model.Ingredient;
import com.fridgify.recipe_api.service.FridgeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FridgeApiControllerTests {

    private MockMvc mockMvc;

    @Mock
    private FridgeService fridgeService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        FridgeController fridgeController = new FridgeController(fridgeService);
        mockMvc = MockMvcBuilders.standaloneSetup(fridgeController).build();
    }

    @Test
    public void testGetIngredientCategories() throws Exception {
        mockMvc.perform(get("/fridge-api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    public void testGetInFridge() throws Exception {
        when(fridgeService.getInFridge(anyLong(), any(), any(), any(), any()))
                .thenReturn(Collections.singletonList(new Ingredient()));

        mockMvc.perform(get("/fridge-api/1/inFridge"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    public void testAddToFridge() throws Exception {
        IngredientIdsDTO request = new IngredientIdsDTO(List.of(1L, 2L));
        when(fridgeService.addToFridge(anyLong(), any())).thenReturn(Collections.emptyList());

        mockMvc.perform(put("/fridge-api/1/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ingredientIds\":[1,2]}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").doesNotExist());
    }

    @Test
    public void testRemoveFromFridge() throws Exception {
        when(fridgeService.removeFromFridge(anyLong(), any()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(put("/fridge-api/1/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ingredientIds\":[3,4]}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").doesNotExist());
    }

    @Test
    public void testGetNotInFridge() throws Exception {
        when(fridgeService.getNotInFridge(anyLong(), any(), any(), any(), any()))
                .thenReturn(Collections.singletonList(new Ingredient()));

        mockMvc.perform(get("/fridge-api/1/notInFridge"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }
}