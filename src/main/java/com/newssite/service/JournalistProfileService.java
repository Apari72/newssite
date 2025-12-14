package com.newssite.service;

import com.newssite.dto.ArticleSummaryDto;
import com.newssite.dto.JournalistProfileDto;
import com.newssite.model.JournalistProfile;
import com.newssite.model.User;
import com.newssite.repository.ArticleRepository;
import com.newssite.repository.JournalistProfileRepository;
import com.newssite.repository.UserRepository;
import org.springframework.stereotype.Service;

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

    public JournalistProfileDto getProfile(Long userId) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        JournalistProfile profile = profileRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        List<ArticleSummaryDto> articles =
                articleRepo.findByAuthorId(user.getId())
                        .stream()
                        .map(a -> new ArticleSummaryDto(
                                a.getId(),
                                a.getTitle(),
                                a.getViews(),
                                a.getCreatedAt()
                        ))
                        .toList();

        return new JournalistProfileDto(
                user.getId(),
                user.getName(),
                profile.getBio(),
                profile.getRatingScore(),
                profile.getTotalViews(),
                articles
        );
    }
}
