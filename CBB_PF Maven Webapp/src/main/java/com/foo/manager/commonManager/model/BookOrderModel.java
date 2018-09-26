package com.foo.manager.commonManager.model;

import java.util.List;
import java.util.Map;

public class BookOrderModel {

	private Map head;
	private Map orderitem;
	private List<Map> bookItems;
	
	public Map getHead() {
		return head;
	}
	public void setHead(Map head) {
		this.head = head;
	}

	public Map getOrderitem() {
		return orderitem;
	}
	public void setOrderitem(Map orderitem) {
		this.orderitem = orderitem;
	}
	public List<Map> getBookItems() {
		return bookItems;
	}
	public void setBookItems(List<Map> bookItems) {
		this.bookItems = bookItems;
	}
	
	

}
