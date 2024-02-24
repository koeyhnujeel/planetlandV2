package com.planetlandV2.exception.signup;

import com.planetlandV2.constant.HttpStatusCode;
import com.planetlandV2.exception.CustomException;

public class ExistsNicknameException extends CustomException {

	private static final String MESSAGE = "잘못된 요청입니다.";

	public ExistsNicknameException() {
		super(MESSAGE);
		addValidation("nickname", "This nickname is already registered.");
	}

	@Override
	public int getStatusCode() {
		return HttpStatusCode.CONFLICT;
	}
}
