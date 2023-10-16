package com.planetlandV2.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.planetlandV2.crypto.PasswordEncoder;
import com.planetlandV2.domain.Session;
import com.planetlandV2.domain.User;
import com.planetlandV2.exception.ExistsEmailException;
import com.planetlandV2.exception.ExistsNicknameException;
import com.planetlandV2.exception.InvalidSignInInformation;
import com.planetlandV2.repository.UserRepository;
import com.planetlandV2.requset.Login;
import com.planetlandV2.requset.Signup;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public String signIn(Login login) {
		User user = userRepository.findByEmail(login.getEmail())
			.orElseThrow(() -> new InvalidSignInInformation());

		boolean matches = passwordEncoder.matches(login.getPassword(), user.getPassword());
		if (!matches) {
			throw new InvalidSignInInformation();
		}
		Session session = user.addSession();

		return session.getAccessToken();
	}

	public void signup(Signup signup) {
		boolean existsEmail = userRepository.existsByEmail(signup.getEmail());
		boolean existsNickname = userRepository.existsByNickname(signup.getNickname());
		if (existsEmail) {
			throw new ExistsEmailException();
		} else if (existsNickname) {
			throw new ExistsNicknameException(); //todo 다른 방법 생각해보기
		}

		String encryptedPassword = passwordEncoder.encrypt(signup.getPassword());

		User user = User.builder()
			.email(signup.getEmail())
			.password(encryptedPassword)
			.nickname(signup.getNickname())
			.build();
		userRepository.save(user);
	}
}
