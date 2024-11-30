package com.fridgify.user_api.service;

import com.fridgify.user_api.dto.AllergenDTO;
import com.fridgify.user_api.model.User;
import com.fridgify.user_api.model.Allergen;
import com.fridgify.user_api.repository.UserRepository;
import com.fridgify.user_api.repository.AllergenRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AllergenService {

    private final UserRepository userRepository;
    private final AllergenRepository allergenRepository;

    public AllergenService(UserRepository userRepository, AllergenRepository allergenRepository) {
        this.userRepository = userRepository;
        this.allergenRepository = allergenRepository;
    }

    public AllergenDTO addAllergenToUser(Long userId, Long allergenId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Optional<Allergen> existingAllergen = allergenRepository.findByUserAndAllergenId(user, allergenId);
            if (existingAllergen.isPresent()) {
                throw new IllegalArgumentException("Allergen already exists for the user.");
            }
            Allergen allergen = new Allergen();
            allergen.setUser(user);
            allergen.setAllergenId(allergenId);
            allergenRepository.save(allergen);
            return new AllergenDTO(userId, allergenId);
        }
        throw new IllegalArgumentException("User not found.");
    }

    public void removeAllergenFromUser(Long userId, Long allergenId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Optional<Allergen> allergenOpt = allergenRepository.findByUserAndAllergenId(user, allergenId);
            if (allergenOpt.isPresent()) {
                allergenRepository.delete(allergenOpt.get());}
            else{
                throw new IllegalArgumentException("Allergen not found.");
            }
        }
        else {
            throw new IllegalArgumentException("User not found.");
        }
    }

    public List<Object> getAllAllergens(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Fetch allergens associated with the user
            List<Allergen> allergens = allergenRepository.findAllByUser(user);

            // Create a list to hold the result objects
            List<Object> result = new ArrayList<>();

            // Add the userId as a separate object
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("userId", user.getId());
            result.add(userMap);

            // Create a list of allergenIds
            List<Long> allergenIds = allergens.stream()
                    .map(Allergen::getAllergenId)
                    .collect(Collectors.toList());

            // Add the allergenIds as a separate object
            Map<String, Object> allergenMap = new HashMap<>();
            allergenMap.put("allergenId", allergenIds);
            result.add(allergenMap);

            return result;
        }

        throw new IllegalArgumentException("User not found.");
    }


}
