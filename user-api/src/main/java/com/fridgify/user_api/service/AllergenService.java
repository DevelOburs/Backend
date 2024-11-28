package com.fridgify.user_api.service;

import com.fridgify.user_api.model.User;
import com.fridgify.user_api.model.Allergen;
import com.fridgify.user_api.repository.UserRepository;
import com.fridgify.user_api.repository.AllergenRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AllergenService {

    private final UserRepository userRepository;
    private final AllergenRepository allergenRepository;

    public AllergenService(UserRepository userRepository, AllergenRepository allergenRepository) {
        this.userRepository = userRepository;
        this.allergenRepository = allergenRepository;
    }

    public boolean addAllergenToUser(Long userId, Long allergenId) {
        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            Optional<Allergen> existingAllergen = allergenRepository.findByUserAndAllergenId(user, allergenId);
            if (existingAllergen.isPresent()) {
                return false;
            }
            Allergen allergen = new Allergen();
            allergen.setUser(user);
            allergen.setAllergenId(allergenId);
            allergenRepository.save(allergen);

            return true;
        }

        return false;
    }

    public boolean removeAllergenFromUser(Long userId, Long allergenId) {
        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Optional<Allergen> allergenOpt = allergenRepository.findByUserAndAllergenId(user, allergenId);
            if (allergenOpt.isPresent()) {
                allergenRepository.delete(allergenOpt.get());
                return true;
            }
        }

        return false;
    }
}
