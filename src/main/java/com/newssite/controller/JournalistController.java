package com.newssite.controller;

import com.newssite.model.User;
import com.newssite.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/journalist")
public class JournalistController {

    private final UserRepository userRepository;

    public JournalistController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        User user = userRepository
                .findByEmail(principal.getName())
                .orElseThrow();

        model.addAttribute("user", user);
        return "journalist-dashboard";
    }
}
