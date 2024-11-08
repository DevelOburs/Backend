package com.fridgify.user_api.controller;
import com.fridgify.user_api.dto.LoginUserDTO;
import com.fridgify.user_api.dto.RegisterUserDTO;
import com.fridgify.user_api.dto.ResponseUserDTO;
import com.fridgify.user_api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@RestController
@RequestMapping("/user-api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Optional<ResponseUserDTO>> registerUser(@RequestBody RegisterUserDTO registerUserDTO) {
        Optional<ResponseUserDTO> response = userService.registerUser(registerUserDTO);
        if (response.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Optional<ResponseUserDTO>> loginUser(@RequestBody LoginUserDTO loginUserDTO) {
        Optional<ResponseUserDTO> response = userService.loginUser(loginUserDTO);
        if (response.isPresent()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @GetMapping("/find/{username}")
    public ResponseEntity<Optional<ResponseUserDTO>> getUserByUsername(@PathVariable String username) {
        Optional<ResponseUserDTO> response = userService.findByUsername(username);
        if (response.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("User API is working");
    }
}
