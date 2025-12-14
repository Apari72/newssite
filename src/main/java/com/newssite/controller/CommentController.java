package com.newssite.controller;

import com.newssite.dto.CommentDto;
import com.newssite.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // ---------- ADD COMMENT ----------
    @PostMapping("/articles/{id}/comments")
    public CommentDto add(
            @PathVariable Long id,
            @RequestBody String content,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new RuntimeException("Unauthorized");
        }

        return commentService.addComment(id, userDetails.getUsername(), content);
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

    // ---------- EDIT COMMENT (OWNER ONLY) ----------
    @PutMapping("/comments/{id}")
    public CommentDto edit(
            @PathVariable Long id,
            @RequestBody String content,
            @AuthenticationPrincipal UserDetails user
    ) {
        if (user == null) {
            throw new RuntimeException("Unauthorized");
        }

        return commentService.editComment(id, user.getUsername(), content);
    }

    // ---------- DELETE COMMENT (OWNER OR ADMIN) ----------
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails user
    ) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        commentService.deleteComment(id, user.getUsername());
        return ResponseEntity.noContent().build(); // ✅ CRITICAL
    }

}
