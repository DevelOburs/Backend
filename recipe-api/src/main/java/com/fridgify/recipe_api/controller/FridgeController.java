package com.fridgify.recipe_api.controller;


import com.fridgify.recipe_api.dto.FridgeDTO;
import com.fridgify.recipe_api.service.FridgeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fridge-api")
public class FridgeController {

    private final FridgeService fridgeService;

    public FridgeController(FridgeService fridgeService) {
        this.fridgeService = fridgeService;
    }

    @GetMapping("{userId}")
    public ResponseEntity<FridgeDTO> getAllIngredientsInFridge(@PathVariable Long userId) {
        return new ResponseEntity<>(fridgeService.getAllIngredientsInFridge(userId), HttpStatus.OK);
    }

}
