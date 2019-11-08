package com.revature.rpm.exceptions;

/**
 * This project not found exception is utilized for projects that have an invalid id when being
 * queried for the client. There is a custom message for this exception to explain why the exception
 * was thrown.
 */
public class ProjectNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 8875859203928017162L;

  public ProjectNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public ProjectNotFoundException(String message) {
    super(message);
  }

  public ProjectNotFoundException(Throwable cause) {
    super(cause);
  }
}
