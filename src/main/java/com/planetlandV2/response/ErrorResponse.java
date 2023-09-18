package com.planetlandV2.response;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
// @JsonInclude(value = JsonInclude.Include.NON_EMPTY) // 빈 값은 빼고 응답한다.
public class ErrorResponse {

	private final String code;
	private final String message;
	private final Map<String, String> validation;

	@Builder
	public ErrorResponse(String code, String message, Map<String, String> validation) {
		this.code = code;
		this.message = message;
		this.validation = validation != null ? validation : new HashMap<>();
	}

	public void addValidation(String fieldName, String errorMessage) {
		validation.put(fieldName, errorMessage);
	}
}
