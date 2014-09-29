package com.bkl.Kcoin.form;

import com.km.common.vo.RetCode;

public abstract class ValidateForm {

	ThreadLocal<RetCode> ERROR_LOCAL = new ThreadLocal<RetCode>();

	public boolean validate() {
		return true;
	}

	public void setRetCode(RetCode retCode) {
		ERROR_LOCAL.set(retCode);
	}

	public RetCode getErrorCode() {
		return ERROR_LOCAL.get();
	}
}
