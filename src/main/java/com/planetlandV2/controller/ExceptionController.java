package com.planetlandV2.controller;

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

		response.addValidation(e.getRequestPartName(), "이미지 파일을 업로드 해주세요.");
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
