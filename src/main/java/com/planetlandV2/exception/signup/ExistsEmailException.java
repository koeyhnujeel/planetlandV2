package com.planetlandV2.exception;

public class ExistsEmailException extends CustomException {

	private static final String MESSAGE = "이미 가입된 이메일입니다.";

	public ExistsEmailException() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return 409;
	}
}
