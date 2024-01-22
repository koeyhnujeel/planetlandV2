package com.planetlandV2.exception.login;

import com.planetlandV2.constant.HttpStatusCode;
import com.planetlandV2.exception.CustomException;

public class SessionNotFound extends CustomException {

	private static final String MESSAGE = "세션 정보가 존재하지 않습니다.";

	public SessionNotFound() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return HttpStatusCode.NOT_FOUND;
	}
}
