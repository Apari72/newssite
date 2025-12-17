package com.newssite.controller;

import com.newssite.dto.JournalistProfileDto;
import com.newssite.dto.JournalistProfileStatsDto;
import com.newssite.service.JournalistProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/journalists")
public class JournalistProfileController {

    private final JournalistProfileService service;

    public JournalistProfileController(JournalistProfileService service) {
        this.service = service;
    }


    @GetMapping("/{id}")
    public JournalistProfileStatsDto getProfile(
            @PathVariable Long id,
            Authentication auth
    ) {
        return service.getProfile(
                id,
                auth != null ? auth.getName() : null
        );
    }
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBio(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            Authentication auth
    ) {
        service.updateBio(id, body.get("bio"), auth.getName());
    }


}
