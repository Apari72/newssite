package com.newssite.controller;

import com.newssite.model.User;
import com.newssite.repository.UserRepository;
import com.newssite.service.AdminService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final AdminService adminService;

    public AdminController(UserRepository userRepository, AdminService adminService) {
        this.userRepository = userRepository;
        this.adminService = adminService;
    }

    // ✅ LIST ALL USERS (ADMIN ONLY)
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ✅ PROMOTE USER → JOURNALIST
    @PostMapping("/promote/{id}")
    public User promote(@PathVariable Long id) {
        return adminService.promoteToJournalist(id).getUser();
    }

}
