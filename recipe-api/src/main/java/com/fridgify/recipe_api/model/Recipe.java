package com.fridgify.recipe_api.model;

import com.fridgify.recipe_api.dto.IngredientDTO;
import com.fridgify.recipe_api.dto.RecipeDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false) // Establish the foreign key
    private User user;

    @Column(name = "like_count", nullable = false)
    private Long likeCount = 0L;

    @Column(name = "comment_count", nullable = false)
    private Long commentCount = 0L;

    @Column(name = "save_count", nullable = false)
    private Long saveCount = 0L;

    @Column(name = "image_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private RecipeCategory category;

    @Column(name = "calories", nullable = false)
    private Long calories;

    @Column(name = "cooking_time", nullable = false)
    private Long cookingTime;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<RecipeIngredient> ingredients = new ArrayList<>();

    public void updateIngredients(List<RecipeIngredient> newIngredients) {
        ingredients.clear();
        ingredients.addAll(newIngredients);
    }

    public RecipeDTO toResponse() {
        return RecipeDTO.builder()
                .id(this.getId())
                .name(this.getName())
                .description(this.getDescription())
                .likeCount(this.getLikeCount())
                .commentCount(this.getCommentCount())
                .saveCount(this.getSaveCount())
                .imageUrl(this.getImageUrl())
                .createdAt(this.getCreatedAt())
                .userId(this.getUser().getId())
                .userUsername(this.getUser().getUsername())
                .userFirstName(this.getUser().getFirstName())
                .userLastName(this.getUser().getLastName())
                .category(this.getCategory())
                .calories(this.getCalories())
                .cookingTime(this.getCookingTime())
                .ingredients(this.getIngredients().stream()
                        .map(RecipeIngredient::getIngredient)
                        .map(IngredientDTO::toResponse)
                        .collect(Collectors.toList()))
                .build();
    }
}