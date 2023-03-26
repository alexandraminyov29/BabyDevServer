package com.babydev.app.exception;

public class EmptyFieldException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmptyFieldException() {
		super("Field is empty.");
	}
}
