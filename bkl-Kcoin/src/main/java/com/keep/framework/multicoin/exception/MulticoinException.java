package com.keep.framework.multicoin.exception;

public class MulticoinException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String errorCode;

	@SuppressWarnings("unused")
	private MulticoinException() {
		throw new UnsupportedOperationException("unsupport empty constructor.");
	}

	public MulticoinException(ExceptionCode code) {
		this(code.getCode(), code.getMessage());
	}

	public MulticoinException(String errorCode, String message) {
		super(message);
	}

	public String getErrorCode() {
		return errorCode;
	}
}
