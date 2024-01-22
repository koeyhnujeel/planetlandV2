package com.planetlandV2.exception;

public class NotOwnerException extends CustomException{

	private static final String MESSAGE = "본인 소유에 행성이 아닙니다.";

	public NotOwnerException() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return 400;
	}
}
