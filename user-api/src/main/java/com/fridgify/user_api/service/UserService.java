package com.fridgify.user_api.service;

import com.fridgify.user_api.dto.*;
import com.fridgify.user_api.model.Role;
import com.fridgify.user_api.model.User;
import com.fridgify.user_api.repository.RoleRepository;
import com.fridgify.user_api.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<ResponseUserDTO> findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(value -> ResponseUserDTO.builder()
                .email(value.getEmail())
                .username(value.getUsername())
                .firstName(value.getFirstName())
                .lastName(value.getLastName())
                .build());
    }

    public Optional<ResponseUserDTO> registerUser(RegisterUserDTO registerUserDTO){
        if(userRepository.findByUsername(registerUserDTO.getUsername()).isPresent() ||
                userRepository.findByEmailIgnoreCase(registerUserDTO.getEmail()).isPresent()){
            return Optional.empty();
        }

        User user = new User();
        user.setUsername(registerUserDTO.getUsername());
        user.setEmail(registerUserDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerUserDTO.getPassword()));
        user.setFirstName(registerUserDTO.getFirstName());
        user.setLastName(registerUserDTO.getLastName());
        List<Role> roles = new ArrayList<>();
        Role userRole = roleRepository.findByName("USER").orElse(new Role("USER"));
        roles.add(userRole);
        user.setRoles(roles);
        userRepository.save(user);

        return Optional.ofNullable(ResponseUserDTO.builder()
                        .userId(user.getId())
                        .email(registerUserDTO.getEmail())
                        .username(registerUserDTO.getUsername())
                        .firstName(registerUserDTO.getFirstName())
                        .roles(List.of("USER"))
                        .build());
    }

    public Optional<ResponseUserDTO> loginUser(LoginUserDTO loginUserDTO){
        Optional<User> user_with_username = userRepository.findByUsername(loginUserDTO.getUsername());
        Optional<User> user_with_email = userRepository.findByEmailIgnoreCase(loginUserDTO.getEmail());
        if(user_with_username.isEmpty() && user_with_email.isEmpty()){
            return Optional.empty();
        }

        Optional<User> existing_user = user_with_email.isPresent() ? user_with_email : user_with_username;
        if(passwordEncoder.matches(loginUserDTO.getPassword(), existing_user.get().getPassword())){
            return Optional.ofNullable(ResponseUserDTO.builder()
                    .userId(existing_user.get().getId())
                    .email(existing_user.get().getEmail())
                    .username(existing_user.get().getUsername())
                    .firstName(existing_user.get().getFirstName())
                    .lastName(existing_user.get().getLastName())
                    .roles(existing_user.get().getRoles().stream().map(Role::getName).toList())
                    .build());
        } else {
            return Optional.empty();
        }
    }

    public Optional<ResponseUserDTO> changePassword(ChangePasswordUserDTO ChangePasswordUserDTO){
        Optional<User> user = userRepository.findByUsername(ChangePasswordUserDTO.getUsername());
        if(user.isEmpty()){
            return Optional.empty();
        }

        User existing_user = user.get();

        if(!passwordEncoder.matches(ChangePasswordUserDTO.getPassword(), existing_user.getPassword())){
            return Optional.empty();
        }

        user.get().setPassword(passwordEncoder.encode(ChangePasswordUserDTO.getNewPassword()));
        userRepository.save(existing_user);

        return Optional.ofNullable(ResponseUserDTO.builder()
                .email(existing_user.getEmail())
                .username(existing_user.getUsername())
                .firstName(existing_user.getFirstName())
                .lastName(existing_user.getLastName())
                .build());
    }

    public Optional<ResponseUserDTO> updateUser(String username, UpdateUserDTO UpdateUserDTO) {

        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }

        if(UpdateUserDTO.getEmail() != null){
            user.get().setEmail(UpdateUserDTO.getEmail());
        }
        if(UpdateUserDTO.getFirstName() != null){
            user.get().setFirstName(UpdateUserDTO.getFirstName());
        }
        if(UpdateUserDTO.getLastName() != null){
            user.get().setLastName(UpdateUserDTO.getLastName());
        }
        userRepository.save(user.get());

        return Optional.ofNullable(ResponseUserDTO.builder()
                .email(user.get().getEmail())
                .username(user.get().getUsername())
                .firstName(user.get().getFirstName())
                .lastName(user.get().getLastName())
                .build());
    }

    public String deleteUserById(Long id) {

        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()){
            throw new NotFoundException("User not found");
        }

        userRepository.deleteById(id);

        return "user deleted successfully with id: " + id;
    }

    public boolean isUserTryingToDeleteOwnProfile(Long userId, String username) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        return user.getUsername().equals(username);
    }
}
