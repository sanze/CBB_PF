package com.foo.model.suningModel;

import java.util.HashMap;
import java.util.Map;

import com.foo.model.suningModel.LogisticsCrossbuyTask.QueryLogisticsCrossbuyTask;

public class ModelToMap {
	public static Map<String, Object> toLogisticsTask(QueryLogisticsCrossbuyTask task){
		Map<String, Object> resultMap=new HashMap<String, Object>();
		resultMap.put("isUsed", task.isUsed());
		
		resultMap.put("ORDER_NO", task.getFeedBackImformation().get("btcItemOrderId") == null?"":task.getFeedBackImformation().get("btcItemOrderId"));
		resultMap.put("NOTE", task.getFeedBackImformation().get("note") == null?"":task.getFeedBackImformation().get("note"));
		resultMap.put("LOGISTICS_ORDER_ID", task.getFeedBackImformation().get("logisticsOrderId") == null?"":task.getFeedBackImformation().get("logisticsOrderId"));
		
		Double WEIGHT = 0.0;
		Double PACK_NO = 0.0;
		String GOODS_INFO = "";
		for(HashMap<String, String> item:task.getFeedBackOrderItems()){
			String goodsWeight=item.get("goodsWeight") == null?"":item.get("goodsWeight");
			String goodsNumber=item.get("goodsNumber") == null?"":item.get("goodsNumber");
			String goodsDescription=item.get("goodsDescription") == null?"":item.get("goodsDescription");
			if(goodsWeight!=null&&!goodsWeight.isEmpty()){
				WEIGHT+=Double.valueOf(goodsWeight);
			}
			if(goodsNumber!=null&&!goodsNumber.isEmpty()){
				PACK_NO+=Double.valueOf(goodsNumber);
			}
			if(goodsDescription!=null&&!goodsDescription.isEmpty()){
				GOODS_INFO+="/"+goodsDescription;
			}
		}
		GOODS_INFO=GOODS_INFO.replaceFirst("^/", "");
		resultMap.put("WEIGHT", WEIGHT);
		resultMap.put("NET_WEIGHT", WEIGHT);
		resultMap.put("PACK_NO", PACK_NO);
		resultMap.put("GOODS_INFO", GOODS_INFO);
		
		for(HashMap<String, String> customer:task.getFeedBackOrderCustomers()){
			String customerType=customer.get("customerType");
			if(customerType!=null){
				if("WE".equals(customerType)){
					resultMap.put("CONSIGNEE", customer.get("name") == null?"":customer.get("name"));
					resultMap.put("CONSIGNEE_ADDRESS", customer.get("address") == null?"":customer.get("address"));
					resultMap.put("CONSIGNEE_TELEPHONE", customer.get("fixedLineTelephone") == null?"":customer.get("fixedLineTelephone")+"/"+customer.get("mobilePhone") == null?"":customer.get("mobilePhone"));
					resultMap.put("CONSIGNEE_COUNTRY", customer.get("country") == null?"":customer.get("country"));
				}else if("AG".equals(customerType)){
					resultMap.put("SHIPPER", customer.get("name") == null?"":customer.get("name"));
					resultMap.put("SHIPPER_ADDRESS", customer.get("address") == null?"":customer.get("address"));
					resultMap.put("SHIPPER_TELEPHONE", customer.get("fixedLineTelephone") == null?"":customer.get("fixedLineTelephone")+"/"+customer.get("mobilePhone") == null?"":customer.get("mobilePhone"));
					resultMap.put("SHIPPER_COUNTRY", customer.get("country") == null?"":customer.get("country"));
				}
			}
		}
		return resultMap;
	}
}
