package com.newssite.repository;

import com.newssite.model.Article;
import com.newssite.model.Like;
import com.newssite.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByUserAndArticle(User user, Article article);
    void deleteByUserAndArticle(User user, Article article);

}


