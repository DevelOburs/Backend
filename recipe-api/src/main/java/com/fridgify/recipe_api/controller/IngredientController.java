package com.fridgify.recipe_api.controller;

import com.fridgify.recipe_api.dto.IngredientDTO;
import com.fridgify.recipe_api.model.Ingredient;
import com.fridgify.recipe_api.service.IngredientService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recipe-api/ingredient")
public class IngredientController {

    private final IngredientService ingredientService;

    @GetMapping
    public ResponseEntity<List<IngredientDTO>> getAllIngredients() {
        List<IngredientDTO> ingredients = ingredientService
            .getAllIngredients()
            .stream()
            .map(ingredient ->
                new IngredientDTO(ingredient.getId(), ingredient.getName())
            )
            .collect(Collectors.toList());
        return new ResponseEntity<>(ingredients, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientDTO> getIngredientById(
        @PathVariable Long id
    ) {
        Optional<Ingredient> ingredient = ingredientService.getIngredientById(
            id
        );
        if (ingredient.isPresent()) {
            IngredientDTO ingredientDTO = new IngredientDTO(
                ingredient.get().getId(),
                ingredient.get().getName()
            );
            return new ResponseEntity<>(ingredientDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<IngredientDTO> createIngredient(
        @RequestBody IngredientDTO ingredientDTO
    ) {
        Ingredient ingredient = ingredientDTO.toModel();
        Ingredient savedIngredient = ingredientService.saveIngredient(
            ingredient
        );
        IngredientDTO savedIngredientDTO = new IngredientDTO(
            savedIngredient.getId(),
            savedIngredient.getName()
        );
        return new ResponseEntity<>(savedIngredientDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngredientDTO> updateIngredient(
        @PathVariable Long id,
        @RequestBody IngredientDTO ingredientDTO
    ) {
        if (!ingredientService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Ingredient ingredient = ingredientDTO.toModel();
        ingredient.setId(id); // Ensure the ID is set for the update
        Ingredient updatedIngredient = ingredientService.saveIngredient(
            ingredient
        );
        IngredientDTO updatedIngredientDTO = new IngredientDTO(
            updatedIngredient.getId(),
            updatedIngredient.getName()
        );
        return new ResponseEntity<>(updatedIngredientDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long id) {
        if (!ingredientService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ingredientService.deleteIngredientById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
