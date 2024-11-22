package com.fridgify.recipe_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "recipe_comment")
@Getter
@Setter
@NoArgsConstructor  // JPA requires a no-argument constructor
@AllArgsConstructor // This creates an all-argument constructor for Lombok's builder
@Builder
public class RecipeComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recipe_id", nullable = false)
    private Long recipeId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String comment;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    // Constructors, Getters, Setters
}
