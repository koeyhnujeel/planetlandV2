package com.planetlandV2.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LogoutSuccessResponse {
	private String message;

	@Builder
	public LogoutSuccessResponse(String message) {
		this.message = message;
	}
}
