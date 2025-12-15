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

    @PostMapping("/image")
    public Map<String, String> uploadImage(
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        String original = file.getOriginalFilename()
                .replaceAll("[^a-zA-Z0-9.]", "_");

        String filename = UUID.randomUUID() + "_" + original;
        Path path = Paths.get("uploads", filename);

        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());

        return Map.of("url", "/uploads/" + filename);
    }

}
