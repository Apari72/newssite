package com.newssite.repository;



import com.newssite.model.JournalistProfile;
import com.newssite.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface JournalistProfileRepository extends JpaRepository<JournalistProfile, Long> {
    boolean existsByUser(User user);
    Optional<JournalistProfile> findByUser(User user);
}

