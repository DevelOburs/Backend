package com.fridgify.user_api.repository;

import com.fridgify.user_api.model.Allergen;
import com.fridgify.user_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AllergenRepository extends JpaRepository<Allergen, Long> {
    Optional<Allergen> findByUserAndAllergenId(User user, long allergenId);
    Optional<Allergen> findById(Long id);
    List<Allergen> findAllByUser(User user);
}
