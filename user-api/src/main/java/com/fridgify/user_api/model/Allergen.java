package com.fridgify.user_api.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(AllergenId.class)
@Table(name = "user_allergen")
public class Allergen {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false) // Join with User table
    private User user;

    @Id
    @Column(name = "allergen_id", nullable = false) // Represents allergen ID
    private long allergenId;
}