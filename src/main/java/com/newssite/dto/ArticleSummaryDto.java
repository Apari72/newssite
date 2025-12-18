package com.newssite.dto;

import java.time.LocalDateTime;

public class ArticleSummaryDto {

    public Long id;
    public String title;

    public Long journalistId;
    public String journalistName;

    public String summary;
    public String category;
    public String imageUrl;

    public LocalDateTime createdAt;
    public int views;
    public int likeCount;

    public ArticleSummaryDto(
            Long id,
            String title,
            Long journalistId,
            String journalistName,
            LocalDateTime createdAt,
            int views,
            int likeCount,
            String summary,
            String category,
            String imageUrl
    ) {
        this.id = id;
        this.title = title;
        this.journalistId = journalistId;
        this.journalistName = journalistName;
        this.createdAt = createdAt;
        this.views = views;
        this.likeCount = likeCount;
        this.summary = summary;
        this.category = category;
        this.imageUrl = imageUrl;
    }
}

