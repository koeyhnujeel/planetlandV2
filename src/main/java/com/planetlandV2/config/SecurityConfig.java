package com.planetlandV2.config;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planetlandV2.config.handler.Http401Handler;
import com.planetlandV2.config.handler.Http403Handler;
import com.planetlandV2.config.handler.LoginFailHandler;
import com.planetlandV2.config.handler.LoginSuccessHandler;
import com.planetlandV2.config.handler.LogoutSuccessHandler;
import com.planetlandV2.domain.User;
import com.planetlandV2.exception.UserNotFound;
import com.planetlandV2.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final ObjectMapper objectMapper;
	private final UserRepository userRepository;

	/**
	* Security 적용 안하기
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
				.anyRequest().permitAll()
			.and()
			.cors()
			.and()
			.addFilterBefore(usernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
			.logout()
				.logoutUrl("/logout")
				.logoutSuccessHandler(new LogoutSuccessHandler(objectMapper))
				.invalidateHttpSession(true)
				.deleteCookies("SESSION")
				.permitAll()
			.and()
			// .formLogin()
			// 	.loginPage("/auth/login")
			// 	.loginProcessingUrl("/auth/login")
			// 	.usernameParameter("username")
			// 	.passwordParameter("password")
			// 	.defaultSuccessUrl("/")
			// 	.failureHandler(new LoginFailHandler(objectMapper))
			// .and()
			.exceptionHandling(e -> {
				e.accessDeniedHandler(new Http403Handler(objectMapper));
				e.authenticationEntryPoint(new Http401Handler(objectMapper));
			})
			.rememberMe(rm -> rm.rememberMeParameter("remember")
				.alwaysRemember(false)
				.tokenValiditySeconds(2592000)
			)
			.csrf(AbstractHttpConfigurer::disable)
			.build();
	}

	@Bean
	public EmailPasswordAuthFilter usernamePasswordAuthenticationFilter() {
		EmailPasswordAuthFilter filter = new EmailPasswordAuthFilter("/auth/login", objectMapper);
		filter.setAuthenticationManager(authenticationManager());
		filter.setAuthenticationSuccessHandler(new LoginSuccessHandler(objectMapper));
		filter.setAuthenticationFailureHandler(new LoginFailHandler(objectMapper));
		filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());

		// SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
		// rememberMeServices.setAlwaysRemember(true);
		// rememberMeServices.setValiditySeconds(3600 * 24 * 30);
		// filter.setRememberMeServices(rememberMeServices);
		return filter;
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService(userRepository));
		provider.setPasswordEncoder(passwordEncoder());

		return new ProviderManager(provider);
	}
	/**
	* DB 회원정보로 로그인
	* 로그인 할 때, 사용자 조회, 비밀번호 일치 여부 확인
	*/
	@Bean
	public UserDetailsService userDetailsService(UserRepository userRepository) {
		return username -> {
			User user = userRepository.findByEmail(username)
				.orElseThrow(UserNotFound::new);
			return new UserPrincipal(user);
		};
	}

	/**
	 * 비밀번호 암호화
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
