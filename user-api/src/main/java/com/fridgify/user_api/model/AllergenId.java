package com.fridgify.user_api.model;

import java.io.Serializable;
import java.util.Objects;

public class AllergenId implements Serializable {
    private Long user;
    private Long allergenId;

    // Default constructor
    public AllergenId() {}

    // Parameterized constructor
    public AllergenId(Long user, Long allergenId) {
        this.user = user;
        this.allergenId = allergenId;
    }

    // Getters and setters
    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Long getAllergenId() {
        return allergenId;
    }

    public void setAllergenId(Long allergenId) {
        this.allergenId = allergenId;
    }

    // hashCode and equals methods
    @Override
    public int hashCode() {
        return Objects.hash(user, allergenId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AllergenId that = (AllergenId) obj;
        return Objects.equals(user, that.user) && Objects.equals(allergenId, that.allergenId);
    }
}