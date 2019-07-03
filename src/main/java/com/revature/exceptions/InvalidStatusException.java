package com.revature.exceptions;

public class InvalidStatusException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public InvalidStatusException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
	
	public InvalidStatusException(String arg0) {
		super(arg0);
	}
	
	public InvalidStatusException(Throwable arg1) {
		super(arg1);
	}
	
	
}
