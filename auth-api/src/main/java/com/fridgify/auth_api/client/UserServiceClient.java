package com.fridgify.auth_api.client;

import com.fridgify.auth_api.common.UserDTO;
import com.fridgify.auth_api.dto.RegisterRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-api", url = "http://localhost:8082")
public interface UserServiceClient {

    @PostMapping("/user-api/register")
    String registerUser(@RequestBody RegisterRequest registerRequest);

    @PostMapping("/user-api/login")
    String loginUser(@RequestBody UserDTO registerRequest);

    @GetMapping("/user-api/find/{username}")
    UserDTO getUserByUsername(@PathVariable("username") String username);

}