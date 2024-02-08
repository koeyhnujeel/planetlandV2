package com.planetlandV2.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planetlandV2.domain.User;
import com.planetlandV2.repository.UserRepository;
import com.planetlandV2.request.Signup;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void clean() {
		userRepository.deleteAll();
	}

	@Test
	@DisplayName("회원가입")
	void test7() throws Exception {
		// given
		Signup signup = Signup.builder()
			.email("test@email.com")
			.password("1111")
			.nickname("test")
			.build();

		String json = objectMapper.writeValueAsString(signup);

		// expected
		mockMvc.perform(post("/auth/signup")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	@DisplayName("회원가입 시 이메일 중복불가")
	void test8() throws Exception {
		// given
		userRepository.save(User.builder()
			.email("test@email.com")
			.password("1234")
			.nickname("zunza")
			.build());

		Signup signup = Signup.builder()
			.email("test@email.com")
			.password("1111")
			.nickname("test")
			.build();

		String json = objectMapper.writeValueAsString(signup);

		// expected
		mockMvc.perform(post("/auth/signup")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isConflict())
			.andExpect(jsonPath("$.code").value("409"))
			.andExpect(jsonPath("$.message").value("This email is already registered."))
			.andDo(print());
	}

	@Test
	@DisplayName("회원가입 시 닉네임 중복불가")
	void test9() throws Exception {
		// given
		userRepository.save(User.builder()
			.email("test@email.com")
			.password("1234")
			.nickname("zunza")
			.build());

		Signup signup = Signup.builder()
			.email("test11@email.com")
			.password("1111")
			.nickname("zunza")
			.build();

		String json = objectMapper.writeValueAsString(signup);

		// expected
		mockMvc.perform(post("/auth/signup")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isConflict())
			.andExpect(jsonPath("$.code").value("409"))
			.andExpect(jsonPath("$.message").value("This nickname is already registered."))
			.andDo(print());
	}
}
