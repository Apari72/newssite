package com.newssite.model;

import jakarta.persistence.*;

@Entity
@Table(name = "journalist_profiles")
public class JournalistProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String bio;

    private Double ratingScore = 0.0;

    private Integer totalViews = 0;

    // ---------- Getters & Setters ----------

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public String getBio() { return bio; }

    public void setBio(String bio) { this.bio = bio; }

    public Double getRatingScore() { return ratingScore; }

    public void setRatingScore(Double ratingScore) { this.ratingScore = ratingScore; }

    public Integer getTotalViews() { return totalViews; }

    public void setTotalViews(Integer totalViews) { this.totalViews = totalViews; }
}
