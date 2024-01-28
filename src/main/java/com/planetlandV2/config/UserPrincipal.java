package com.planetlandV2.config;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserPrincipal extends User {

	private final Long userId;

	public UserPrincipal(com.planetlandV2.domain.User user) {
		super(user.getEmail(), user.getPassword(), List.of(new SimpleGrantedAuthority("ADMIN")));
		this.userId = user.getId();
	}

	public Long getUserId() {
		return userId;
	}
}
