package com.revature.rpm.dtos;

/**
 * This project error response model is used within the exception handler to gather the http status,
 * custom message defined in the exception models and timeStamp of the specified exception being
 * thrown.
 */
public class ProjectErrorResponse {
  private int status;
  private String message;
  private long timeStamp;

  public ProjectErrorResponse() {
    super();
  }

  public ProjectErrorResponse(int status, String message, long timeStamp) {
    super();
    this.status = status;
    this.message = message;
    this.timeStamp = timeStamp;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public long getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(long timeStamp) {
    this.timeStamp = timeStamp;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((message == null) ? 0 : message.hashCode());
    result = prime * result + status;
    result = prime * result + (int) (timeStamp ^ (timeStamp >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    ProjectErrorResponse other = (ProjectErrorResponse) obj;
    if (message == null) {
      if (other.message != null) return false;
    } else if (!message.equals(other.message)) return false;
    if (status != other.status) return false;
    if (timeStamp != other.timeStamp) return false;
    return true;
  }

  @Override
  public String toString() {
    return "ProjectErrorResponse [status="
        + status
        + ", message="
        + message
        + ", timeStamp="
        + timeStamp
        + "]";
  }
}
