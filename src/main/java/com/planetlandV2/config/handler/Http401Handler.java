package com.planetlandV2.config.handler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

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
public class Http401Handler implements AuthenticationEntryPoint {

	private final ObjectMapper objectMapper;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {
		log.error("[인증오류] 로그인이 필요한 서비스 입니다.");

		ErrorResponse errorResponse = ErrorResponse.builder()
			.code(String.valueOf(HttpStatusCode.UNAUTHORIZED))
			.message("Login required.")
			.build();

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setStatus(HttpStatusCode.UNAUTHORIZED);
		objectMapper.writeValue(response.getWriter(), errorResponse);
	}
}
