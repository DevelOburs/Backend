package com.fridgify.recipe_api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@Builder
public class RecipeCommentDTO {

    private Long id;
    private Long recipeId;
    private Long userId;
    private String username;
    private String comment;
    private Date createdAt;
}
