package com.fridgify.auth_api.client;

import com.fridgify.auth_api.dto.LoginUserDTO;
import com.fridgify.auth_api.dto.RegisterUserDTO;
import com.fridgify.auth_api.dto.ResponseUserDTO;
import java.util.Optional;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-api", url = "http://localhost:8082")
public interface UserServiceClient {
    @PostMapping("/user-api/register")
    ResponseEntity<Optional<ResponseUserDTO>> registerUser(
        @RequestBody RegisterUserDTO registerUserDTO
    );

    @PostMapping("/user-api/login")
    ResponseEntity<Optional<ResponseUserDTO>> loginUser(
        @RequestBody LoginUserDTO loginUserDTO
    );

    @GetMapping("/user-api/find/{username}")
    ResponseEntity<ResponseUserDTO> getUserByUsername(
        @PathVariable("username") String username
    );
}
