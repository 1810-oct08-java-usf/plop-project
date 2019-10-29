package com.revature.exceptions;

/**
 * This project file size too large exception is utilized for projects that are being added to the
 * database, but contain data models, screenshots, or github repos that are larger than the allowed
 * size. There is a custom message for this exception to explain why the exception was thrown.
 */
public class FileSizeTooLargeException extends RuntimeException {

  private static final long serialVersionUID = -8839652830330269073L;

  public FileSizeTooLargeException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

  public FileSizeTooLargeException(String arg0) {
    super(arg0);
  }

  public FileSizeTooLargeException(Throwable arg0) {
    super(arg0);
  }
}
