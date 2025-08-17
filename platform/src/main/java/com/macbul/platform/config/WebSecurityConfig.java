package com.macbul.platform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Use BCrypt for password hashing when you implement user registration/login
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // We’re building a stateless REST service, so disable CSRF
            .csrf().disable()

            // Set session creation policy to stateless (no HTTP sessions)
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()

            // Define which endpoints should be permitted without authentication
            .authorizeHttpRequests(authorize -> authorize
                // Allow unauthenticated access to Swagger UI and API docs:
                .requestMatchers(
                    "/v3/api-docs/**",            // OpenAPI JSON
                    "/swagger-ui.html",           // Swagger UI HTML
                    "/swagger-ui/**",             // Swagger UI assets (CSS, JS, etc)
                    "/v1/**"          // optionally: allow user registration without login
                ).permitAll()
                
                // If you also have actuator or health check endpoints, permit them:
                // .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                
                // All other requests must be authenticated
                .anyRequest().authenticated()
            )
            
            // For now, use HTTP Basic (JWT or form‐login can be added later)
            .httpBasic();

        return http.build();
    }
}
