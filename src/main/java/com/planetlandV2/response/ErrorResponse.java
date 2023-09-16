package com.planetlandV2.response;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {

	private final String code;
	private final String message;
	private Map<String, String> validation = new HashMap<>();

	public void addValidation(String fieldName, String errorMessage) {
		validation.put(fieldName, errorMessage);
	}
}
