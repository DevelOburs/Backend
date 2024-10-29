package com.fridgify.recipe_api.service;

import com.fridgify.recipe_api.model.Ingredient;
import com.fridgify.recipe_api.repository.IngredientRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    public Optional<Ingredient> getIngredientById(Long id) {
        return Optional.ofNullable(ingredientRepository.findIngredientById(id));
    }

    public List<Ingredient> getIngredientsByName(String name) {
        return ingredientRepository.findIngredientsByName(name);
    }

    public Ingredient saveIngredient(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    public void deleteIngredientById(Long id) {
        ingredientRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return ingredientRepository.existsById(id);
    }
}
