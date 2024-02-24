package com.planetlandV2.exception.trade;

import com.planetlandV2.exception.CustomException;

public class AlreadyOnSaleException extends CustomException {

	private static final String MESSAGE = "The planet is already being sold.";
	public AlreadyOnSaleException() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return 400;
	}
}
