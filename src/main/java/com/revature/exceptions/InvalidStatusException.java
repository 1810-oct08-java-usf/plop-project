package com.revature.exceptions;

/**
 * If an invalid status is inputed, then throws this exception
 * 
 * @author Austin Bark and Kevin Ocampo (190422-Java-Spark)
 *
 */
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

