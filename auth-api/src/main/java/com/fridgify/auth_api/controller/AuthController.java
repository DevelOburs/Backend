package com.fridgify.auth_api.controller;

import com.fridgify.auth_api.dto.LoginRequest;
import com.fridgify.auth_api.dto.RegisterRequest;
import com.fridgify.auth_api.dto.UserResponse;
import com.fridgify.shared.jwt.util.JwtUtil;
import com.fridgify.auth_api.client.DummyUserServiceClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth-api")
public class AuthController {

    private final JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder;

    private final DummyUserServiceClient userServiceClient;  // Feign client to call user service

    public AuthController(JwtUtil jwtUtil, BCryptPasswordEncoder passwordEncoder, DummyUserServiceClient userServiceClient) {
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userServiceClient = userServiceClient;
    }

    // Register endpoint
    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest registerRequest) {
        // Forward registration request to user-service
        return userServiceClient.registerUser(registerRequest);
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        // Retrieve user from user-service
        UserResponse user = userServiceClient.getUserByUsername(loginRequest.getUsername());

        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid credentials");
        }

        // Generate JWT token
        return ResponseEntity.status(HttpStatus.OK).body(jwtUtil.generateToken(user.getUsername()));
    }

}

