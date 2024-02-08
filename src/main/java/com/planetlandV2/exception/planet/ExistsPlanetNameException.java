package com.planetlandV2.exception.planet;

import com.planetlandV2.constant.HttpStatusCode;
import com.planetlandV2.exception.CustomException;

public class ExistsPlanetNameException extends CustomException {

	private static final String MESSAGE = "The planet already exists.";

	public ExistsPlanetNameException() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return HttpStatusCode.CONFLICT;
	}
}
