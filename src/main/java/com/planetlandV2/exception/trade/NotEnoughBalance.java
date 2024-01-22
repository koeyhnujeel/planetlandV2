package com.planetlandV2.exception.trade;

import com.planetlandV2.constant.HttpStatusCode;
import com.planetlandV2.exception.CustomException;

public class NotEnoughBalance extends CustomException {

	private static final String MESSAGE = "잔고가 부족합니다.";

	public NotEnoughBalance() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return HttpStatusCode.BAD_REQUEST;
	}
}
