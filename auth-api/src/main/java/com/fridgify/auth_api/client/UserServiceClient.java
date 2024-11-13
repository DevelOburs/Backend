package com.fridgify.auth_api.client;

import com.fridgify.auth_api.dto.LoginUserDTO;
import com.fridgify.auth_api.dto.RegisterUserDTO;
import com.fridgify.auth_api.dto.ResponseUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@FeignClient(name = "user-api")
public interface UserServiceClient {

    @PostMapping("/user-api/register")
    ResponseEntity<Optional<ResponseUserDTO>> registerUser(@RequestBody RegisterUserDTO registerUserDTO);

    @PostMapping("/user-api/login")
    ResponseEntity<Optional<ResponseUserDTO>> loginUser(@RequestBody LoginUserDTO loginUserDTO);

    @GetMapping("/user-api/find/{username}")
    ResponseEntity<ResponseUserDTO> getUserByUsername(@PathVariable("username") String username);

}