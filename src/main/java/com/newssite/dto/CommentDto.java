package com.newssite.dto;

import java.time.LocalDateTime;

public class CommentDto {

    public Long id;
    public String content;
    public String authorName;
    public LocalDateTime createdAt;

    public boolean canEdit;
    public boolean canDelete;

    public int likeCount;
    public boolean likedByCurrentUser;

    public CommentDto(
            Long id,
            String content,
            String authorName,
            LocalDateTime createdAt,
            boolean canEdit,
            boolean canDelete,
            int likeCount,
            boolean likedByCurrentUser
    ) {
        this.id = id;
        this.content = content;
        this.authorName = authorName;
        this.createdAt = createdAt;
        this.canEdit = canEdit;
        this.canDelete = canDelete;
        this.likeCount = likeCount;
        this.likedByCurrentUser = likedByCurrentUser;
    }
}


