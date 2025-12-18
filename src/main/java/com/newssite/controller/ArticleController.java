package com.newssite.controller;

import com.newssite.dto.ArticleCreateRequest;
import com.newssite.dto.ArticleSummaryDto;
import com.newssite.model.Article;
import com.newssite.service.ArticleService;
import com.newssite.service.LikeService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;
    private final LikeService likeService;

    public ArticleController(ArticleService articleService,LikeService likeService) {
        this.articleService = articleService;
        this.likeService = likeService;
    }

    @PostMapping
    public ArticleSummaryDto createArticle(
            @RequestBody ArticleCreateRequest request,
            @AuthenticationPrincipal UserDetails user
    ) {
        return articleService.createArticle(
                user.getUsername(),
                request.getTitle(),
                request.getContent()
        );
    }


    @GetMapping
    public List<ArticleSummaryDto> list(Authentication authentication) { // Change return type
        return articleService.getAllArticles(
                authentication != null ? authentication.getName() : null
        );
    }


    @GetMapping("/{id}")
    public Article read(
            @PathVariable Long id,
            Authentication authentication
    ) {
        return articleService.getArticle(
                id,
                authentication != null ? authentication.getName() : null
        );
    }


    @PostMapping("/{id}/like")
    public Article like(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new RuntimeException("Unauthorized");
        }

        return likeService.toggleLike(id, userDetails.getUsername());
    }
    @PutMapping("/{id}")
    public Article updateArticle(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            Authentication authentication
    ) {
        return articleService.updateArticle(
                id,
                body.get("title"),
                body.get("content"),
                authentication.getName()
        );
    }

    @DeleteMapping("/{id}")
    public void deleteArticle(
            @PathVariable Long id,
            Authentication authentication
    ) {
        articleService.deleteArticle(id, authentication.getName());
    }



}

