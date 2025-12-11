package com.newssite.service;

import com.newssite.model.Article;
import com.newssite.model.Like;
import com.newssite.model.User;
import com.newssite.repository.ArticleRepository;
import com.newssite.repository.LikeRepository;
import com.newssite.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    public LikeService(LikeRepository likeRepository, UserRepository userRepository,
                       ArticleRepository articleRepository) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    @Transactional
    public void toggleLike(Long articleId, String email) {

        User user = userRepository.findByEmail(email).orElseThrow();
        Article article = articleRepository.findById(articleId).orElseThrow();

        boolean hasLiked = likeRepository.existsByUserAndArticle(user, article);

        if (hasLiked) {
            likeRepository.deleteByUserAndArticle(user, article);
            article.setLikeCount(article.getLikeCount() - 1);
        } else {
            Like like = new Like();
            like.setUser(user);
            like.setArticle(article);
            likeRepository.save(like);
            article.setLikeCount(article.getLikeCount() + 1);
        }

        articleRepository.save(article);
    }

    public boolean userHasLiked(String email, Long articleId) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Article article = articleRepository.findById(articleId).orElseThrow();
        return likeRepository.existsByUserAndArticle(user, article);
    }
}






