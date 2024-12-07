package com.fridgify.recipe_api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fridgify.recipe_api.model.Recipe;
import com.fridgify.recipe_api.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String imageUrl;


    public Recipe toModel() {
        return Recipe.builder()
                .name(this.name)
                .description(this.description)
                .likeCount(this.likeCount)
                .commentCount(this.commentCount)
                .saveCount(this.saveCount)
                .createdAt(LocalDateTime.now())
                .imageUrl(this.imageUrl)
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
                .build();
    }
}