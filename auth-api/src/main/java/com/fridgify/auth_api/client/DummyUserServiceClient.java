package com.fridgify.auth_api.client;

import com.fridgify.auth_api.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service", url = "http://localhost:8082")
public interface DummyUserServiceClient {

    @PostMapping("/dummy-user-api/register")
    String registerUser(@RequestBody RegisterRequest registerRequest);

    @GetMapping("/dummy-user-api/find/{username}")
    UserResponse getUserByUsername(@PathVariable("username") String username);
}
