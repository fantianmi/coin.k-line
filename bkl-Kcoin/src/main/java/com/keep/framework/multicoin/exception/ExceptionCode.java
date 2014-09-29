package com.keep.framework.multicoin.exception;

public enum ExceptionCode {
	
	PARAMS_ERROR("CODE_PARAMS_ERROR", "参数异常!"),
	
	METHOD_NOT_FOUND("CODE_METHOD_NOT_FOUND", "无效方法!"),
	
	CONNET_FAILED("CODE_CONNET_FAILED", "连接失败!"),
	
	AUTH_FAILURE("CODE_AUTH_FAILURE", "鉴权失败!"),
	
	UNKNOW_EXCEPTION("CODE_UNKNOW_EXCEPTION", "未知异常!");
	
	private String code;

	private String message;

	ExceptionCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
}
