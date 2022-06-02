package com.bytehonor.sdk.jdbc.bytehonor.web.error;

public final class ExceptionHolder {
	
	/**
	 * Http Status
	 */
	private Integer status;
	
	/**
	 * Error Code
	 */
	private Integer code;
	
	/**
	 * Exception bean
	 */
	private Exception exception;
	
	public ExceptionHolder(Integer code, Exception exception) {
		this.code = code;
		this.exception = exception;
	}

	public ExceptionHolder() {
		this(null, null);
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}
}
