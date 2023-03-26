package com.babydev.app.exception;

public class PasswordConditionsException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PasswordConditionsException() {
		super("Password should at least be of 8 characters and contain one uppercased letter, one lowercased, "
				+ "one digit and one special character");
	}
}
