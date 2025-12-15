package com.newssite.service;

import com.newssite.dto.ArticleSummaryDto;
import com.newssite.model.Article;
import com.newssite.model.Role;
import com.newssite.model.User;
import com.newssite.repository.ArticleRepository;
import com.newssite.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public ArticleService(ArticleRepository articleRepository,
                          UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    // ---------- CREATE ARTICLE (JOURNALIST / ADMIN) ----------
    @Transactional
    public ArticleSummaryDto createArticle(String email, String title, String content) {

        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (author.getRole() != Role.JOURNALIST && author.getRole() != Role.ADMIN) {
            throw new RuntimeException("Forbidden");
        }

        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setAuthor(author);

        Article saved = articleRepository.save(article);
        return toSummaryDto(saved);
    }

    // ---------- GET ALL ----------
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    // ---------- GET SINGLE + VIEW INCREMENT ----------
    @Transactional
    public Article getArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        article.setViews(article.getViews() + 1);
        return article;
    }

    // ---------- DTO MAPPER ----------
    private ArticleSummaryDto toSummaryDto(Article article) {
        return new ArticleSummaryDto(
                article.getId(),
                article.getTitle(),
                article.getAuthor().getName(),
                article.getCreatedAt(),
                article.getViews(),
                article.getLikeCount()
        );
    }

}
