package com.newssite.dto;

import java.time.LocalDateTime;

public class ArticleSummaryDto {

    public Long id;
    public String title;
    public int views;
    public LocalDateTime createdAt;

    public ArticleSummaryDto(Long id, String title, int views, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.views = views;
        this.createdAt = createdAt;
    }
}
