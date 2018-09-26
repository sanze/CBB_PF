package com.foo.manager.commonManager.model;

import java.util.List;
import java.util.Map;

public class FPAPI_OrderModel {
	// 订单信息
	private List<Map> orderImformation;
	// 面单信息
	private Map orderExpBill;
	// 订单综合信息
	private List<Map> orderDeclare;
	
	
	public List<Map> getOrderImformation() {
		return orderImformation;
	}
	public void setOrderImformation(List<Map> orderImformation) {
		this.orderImformation = orderImformation;
	}
	public Map getOrderExpBill() {
		return orderExpBill;
	}
	public void setOrderExpBill(Map orderExpBill) {
		this.orderExpBill = orderExpBill;
	}
	public List<Map> getOrderDeclare() {
		return orderDeclare;
	}
	public void setOrderDeclare(List<Map> orderDeclare) {
		this.orderDeclare = orderDeclare;
	}
}
