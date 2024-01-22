package com.planetlandV2.exception.login;

import com.planetlandV2.constant.HttpStatusCode;
import com.planetlandV2.exception.CustomException;

public class Unauthorized extends CustomException {

	private static final String MESSAGE = "로그인이 필요한 서비스입니다.";

	public Unauthorized() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return HttpStatusCode.UNAUTHORIZED;
	}
}
