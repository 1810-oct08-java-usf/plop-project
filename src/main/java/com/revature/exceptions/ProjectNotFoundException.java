package com.revature.exceptions;

public class ProjectNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 8875859203928017162L;

	public ProjectNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ProjectNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ProjectNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
