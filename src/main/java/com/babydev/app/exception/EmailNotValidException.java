package com.babydev.app.exception;

public class EmailNotValidException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmailNotValidException() {
		super("Email is not legal.");
	}
}
