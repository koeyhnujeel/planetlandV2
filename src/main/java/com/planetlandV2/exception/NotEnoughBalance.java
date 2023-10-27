package com.planetlandV2.exception;

public class NotEnoughBalance extends CustomException {

	private static final String MESSAGE = "잔고가 부족합니다.";

	public NotEnoughBalance() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return 400;
	}
}
