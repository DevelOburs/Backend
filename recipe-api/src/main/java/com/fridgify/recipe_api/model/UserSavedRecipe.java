package com.fridgify.recipe_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "user_saved_recipe")
@Getter
@Setter
@NoArgsConstructor  // JPA requires a no-argument constructor
@AllArgsConstructor // This creates an all-argument constructor for Lombok's builder
@Builder
public class UserSavedRecipe {
    @EmbeddedId
    private UserSavedRecipe.UserSavedRecipeId id;

    @Getter
    @Setter
    @Embeddable
    public static class UserSavedRecipeId implements Serializable {
        @Column(name = "user_id")
        private long userId;

        @Column(name = "recipe_id")
        private long recipeId;
    }
}
