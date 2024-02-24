package com.planetlandV2.exception.trade;

import com.planetlandV2.constant.HttpStatusCode;
import com.planetlandV2.exception.CustomException;

public class NotForSaleException extends CustomException {

	private static final String MESSAGE = "This planet is not for sale.";

	public NotForSaleException() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return HttpStatusCode.BAD_REQUEST;
	}
}
