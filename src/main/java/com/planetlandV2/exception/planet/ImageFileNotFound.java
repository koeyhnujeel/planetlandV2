package com.planetlandV2.exception.planet;

import com.planetlandV2.constant.HttpStatusCode;
import com.planetlandV2.exception.CustomException;

public class ImageFileNotFound extends CustomException {

	private static final String MESSAGE = "이미지 파일을 업로드 해주세요.";

	public ImageFileNotFound() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return HttpStatusCode.NOT_FOUND;
	}
}
