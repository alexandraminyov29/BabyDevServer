package com.babydev.app.exception;

public class NotAuthorizedException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotAuthorizedException() {
		super("You are not authorized to perform this action.");
	}
}
