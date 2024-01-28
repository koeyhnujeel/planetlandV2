package com.planetlandV2.config;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.authorizeHttpRequests()
			.requestMatchers("/auth/login").permitAll()
			.anyRequest().authenticated()
			.and()
			.csrf(AbstractHttpConfigurer::disable)
			.build();
	}

	/*
	Security 적용 안하기
	*/
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring()
			.requestMatchers("/favicon.ico")
			.requestMatchers("/error")
			.requestMatchers(toH2Console());
	}
}
