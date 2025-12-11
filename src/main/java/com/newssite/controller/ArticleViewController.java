package com.newssite.controller;

import com.newssite.model.Article;
import com.newssite.service.ArticleService;
import com.newssite.service.CommentService;
import com.newssite.service.LikeService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ArticleViewController {

    private final LikeService likeService;
    private final ArticleService articleService;
    private final CommentService commentService;
    public ArticleViewController(ArticleService articleService, CommentService commentService, LikeService likeService) {
        this.articleService = articleService;
        this.commentService = commentService;
        this.likeService = likeService;
    }

    @GetMapping("/articles")
    public String articlesPage(Model model) {
        model.addAttribute("articles", articleService.getAllArticles());
        return "articles";
    }

    @GetMapping("/articles/{id}")
    public String articleDetail(
            @PathVariable Long id,
            @RequestParam(value = "skipView", required = false) Boolean skipView,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User user,
            Model model) {

        Article article = (skipView != null && skipView)
                ? articleService.findArticle(id)
                : articleService.getArticle(id);

        model.addAttribute("article", article);
        model.addAttribute("comments", commentService.getComments(id));

        boolean hasLiked = (user != null)
                ? likeService.userHasLiked(user.getUsername(), id)
                : false;

        model.addAttribute("hasLiked", hasLiked);

        return "article-detail";
    }



}
