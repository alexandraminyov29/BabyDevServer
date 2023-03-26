package com.babydev.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.babydev.app.exception.EmailIsTakenException;
import com.babydev.app.exception.EmailNotFoundException;
import com.babydev.app.exception.EmailNotValidException;
import com.babydev.app.exception.EmptyFieldException;
import com.babydev.app.exception.NotAuthorizedException;
import com.babydev.app.exception.PasswordConditionsException;
import com.babydev.app.exception.PhoneNumberFormatException;
import com.babydev.app.exception.WrongPasswordException;

@ControllerAdvice
public class AdviceController {
	
	@ExceptionHandler
	public static ResponseEntity<Object> handleNotAuthorisedException(NotAuthorizedException exception) {
		return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
	}
	
	@ExceptionHandler
	public static ResponseEntity<Object> handleEmptyFieldException(EmptyFieldException exception) {
		return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.NO_CONTENT);
	}
	
	@ExceptionHandler
	public static ResponseEntity<Object> handleEmailIsTaken(EmailIsTakenException exception) {
		return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler
	public static ResponseEntity<Object> handleEmailNotValid(EmailNotValidException exception) {
		return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler
	public static ResponseEntity<Object> handlePhoneNumberFormat(PhoneNumberFormatException exception) {
		return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.NOT_ACCEPTABLE);
	}
	
	@ExceptionHandler
	public static ResponseEntity<Object> handleWrongPassword(WrongPasswordException exception) {
		return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.PARTIAL_CONTENT);
	}
	
	@ExceptionHandler
	public static ResponseEntity<Object> handleEmailNotFound(EmailNotFoundException exception) {
		return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public static ResponseEntity<Object> handlePasswordConditionsNotMet(PasswordConditionsException exception) {
		return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.EXPECTATION_FAILED);
	}
}
