package com.babydev.app.exception;

public class EmailIsTakenException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmailIsTakenException() {
		super("Email is already taken.");
	}
}
