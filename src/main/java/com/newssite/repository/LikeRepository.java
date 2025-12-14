package com.newssite.repository;

import com.newssite.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Like findByUserIdAndArticleId(Long userId, Long articleId);

}



