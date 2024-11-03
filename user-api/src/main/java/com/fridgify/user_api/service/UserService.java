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

    public UserDTO findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()) {
            return UserDTO.builder()
                    .username(user.get().getUsername())
                    .password(passwordEncoder.encode(user.get().getPassword()))
                    .build();
        }
        return null;
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
        Optional<User> user_with_username = userRepository.findByUsername(userDTO.getUsername());
        Optional<User> user_with_email = userRepository.findByEmailIgnoreCase(userDTO.getEmail());
        if(user_with_username.isEmpty() && user_with_email.isEmpty()){
            return "User does not exist";
        }

        Optional<User> existing_user = user_with_email.isPresent() ? user_with_email : user_with_username;
        if(passwordEncoder.matches(userDTO.getPassword(), existing_user.get().getPassword())){
            return "Login successful";
        } else {
            return "Login failed";
        }
    }


}

