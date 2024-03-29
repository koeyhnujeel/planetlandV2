package com.planetlandV2.exception.planet;

import com.planetlandV2.constant.HttpStatusCode;
import com.planetlandV2.exception.CustomException;

import lombok.Getter;

@Getter
public class InvalidRequest extends CustomException {

	private static final String MESSAGE = "잘못된 요청입니다.";

	private String fieldName;
	private String errorMessage;

	public InvalidRequest() {
		super(MESSAGE);
	}

	public InvalidRequest(String fieldName, String errorMessage) {
		super(MESSAGE);
		addValidation(fieldName, errorMessage);
	}

	@Override
	public int getStatusCode() {
		return HttpStatusCode.BAD_REQUEST;
	}
}
