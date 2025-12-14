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
    public Article toggleLike(Long articleId, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        Like existingLike =
                likeRepository.findByUserIdAndArticleId(user.getId(), article.getId());

        if (existingLike != null) {
            // UNLIKE
            likeRepository.delete(existingLike);
            article.setLikeCount(article.getLikeCount() - 1);
        } else {
            // LIKE
            Like like = new Like();
            like.setUser(user);
            like.setArticle(article);
            likeRepository.save(like);
            article.setLikeCount(article.getLikeCount() + 1);
        }

        return articleRepository.save(article);
    }





    public boolean userHasLiked(String email, Long articleId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return likeRepository.findByUserIdAndArticleId(user.getId(), articleId) != null;
    }}







