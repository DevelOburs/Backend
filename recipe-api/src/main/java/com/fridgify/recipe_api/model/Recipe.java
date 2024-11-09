package com.fridgify.recipe_api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor // JPA requires a no-argument constructor
@AllArgsConstructor // This creates an all-argument constructor for Lombok's builder
@Builder
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
