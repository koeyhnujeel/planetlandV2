package com.planetlandV2.exception.planet;

import com.planetlandV2.constant.HttpStatusCode;
import com.planetlandV2.exception.CustomException;

public class NotSupportedExtension extends CustomException {

	private static final String MESSAGE = "지원하지 않는 파일 형식입니다.";

	public NotSupportedExtension() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return HttpStatusCode.UNSUPPORTED_MEDIA_TYPE;
	}
}
