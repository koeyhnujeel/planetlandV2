package com.planetlandV2.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginSuccessResponse {
	private String nickname;

	@Builder
	public LoginSuccessResponse(String nickname) {
		this.nickname = nickname;
	}
}
