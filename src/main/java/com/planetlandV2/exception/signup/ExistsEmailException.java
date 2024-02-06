package com.planetlandV2.exception.signup;

import com.planetlandV2.constant.HttpStatusCode;
import com.planetlandV2.exception.CustomException;

public class ExistsEmailException extends CustomException {

	private static final String MESSAGE = "This email is already registered.";

	public ExistsEmailException() {
		super(MESSAGE);
		addValidation("email", "이미 가입된 이메일입니다.");
	}

	@Override
	public int getStatusCode() {
		return HttpStatusCode.CONFLICT;
	}
}
