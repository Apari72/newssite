package com.newssite.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String authorName;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt = LocalDateTime.now();

    // Many comments → 1 article
    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    public Long getId() { return id; }
    public String getAuthorName() { return authorName; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Article getArticle() { return article; }

    public void setId(Long id) { this.id = id; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
    public void setContent(String content) { this.content = content; }
    public void setArticle(Article article) { this.article = article; }
}
