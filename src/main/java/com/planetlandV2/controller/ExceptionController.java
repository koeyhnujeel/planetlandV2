package com.planetlandV2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.planetlandV2.response.ErrorResponse;

@RestController
@ControllerAdvice
public class ExceptionController {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ErrorResponse invalidRequest(MethodArgumentNotValidException e) {
		ErrorResponse response = new ErrorResponse("400", "잘못된 요청입니다.");

		for (FieldError fieldError : e.getFieldErrors()) {
			response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
		}
		return response;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MissingServletRequestPartException.class)
	public ErrorResponse invalidRequest(MissingServletRequestPartException e) {
		ErrorResponse response = new ErrorResponse("400", "잘못된 요청입니다.");
		response.addValidation(e.getRequestPartName(), "이미지 파일을 업로드 해주세요.");
		return response;
	}
}
