package com.planetlandV2.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planetlandV2.domain.Session;
import com.planetlandV2.domain.User;
import com.planetlandV2.repository.SessionRepository;
import com.planetlandV2.repository.UserRepository;
import com.planetlandV2.requset.Login;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SessionRepository sessionRepository;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void clean() {
		userRepository.deleteAll();
	}

	@Test
	@DisplayName("로그인 성공")
	void test1() throws Exception{
		// given
		userRepository.save((User.builder()
			.email("test@email.com")
			.password("1234")
			.nickname("zunza")
			.build()));

		Login login = Login.builder()
			.email("test@email.com")
			.password("1234")
			.build();

		String json = objectMapper.writeValueAsString(login);

		// expected
		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andExpect(status().isOk());
	}

	@Test
	@Transactional
	@DisplayName("로그인 성공 후 세션 1개 생성")
	void test2() throws Exception{
		// given
		User user = userRepository.save((User.builder()
			.email("test@email.com")
			.password("1234")
			.nickname("zunza")
			.build()));

		Login login = Login.builder()
			.email("test@email.com")
			.password("1234")
			.build();

		String json = objectMapper.writeValueAsString(login);

		// expected
		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andExpect(status().isOk())
			.andDo(print());

		Assertions.assertEquals(1L, user.getSessions().size());
	}

	@Test
	@DisplayName("로그인 성공 후 세션 응답")
	void test3() throws Exception {
		// given
		User user = userRepository.save((User.builder()
			.email("test@email.com")
			.password("1234")
			.nickname("zunza")
			.build()));

		Login login = Login.builder()
			.email("test@email.com")
			.password("1234")
			.build();

		String json = objectMapper.writeValueAsString(login);

		// expected
		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.accessToken").isNotEmpty())
			.andDo(print());
	}

	@Test
	@DisplayName("로그인 후 권한이 필요한 페이지 접속한다 /foo")
	void test4() throws Exception {
		// given
		User user = User.builder()
			.email("test@email.com")
			.password("1234")
			.nickname("zunza")
			.build();

		Session session = user.addSession();
		userRepository.save(user);

		// expected
		mockMvc.perform(get("/foo")
				.header("Authorization", session.getAccessToken())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	@DisplayName("로그인 후 검증되지 않은 세션값으로 권한이 필요한 페이지에 접속할 수 없다.")
	void test5() throws Exception {
		// given
		User user = User.builder()
			.email("test@email.com")
			.password("1234")
			.nickname("zunza")
			.build();

		Session session = user.addSession();
		userRepository.save(user);

		// expected
		mockMvc.perform(get("/foo")
				.header("Authorization", session.getAccessToken() + "other")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized())
			.andDo(print());
	}
}
