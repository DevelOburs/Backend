package com.fridgify.user_api.service;

import com.fridgify.user_api.entity.DummyUser;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class DummyUserService {

    private final Map<String, DummyUser> users = new HashMap<>();

    private final PasswordEncoder passwordEncoder;

    public DummyUserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String registerUser(String username, String password) {
        if (users.containsKey(username)) {
            return "User already exists";
        }
        // Hash the password before saving
        String hashedPassword = passwordEncoder.encode(password);
        users.put(username, new DummyUser(username, hashedPassword));
        return "User registered successfully";
    }

    public DummyUser findByUsername(String username) {
        return users.get(username);
    }
}

