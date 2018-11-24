package com.foo.manager.commonManager.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.foo.IService.ISNCommonManagerService;
import com.foo.abstractAction.AbstractAction;
import com.foo.common.CommonDefine;
import com.foo.common.CommonException;
import com.foo.common.IMethodLog;

public class SNCommonAction extends AbstractAction{

	@Resource
	public ISNCommonManagerService snCommonManagerService;

	@IMethodLog(desc = "SN装载单查询")
	public String getAllLoads(){
		try {
			Map<String,Object> data = snCommonManagerService.getAllLoads(params);
			resultObj = JSONObject.fromObject(data);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}
	
	@IMethodLog(desc = "SN装载单状态更新")
	public String modify(){
		try {
			JSONArray jsonObject = JSONArray.fromObject(jsonString);
			
			List dataList = jsonObject.toList(jsonObject, Map.class);

			snCommonManagerService.modify(dataList);
			
			result.setReturnResult(CommonDefine.SUCCESS);
			resultObj = JSONObject.fromObject(result);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}
	
	@IMethodLog(desc = "SN装载单状态发送")
	public String send(){
		try {
			JSONArray jsonObject = JSONArray.fromObject(jsonString);
			
			List dataList = jsonObject.toList(jsonObject, Map.class);

			snCommonManagerService.send(dataList);
			
			result.setReturnResult(CommonDefine.SUCCESS);
			resultObj = JSONObject.fromObject(result);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}
	
	
	
	@IMethodLog(desc = "SN商品查询")
	public String getAllSku(){
		try {
			Map<String,Object> data = snCommonManagerService.getAllSku(params);
			resultObj = JSONObject.fromObject(data);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}
	
	@IMethodLog(desc = "SN入库单查询")
	public String getAllReceipt(){
		try {
			Map<String,Object> data = snCommonManagerService.getAllReceipt(params);
			resultObj = JSONObject.fromObject(data);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}
	
	@IMethodLog(desc = "SN出库单查询")
	public String getAllInventory(){
		try {
			Map<String,Object> data = snCommonManagerService.getAllInventory(params);
			resultObj = JSONObject.fromObject(data);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}
	
	public void setLOAD_ID(String LOAD_ID){
		params.put("LOAD_ID", LOAD_ID);
	}
	
	public void setLOAD_NO(String LOAD_NO){
		params.put("LOAD_NO", LOAD_NO);
	}
	public void setCAR_NO(String CAR_NO){
		params.put("CAR_NO", CAR_NO);
	}
	
	public void setCREAT_DATE(String CREAT_DATE){
		params.put("CREAT_DATE", CREAT_DATE);
	}
	
	public void setSTATUS(String STATUS){
		params.put("STATUS", STATUS);
	}
	
	public void setITEM_NO(String ITEM_NO){
		params.put("ITEM_NO", ITEM_NO);
	}
	
	public void setG_CODE(String G_CODE){
		params.put("G_CODE", G_CODE);
	}
	
	public void setSJ_COUNTRY(String SJ_COUNTRY){
		params.put("SJ_COUNTRY", SJ_COUNTRY);
	}
	
	public void setUNIT(String UNIT){
		params.put("UNIT", UNIT);
	}
	
	
	public void setUNIT1(String UNIT1){
		params.put("UNIT1", UNIT1);
	}
	
	public void setUNIT2(String UNIT2){
		params.put("UNIT2", UNIT2);
	}
	
	public void setSKU(String SKU){
		params.put("SKU", SKU);
	}
	
	public void setRECEIPT_NO(String RECEIPT_NO){
		params.put("RECEIPT_NO", RECEIPT_NO);
	}
	
	public void setORDER_NO(String ORDER_NO){
		params.put("ORDER_NO", ORDER_NO);
	}
	
	public void setITEM_NUMBER(String ITEM_NUMBER){
		params.put("ITEM_NUMBER", ITEM_NUMBER);
	}
	
	public void setFuzzy(String fuzzy){
		params.put("Fuzzy", fuzzy);
	}

}
