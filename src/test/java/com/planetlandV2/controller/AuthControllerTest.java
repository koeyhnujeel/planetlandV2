package com.planetlandV2.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.servlet.http.Cookie;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planetlandV2.crypto.PasswordEncoder;
import com.planetlandV2.domain.Session;
import com.planetlandV2.domain.User;
import com.planetlandV2.repository.SessionRepository;
import com.planetlandV2.repository.UserRepository;
import com.planetlandV2.request.Login;
import com.planetlandV2.request.Signup;

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

	@Autowired
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	void clean() {
		userRepository.deleteAll();
	}

	@Test
	@DisplayName("로그인 성공")
	void test1() throws Exception{
		// given
		String encryptPassword = passwordEncoder.encrypt("1234");

		userRepository.save((User.builder()
			.email("test@email.com")
			.password(encryptPassword)
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
		String encryptPassword = passwordEncoder.encrypt("1234");

		User user = userRepository.save((User.builder()
			.email("test@email.com")
			.password(encryptPassword)
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
		String encryptPassword = passwordEncoder.encrypt("1234");

		User user = userRepository.save((User.builder()
			.email("test@email.com")
			.password(encryptPassword)
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
			.andExpect(cookie().exists("SESSION"))
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

		Cookie cookie = new MockCookie("SESSION", session.getAccessToken());
		// expected
		mockMvc.perform(get("/foo")
				.cookie(cookie)
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

		Cookie cookie = new MockCookie("SESSION", session.getAccessToken() + "other");

		// expected
		mockMvc.perform(get("/foo")
				.cookie(cookie)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized())
			.andDo(print());
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
			.andExpect(jsonPath("$.message").value("이미 가입된 이메일입니다."))
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
			.andExpect(jsonPath("$.message").value("사용중인 닉네임 입니다."))
			.andDo(print());
	}
}
