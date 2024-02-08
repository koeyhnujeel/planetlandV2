package com.planetlandV2.exception.planet;

import com.planetlandV2.constant.HttpStatusCode;
import com.planetlandV2.exception.CustomException;

public class PlanetNotFound extends CustomException {

	private static final String MESSAGE = "The planet does not exist.";

	public PlanetNotFound() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return HttpStatusCode.NOT_FOUND;
	}
}
