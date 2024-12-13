package com.fridgify.recipe_api.controller;

import com.fridgify.recipe_api.dto.RecipeDTO;
import com.fridgify.recipe_api.service.RecipeLikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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

    @GetMapping("/totalCountOfUser")
    public ResponseEntity<?> getTotalLikeCountOfUser(@RequestParam Long userId) {
        try {
            if (userId == null || userId <= 0) {
                return ResponseEntity.badRequest().body("Invalid user ID");
            }

            long totalLikeCount = recipeLikeService.totalCountLikes(userId);
            return ResponseEntity.ok(totalLikeCount);
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching the total like count.");
        }
    }

    @GetMapping("userLikedRecipes")
    public ResponseEntity<?> getUserLikedRecipes(@RequestParam Long userId) {
        try {
            if (userId == null || userId <= 0) {
                return ResponseEntity.badRequest().body("Invalid user ID");
            }

            List<RecipeDTO> recipeDTOList = recipeLikeService.getRecipesLikedByUser(userId);
            return ResponseEntity.ok(recipeDTOList);
        } catch (RuntimeException e) {
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching liked recipes.");
        }
    }
}

