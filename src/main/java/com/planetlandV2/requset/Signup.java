package com.planetlandV2.requset;

import javax.validation.constraints.Email;

import com.planetlandV2.domain.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Signup {

	@Email(message = "올바른 이메일 형식이 아닙니다.")
	private String email;

	private String password;
	private String nickname;

	@Builder
	public Signup(String email, String password, String nickname) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
	}

	public User toEntity(String encryptedPassword) {
		return User.builder()
			.email(this.email)
			.password(encryptedPassword)
			.nickname(this.nickname)
			.build();
	}
}
