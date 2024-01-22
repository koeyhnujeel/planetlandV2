package com.planetlandV2.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.planetlandV2.crypto.PasswordEncoder;
import com.planetlandV2.domain.Session;
import com.planetlandV2.domain.User;
import com.planetlandV2.exception.signup.ExistsEmailAndNicknameException;
import com.planetlandV2.exception.signup.ExistsEmailException;
import com.planetlandV2.exception.signup.ExistsNicknameException;
import com.planetlandV2.exception.login.InvalidSignInInformation;
import com.planetlandV2.exception.login.SessionNotFound;
import com.planetlandV2.exception.UserNotFound;
import com.planetlandV2.repository.SessionRepository;
import com.planetlandV2.repository.UserRepository;
import com.planetlandV2.requset.Login;
import com.planetlandV2.requset.Signup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final SessionRepository sessionRepository;

	public void signup(Signup signup) {
		checkDuplicate(signup);
		String encryptedPassword = passwordEncoder.encrypt(signup.getPassword());

		User user = signup.toEntity(encryptedPassword);
		userRepository.save(user);
	}

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

	@Transactional
	public void signOut(Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFound());
		List<Session> sessions = user.getSessions();
		sessions.clear();

		Session session = sessionRepository.findByUser(user)
			.orElseThrow(() -> new SessionNotFound());
		sessionRepository.delete(session);
	}

	private void checkDuplicate(Signup signup) {
		boolean existsEmail = userRepository.existsByEmail(signup.getEmail());
		boolean existsNickname = userRepository.existsByNickname(signup.getNickname());

		if(existsEmail && existsNickname) throw new ExistsEmailAndNicknameException();
		else if (existsEmail) throw new ExistsEmailException();
		else if (existsNickname) throw new ExistsNicknameException();
	}
}
