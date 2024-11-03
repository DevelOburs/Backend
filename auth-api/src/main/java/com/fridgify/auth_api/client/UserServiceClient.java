package com.fridgify.auth_api.client;

import com.fridgify.auth_api.common.UserDTO;
import com.fridgify.auth_api.dto.LoginRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-api", url = "http://localhost:8082")
public interface UserServiceClient {

    @PostMapping("/user-api/register")
    ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO);

    @PostMapping("/user-api/login")
    ResponseEntity<String> loginUser(@RequestBody LoginRequest loginRequestDTO);

    @GetMapping("/user-api/find/{username}")
    ResponseEntity<UserDTO> getUserByUsername(@PathVariable("username") String username);

}