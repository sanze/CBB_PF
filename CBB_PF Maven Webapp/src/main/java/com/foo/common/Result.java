package com.foo.common;

public abstract class Result {

	// 0 成功 1 失败 9 权限检查失败
	protected int returnResult;

	// 返回消息
	protected String returnMessage;

	public int getReturnResult() {
		return returnResult;
	}

	public void setReturnResult(int returnResult) {
		this.returnResult = returnResult;
	}

	public String getReturnMessage() {
		return returnMessage;
	}

	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}

}
