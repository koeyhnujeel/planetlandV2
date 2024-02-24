package com.planetlandV2.request;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import com.planetlandV2.domain.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Signup {

	@Email(message = "The email format is incorrect.")
	@NotBlank(message = "이메일을 입력해주세요.")
	private String email;

	@Length(min = 8, max = 16, message = "Please set a password between 8 and 16 characters.")
	@NotBlank(message = "비빌번호를 입력해주세요.")
	private String password;

	@Length(min = 3, max = 15, message = "Please set a nickname between 3 and 15 characters.")
	@NotBlank(message = "닉네임을 입력해주세요.")
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
