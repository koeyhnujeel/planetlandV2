package com.planetlandV2.exception.signup;

import com.planetlandV2.constant.HttpStatusCode;
import com.planetlandV2.exception.CustomException;

public class ExistsEmailAndNicknameException extends CustomException {

	private static final String MESSAGE = "잘못된 요청입니다.";

	public ExistsEmailAndNicknameException() {
		super(MESSAGE);
		addValidation("email", "This email is already registered.");
		addValidation("nickname", "This nickname is already registered.");
	}

	@Override
	public int getStatusCode() {
		return HttpStatusCode.CONFLICT;
	}
}
