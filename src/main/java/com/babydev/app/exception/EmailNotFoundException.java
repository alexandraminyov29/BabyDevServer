package com.babydev.app.exception;

public class EmailNotFoundException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmailNotFoundException() {
		super("No account with this email address was found.");
	}
}
