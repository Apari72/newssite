package com.newssite.repository;

import com.newssite.model.Comment;
import com.newssite.model.CommentLike;
import com.newssite.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    Optional<CommentLike> findByCommentAndUser(Comment comment, User user);

    int countByComment(Comment comment);
}

