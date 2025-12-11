package com.newssite.controller;

import com.newssite.model.Article;
import com.newssite.service.ArticleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    // Journalist publishes
    @PostMapping("/create")
    public Article create(
            @RequestParam Long journalistId,
            @RequestParam String title,
            @RequestParam String content
    ) {
        return articleService.createArticle(journalistId, title, content);
    }

    // Public feed
    @GetMapping
    public List<Article> list() {
        return articleService.getAllArticles();
    }

    // Read article (adds view)
    @GetMapping("/{id}")
    public Article read(@PathVariable Long id) {
        return articleService.getArticle(id);
    }
}
