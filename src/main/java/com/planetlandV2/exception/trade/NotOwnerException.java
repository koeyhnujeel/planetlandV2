package com.planetlandV2.exception.trade;

import com.planetlandV2.constant.HttpStatusCode;
import com.planetlandV2.exception.CustomException;

public class NotOwnerException extends CustomException {

	private static final String MESSAGE = "This is not your own planet.";

	public NotOwnerException() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return HttpStatusCode.BAD_REQUEST;
	}
}
