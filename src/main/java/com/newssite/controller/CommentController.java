package com.newssite.controller;

import com.newssite.model.User;
import com.newssite.repository.UserRepository;
import com.newssite.service.CommentService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserRepository userRepository;

    public CommentController(CommentService commentService, UserRepository userRepository) {
        this.commentService = commentService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public String addComment(
            @RequestParam Long articleId,
            @RequestParam String content,
            Authentication authentication
    ) {
        // email (used for login)
        String email = authentication.getName();

        // load full user object
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String displayName = user.getName(); // this is “alper”, “john”, etc.

        commentService.addComment(articleId, displayName, content);

        return "redirect:/articles/" + articleId;
    }

}
