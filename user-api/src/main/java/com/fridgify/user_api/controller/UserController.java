package com.fridgify.user_api.controller;
import com.fridgify.user_api.dto.*;
import com.fridgify.user_api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
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

    @PutMapping("/changePassword")
    public ResponseEntity<Optional<ResponseUserDTO>> changePassword(@RequestBody ChangePasswordUserDTO ChangePasswordUserDTO) {
        Optional<ResponseUserDTO> response = userService.changePassword(ChangePasswordUserDTO);
        if (response.isPresent()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/updateUser")
    public ResponseEntity<Optional<ResponseUserDTO>> updateUser(@RequestParam("username") String username, @RequestBody UpdateUserDTO UpdateUserDTO) {

        Optional<ResponseUserDTO> response = userService.updateUser(username, UpdateUserDTO);
        if (response.isPresent()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

    }

    @PreAuthorize("hasRole('ADMIN') or @userService.isUserTryingToDeleteOwnProfile(#id, authentication.name)")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        String response = userService.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<List<ResponseUserDTO>> listUsers() {
        List<ResponseUserDTO> response = userService.listUsers();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
