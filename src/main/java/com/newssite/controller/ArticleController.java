package com.newssite.controller;

import com.newssite.model.Article;
import com.newssite.service.ArticleService;
import com.newssite.service.LikeService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;
    private final LikeService likeService;

    public ArticleController(ArticleService articleService,LikeService likeService) {
        this.articleService = articleService;
        this.likeService = likeService;
    }

    @PostMapping("/create")
    public Article create(
            @RequestParam Long journalistId,
            @RequestParam String title,
            @RequestParam String content
    ) {
        return articleService.createArticle(journalistId, title, content);
    }

    @GetMapping
    public List<Article> list() {
        return articleService.getAllArticles();
    }

    @GetMapping("/{id}")
    public Article read(@PathVariable Long id) {
        return articleService.getArticle(id);
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

}

