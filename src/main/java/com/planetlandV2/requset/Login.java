package com.planetlandV2.requset;

import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Login {

	@NotBlank(message = "이메일을 입력해주세요.")
	private String email;

	@NotBlank(message = "비밀번호를 입력해주세요.")
	private String password;

	@Builder
	public Login(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
