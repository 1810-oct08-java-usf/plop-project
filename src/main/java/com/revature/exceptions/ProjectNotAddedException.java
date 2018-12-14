package com.revature.exceptions;

/**
 * This project not added exception is utilized for projects that are being added to the database,
 * but missing key information such as the name, batch, repo links and tech stack.
 * There is a custom message for this exception to explain why the exception was thrown.
 */
public class ProjectNotAddedException extends RuntimeException {

	private static final long serialVersionUID = -8839652830330269073L;

	public ProjectNotAddedException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public ProjectNotAddedException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public ProjectNotAddedException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

}
