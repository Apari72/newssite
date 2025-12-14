package com.newssite.controller;

import com.newssite.dto.JournalistProfileDto;
import com.newssite.service.JournalistProfileService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/journalists")
public class JournalistProfileController {

    private final JournalistProfileService service;

    public JournalistProfileController(JournalistProfileService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public JournalistProfileDto getProfile(@PathVariable Long id) {
        return service.getProfile(id);
    }
}
