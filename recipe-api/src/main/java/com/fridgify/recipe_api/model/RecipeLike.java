package com.fridgify.recipe_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "recipe_like")
@Getter
@Setter
@NoArgsConstructor  // JPA requires a no-argument constructor
@AllArgsConstructor // This creates an all-argument constructor for Lombok's builder
@Builder
public class RecipeLike {

    @EmbeddedId
    private RecipeLikeId id;

    @Column(nullable = false)
    private Boolean liked = true;

    @Setter
    @Getter
    @Embeddable
    public static class RecipeLikeId implements Serializable {
        @Column(name = "recipe_id")
        private Long recipeId;

        @Column(name = "user_id")
        private Long userId;



    }

    // Constructors, Getters, Setters
}
