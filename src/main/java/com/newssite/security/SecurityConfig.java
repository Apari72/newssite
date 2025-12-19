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

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/login",
                                "/register",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/uploads/**"
                        ).permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/articles/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/auth/me").permitAll()

                        .requestMatchers(
                                "/api/articles/*/like",
                                "/api/comments/**",
                                "/api/journalists/**"
                        ).authenticated()

                        .requestMatchers(HttpMethod.POST, "/api/uploads/**")
                        .hasAnyRole("ADMIN", "JOURNALIST")

                        .requestMatchers(HttpMethod.POST, "/api/articles")
                        .hasAnyRole("ADMIN", "JOURNALIST")

                        .requestMatchers(HttpMethod.PUT, "/api/articles/**")
                        .hasAnyRole("ADMIN", "JOURNALIST")

                        .requestMatchers(HttpMethod.DELETE, "/api/articles/**")
                        .hasAnyRole("ADMIN", "JOURNALIST")

                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("https://newssite-frontend.vercel.app/", true)
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("https://newssite-frontend.vercel.app/")
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

        // MODIFIED: Use setAllowedOriginPatterns to support Vercel's preview links
        config.setAllowedOriginPatterns(List.of(
                "http://localhost:5173",
                "https://newssite-frontend.vercel.app",
                "https://*-ahmed-akif-aparis-projects.vercel.app"
        ));

        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}