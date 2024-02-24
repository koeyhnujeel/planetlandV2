package com.planetlandV2.controller;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.planetlandV2.exception.CustomException;
import com.planetlandV2.response.ErrorResponse;

@RestControllerAdvice
public class ExceptionController {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ErrorResponse invalidRequest(MethodArgumentNotValidException e) {
		ErrorResponse response = ErrorResponse.builder()
			.code("400")
			.message("잘못된 요청입니다.")
			.build();

		for (FieldError fieldError : e.getFieldErrors()) {
			response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
		}
		return response;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MissingServletRequestPartException.class)
	public ErrorResponse invalidRequest(MissingServletRequestPartException e) {
		ErrorResponse response = ErrorResponse.builder()
			.code("400")
			.message("잘못된 요청입니다.")
			.build();

		if (e.getRequestPartName().equals("planetCreate")) {
			response.addValidation(e.getRequestPartName(), "행성 생성 오류");
		} else if (e.getRequestPartName().equals("planetEdit")) {
			response.addValidation(e.getRequestPartName(), "행성 수정 오류");
		} else {
			response.addValidation(e.getRequestPartName(), "Please upload an image file.");
		}
		return response;
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(FileSizeLimitExceededException.class)
	public ErrorResponse invalidRequest(FileSizeLimitExceededException e) {
		ErrorResponse response = ErrorResponse.builder()
			.code("400")
			.message("잘못된 요청입니다.")
			.build();

		response.addValidation(e.getFieldName(), "이미지파일 최대 용량은 10MB 입니다.");
		return response;
	}

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponse> CustomException(CustomException e) {
		int statusCode = e.getStatusCode();

		ErrorResponse body = ErrorResponse.builder()
			.code(String.valueOf(statusCode))
			.message(e.getMessage())
			.validation(e.getValidation())
			.build();

		return ResponseEntity.status(statusCode)
			.body(body);
	}
}
