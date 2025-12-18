package com.newssite.controller;

import com.newssite.dto.CommentDto;
import com.newssite.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // ---------- LIST COMMENTS ----------
    @GetMapping("/articles/{id}/comments")
    public List<CommentDto> list(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails != null ? userDetails.getUsername() : null;
        return commentService.getComments(id, email);
    }

    // ---------- ADD COMMENT (FIXED) ----------
    @PostMapping("/articles/{articleId}/comments")
    public CommentDto addComment(
            @PathVariable Long articleId,
            @RequestBody Map<String, String> payload, // 1. Use Map to catch JSON
            Authentication authentication
    ) {
        if (authentication == null) {
            throw new RuntimeException("You must be logged in to comment.");
        }

        // 2. Extract strictly the text
        String content = payload.get("content");
        if (content == null || content.trim().isEmpty()) {
            throw new RuntimeException("Comment cannot be empty");
        }

        return commentService.addComment(
                articleId,
                authentication.getName(),
                content
        );

    }

    // ---------- EDIT COMMENT (FIXED) ----------
    // Check your 'edit' method. It MUST accept Map<String, String>, NOT String.
    @PutMapping("/comments/{id}")
    public CommentDto edit(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload, // <--- CRITICAL: Must be Map
            @AuthenticationPrincipal UserDetails user
    ) {
        if (user == null) throw new RuntimeException("Unauthorized");

        String content = payload.get("content"); // <--- Extract text
        return commentService.editComment(id, user.getUsername(), content);
    }

    // ---------- LIKE / UNLIKE COMMENT ----------
    @PostMapping("/comments/{id}/like")
    public CommentDto like(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails user
    ) {
        if (user == null) {
            throw new RuntimeException("Unauthorized");
        }
        return commentService.toggleLike(id, user.getUsername());
    }

    // ---------- DELETE COMMENT ----------
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails user
    ) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        commentService.deleteComment(id, user.getUsername());
        return ResponseEntity.noContent().build();
    }
}