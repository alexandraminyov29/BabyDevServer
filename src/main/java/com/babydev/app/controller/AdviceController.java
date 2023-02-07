package com.babydev.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.babydev.app.exception.NotAuthorizedException;

@ControllerAdvice
public class AdviceController {
	
	@ExceptionHandler
	public ResponseEntity<Object> handleNotAuthorisedException(NotAuthorizedException exception) {
		return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
	}
}
