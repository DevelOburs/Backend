package com.fridgify.auth_api.controller;

import com.fridgify.auth_api.client.UserServiceClient;
import com.fridgify.auth_api.common.UserDTO;
import com.fridgify.auth_api.dto.LoginRequest;
import com.fridgify.auth_api.dto.RegisterRequest;
import com.fridgify.shared.jwt.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth-api")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserServiceClient userServiceClient;

    public AuthController(JwtUtil jwtUtil, BCryptPasswordEncoder passwordEncoder, UserServiceClient userServiceClient1) {
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userServiceClient = userServiceClient1;
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
        UserDTO user = userServiceClient.getUserByUsername(loginRequest.getUser().getUsername());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");
        }


        if(userServiceClient.loginUser(user).equals("Login successful")){
            // Generate JWT token
            String token = jwtUtil.generateToken(user.getUsername());
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed");
        }

    }

}

