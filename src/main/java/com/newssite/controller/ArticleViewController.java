package com.newssite.controller;

import com.newssite.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ArticleViewController {

    private final ArticleService articleService;

    public ArticleViewController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/articles")
    public String articlesPage(Model model) {
        model.addAttribute("articles", articleService.getAllArticles());
        return "articles";
    }

    @GetMapping("/articles/{id}")
    public String articleDetail(@PathVariable Long id, Model model) {
        model.addAttribute("article", articleService.getArticle(id));
        return "article-detail";
    }
}
