package com.newssite.repository;

import com.newssite.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByCategoryOrderByCreatedAtDesc(String category);
    List<Article> findByAuthorIdOrderByCreatedAtDesc(Long authorId);
    List<Article> findAllByOrderByCreatedAtDesc();
}
