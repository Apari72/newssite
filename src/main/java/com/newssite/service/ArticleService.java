package com.newssite.service;

import com.newssite.dto.ArticleSummaryDto;
import com.newssite.model.Article;
import com.newssite.model.Role;
import com.newssite.model.User;
import com.newssite.repository.ArticleRepository;
import com.newssite.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.newssite.dto.ArticleSummaryDto;
import java.util.stream.Collectors;
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
    public List<ArticleSummaryDto> getAllArticles(String email) {

        User user = null;
        if (email != null) {
            user = userRepository.findByEmail(email).orElse(null);
        }

        List<Article> articles = articleRepository.findAll();

        // Convert the raw Entities into your DTOs
        return articles.stream()
                .map(this::toSummaryDto) // This converts Entity -> DTO (populating journalistName)
                .collect(Collectors.toList());
    }


    // ---------- GET SINGLE + VIEW INCREMENT ----------
    @Transactional
    public Article getArticle(Long id, String email) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        article.setViews(article.getViews() + 1);

        User user = null;
        if (email != null) {
            user = userRepository.findByEmail(email).orElse(null);
        }

        boolean canEdit =
                user != null &&
                        (user.getRole() == Role.ADMIN ||
                                article.getAuthor().getId().equals(user.getId()));

        article.setCanEdit(canEdit);

        return article;
    }


    // ---------- DTO MAPPER ----------
    private ArticleSummaryDto toSummaryDto(Article article) {
        return new ArticleSummaryDto(
                article.getId(),
                article.getTitle(),
                article.getAuthor().getId(),
                article.getAuthor().getName(),
                article.getCreatedAt(),
                article.getViews(),
                article.getLikeCount(),
                article.getSummary(),
                article.getCategory(),
                article.getImageUrl()
        );

    }
    @Transactional
    public Article updateArticle(Long id, String title, String content, String email) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isAdmin = user.getRole() == Role.ADMIN;
        boolean isAuthor = article.getAuthor().getId().equals(user.getId());

        if (!isAdmin && !isAuthor) {
            throw new RuntimeException("Forbidden");
        }

        article.setTitle(title);
        article.setContent(content);

        return articleRepository.save(article);
    }

    @Transactional
    public void deleteArticle(Long id, String email) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isAdmin = user.getRole() == Role.ADMIN;
        boolean isAuthor = article.getAuthor().getId().equals(user.getId());

        if (!isAdmin && !isAuthor) {
            throw new RuntimeException("Forbidden");
        }

        articleRepository.delete(article);
    }


}
