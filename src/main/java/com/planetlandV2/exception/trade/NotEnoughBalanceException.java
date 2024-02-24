package com.planetlandV2.exception.trade;

import com.planetlandV2.constant.HttpStatusCode;
import com.planetlandV2.exception.CustomException;

public class NotEnoughBalanceException extends CustomException {

	private static final String MESSAGE = "Insufficient balance.";

	public NotEnoughBalanceException() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return HttpStatusCode.BAD_REQUEST;
	}
}
