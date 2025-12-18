package com.newssite.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/uploads")
public class UploadController {

    // Read directory from application.properties
    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping("/image")
    public Map<String, String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {

        // Use the configured path
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Sanitize filename to prevent issues on servers
        String originalName = file.getOriginalFilename();
        if (originalName == null) originalName = "image.jpg";

        String filename = UUID.randomUUID() + "_" + originalName.replaceAll("[^a-zA-Z0-9.-]", "_");

        Path filePath = uploadPath.resolve(filename);
        Files.write(filePath, file.getBytes());

        // Return the relative URL (Frontend will prepend server domain if needed)
        return Map.of("url", "/uploads/" + filename);
    }
}