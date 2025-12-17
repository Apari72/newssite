package com.newssite.dto;

import java.util.List;

public class JournalistProfileStatsDto {

    public Long userId;
    public String name;
    public String bio;
    public boolean canEdit;

    public int totalArticles;
    public long totalViews;
    public double rating;
    public int totalLikes;
    public int totalComments;


    public List<ArticleSummaryDto> articles;
}

