package com.newssite.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // public pages
                        .requestMatchers(
                                "/",
                                "/login",
                                "/register",
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll()

                        // public API (READ)
                        .requestMatchers(HttpMethod.GET, "/api/articles/**").permitAll()

                        // authenticated API (WRITE)
                        .requestMatchers(
                                "/api/articles/*/like",
                                "/api/comments/**"
                        ).authenticated()

                        // role-based
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/journalist/**").hasAnyRole("JOURNALIST", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/journalists/**").permitAll()

                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("http://localhost:5173/", true)
                        .permitAll()
                )

                .logout(logout -> logout.permitAll());

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
        config.addAllowedOrigin("http://localhost:5173");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
