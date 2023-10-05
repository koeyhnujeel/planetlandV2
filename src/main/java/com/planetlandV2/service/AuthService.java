package com.planetlandV2.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.planetlandV2.domain.Session;
import com.planetlandV2.domain.User;
import com.planetlandV2.exception.InvalidSignInInformation;
import com.planetlandV2.repository.UserRepository;
import com.planetlandV2.requset.Login;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;

	@Transactional
	public String signIn(Login login) {
		User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
			.orElseThrow(() -> new InvalidSignInInformation());
		Session session = user.addSession();

		return session.getAccessToken();
	}
}
