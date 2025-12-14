package com.newssite.model;

import jakarta.persistence.*;

@Entity
@Table(
        name = "comment_likes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"comment_id", "user_id"})
)
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

