package com.fridgify.recipe_api.repository;

import com.fridgify.recipe_api.model.Recipe;
import com.fridgify.recipe_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserById(Long id);

    boolean existsById(Long id);
}
