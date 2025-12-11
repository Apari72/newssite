package com.newssite.controller;

import com.newssite.service.LikeService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/articles")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/{articleId}/like")
    public String toggleLike(
            @PathVariable Long articleId,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User user
    ) {
        likeService.toggleLike(articleId, user.getUsername());
        return "redirect:/articles/" + articleId + "?skipView=true";
    }
}






