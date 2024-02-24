package com.planetlandV2.exception.signup;

import com.planetlandV2.constant.HttpStatusCode;
import com.planetlandV2.exception.CustomException;

public class ExistsEmailException extends CustomException {

	private static final String MESSAGE = "잘못된 요청입니다.";

	public ExistsEmailException() {
		super(MESSAGE);
		addValidation("email", "This email is already registered.");
	}

	@Override
	public int getStatusCode() {
		return HttpStatusCode.CONFLICT;
	}
}
