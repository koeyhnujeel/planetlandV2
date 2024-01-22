package com.planetlandV2.exception;

public class ExistsNicknameException extends CustomException {

	private static final String MESSAGE = "사용중인 닉네임 입니다.";

	public ExistsNicknameException() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return 409;
	}
}
