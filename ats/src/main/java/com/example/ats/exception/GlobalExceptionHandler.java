package com.example.ats.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.ats.dto.ErrorMessage;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(EventValidationException.class)
	public ResponseEntity<ErrorMessage> handleEventValidation(EventValidationException e) {
		ErrorMessage error = new ErrorMessage(e.getMessage());
		return ResponseEntity.badRequest().body(error);
	}
}
