package com.healthcare.exception;

public class FieldBlankException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public FieldBlankException(String message) {
		super(message);
	}
}
