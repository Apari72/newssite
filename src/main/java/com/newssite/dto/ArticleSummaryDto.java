package com.newssite.dto;

import java.time.LocalDateTime;

public class ArticleSummaryDto {

    public Long id;
    public String title;
    public String authorName;
    public LocalDateTime createdAt;
    public int views;
    public int likeCount;

    public ArticleSummaryDto(
            Long id,
            String title,
            String authorName,
            LocalDateTime createdAt,
            int views,
            int likeCount
    ) {
        this.id = id;
        this.title = title;
        this.authorName = authorName;
        this.createdAt = createdAt;
        this.views = views;
        this.likeCount = likeCount;
    }
}
