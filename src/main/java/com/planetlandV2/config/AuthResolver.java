package com.planetlandV2.config;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.planetlandV2.config.data.UserSession;
import com.planetlandV2.domain.Session;
import com.planetlandV2.exception.Unauthorized;
import com.planetlandV2.repository.SessionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

	private final SessionRepository sessionRepository;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(UserSession.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

		HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
		if (servletRequest == null) {
			log.error("servletRequest null");
			throw new Unauthorized();
		}

		Cookie[] cookies = servletRequest.getCookies();
		if (cookies == null) {
			log.error("cookie null");
			throw new Unauthorized();
		}
		String accessToken = cookies[0].getValue();

		Session session = sessionRepository.findByAccessToken(accessToken)
			.orElseThrow(() -> new Unauthorized());

		return new UserSession(session.getUser().getId());
	}
}
