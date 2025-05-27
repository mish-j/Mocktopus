package com.mock.mocktopus.controller;

import com.mock.mocktopus.model.User;
import com.mock.mocktopus.payload.LoginRequest;
import com.mock.mocktopus.payload.SignupRequest;
import com.mock.mocktopus.repository.UserRepository;
import com.mock.mocktopus.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.annotation.Configuration;

@Configuration
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already taken!");
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already in use!");
        }

        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setRole(signupRequest.getRole());

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest().body("Error: User not found.");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Error: Invalid password.");
        }

        String jwt = jwtUtils.generateJwtToken(user.getUsername());

        return ResponseEntity.ok("Bearer " + jwt);
    }
}
