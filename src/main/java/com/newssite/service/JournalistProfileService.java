package com.newssite.service;

import com.newssite.dto.*;

import com.newssite.model.Article;
import com.newssite.model.JournalistProfile;
import com.newssite.model.*;

import com.newssite.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JournalistProfileService {

    private final UserRepository userRepo;
    private final JournalistProfileRepository profileRepo;
    private final ArticleRepository articleRepo;

    public JournalistProfileService(
            UserRepository userRepo,
            JournalistProfileRepository profileRepo,
            ArticleRepository articleRepo
    ) {
        this.userRepo = userRepo;
        this.profileRepo = profileRepo;
        this.articleRepo = articleRepo;
    }
    private ArticleSummaryDto toSummary(Article article) {
        return new ArticleSummaryDto(
                article.getId(),
                article.getTitle(),
                article.getAuthor().getName(),
                article.getCreatedAt(),
                article.getViews(),
                article.getLikeCount()
        );
    }


    public JournalistProfileStatsDto getProfile(Long userId, String requesterEmail) {

        User journalist = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        JournalistProfile profile = profileRepo.findByUser(journalist)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        User requester = requesterEmail == null
                ? null
                : userRepo.findByEmail(requesterEmail).orElse(null);

        boolean canEdit =
                requester != null &&
                        (requester.getRole() == Role.ADMIN ||
                                requester.getId().equals(journalist.getId()));

        List<Article> articles = articleRepo.findByAuthorId(journalist.getId());

        int totalArticles = articles.size();
        long totalViews = articles.stream().mapToLong(Article::getViews).sum();
        int totalLikes = articles.stream().mapToInt(Article::getLikeCount).sum();

        int totalComments = articles.stream()
                .mapToInt(a -> a.getComments() == null ? 0 : a.getComments().size())
                .sum();

        double rating = calculateRating(totalLikes, totalComments, totalArticles);

        List<ArticleSummaryDto> articleDtos = articles.stream()
                .map(this::toSummary)
                .toList();

        JournalistProfileStatsDto dto = new JournalistProfileStatsDto();
        dto.userId = journalist.getId();
        dto.name = journalist.getName();
        dto.bio = profile.getBio();
        dto.canEdit = canEdit;
        dto.totalArticles = totalArticles;
        dto.totalViews = totalViews;
        dto.totalLikes = totalLikes;
        dto.totalComments = totalComments;
        dto.rating = rating;
        dto.articles = articleDtos;

        return dto;
    }
    private double calculateRating(int likes, int comments, int articles) {
        double raw =
                likes * 1.0 +
                        comments * 0.5 -
                        articles * 0.2;

        return Math.max(0,
                Math.min(10,
                        (raw / (articles + 5)) * 2
                ));
    }
    @Transactional
    public void updateBio(Long userId, String bio, String email) {

        User editor = userRepo.findByEmail(email).orElseThrow();
        User owner = userRepo.findById(userId).orElseThrow();

        boolean allowed =
                editor.getRole() == Role.ADMIN ||
                        editor.getId().equals(owner.getId());

        if (!allowed) throw new RuntimeException("Forbidden");

        JournalistProfile profile = profileRepo.findByUser(owner).orElseThrow();
        profile.setBio(bio);
    }



}

