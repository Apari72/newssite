package com.newssite.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // Use allowedOriginPatterns to support Vercel wildcards
                .allowedOriginPatterns(
                        "http://localhost:5173",                 // Local React
                        "https://newssite-frontend.vercel.app",  // Main Vercel Site
                        "https://*-ahmed-akif-aparis-projects.vercel.app" // Vercel Previews
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}