package com.planetlandV2.config.handler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planetlandV2.constant.HttpStatusCode;
import com.planetlandV2.response.ErrorResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LoginFailHandler implements AuthenticationFailureHandler {

	private final ObjectMapper objectMapper;
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws IOException, ServletException {
		log.error("[인증오류] 아이디 또는 비밀번호를 확인해주세요.");

		ErrorResponse errorResponse = ErrorResponse.builder()
			.code(String.valueOf(HttpStatusCode.BAD_REQUEST))
			.message("아이디 또는 비밀번호를 확인해주세요.")
			.build();

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setStatus(HttpStatusCode.BAD_REQUEST);
		objectMapper.writeValue(response.getWriter(), errorResponse);
	}
}

