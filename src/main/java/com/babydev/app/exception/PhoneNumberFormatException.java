package com.babydev.app.exception;

public class PhoneNumberFormatException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PhoneNumberFormatException() {
		super("Phone number should be only digits.");
	}
}
