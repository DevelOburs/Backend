package com.fridgify.user_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(Allergen.AllergenId.class)
@Table(name = "user_allergen")
public class Allergen {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Id
    @Column(name = "allergen_id", nullable = false)
    private long allergenId;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AllergenId implements Serializable {
        private Long user;
        private Long allergenId;
    }
}