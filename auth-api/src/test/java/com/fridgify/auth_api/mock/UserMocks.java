package com.fridgify.auth_api.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fridgify.auth_api.dto.RegisterUserDTO;
import com.fridgify.auth_api.dto.ResponseUserDTO;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.http.HttpStatus;

import java.io.IOException;


public class UserMocks {
    public static void setupMockLoginResponse(WireMockServer mockService) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseUserDTO responseUser = new ResponseUserDTO(
                "test", "test", "test", "test");
        String jsonResponse = objectMapper.writeValueAsString(responseUser);

        mockService.stubFor(WireMock.post(WireMock.urlEqualTo("/user-api/login"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonResponse)
                ));
    }

    public static void setupMockRegisterResponse(WireMockServer mockService) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        RegisterUserDTO registerUser = new RegisterUserDTO();
        registerUser.setUsername("AAA");
        registerUser.setEmail("BBB");
        registerUser.setFirstName("CCC");
        registerUser.setLastName("DDD");
        registerUser.setPassword("EEE");
        String jsonResponse = objectMapper.writeValueAsString(registerUser);

        mockService.stubFor(WireMock.post(WireMock.urlEqualTo("/user-api/register"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.CREATED.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonResponse)
                ));
    }
}
