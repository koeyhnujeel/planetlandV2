package com.planetlandV2.config;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserPrincipal extends User {

	private final Long userId;

	// role : 역할 -> 관리자, 사용자
	// authority: 권한 -> 글 쓰기, 글 읽기, 글 삭제
	// 아래 메소드에서 new SimpleGrantedAuthority("ADMIN") 는 권한이다.
	// 역할을 설정하고 싶다면 파라미터에 "ROLE_ADMIN"
	public UserPrincipal(com.planetlandV2.domain.User user) {
		super(user.getEmail(), user.getPassword(),
			List.of(
				new SimpleGrantedAuthority("ROLE_USER")
			));
		this.userId = user.getId();
	}

	public Long getUserId() {
		return userId;
	}
}
