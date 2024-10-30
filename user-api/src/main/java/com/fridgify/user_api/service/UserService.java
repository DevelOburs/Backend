package com.fridgify.user_api.service;

import com.fridgify.user_api.dto.UserDTO;
import com.fridgify.user_api.model.User;
import com.fridgify.user_api.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElse(null);
    }

    public String registerUser(UserDTO userDTO){
        if(userRepository.findByUsername(userDTO.getUsername()).isPresent()){
            return "User already exists";
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        userRepository.save(user);
        return "User registered successfully";
    }

    public String loginUser(UserDTO userDTO){
        if(userRepository.findByUsername(userDTO.getUsername()).isEmpty()){
            return "User does not exist";
        }

        User user = userRepository.findByUsername(userDTO.getUsername()).get();
        if(passwordEncoder.matches(userDTO.getPassword(), user.getPassword())){
            return "Login successful";
        } else {
            return "Login failed";
        }
    }


}

