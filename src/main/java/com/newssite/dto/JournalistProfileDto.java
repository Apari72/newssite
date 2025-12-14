package com.newssite.dto;

import java.util.List;

public class JournalistProfileDto {

    public Long userId;
    public String name;
    public String bio;
    public double ratingScore;
    public int totalViews;
    public List<ArticleSummaryDto> articles;

    public JournalistProfileDto(
            Long userId,
            String name,
            String bio,
            Double ratingScore,
            int totalViews,
            List<ArticleSummaryDto> articles
    ) {
        this.userId = userId;
        this.name = name;
        this.bio = bio;
        this.ratingScore = ratingScore;
        this.totalViews = totalViews;
        this.articles = articles;
    }
}
