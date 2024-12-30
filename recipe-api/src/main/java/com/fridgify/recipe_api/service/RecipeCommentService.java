package com.fridgify.recipe_api.service;

import com.fridgify.recipe_api.dto.RecipeCommentDTO;
import com.fridgify.recipe_api.model.Recipe;
import com.fridgify.recipe_api.model.RecipeComment;
import com.fridgify.recipe_api.model.User;
import com.fridgify.recipe_api.repository.RecipeCommentRepository;
import com.fridgify.recipe_api.repository.RecipeRepository;
import com.fridgify.recipe_api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeCommentService {

    private final RecipeCommentRepository recipeCommentRepository;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public RecipeCommentService(RecipeCommentRepository recipeCommentRepository,
                                RecipeRepository recipeRepository, UserRepository userRepository) {
        this.recipeCommentRepository = recipeCommentRepository;
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }
    @Transactional
    public RecipeCommentDTO addComment(Long recipeId, Long userId, String comment) {
        RecipeComment recipeComment = new RecipeComment();
        recipeComment.setRecipeId(recipeId);
        recipeComment.setUserId(userId);
        recipeComment.setComment(comment);

        RecipeComment savedComment = recipeCommentRepository.save(recipeComment);
        updateCommentCount(recipeId, 1);
        return RecipeCommentDTO.builder()
                .id(savedComment.getId())
                .recipeId(savedComment.getRecipeId())
                .userId(savedComment.getUserId())
                .username(userRepository.findById(savedComment.getUserId()).map(User::getUsername).orElse(""))
                .comment(savedComment.getComment())
                .createdAt(savedComment.getCreatedAt()).build();
    }

    public List<RecipeCommentDTO> getComments(Long recipeId) {
        return recipeCommentRepository.findByRecipeIdOrderByCreatedAtDesc(recipeId)
                .stream()
                .map(comment ->  RecipeCommentDTO.builder()
                        .id(comment.getId())
                        .recipeId(comment.getRecipeId())
                        .userId(comment.getUserId())
                        .username(userRepository.findById(comment.getUserId()).map(User::getUsername).orElse(""))
                        .comment(comment.getComment())
                        .createdAt(comment.getCreatedAt())
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteComment(Long commentId, Long userId) throws IllegalAccessException {
        // Find the comment by ID
        RecipeComment comment = recipeCommentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        // Ensure the user is authorized to delete the comment
        if (!comment.getUserId().equals(userId)) {
            throw new IllegalAccessException("User not authorized to delete this comment");
        }

        Long recipeId = comment.getRecipeId();

        // Delete the comment
        recipeCommentRepository.delete(comment);

        // Update the comment count for the recipe
        updateCommentCount(recipeId, -1);
    }

    private void updateCommentCount(Long recipeId, int change) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("Recipe not found"));
        long updatedCount = Math.max(0, recipe.getCommentCount() + change);
        recipeRepository.updateCommentCount(recipeId, updatedCount);
    }
}
