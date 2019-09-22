package com.revature.exceptions;

import java.io.IOException;

/**
 * In case of attempted subversion around Zuul we want to invalidate the
 * session, so we can guarantee that the user will not be authenticated
 * 
 * @author Austin Bark (190422-Java-Spark)
 *
 */
public class SubversionAttemptException extends IOException {

	private static final long serialVersionUID = -1736687159994666796L;

	public SubversionAttemptException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public SubversionAttemptException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public SubversionAttemptException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
