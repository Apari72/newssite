package com.newssite.service;

import com.newssite.model.Article;
import com.newssite.model.Role;
import com.newssite.model.User;
import com.newssite.repository.ArticleRepository;
import com.newssite.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public ArticleService(ArticleRepository articleRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    public Article createArticle(Long journalistId, String title, String content) {
        User user = userRepository.findById(journalistId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.JOURNALIST && user.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only journalists or admins can publish articles");
        }


        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setAuthor(user);

        return articleRepository.save(article);
    }

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Article getArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        article.setViews(article.getViews() + 1);
        return articleRepository.save(article);
    }
}
