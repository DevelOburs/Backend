package com.fridgify.user_api.controller;
import com.fridgify.user_api.dto.*;
import com.fridgify.user_api.service.AllergenService;
import com.fridgify.user_api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@RestController
@RequestMapping("/user-api/allergen")
public class AllergenController {

    private final AllergenService allergenService;

    public AllergenController(AllergenService allergenService) {
        this.allergenService = allergenService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<Boolean> addAllergen(@PathVariable Long id, @RequestParam Long allergenId) {
        boolean response = allergenService.addAllergenToUser(id, allergenId);
        if (response) {
            return ResponseEntity.status(HttpStatus.CREATED).body(true);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> removeAllergen(@PathVariable Long id, @RequestParam Long allergenId) {
        boolean response = allergenService.removeAllergenFromUser(id, allergenId);
        if (response) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }
}
