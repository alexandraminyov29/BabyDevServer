package com.babydev.app.exception;

public class RegistrationTokenNotValidException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RegistrationTokenNotValidException() {
		super("Verification token expired.");
	}
}
