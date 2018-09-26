package com.foo.common;

import com.foo.handler.MessageHandler;


/**
 * @author xuxiaojun
 * 
 */
public class CommonException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6707320754830428568L;
	private Exception e;
	private int errorCode;
	private String errorMessage;

	public CommonException(Exception e ,int errorCode) {
		this.e = e;
		this.errorCode = errorCode;
		this.errorMessage = MessageHandler.getErrorMessage(errorCode);
	}
	
	public CommonException(Exception e ,int errorCode,String errorMessage) {
		this.e = e;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public Exception getE() {
		return e;
	}

	public void setE(Exception e) {
		this.e = e;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
