package com.foo.model.suningModel;

public class SnReponseContentModel {
	
	private SnHeadModel sn_head;
	private String sn_body;
	private SnErrorModel sn_error;
	
	public SnHeadModel getSn_head() {
		return sn_head;
	}
	public void setSn_head(SnHeadModel sn_head) {
		this.sn_head = sn_head;
	}
	
	public String getSn_body() {
		return sn_body;
	}
	public void setSn_body(String sn_body) {
		this.sn_body = sn_body;
	}
	public SnErrorModel getSn_error() {
		return sn_error;
	}
	public void setSn_error(SnErrorModel sn_error) {
		this.sn_error = sn_error;
	}
}
