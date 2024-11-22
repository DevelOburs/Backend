package com.fridgify.recipe_api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fridgify.recipe_api.model.Recipe;
import com.fridgify.recipe_api.model.User;
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
    private Long userId; // This field will be populated from JSON
    private Long likeCount;
    private Long commentCount;
    private Long saveCount;

    @JsonIgnore
    private User user; // Used internally to map the user entity
    // You can add other fields if necessary, but these are the basics

    public Recipe toModel() {
        return Recipe.builder()
                .name(this.name)
                .description(this.description)
                .user(User.builder().id(this.userId).build())
                .likeCount(this.likeCount)
                .commentCount(this.commentCount)
                .createdAt(LocalDateTime.now())
                .build();
    }
}