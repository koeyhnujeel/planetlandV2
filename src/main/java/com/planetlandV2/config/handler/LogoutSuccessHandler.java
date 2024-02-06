package com.planetlandV2.config.handler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planetlandV2.config.UserPrincipal;
import com.planetlandV2.constant.HttpStatusCode;
import com.planetlandV2.response.LogoutSuccessResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LogoutSuccessHandler implements
	org.springframework.security.web.authentication.logout.LogoutSuccessHandler {

	private final ObjectMapper objectMapper;
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

		log.info("[로그아웃]");

		LogoutSuccessResponse logoutResponse = LogoutSuccessResponse.builder()
			.message("You have been logged out.")
			.build();

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setStatus(HttpStatusCode.OK);
		objectMapper.writeValue(response.getWriter(), logoutResponse);
	}
}
