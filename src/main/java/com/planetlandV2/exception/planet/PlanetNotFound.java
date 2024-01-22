package com.planetlandV2.exception.planet;

import com.planetlandV2.constant.HttpStatusCode;
import com.planetlandV2.exception.CustomException;

public class PlanetNotFound extends CustomException {

	private static final String MESSAGE = "존재하지 않는 행성입니다.";

	public PlanetNotFound() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return HttpStatusCode.NOT_FOUND;
	}
}
