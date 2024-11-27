package com.fridgify.recipe_api.controller;

import com.fridgify.recipe_api.dto.IngredientDTO;
import com.fridgify.recipe_api.dto.IngredientIdsDTO;
import com.fridgify.recipe_api.service.FridgeService;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<List<IngredientDTO>> getAllIngredientsInFridge(@PathVariable Long userId,
                                                               @RequestParam(required = false) String nameFilter,
                                                               @RequestParam(required = false) List<String> categoryFilters) {
        return new ResponseEntity<>(fridgeService.getAllIngredientsInFridge(userId, nameFilter, categoryFilters), HttpStatus.OK);
    }

    @GetMapping("{userId}/inFridgePaginated")
    public ResponseEntity<Page<IngredientDTO>> getAllIngredientsInFridgePaginated(
            @PathVariable Long userId,
            @RequestParam(required = false) String nameFilter,
            @RequestParam(required = false) List<String> categoryFilters,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(fridgeService.getAllIngredientsInFridge(userId, nameFilter, categoryFilters, page, size), HttpStatus.OK);
    }

    @GetMapping("{userId}/inFridge/{ingredientCategory}")
    public ResponseEntity<List<IngredientDTO>> getAllIngredientsInFridgeByCategory(@PathVariable Long userId,
                                                                      @PathVariable String ingredientCategory) {
        return new ResponseEntity<>(fridgeService.getAllIngredientsInFridge(userId, null, List.of(ingredientCategory)), HttpStatus.OK);
    }

    @GetMapping("{userId}/notInFridge")
    public ResponseEntity<List<IngredientDTO>> getIngredientsNotInFridge(@PathVariable Long userId,
                                                                         @RequestParam(required = false) String nameFilter,
                                                                         @RequestParam(required = false) List<String> categoryFilters) {
        return new ResponseEntity<>(fridgeService.getIngredientsNotInFridge(userId, nameFilter, categoryFilters), HttpStatus.OK);
    }

    @GetMapping("{userId}/notInFridgePaginated")
    public ResponseEntity<Page<IngredientDTO>> getIngredientsNotInFridge(@PathVariable Long userId,
                                                                         @RequestParam(required = false) String nameFilter,
                                                                         @RequestParam(required = false) List<String> categoryFilters,
                                                                         @RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(fridgeService.getIngredientsNotInFridge(userId, nameFilter, categoryFilters, page, size), HttpStatus.OK);
    }

    @PutMapping("{userId}/add")
    public ResponseEntity<List<IngredientDTO>> addIngredientsToFridge(
            @PathVariable Long userId,
            @RequestBody IngredientIdsDTO request) {
        return new ResponseEntity<>(fridgeService.addIngredientsToFridge(userId, request.getIngredientIds()), HttpStatus.OK);
    }

    @DeleteMapping("{userId}/remove")
    public ResponseEntity<List<IngredientDTO>> removeIngredientsFromFridge(
            @PathVariable Long userId,
            @RequestBody IngredientIdsDTO request) {
        return new ResponseEntity<>(fridgeService.removeIngredientsFromFridge(userId, request.getIngredientIds()), HttpStatus.OK);
    }
}
