package com.planetlandV2.controller;

import java.time.Duration;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.planetlandV2.config.data.UserSession;
import com.planetlandV2.request.Login;
import com.planetlandV2.request.Signup;
import com.planetlandV2.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	@PostMapping("/auth/signup")
	public void signup(@RequestBody @Valid Signup signup) {
		authService.signup(signup);
	}

	@PostMapping("/auth/login")
	public ResponseEntity<Object> login(@RequestBody @Valid Login login) {
		String accessToken = authService.signIn(login);
		ResponseCookie cookie = ResponseCookie.from("SESSION", accessToken)
			.domain("localhost") // todo 서버 환경에 따른 분리 필요
			.path("/")
			.httpOnly(true)
			.secure(false)
			.maxAge(Duration.ofDays(30))
			.sameSite("Strict")
			.build();

		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, cookie.toString())
			.build();
	}

	@PostMapping("/auth/logout")
	public ResponseEntity<Object> logout(UserSession userSession) {
		authService.signOut(userSession.id);
		ResponseCookie cookie = ResponseCookie.from("SESSION", "")
			.path("/")
			.maxAge(0)
			.build();

		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, cookie.toString())
			.build();
	}
}
