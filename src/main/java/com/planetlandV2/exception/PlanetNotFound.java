package com.planetlandV2.exception;

public class PlanetNotFound extends CustomException {

	private static final String MESSAGE = "존재하지 않는 행성입니다.";

	public PlanetNotFound() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return 404;
	}
}
