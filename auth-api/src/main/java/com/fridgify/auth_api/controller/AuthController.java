package com.fridgify.auth_api.controller;

import com.fridgify.auth_api.client.UserServiceClient;
import com.fridgify.auth_api.dto.LoginUserDTO;
import com.fridgify.auth_api.dto.RegisterUserDTO;
import com.fridgify.auth_api.dto.ResponseUserDTO;
import com.fridgify.auth_api.dto.TokenDTO;
import com.fridgify.shared.jwt.util.JwtUtil;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/auth-api")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserServiceClient userServiceClient;

    public AuthController(JwtUtil jwtUtil, UserServiceClient userServiceClient1) {
        this.jwtUtil = jwtUtil;
        this.userServiceClient = userServiceClient1;
    }

    // Register endpoint
    @PostMapping("/register")
    public ResponseEntity<Optional<ResponseUserDTO>> register(@RequestBody RegisterUserDTO registerUserDTO) {
        try {
            return userServiceClient.registerUser(registerUserDTO);
        } catch (FeignException.FeignClientException.BadRequest e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (FeignException.FeignClientException.InternalServerError e) {
            // e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginUserDTO loginUserDTO) {
        try {
            ResponseEntity<Optional<ResponseUserDTO>> response = userServiceClient.loginUser(loginUserDTO);
            assert Objects.requireNonNull(response.getBody()).isPresent();
            // Generate JWT token
            String token = jwtUtil.generateToken(loginUserDTO.getUsername());
            TokenDTO tokenResponse = TokenDTO.builder()
                    .token(token)
                    .username(response.getBody().get().getUsername())
                    .email(response.getBody().get().getEmail())
                    .build();
            return ResponseEntity.ok(tokenResponse);
        } catch (FeignException.FeignClientException.BadRequest e) {
            TokenDTO tokenResponse = TokenDTO.builder()
                    .error("bad request")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(tokenResponse);
        } catch (FeignException.FeignClientException.Unauthorized e) {
            TokenDTO tokenResponse = TokenDTO.builder()
                    .error("username, email or password is wrong")
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(tokenResponse);
        }
    }
}
