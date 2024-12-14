package com.fridgify.recipe_api.controller;

import com.fridgify.recipe_api.dto.IngredientDTO;
import com.fridgify.recipe_api.dto.IngredientIdsDTO;
import com.fridgify.recipe_api.model.Ingredient;
import com.fridgify.recipe_api.model.IngredientCategory;
import com.fridgify.recipe_api.service.FridgeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/fridge-api")
public class FridgeController {

    private final FridgeService fridgeService;

    public FridgeController(FridgeService fridgeService) {
        this.fridgeService = fridgeService;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getIngredientCategories() {
        return new ResponseEntity<>(
                Arrays.stream(IngredientCategory.values())
                        .map(IngredientCategory::name)
                        .toList(),
                HttpStatus.OK
        );
    }

    @GetMapping("{userId}/inFridge")
    public ResponseEntity<List<IngredientDTO>> getInFridge(
            @PathVariable(value = "userId") Long userId,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "category", required = false) IngredientCategory category,
            @RequestParam(value = "nameFilter", required = false) String nameFilter
    ) {

        List<Ingredient> ingredients = fridgeService.getInFridge(userId, limit, pageNumber, category, nameFilter);

        List<IngredientDTO> ingredientDTOs = ingredients.stream()
                .map(IngredientDTO::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ingredientDTOs);
    }

    @GetMapping("{userId}/notInFridge")
    public ResponseEntity<List<IngredientDTO>> getNotInFridge(@PathVariable Long userId,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "category", required = false) IngredientCategory category,
            @RequestParam(value = "nameFilter", required = false) String nameFilter
    ) {

        List<Ingredient> ingredients = fridgeService.getNotInFridge(userId, limit, pageNumber, category, nameFilter);

        List<IngredientDTO> ingredientDTOs = ingredients.stream()
                .map(IngredientDTO::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ingredientDTOs);
    }

    @PutMapping("{userId}/add")
    public ResponseEntity<List<IngredientDTO>> addToFridge(
            @PathVariable Long userId,
            @RequestBody IngredientIdsDTO request) {

        List<Ingredient> ingredients = fridgeService.addToFridge(userId, request.getIngredientIds());

        List<IngredientDTO> ingredientDTOs = ingredients.stream()
                .map(IngredientDTO::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ingredientDTOs);
    }

    @PutMapping("{userId}/remove")
    public ResponseEntity<List<IngredientDTO>> removeFromFridge(
            @PathVariable Long userId,
            @RequestBody IngredientIdsDTO request) {

        List<Ingredient> ingredients = fridgeService.removeFromFridge(userId, request.getIngredientIds());

        List<IngredientDTO> ingredientDTOs = ingredients.stream()
                .map(IngredientDTO::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ingredientDTOs);
    }

}
