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

    @Value("${file.upload-dir}")
    private String uploadDir;

    // Inject the Backend URL, default to your Render address
    @Value("${app.backend.url:https://newssite-i504.onrender.com}")
    private String backendUrl;

    @PostMapping("/image")
    public Map<String, String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {

        // 1. Create directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 2. Generate unique filename
        String originalName = file.getOriginalFilename();
        if (originalName == null) originalName = "image.jpg";
        // Clean the filename
        String cleanName = originalName.replaceAll("[^a-zA-Z0-9.-]", "_");
        String filename = UUID.randomUUID() + "_" + cleanName;

        // 3. Save the file
        Path filePath = uploadPath.resolve(filename);
        Files.write(filePath, file.getBytes());

        // 4. Return the FULL URL so Vercel knows where to find it
        // Result: "https://newssite-i504.onrender.com/uploads/filename.jpg"
        String fullUrl = backendUrl + "/uploads/" + filename;

        return Map.of("url", fullUrl);
    }
}