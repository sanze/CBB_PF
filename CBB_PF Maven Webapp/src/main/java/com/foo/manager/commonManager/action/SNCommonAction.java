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
	
	public void setFuzzy(String fuzzy){
		params.put("Fuzzy", fuzzy);
	}

}
