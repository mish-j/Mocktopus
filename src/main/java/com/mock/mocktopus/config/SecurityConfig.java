package com.mock.mocktopus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Disable CSRF for simplicity (adjust for production!)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**", "/", "/h2-console/**").permitAll()  // Allow public access here
                .anyRequest().authenticated()  // All other requests require authentication
            )
            .headers(headers -> headers.frameOptions().sameOrigin()); // Allow H2 console frames
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
