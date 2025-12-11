package com.newssite.service;

import com.newssite.model.JournalistProfile;
import com.newssite.model.Role;
import com.newssite.model.User;
import com.newssite.repository.JournalistProfileRepository;
import com.newssite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final JournalistProfileRepository journalistProfileRepository;

    @Autowired
    public AdminService(UserRepository userRepository,
                        JournalistProfileRepository journalistProfileRepository) {
        this.userRepository = userRepository;
        this.journalistProfileRepository = journalistProfileRepository;
    }

    public JournalistProfile promoteToJournalist(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));


        if (user.getRole() == Role.ADMIN) {
            throw new RuntimeException("ADMIN users cannot be promoted to JOURNALIST");
        }


        if (user.getRole() == Role.JOURNALIST) {
            return journalistProfileRepository.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("Journalist profile missing"));
        }


        user.setRole(Role.JOURNALIST);
        userRepository.save(user);

        JournalistProfile profile = new JournalistProfile();
        profile.setUser(user);
        profile.setBio("New Journalist");
        profile.setRatingScore(0.0);
        profile.setTotalViews(0);

        return journalistProfileRepository.save(profile);
    }
}
