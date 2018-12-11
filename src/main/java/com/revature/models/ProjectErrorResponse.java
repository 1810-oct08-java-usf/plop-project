package com.revature.models;


public class ProjectErrorResponse {
	private int status;
	private String message;
	private long timmeStamp;
	
	public ProjectErrorResponse() {
		super();
	}

	public ProjectErrorResponse(int status, String message, long timmeStamp) {
		super();
		this.status = status;
		this.message = message;
		this.timmeStamp = timmeStamp;
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

	public long getTimmeStamp() {
		return timmeStamp;
	}

	public void setTimmeStamp(long timmeStamp) {
		this.timmeStamp = timmeStamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + status;
		result = prime * result + (int) (timmeStamp ^ (timmeStamp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProjectErrorResponse other = (ProjectErrorResponse) obj;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (status != other.status)
			return false;
		if (timmeStamp != other.timmeStamp)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ProjectErrorResponse [status=" + status + ", message=" + message + ", timmeStamp=" + timmeStamp + "]";
	}
	
	
}

