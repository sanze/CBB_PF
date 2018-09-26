package com.foo.manager.commonManager.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

import com.foo.IService.ISuningManagerService;
import com.foo.common.CommonDefine;
import com.foo.common.CommonException;
import com.foo.common.IMethodLog;
import com.foo.common.MessageCodeDefine;

public class SuningAction extends CommonAction{

	@Resource
	public ISuningManagerService suningManagerService;
	
	@IMethodLog(desc = "运单任务查询")
	public String getAllLogisticsTasks(){
		try {
			Map<String,Object> data = suningManagerService.getAllLogisticsTasks(params);
			resultObj = JSONObject.fromObject(data);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}
	@IMethodLog(desc = "运单列表查询")
	public String getAllLogisticses(){
		try {
			Map<String,Object> data = suningManagerService.getAllLogisticses(params);
			resultObj = JSONObject.fromObject(data);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@IMethodLog(desc = "运单保存/提交/状态设置")
	public String setLogistics(){
		try {
			String editType="add";
			if(params.get("editType")!=null){
				editType=""+params.get("editType");
				params.remove("editType");
			}
			if("modStatus".equals(editType)){
				suningManagerService.setLogistics(params,true);
			}else if("mod".equals(editType)){
				suningManagerService.setLogistics(params,false);
			}else{
				List<Map> taskList=(List<Map>)params.get("logisticsTaskList");
				if(taskList==null||taskList.isEmpty()){
					throw new CommonException(null, MessageCodeDefine.COM_EXCPT_INVALID_INPUT);
				}else{
					String msg="";
					for(Map task:taskList){
						try{
							suningManagerService.addLogistics(task);
						}catch (CommonException e) {
							msg+="订单"+task.get("ORDER_NO")+"："+e.getErrorMessage()+"\r\n";
						}
					}
					if(msg!=null&&!msg.isEmpty()){
						msg=msg.replaceAll("\r\n$", "");
						throw new CommonException(null, MessageCodeDefine.COM_EXCPT_PROCESSING_ERROR, msg);
					}
				}
			}
			Map<String,Object> data = new HashMap<String, Object>();
			data.put("success", true);
			//data.put("msg", "");
			resultObj = JSONObject.fromObject(data);
		} catch (CommonException e) {
			Map<String,Object> data = new HashMap<String, Object>();
			data.put("success", false);
			data.put("msg", e.getErrorMessage());
			data.put("returnResult", CommonDefine.FAILED);
			data.put("returnMessage", e.getErrorMessage());
//			result.setReturnResult(CommonDefine.FAILED);
//			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(data);
		}
		return RESULT_OBJ;
	}
	@IMethodLog(desc = "运单删除")
	public String delLogistics(){
		try {
			suningManagerService.delLogistics(params);
			result.setReturnResult(CommonDefine.SUCCESS);
			resultObj = JSONObject.fromObject(result);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}
	
	//苏宁运单任务
	public void setStartTime(String startTime){
		params.put("startTime", startTime);
	}
	public void setEndTime(String endTime){
		params.put("endTime", endTime);
	}
	@SuppressWarnings("rawtypes")
	public void setLogisticsTaskList(String[] taskList){
		List<Map> detailMaps=new ArrayList<Map>();
		for(String item:taskList){
			JsonConfig cfg = new JsonConfig();
			cfg.setRootClass(Map.class);
			cfg.setJavaPropertyFilter(new PropertyFilter() {
				@Override
				public boolean apply(Object source, String name, Object value) {
					return value == JSONNull.getInstance()||
							"".equals(value);
				}
			});
			Map itemMap=(Map)JSONObject.toBean(JSONObject.fromObject(item), cfg);
			detailMaps.add(itemMap);
		}
		params.put("logisticsTaskList", detailMaps);
	}
	
	/*public void set(String ){
		params.put("", );
	}*/
}
