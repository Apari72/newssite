package com.newssite.service;

import com.newssite.model.Role;
import com.newssite.model.User;
import com.newssite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ----------------------
    //     REGISTER USER
    // ----------------------
    public User registerUser(String name, String email, String rawPassword) {

        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setRole(Role.USER); // default role

        return userRepository.save(user);
    }

    // ----------------------
    //        LOGIN
    // ----------------------
    public User login(String email, String rawPassword) {

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return null;
        }

        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            return null;
        }

        return user;
    }
}
