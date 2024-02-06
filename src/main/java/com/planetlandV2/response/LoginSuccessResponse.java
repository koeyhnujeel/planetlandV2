package com.planetlandV2.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {
	private String nickname;

	@Builder
	public LoginResponse(String nickname) {
		this.nickname = nickname;
	}
}
