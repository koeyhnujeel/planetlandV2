package com.planetlandV2.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.planetlandV2.crypto.PasswordEncoder;
import com.planetlandV2.domain.Session;
import com.planetlandV2.domain.User;
import com.planetlandV2.exception.signup.ExistsEmailException;
import com.planetlandV2.exception.signup.ExistsNicknameException;
import com.planetlandV2.exception.login.InvalidSignInInformation;
import com.planetlandV2.repository.SessionRepository;
import com.planetlandV2.repository.UserRepository;
import com.planetlandV2.requset.Login;
import com.planetlandV2.requset.Signup;

@SpringBootTest
class AuthServiceTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SessionRepository sessionRepository;

	@Autowired
	private AuthService authService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	void clean() {
		userRepository.deleteAll();
		sessionRepository.deleteAll();
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

	@Test
	@DisplayName("로그인 성공")
	void test4() {
		//given
		String encryptedPassword = passwordEncoder.encrypt("1234");

		userRepository.save(User.builder()
			.email("test@email.com")
			.password(encryptedPassword)
			.nickname("zunza")
			.build());

		Login login = Login.builder()
			.email("test@email.com")
			.password("1234")
			.build();

		//when
		String accessToken = authService.signIn(login);

		//then
		assertNotNull(accessToken);
	}

	@Test
	@DisplayName("로그인 비밀번호 틀림")
	void test5() {
		//given
		String encryptedPassword = passwordEncoder.encrypt("1234");

		userRepository.save(User.builder()
			.email("test@email.com")
			.password(encryptedPassword)
			.nickname("zunza")
			.build());

		Login login = Login.builder()
			.email("test@email.com")
			.password("1235")
			.build();

		//expected
		assertThrows(InvalidSignInInformation.class, () -> {
			authService.signIn(login);
		});
	}

	@Test
	@DisplayName("로그아웃 성공")
	@Transactional
	void test6() {
		//given
		String encryptedPassword = passwordEncoder.encrypt("1234");

		User user = userRepository.save(User.builder()
			.email("test@email.com")
			.password(encryptedPassword)
			.nickname("zunza")
			.build());
		Session session = user.addSession();
		sessionRepository.save(session);

		User user2 = userRepository.save(User.builder()
			.email("test1@email.com")
			.password(encryptedPassword)
			.nickname("zunza1")
			.build());
		Session session1 = user2.addSession();
		sessionRepository.save(session1);

		//when
		authService.signOut(user.getId());

		//then
		assertEquals(0, user.getSessions().size());
		assertEquals(1, user2.getSessions().size());
		assertEquals(1, sessionRepository.count());
	}
}
