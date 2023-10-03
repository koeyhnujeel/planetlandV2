package com.planetlandV2.controller;

import java.util.Optional;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.planetlandV2.domain.User;
import com.planetlandV2.exception.InvalidRequest;
import com.planetlandV2.exception.InvalidSignInInformation;
import com.planetlandV2.repository.UserRepository;
import com.planetlandV2.requset.Login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

	private final UserRepository userRepository;

	@PostMapping("/auth/login")
	public User login(@RequestBody Login login) {
		// json 아이디/비밀번호
		log.info(">>> login = {}", login);

		// DB 에서 조회
		User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
			.orElseThrow(() -> new InvalidSignInInformation());

		return user;
	}
}
