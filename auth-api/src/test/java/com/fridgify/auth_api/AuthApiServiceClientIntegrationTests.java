package com.fridgify.auth_api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fridgify.auth_api.client.UserServiceClient;
import com.fridgify.auth_api.config.SecurityConfig;
import com.fridgify.auth_api.dto.LoginUserDTO;
import com.fridgify.auth_api.dto.RegisterUserDTO;
import com.fridgify.shared.jwt.util.JwtUtil;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static com.fridgify.auth_api.mock.UserMocks.setupMockLoginResponse;
import static com.fridgify.auth_api.mock.UserMocks.setupMockRegisterResponse;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfig.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EnableConfigurationProperties
@ExtendWith(SpringExtension.class)
public class AuthApiServiceClientIntegrationTests {

    private final String BASE_URL = "/auth-api";

    private final JwtUtil jwtUtil = new JwtUtil();

    @Autowired
    private MockMvc mockMvc;

    private static WireMockServer mockUserService;

    @Autowired
    private UserServiceClient userClient;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void setUp() throws IOException {
        mockUserService = new WireMockServer(options().port(8082));
        mockUserService.start();

        setupMockLoginResponse(mockUserService);
        setupMockRegisterResponse(mockUserService);
    }

    @AfterAll
    public static void tearDownAfterClass() {
        if (mockUserService != null) {
            mockUserService.stop();
        }
    }


    @Test
    public void shouldReturnTokenOnLogin() throws Exception {
        LoginUserDTO user = new LoginUserDTO();
        user.setUsername("test");
        user.setEmail("test");
        user.setPassword("test");
        String userJson = objectMapper.writeValueAsString(user);

        String response = this.mockMvc.perform(post(BASE_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString(user.getEmail())))
                .andExpect(content().string(containsString(user.getUsername())))
                .andExpect(content().string(containsString("\"token\":\"ey")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(response);
        String token = jsonResponse.get("token").asText();
        String username = jsonResponse.get("username").asText();

        assertEquals(user.getUsername(), username);
        assertEquals(user.getUsername(), jwtUtil.extractUsername(token));
    }

    @Test
    public void shouldReturnUserCreatedOnRegister() throws Exception {
        RegisterUserDTO user = new RegisterUserDTO();
        user.setUsername("AAA");
        user.setEmail("BBB");
        user.setFirstName("CCC");
        user.setLastName("DDD");
        user.setPassword("EEE");
        String userJson = objectMapper.writeValueAsString(user);

        this.mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString(user.getEmail())))
                .andExpect(content().string(containsString(user.getUsername())))
                .andExpect(content().string(containsString(user.getFirstName())))
                .andExpect(content().string(containsString(user.getLastName())));
    }
}
