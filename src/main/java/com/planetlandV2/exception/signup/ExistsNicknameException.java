package com.planetlandV2.exception.signup;

import com.planetlandV2.constant.HttpStatusCode;
import com.planetlandV2.exception.CustomException;

public class ExistsNicknameException extends CustomException {

	private static final String MESSAGE = "This nickname is already registered.";

	public ExistsNicknameException() {
		super(MESSAGE);
		addValidation("nickname", "사용중인 닉네임 입니다.");
	}

	@Override
	public int getStatusCode() {
		return HttpStatusCode.CONFLICT;
	}
}
