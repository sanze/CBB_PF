package com.foo.model.suningModel.LogisticsCrossbuyTask;

import java.util.HashMap;
import java.util.List;

public class QueryLogisticsCrossbuyTask {

	private boolean isUsed;
	private HashMap<String,String> feedBackImformation;
	private List<HashMap<String,String>> feedBackOrderItems;
	private List<HashMap<String,String>> feedBackOrderCustomers;

	public boolean isUsed() {
		return isUsed;
	}

	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}

	public HashMap<String,String> getFeedBackImformation() {
		return feedBackImformation;
	}

	public void setFeedBackImformation(HashMap<String,String> feedBackImformation) {
		this.feedBackImformation = feedBackImformation;
	}

	public List<HashMap<String,String>> getFeedBackOrderItems() {
		return feedBackOrderItems;
	}

	public void setFeedBackOrderItems(List<HashMap<String,String>> feedBackOrderItems) {
		this.feedBackOrderItems = feedBackOrderItems;
	}

	public List<HashMap<String,String>> getFeedBackOrderCustomers() {
		return feedBackOrderCustomers;
	}

	public void setFeedBackOrderCustomers(
			List<HashMap<String,String>> feedBackOrderCustomers) {
		this.feedBackOrderCustomers = feedBackOrderCustomers;
	}

}
