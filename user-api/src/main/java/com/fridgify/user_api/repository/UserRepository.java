package com.fridgify.user_api.repository;

import com.fridgify.user_api.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAll();
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmailIgnoreCase(String email);
}
