package com.fridgify.user_api.controller;

import com.fridgify.user_api.entity.DummyUser;
import com.fridgify.user_api.service.DummyUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dummy-user-api")
public class DummyUserController {

    private final DummyUserService dummyUserService;

    public DummyUserController(DummyUserService dummyUserService) {
        this.dummyUserService = dummyUserService;
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody DummyUser user) {
        return dummyUserService.registerUser(user.getUsername(), user.getPassword());
    }

    @GetMapping("/find/{username}")
    public ResponseEntity<DummyUser> getUserByUsername(@PathVariable String username) {
        DummyUser user = dummyUserService.findByUsername(username);
        if (user == null) {
            // Return 404 if user is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("User API is working");
    }
}

