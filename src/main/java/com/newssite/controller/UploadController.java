package com.newssite.controller;

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

    private static final Path UPLOAD_DIR = Paths.get("uploads");

    @PostMapping("/image")
    public Map<String, String> uploadImage(
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        Files.createDirectories(UPLOAD_DIR);

        String filename = UUID.randomUUID() + "_" +
                file.getOriginalFilename().replaceAll("\\s+", "_");

        Path filePath = UPLOAD_DIR.resolve(filename);
        Files.write(filePath, file.getBytes());

        return Map.of(
                "url", "/uploads/" + filename
        );
    }
}



