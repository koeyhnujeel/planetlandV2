package com.planetlandV2.exception;

public class NotSupportedExtension extends CustomException{

	private static final String MESSAGE = "지원하지 않는 파일 형식입니다.";

	public NotSupportedExtension() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return 415;
	}
}
