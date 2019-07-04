package com.revature.exceptions;

public class InvalidStatusException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public InvalidStatusException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public InvalidStatusException(String message) {
		super(message);
	}
	
	public InvalidStatusException(Throwable cause) {
		super(cause);
	}
	
	
}
