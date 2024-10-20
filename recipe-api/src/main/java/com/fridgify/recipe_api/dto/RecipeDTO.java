package com.fridgify.recipe_api.dto;

import com.fridgify.recipe_api.model.Recipe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// Using Lombok annotations to avoid boilerplate code
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDTO {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    // You can add other fields if necessary, but these are the basics

    public Recipe toModel() {
        return Recipe.builder()
                .name(this.name)
                .description(this.description)
                .createdAt(LocalDateTime.now())
                .build();
    }
}