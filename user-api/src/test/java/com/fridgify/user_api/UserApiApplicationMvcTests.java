package com.fridgify.user_api;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fridgify.user_api.config.SecurityConfig;
import com.fridgify.user_api.controller.UserController;
import com.fridgify.user_api.dto.RegisterUserDTO;
import com.fridgify.user_api.dto.LoginUserDTO;
import com.fridgify.user_api.dto.ResponseUserDTO;
import com.fridgify.user_api.model.Role;
import com.fridgify.user_api.model.User;
import com.fridgify.user_api.repository.RoleRepository;
import com.fridgify.user_api.repository.UserRepository;
import com.fridgify.user_api.service.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

@Import(SecurityConfig.class)
@WebMvcTest(UserController.class)
class UserApiApplicationMvcTests {

	private final String BASE_URL = "/user-api";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@SpyBean
	private UserService userService;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private RoleRepository roleRepository;

	@SpyBean
	private BCryptPasswordEncoder passwordEncoder;


	private Optional<User> createMockUser() {
		Role role = new Role("ROLE_USER");
		User userObj = User.builder()
				.username("test")
				.email("test")
				.password(passwordEncoder.encode("test"))
				.roles(List.of(role))
				.build();
		return Optional.of(userObj);
	}

	@Test
	void shouldReturnMethodNotSupportedForRegister() throws Exception {
		this.mockMvc.perform(get(BASE_URL + "/register")).andDo(print())
				.andExpect(status().isInternalServerError())
				.andExpect(content().string(containsString("Request method 'GET' is not supported")));
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
				.roles(List.of("USER"))
				.build());
		String responseUserJson = objectMapper.writeValueAsString(responseUser);

		this.mockMvc.perform(post(BASE_URL + "/register")
						.content(userJson)
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(content().string(containsString(responseUserJson)));
	}

	@Test
	void shouldReturnBadRequestForRegister() throws Exception {
		RegisterUserDTO user = new RegisterUserDTO();
		user.setUsername("test");
		user.setEmail("test");
		user.setPassword("test");
		user.setFirstName("test");
		user.setLastName("test");
		String userJson = objectMapper.writeValueAsString(user);

		Optional<User> userObj = this.createMockUser();
		when(userRepository.findByUsername("test")).thenReturn(userObj);

		this.mockMvc.perform(post(BASE_URL + "/register")
						.content(userJson)
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	void shouldReturnOkForLogin() throws Exception {
		LoginUserDTO user = new LoginUserDTO();
		user.setUsername("test");
		user.setEmail("test");
		user.setPassword("test");
		String userJson = objectMapper.writeValueAsString(user);

		Optional<ResponseUserDTO> responseUser = Optional.ofNullable(ResponseUserDTO.builder()
				.email(user.getEmail())
				.username(user.getUsername())
				.roles(List.of("ROLE_USER"))
				.build());
		String responseUserJson = objectMapper.writeValueAsString(responseUser);

		Optional<User> userObj = this.createMockUser();

		when(userRepository.findByUsername("test")).thenReturn(userObj);
		when(userRepository.findByEmailIgnoreCase("test")).thenReturn(Optional.empty());

		this.mockMvc.perform(post(BASE_URL + "/login")
						.content(userJson)
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString(responseUserJson)));
	}

	@Test
	void shouldReturnUnauthorizedForLogin() throws Exception {
		LoginUserDTO user = new LoginUserDTO();
		user.setUsername("test");
		user.setEmail("test");
		user.setPassword("test");
		String userJson = objectMapper.writeValueAsString(user);

		when(userRepository.findByUsername("test")).thenReturn(Optional.empty());
		when(userRepository.findByEmailIgnoreCase("test")).thenReturn(Optional.empty());

		this.mockMvc.perform(post(BASE_URL + "/login")
						.content(userJson)
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isUnauthorized());
	}

	@Test
	void shouldReturnOkForFindUsername() throws Exception {
		Optional<ResponseUserDTO> responseUser = Optional.ofNullable(ResponseUserDTO.builder()
				.email("test")
				.username("test")
				.build());
		String responseUserJson = objectMapper.writeValueAsString(responseUser);

		Optional<User> userObj = this.createMockUser();
		when(userRepository.findByUsername("test")).thenReturn(userObj);

		this.mockMvc.perform(get(BASE_URL + "/find/test"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString(responseUserJson)));
	}

	@Test
	void shouldReturnNotFoundForFindUsername() throws Exception {
		this.mockMvc.perform(get(BASE_URL + "/find/test"))
				.andExpect(status().isNotFound());
	}
}
