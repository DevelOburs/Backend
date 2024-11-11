package com.fridgify.auth_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fridgify.auth_api.client.UserServiceClient;
import com.fridgify.auth_api.config.SecurityConfig;
import com.fridgify.auth_api.controller.AuthController;
import com.fridgify.auth_api.dto.LoginUserDTO;
import com.fridgify.auth_api.dto.RegisterUserDTO;
import com.fridgify.auth_api.dto.ResponseUserDTO;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfig.class)
@WebMvcTest(AuthController.class)
class AuthApiApplicationMvcTests {

	private final String BASE_URL = "/auth-api";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private UserServiceClient userServiceClient;

	@Test
	void shouldReturnOkOnRegister() throws Exception {
		RegisterUserDTO user = new RegisterUserDTO();
		user.setUsername("test");
		user.setEmail("test");
		user.setPassword("test");
		user.setFirstName("test");
		user.setLastName("test");
		String userJson = objectMapper.writeValueAsString(user);

		ResponseUserDTO responseUser = new ResponseUserDTO();
		responseUser.setUsername("test");
		responseUser.setEmail("test");
		responseUser.setFirstName("test");
		responseUser.setLastName("test");
		String responseUserJson = objectMapper.writeValueAsString(responseUser);

		ResponseEntity<Optional<ResponseUserDTO>> responseEntity = ResponseEntity.ok(Optional.of(responseUser));

		when(userServiceClient.registerUser(user)).thenReturn(responseEntity);

		this.mockMvc.perform(post(BASE_URL + "/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(userJson))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString(responseUserJson)));
	}

	@Test
	void shouldReturnBadRequestOnRegister() throws Exception {
		RegisterUserDTO user = new RegisterUserDTO();
		user.setUsername("test");
		user.setEmail("test");
		user.setPassword("test");
		user.setFirstName("test");
		user.setLastName("test");
		String userJson = objectMapper.writeValueAsString(user);

		when(userServiceClient.registerUser(any(RegisterUserDTO.class)))
				.thenThrow(new FeignException.BadRequest(
						"Bad request error message",
						Request.create(
								Request.HttpMethod.POST, "/register",
								Map.of(), null, null,
								null),
						null,
						Map.of()
				));

		this.mockMvc.perform(post(BASE_URL + "/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(userJson))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	void shouldReturnTokenOnLogin() throws Exception {
		LoginUserDTO user = new LoginUserDTO();
		user.setUsername("test");
		user.setEmail("test");
		user.setPassword("test");
		String userJson = objectMapper.writeValueAsString(user);

		ResponseUserDTO responseUser = new ResponseUserDTO();
		responseUser.setUsername("testname");
		responseUser.setEmail("testmail");
		responseUser.setFirstName("test");
		responseUser.setLastName("test");

		ResponseEntity<Optional<ResponseUserDTO>> responseEntity = ResponseEntity.ok(Optional.of(responseUser));

		when(userServiceClient.loginUser(user)).thenReturn(responseEntity);

		this.mockMvc.perform(post(BASE_URL + "/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(userJson))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString(responseUser.getEmail())))
				.andExpect(content().string(containsString(responseUser.getUsername())))
				.andExpect(content().string(containsString("\"token\":\"ey")));
	}

	@Test
	void shouldReturnBadRequestOnLogin() throws Exception {
		LoginUserDTO user = new LoginUserDTO();
		user.setUsername("test");
		user.setEmail("test");
		user.setPassword("test");
		String userJson = objectMapper.writeValueAsString(user);

		when(userServiceClient.loginUser(any(LoginUserDTO.class)))
				.thenThrow(new FeignException.BadRequest(
						"Bad request error message",
						Request.create(
								Request.HttpMethod.POST, "/login",
								Map.of(), null, null,
								null),
						null,
						Map.of()
				));

		this.mockMvc.perform(post(BASE_URL + "/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(userJson))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	void shouldReturnNotAuthorizedOnLogin() throws Exception {
		LoginUserDTO user = new LoginUserDTO();
		user.setUsername("test");
		user.setEmail("test");
		user.setPassword("test");
		String userJson = objectMapper.writeValueAsString(user);

		when(userServiceClient.loginUser(any(LoginUserDTO.class)))
				.thenThrow(new FeignException.Unauthorized(
						"Unauthorized error message",
						Request.create(
								Request.HttpMethod.POST, "/login",
								Map.of(), null, null,
								null),
						null,
						Map.of()
				));

		this.mockMvc.perform(post(BASE_URL + "/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(userJson))
				.andDo(print())
				.andExpect(status().isUnauthorized());
	}
}
