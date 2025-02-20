package com.fridgify.auth_api.client;

import com.fridgify.auth_api.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@FeignClient(name = "user-api")
public interface UserServiceClient {

    @PostMapping("/user-api/register")
    ResponseEntity<Optional<ResponseUserDTO>> registerUser(@RequestBody RegisterUserDTO registerUserDTO);

    @PostMapping("/user-api/login")
    ResponseEntity<Optional<ResponseUserDTO>> loginUser(@RequestBody LoginUserDTO loginUserDTO);
}