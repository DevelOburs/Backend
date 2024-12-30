package com.fridgify.recipe_api.controller;

import com.fridgify.recipe_api.dto.RecipeCommentDTO;
import com.fridgify.recipe_api.dto.RecipeCreateCommentDTO;
import com.fridgify.recipe_api.service.RecipeCommentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipe-api/comment")
public class RecipeCommentController {

    private final RecipeCommentService recipeCommentService;

    public RecipeCommentController(RecipeCommentService recipeCommentService) {
        this.recipeCommentService = recipeCommentService;
    }

    @PostMapping("/{recipeId}")
    public ResponseEntity<RecipeCommentDTO> addComment(
            @PathVariable Long recipeId,
            @RequestParam Long userId,
            @RequestBody RecipeCreateCommentDTO commentDTO) {

        RecipeCommentDTO savedComment = recipeCommentService.addComment(recipeId, userId, commentDTO.getComment());
        return ResponseEntity.ok(savedComment);
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<List<RecipeCommentDTO>> getComments(@PathVariable Long recipeId) {
        List<RecipeCommentDTO> comments = recipeCommentService.getComments(recipeId);
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId,
                                              @RequestParam Long userId) {
        try {
            recipeCommentService.deleteComment(commentId, userId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("There is no recipe comment"); // Comment not found
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You cannot delete another person's comment!"); // Unauthorized access
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred."); // Generic error
        }
    }
}
