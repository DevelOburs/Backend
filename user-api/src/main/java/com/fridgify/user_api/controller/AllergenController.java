package com.fridgify.user_api.controller;
import com.fridgify.user_api.dto.*;
import com.fridgify.user_api.service.AllergenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
@RequestMapping("/user-api/allergen")
public class AllergenController {

    private final AllergenService allergenService;

    public AllergenController(AllergenService allergenService) {
        this.allergenService = allergenService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<AllergenDTO> addAllergen(@PathVariable Long id, @RequestParam Long allergenId) {
        try {
            AllergenDTO response = allergenService.addAllergenToUser(id, allergenId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeAllergen(@PathVariable Long id, @RequestParam Long allergenId) {
        try {
            allergenService.removeAllergenFromUser(id, allergenId);
            return ResponseEntity.status(HttpStatus.OK).body("Allergen removed.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Object>> getAllAllergens(@PathVariable Long id) {
        try {
            List<Object> response = allergenService.getAllAllergens(id); // Get the modified response with Map objects
            return ResponseEntity.status(HttpStatus.OK).body(response); // Return the response as a List of Map objects
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Handle user not found scenario
        }
    }


}
