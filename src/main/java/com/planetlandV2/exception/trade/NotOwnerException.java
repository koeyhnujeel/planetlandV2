package com.planetlandV2.exception.trade;

import com.planetlandV2.constant.HttpStatusCode;
import com.planetlandV2.exception.CustomException;

public class NotOwnerException extends CustomException {

	private static final String MESSAGE = "본인 소유에 행성이 아닙니다.";

	public NotOwnerException() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return HttpStatusCode.BAD_REQUEST;
	}
}
