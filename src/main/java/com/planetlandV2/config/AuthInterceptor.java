package com.planetlandV2.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.planetlandV2.exception.Unauthorized;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		Exception {

		String accessToken = request.getParameter("accessToken");
		if (accessToken != null && accessToken.equals("zunza")) {
			return true;
		}
		throw new Unauthorized();
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
		ModelAndView modelAndView) throws Exception {
		log.info(">> postHandle");
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
		Exception ex) throws Exception {
		log.info(">> afterCompletion");
	}
}
