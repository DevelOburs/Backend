package com.fridgify.recipe_api.repository;

import com.fridgify.recipe_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserById(Long id);

    boolean existsById(Long id);

    // Fetch user with ingredients eagerly loaded
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.fridgeIngredients WHERE u.id = :userId")
    Optional<User> findByIdWithFridgeIngredients(@Param("userId") Long userId);
}
