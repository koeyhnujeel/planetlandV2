package com.planetlandV2.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LogoutResponse {
	private String message;

	@Builder
	public LogoutResponse(String message) {
		this.message = message;
	}
}
