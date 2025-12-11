package com.newssite.repository;

import com.newssite.model.Comment;
import com.newssite.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByArticleOrderByCreatedAtDesc(Article article);

}
