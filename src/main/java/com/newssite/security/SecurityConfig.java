package com.newssite.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        // ---------- PUBLIC PAGES ----------
                        .requestMatchers(
                                "/",
                                "/login",
                                "/register",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/uploads/**"
                        ).permitAll()

                        // ---------- PUBLIC API (READ ONLY) ----------
                        .requestMatchers(HttpMethod.GET, "/api/articles/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/auth/me").permitAll() // Allow checking session

                        // ---------- AUTHENTICATED USER ACTIONS ----------
                        .requestMatchers(
                                "/api/articles/*/like",
                                "/api/comments/**",
                                "/api/journalists/**"
                        ).authenticated()

                        // ---------- IMAGE UPLOAD ----------
                        .requestMatchers(HttpMethod.POST, "/api/uploads/**")
                        .hasAnyRole("ADMIN", "JOURNALIST")

                        // ---------- ARTICLE WRITE / EDIT / DELETE ----------
                        .requestMatchers(HttpMethod.POST, "/api/articles")
                        .hasAnyRole("ADMIN", "JOURNALIST")

                        .requestMatchers(HttpMethod.PUT, "/api/articles/**")
                        .hasAnyRole("ADMIN", "JOURNALIST")

                        .requestMatchers(HttpMethod.DELETE, "/api/articles/**")
                        .hasAnyRole("ADMIN", "JOURNALIST")

                        // ---------- ADMIN ----------
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // ---------- FALLBACK ----------
                        .anyRequest().authenticated()
                )

                // ---------- LOGIN ----------
                .formLogin(form -> form
                        .loginPage("/login")
                        // IMPORTANT: On success, redirect to the Vercel frontend
                        // Note: locally this might redirect you to vercel too, which is fine
                        .defaultSuccessUrl("https://newssite-frontend-zeta.vercel.app/", true)
                        .permitAll()
                )

                // ---------- LOGOUT ----------
                .logout(logout -> logout
                        .logoutSuccessUrl("https://newssite-frontend-zeta.vercel.app/")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);

        // CRITICAL: Allow BOTH Localhost AND Vercel
        config.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "https://newssite-frontend-zeta.vercel.app"
        ));

        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}