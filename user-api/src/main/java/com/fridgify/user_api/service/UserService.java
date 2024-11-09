package com.fridgify.user_api.service;

import com.fridgify.user_api.dto.LoginUserDTO;
import com.fridgify.user_api.dto.RegisterUserDTO;
import com.fridgify.user_api.dto.ResponseUserDTO;
import com.fridgify.user_api.model.User;
import com.fridgify.user_api.repository.UserRepository;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<ResponseUserDTO> findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(value ->
            ResponseUserDTO
                .builder()
                .email(value.getEmail())
                .username(value.getUsername())
                .firstName(value.getFirstName())
                .lastName(value.getLastName())
                .build()
        );
    }

    public Optional<ResponseUserDTO> registerUser(
        RegisterUserDTO registerUserDTO
    ) {
        if (
            userRepository
                .findByUsername(registerUserDTO.getUsername())
                .isPresent() ||
            userRepository
                .findByEmailIgnoreCase(registerUserDTO.getEmail())
                .isPresent()
        ) {
            return Optional.empty();
        }

        User user = new User();
        user.setUsername(registerUserDTO.getUsername());
        user.setEmail(registerUserDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerUserDTO.getPassword()));
        user.setFirstName(registerUserDTO.getFirstName());
        user.setLastName(registerUserDTO.getLastName());
        userRepository.save(user);

        return Optional.ofNullable(
            ResponseUserDTO
                .builder()
                .email(registerUserDTO.getEmail())
                .username(registerUserDTO.getUsername())
                .firstName(registerUserDTO.getFirstName())
                .lastName(registerUserDTO.getLastName())
                .build()
        );
    }

    public Optional<ResponseUserDTO> loginUser(LoginUserDTO loginUserDTO) {
        Optional<User> user_with_username = userRepository.findByUsername(
            loginUserDTO.getUsername()
        );
        Optional<User> user_with_email = userRepository.findByEmailIgnoreCase(
            loginUserDTO.getEmail()
        );
        if (user_with_username.isEmpty() && user_with_email.isEmpty()) {
            return Optional.empty();
        }

        Optional<User> existing_user = user_with_email.isPresent()
            ? user_with_email
            : user_with_username;
        if (
            passwordEncoder.matches(
                loginUserDTO.getPassword(),
                existing_user.get().getPassword()
            )
        ) {
            System.console();
            return Optional.ofNullable(
                ResponseUserDTO
                    .builder()
                    .email(existing_user.get().getEmail())
                    .username(existing_user.get().getUsername())
                    .firstName(existing_user.get().getFirstName())
                    .lastName(existing_user.get().getLastName())
                    .build()
            );
        } else {
            return Optional.empty();
        }
    }
}
