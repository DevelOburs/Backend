package com.fridgify.recipe_api.dto;

import com.fridgify.recipe_api.model.Recipe;
import com.fridgify.recipe_api.model.RecipeCategory;
import com.fridgify.recipe_api.model.RecipeIngredient;
import com.fridgify.recipe_api.model.Ingredient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDTO {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private Long userId;
    private String userUsername;
    private String userFirstName;
    private String userLastName;
    private Long likeCount;
    private Long commentCount;
    private Long saveCount;
    private List<String> ingredients;
    private String imageUrl;
    private RecipeCategory category;
    private Long calories;
    private Long cookingTime;


    public Recipe toModel() {
        return Recipe.builder()
                .name(this.name)
                .description(this.description)
                .likeCount(this.likeCount)
                .commentCount(this.commentCount)
                .saveCount(this.saveCount)
                .createdAt(LocalDateTime.now())
                .imageUrl(this.imageUrl)
                .category(this.category)
                .calories(this.calories)
                .cookingTime(this.cookingTime)
                .ingredients(new ArrayList<>())
                .build();
    }

    public static RecipeDTO toResponse(Recipe recipe) {
        return builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .description(recipe.getDescription())
                .likeCount(recipe.getLikeCount())
                .commentCount(recipe.getCommentCount())
                .saveCount(recipe.getSaveCount())
                .imageUrl(recipe.getImageUrl())
                .createdAt(recipe.getCreatedAt())
                .userId(recipe.getUser().getId())
                .userUsername(recipe.getUser().getUsername())
                .userFirstName(recipe.getUser().getFirstName())
                .userLastName(recipe.getUser().getLastName())
                .category(recipe.getCategory())
                .calories(recipe.getCalories())
                .cookingTime(recipe.getCookingTime())
                .ingredients(recipe.getIngredients().stream()
                        .map(RecipeIngredient::getIngredient)
                        .map(Ingredient::getName)
                        .collect(Collectors.toList()))
                .build();
    }
}