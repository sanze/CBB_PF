package com.foo.manager.commonManager.model;
import java.io.Serializable;

public class RequestParamModel implements Serializable{
	private static final long serialVersionUID = -8067616826958603556L;
	
	//请求类型
	private String requestType;
	//报文内容
	private String logistics_interface = "";
	//签名
	private String data_digest;
	//校验结果码
	private int errorCode;
	//校验错误信息
	private String errorMessage;
	
	public String getLogistics_interface() {
		return logistics_interface;
	}
	public void setLogistics_interface(String logistics_interface) {
		this.logistics_interface = logistics_interface;
	}
	public String getData_digest() {
		return data_digest;
	}
	public void setData_digest(String data_digest) {
		this.data_digest = data_digest;
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
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	
	
}
