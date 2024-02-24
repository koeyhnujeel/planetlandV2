package com.planetlandV2.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.planetlandV2.domain.User;
import com.planetlandV2.exception.signup.ExistsEmailException;
import com.planetlandV2.exception.signup.ExistsNicknameException;
import com.planetlandV2.repository.TransactionRepository;
import com.planetlandV2.repository.UserRepository;
import com.planetlandV2.request.Signup;

@SpringBootTest
class AuthServiceTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private AuthService authService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	void clean() {
		transactionRepository.deleteAll();
		userRepository.deleteAll();
	}

	@Test
	@DisplayName("회원가입 성공")
	void test1() {
		//given
		Signup signup = Signup.builder()
			.email("test@email.com")
			.password("1234")
			.nickname("zunza")
			.build();

		//when
		authService.signup(signup);

		//then
		assertEquals(1, userRepository.count());

		User user = userRepository.findAll().iterator().next();
		assertEquals("test@email.com", user.getEmail());
		assertTrue(passwordEncoder.matches(signup.getPassword(), user.getPassword()));
		assertEquals("zunza", user.getNickname());
	}

	@Test
	@DisplayName("회원가입시 중복된 이메일")
	void test2() {
		//given
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

		//expected
		assertThrows(ExistsEmailException.class, () -> {
			authService.signup(signup);
		});
	}

	@Test
	@DisplayName("회원가입시 중복된 닉네임")
	void test3() {
		//given
		userRepository.save(User.builder()
			.email("test@email.com")
			.password("1234")
			.nickname("zunza")
			.build());

		Signup signup = Signup.builder()
			.email("test123@email.com")
			.password("1111")
			.nickname("zunza")
			.build();

		//expected
		assertThrows(ExistsNicknameException.class, () ->
			authService.signup(signup));
	}
}
