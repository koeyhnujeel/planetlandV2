package com.planetlandV2.exception.signup;

import com.planetlandV2.constant.HttpStatusCode;
import com.planetlandV2.exception.CustomException;

public class ExistsEmailAndNicknameException extends CustomException {

	private static final String MESSAGE = "The email and nickname have already been registered.";

	public ExistsEmailAndNicknameException() {
		super(MESSAGE);
		addValidation("email", "이미 가입된 이메일과 사용중인 닉네임입니다.");
		addValidation("nickname", "이미 가입된 이메일과 사용중인 닉네임입니다.");
	}

	@Override
	public int getStatusCode() {
		return HttpStatusCode.CONFLICT;
	}
}
