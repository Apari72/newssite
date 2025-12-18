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
    // Update arguments to include category and imageUrl
    @Transactional
    public ArticleSummaryDto createArticle(String email, String title, String content, String category, String imageUrl) {

        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (author.getRole() != Role.JOURNALIST && author.getRole() != Role.ADMIN) {
            throw new RuntimeException("Forbidden");
        }

        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setAuthor(author);

        // --- NEW FIELDS ---
        article.setCategory(category != null ? category : "General");
        article.setImageUrl(imageUrl); // <--- Save the URL to DB!
        // ------------------

        Article saved = articleRepository.save(article);
        return toSummaryDto(saved);
    }

    // ---------- GET ALL ----------
    // Update the method signature to accept 'category'
    public List<ArticleSummaryDto> getAllArticles(String email, String category) {

        User user = null;
        if (email != null) {
            user = userRepository.findByEmail(email).orElse(null);
        }

        List<Article> articles;

        // FILTERING LOGIC
        if (category != null && !category.isEmpty() && !category.equals("All")) {
            articles = articleRepository.findByCategoryOrderByCreatedAtDesc(category);
        } else {
            articles = articleRepository.findAllByOrderByCreatedAtDesc();
        }

        return articles.stream()
                .map(this::toSummaryDto)
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
    // UPDATE THE SIGNATURE to accept category and imageUrl
    public Article updateArticle(Long id, String title, String content, String category, String imageUrl, String email) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isAdmin = user.getRole() == Role.ADMIN;
        boolean isAuthor = article.getAuthor().getId().equals(user.getId());

        if (!isAdmin && !isAuthor) {
            throw new RuntimeException("Forbidden");
        }

        // UPDATE FIELDS
        article.setTitle(title);
        article.setContent(content);
        // Add these two lines:
        article.setCategory(category);
        article.setImageUrl(imageUrl);

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
