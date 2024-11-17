package com.fridgify.user_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fridgify.user_api.config.SecurityConfig;
import com.fridgify.user_api.dto.LoginUserDTO;
import com.fridgify.user_api.dto.RegisterUserDTO;
import com.fridgify.user_api.dto.ResponseUserDTO;
import com.fridgify.user_api.model.User;
import com.fridgify.user_api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfig.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserApplicationDbIntegrationTests {

    private final String BASE_URL = "/user-api";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldReturnServerErrorForRegister() throws Exception {
        this.mockMvc.perform(get(BASE_URL + "/register"))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldReturnCreatedForRegister() throws Exception {
        RegisterUserDTO user = new RegisterUserDTO();
        user.setUsername("test");
        user.setEmail("test");
        user.setPassword("test");
        user.setFirstName("test");
        user.setLastName("test");
        String userJson = objectMapper.writeValueAsString(user);

        Optional<ResponseUserDTO> responseUser = Optional.ofNullable(ResponseUserDTO.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build());
        String responseUserJson = objectMapper.writeValueAsString(responseUser);

        this.mockMvc.perform(post(BASE_URL + "/register")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(responseUserJson)));

        Optional<User> userObj = userRepository.findByUsername("test");
        assert  userObj.isPresent() &&
                userObj.get().getFirstName().equals(user.getFirstName()) &&
                userObj.get().getLastName().equals(user.getLastName());
    }

    @Test
    void shouldReturnBadRequestForSameEmailForRegister() throws Exception {
        RegisterUserDTO user = new RegisterUserDTO();
        user.setUsername("berat");
        user.setEmail("test1");
        user.setPassword("test");
        user.setFirstName("test");
        user.setLastName("test");
        String userJson = objectMapper.writeValueAsString(user);

        User userObj = User.builder()
                .username("test1")
                .password("test")
                .email("test1")
                .firstName("test")
                .lastName("test")
                .build();

        userRepository.save(userObj);

        this.mockMvc.perform(post(BASE_URL + "/register")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestForSameUsernameForRegister() throws Exception {
        RegisterUserDTO user = new RegisterUserDTO();
        user.setUsername("test2");
        user.setEmail("www");
        user.setPassword("test");
        user.setFirstName("test");
        user.setLastName("test");
        String userJson = objectMapper.writeValueAsString(user);

        User userObj = User.builder()
                .username("test2")
                .password("test")
                .email("test2")
                .firstName("test")
                .lastName("test")
                .build();

        userRepository.save(userObj);

        this.mockMvc.perform(post(BASE_URL + "/register")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnOkMatchingUsernameForLogin() throws Exception {
        User userObj = User.builder()
                .username("test3")
                .password(passwordEncoder.encode("test"))
                .email("test3")
                .firstName("test")
                .lastName("test")
                .build();
        userRepository.save(userObj);

        LoginUserDTO user = new LoginUserDTO();
        user.setUsername("test3");
        user.setPassword("test");
        String userJson = objectMapper.writeValueAsString(user);

        Optional<ResponseUserDTO> responseUser = Optional.ofNullable(ResponseUserDTO.builder()
                .email(userObj.getEmail())
                .username(userObj.getUsername())
                .firstName(userObj.getFirstName())
                .lastName(userObj.getLastName())
                .build());
        String responseUserJson = objectMapper.writeValueAsString(responseUser);

        this.mockMvc.perform(post(BASE_URL + "/login")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(responseUserJson)));
    }

    @Test
    void shouldReturnOkMatchingEmailForLogin() throws Exception {
        User userObj = User.builder()
                .username("test4")
                .password(passwordEncoder.encode("test"))
                .email("test4")
                .firstName("test")
                .lastName("test")
                .build();
        userRepository.save(userObj);

        LoginUserDTO user = new LoginUserDTO();
        user.setEmail("test4");
        user.setPassword("test");
        String userJson = objectMapper.writeValueAsString(user);

        Optional<ResponseUserDTO> responseUser = Optional.ofNullable(ResponseUserDTO.builder()
                .email(userObj.getEmail())
                .username(userObj.getUsername())
                .firstName(userObj.getFirstName())
                .lastName(userObj.getLastName())
                .build());
        String responseUserJson = objectMapper.writeValueAsString(responseUser);

        this.mockMvc.perform(post(BASE_URL + "/login")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(responseUserJson)));
    }

    @Test
    void shouldReturnUnauthorizedWrongPasswordForLogin() throws Exception {
        User userObj = User.builder()
                .username("test5")
                .password(passwordEncoder.encode("oh wrong pw"))
                .email("test5")
                .firstName("test")
                .lastName("test")
                .build();
        userRepository.save(userObj);

        LoginUserDTO user = new LoginUserDTO();
        user.setUsername("test5");
        user.setPassword("test");
        String userJson = objectMapper.writeValueAsString(user);

        this.mockMvc.perform(post(BASE_URL + "/login")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnOkForFind() throws Exception {
        User userObj = User.builder()
                .username("test6")
                .password(passwordEncoder.encode("oh wrong pw"))
                .email("test6")
                .firstName("test")
                .lastName("test")
                .build();
        userRepository.save(userObj);

        Optional<ResponseUserDTO> responseUser = Optional.ofNullable(ResponseUserDTO.builder()
                .email(userObj.getEmail())
                .username(userObj.getUsername())
                .firstName(userObj.getFirstName())
                .lastName(userObj.getLastName())
                .build());
        String responseUserJson = objectMapper.writeValueAsString(responseUser);

        this.mockMvc.perform(get(BASE_URL + "/find/test6"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(responseUserJson)));
    }

    @Test
    void shouldReturnNotFoundForFind() throws Exception {
        this.mockMvc.perform(get(BASE_URL + "/find/asd"))
                .andExpect(status().isNotFound());
    }
}
