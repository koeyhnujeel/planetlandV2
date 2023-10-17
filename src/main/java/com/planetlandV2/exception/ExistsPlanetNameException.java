package com.planetlandV2.exception;

public class ExistsPlanetNameException extends CustomException {

	private static final String MESSAGE = "이미 존재하는 행성입니다.";

	public ExistsPlanetNameException() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return 400;
	}
}
