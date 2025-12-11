package com.newssite.controller;

        import com.newssite.model.User;
        import com.newssite.repository.UserRepository;
        import com.newssite.service.AdminService;
        import org.springframework.stereotype.Controller;
        import org.springframework.ui.Model;
        import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final AdminService adminService;

    public AdminController(UserRepository userRepository, AdminService adminService) {
        this.userRepository = userRepository;
        this.adminService = adminService;
    }

    // Show admin dashboard
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin-dashboard";
    }

    // Promote user from Thymeleaf
    @PostMapping("/promote")
    public String promote(@RequestParam Long userId) {
        adminService.promoteToJournalist(userId);
        return "redirect:/admin/dashboard";
    }
}
