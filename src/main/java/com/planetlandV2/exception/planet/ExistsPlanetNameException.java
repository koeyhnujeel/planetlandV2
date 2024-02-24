package com.planetlandV2.exception.planet;

import com.planetlandV2.constant.HttpStatusCode;
import com.planetlandV2.exception.CustomException;

public class ExistsPlanetNameException extends CustomException {

	private static final String MESSAGE = "잘못된 요청입니다.";

	public ExistsPlanetNameException() {
		super(MESSAGE);
		addValidation("planetName", "The planet already exists.");
	}

	@Override
	public int getStatusCode() {
		return HttpStatusCode.CONFLICT;
	}
}
