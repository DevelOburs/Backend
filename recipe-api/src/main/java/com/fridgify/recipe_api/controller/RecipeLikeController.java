package com.fridgify.recipe_api.controller;

import com.fridgify.recipe_api.service.RecipeLikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recipe-api/like")
public class RecipeLikeController {

    private final RecipeLikeService recipeLikeService;

    public RecipeLikeController(RecipeLikeService recipeLikeService) {
        this.recipeLikeService = recipeLikeService;
    }

    @PostMapping("/{recipeId}")
    public ResponseEntity<Void> likeRecipe(@PathVariable Long recipeId, @RequestParam Long userId) {
        recipeLikeService.likeRecipe(recipeId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/count/{recipeId}")
    public ResponseEntity<Long> getLikeCount(@PathVariable Long recipeId) {
        long likeCount = recipeLikeService.countLikes(recipeId);
        return ResponseEntity.ok(likeCount);
    }
}

