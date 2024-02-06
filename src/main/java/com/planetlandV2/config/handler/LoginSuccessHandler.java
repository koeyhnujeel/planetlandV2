package com.planetlandV2.config.handler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planetlandV2.config.UserPrincipal;
import com.planetlandV2.constant.HttpStatusCode;
import com.planetlandV2.response.LoginSuccessResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

	private final ObjectMapper objectMapper;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {

		UserPrincipal principal = (UserPrincipal)authentication.getPrincipal();
		log.info("[인증성공] user={}", principal.getUsername());

		LoginSuccessResponse loginResponse = LoginSuccessResponse.builder()
			.nickname(principal.getNickname())
			.build();

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setStatus(HttpStatusCode.OK);
		objectMapper.writeValue(response.getWriter(), loginResponse);
	}
}
