package com.fridgify.recipe_api.controller;


import com.fridgify.recipe_api.dto.FridgeDTO;
import com.fridgify.recipe_api.dto.IngredientDTO;
import com.fridgify.recipe_api.service.FridgeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fridge-api")
public class FridgeController {

    private final FridgeService fridgeService;

    public FridgeController(FridgeService fridgeService) {
        this.fridgeService = fridgeService;
    }

    @GetMapping("{userId}/inFridge")
    public ResponseEntity<FridgeDTO> getAllIngredientsInFridge(@PathVariable Long userId) {
        return new ResponseEntity<>(fridgeService.getAllIngredientsInFridge(userId), HttpStatus.OK);
    }

    @GetMapping("{userId}/notInFridge")
    public ResponseEntity<List<IngredientDTO>> getIngredientsNotInFridge(@PathVariable Long userId) {
        return new ResponseEntity<>(fridgeService.getIngredientsNotInFridge(userId), HttpStatus.OK);
    }

    @PostMapping("{userId}/add")
    public ResponseEntity<FridgeDTO> addIngredientsToFridge(
            @PathVariable Long userId,
            @RequestBody List<IngredientDTO> ingredientDTOs) {
        return new ResponseEntity<>(fridgeService.addIngredientsToFridge(userId, ingredientDTOs), HttpStatus.OK);
    }

    @DeleteMapping("{userId}/remove")
    public ResponseEntity<FridgeDTO> removeIngredientsFromFridge(
            @PathVariable Long userId,
            @RequestBody List<IngredientDTO> ingredientIds) {
        return new ResponseEntity<>(fridgeService.removeIngredientsFromFridge(userId, ingredientIds), HttpStatus.OK);
    }
}
