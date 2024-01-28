package com.planetlandV2.config;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.planetlandV2.domain.User;
import com.planetlandV2.exception.UserNotFound;
import com.planetlandV2.repository.UserRepository;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {

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

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.authorizeHttpRequests()
				.requestMatchers(HttpMethod.POST,"/auth/login").permitAll()
				.requestMatchers(HttpMethod.POST,"/auth/signup").permitAll()
				.anyRequest().authenticated()
			.and()
			.formLogin()
				.loginPage("/auth/login")
				.loginProcessingUrl("/auth/login")
				.usernameParameter("username")
				.passwordParameter("password")
				.defaultSuccessUrl("/")
			.and()
			.rememberMe(rm -> rm.rememberMeParameter("remember")
				.alwaysRemember(false)
				.tokenValiditySeconds(2592000)
			)
			.csrf(AbstractHttpConfigurer::disable)
			.build();
	}
	/*
	로그인 할 때, 사용자 조회, 비밀번호 일치 여부 확인
	*/
	@Bean
	public UserDetailsService userDetailsService(UserRepository userRepository) {
		return username -> {
			User user = userRepository.findByEmail(username)
				.orElseThrow(UserNotFound::new);
			return new UserPrincipal(user);
		};
	}
	/*
	비밀번호 암호화
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new SCryptPasswordEncoder(
			16,
			8,
			1,
			32,
			64);
	}
}
