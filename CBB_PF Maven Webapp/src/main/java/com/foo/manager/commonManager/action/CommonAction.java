package com.foo.manager.commonManager.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

import com.foo.IService.ICommonManagerService;
import com.foo.abstractAction.AbstractAction;
import com.foo.common.CommonDefine;
import com.foo.common.CommonException;
import com.foo.common.IMethodLog;

public class CommonAction extends AbstractAction{

	@Resource
	public ICommonManagerService commonManagerService;

	private File uploadFile;

	@IMethodLog(desc = "商品列表查询")
	public String getAllSkus(){
		try {
			Map<String,Object> data = commonManagerService.getAllSkus(params);
			resultObj = JSONObject.fromObject(data);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}
	@IMethodLog(desc = "商品保存/提交")
	public String setSku(){
		try {
			String editType="add";
			if(params.get("editType")!=null){
				editType=""+params.get("editType");
				params.remove("editType");
			}
			if("mod".equals(editType)){
				commonManagerService.setSku(params,false);
			}else{
				commonManagerService.addSku(params);
			}
			Map<String,Object> data = new HashMap<String, Object>();
			data.put("success", true);
			//data.put("msg", "");
			resultObj = JSONObject.fromObject(data);
		} catch (CommonException e) {
			Map<String,Object> data = new HashMap<String, Object>();
			data.put("success", false);
			data.put("msg", e.getErrorMessage());
//			result.setReturnResult(CommonDefine.FAILED);
//			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(data);
		}
		return RESULT_OBJ;
	}
	@IMethodLog(desc = "商品删除")
	public String delSku(){
		try {
			commonManagerService.delSku(params);
			result.setReturnResult(CommonDefine.SUCCESS);
			resultObj = JSONObject.fromObject(result);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}
	
	/**
	 * 获取codeName
	 * @return
	 */
	public String getAllCodeNames(){
		try {
			Map<String,Object> data = commonManagerService.getAllCodeNames(params);
			resultObj = JSONObject.fromObject(data);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}
	
	/**
	 * 获取codeName所有类别
	 * @return
	 */
	public String getAllCodeCategory(){
		try {
			List<Map> dataList = commonManagerService.getAllCodeCategory();
			// 将返回的结果转成JSON对象，返回前台
			Map data = new HashMap();
			data.put("rows", dataList);
			resultObj = JSONObject.fromObject(data);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}
	
	/**
	 * 获取codeName所有类别
	 * @return
	 */
	public String getCodeCategory(){
		try {
			String relationCategory = params.get("relationCategory").toString();

			List<Map<String, Object>> dataList = commonManagerService.getCodeCategory(relationCategory);
			// 将返回的结果转成JSON对象，返回前台
			Map data = new HashMap();
			data.put("rows", dataList);
			resultObj = JSONObject.fromObject(data);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}
	
	
	/**
	 * 新增codenames数据
	 * @return
	 */
	public String addCodeName(){
		try {
			JSONObject jsonObject = JSONObject.fromObject(jsonString);
			
			Map data = (Map) jsonObject.toBean(jsonObject, Map.class);
			//检查数据
			Map checkData = new HashMap();
			checkData.put("RELATION_CATEGORY", data.get("RELATION_CATEGORY"));
			checkData.put("CODE", data.get("CODE"));
			
			if(commonManagerService.checkCodeName(checkData)){
				//新增数据
				commonManagerService.addCodeName(data);
				result.setReturnResult(CommonDefine.SUCCESS);
			}else{
				result.setReturnResult(CommonDefine.FAILED);
				result.setReturnMessage("此类别下代号重复，请重新输入！");
			}
			resultObj = JSONObject.fromObject(result);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}

	/**
	 * 更新codenames数据
	 * @return
	 */
	public String modifyCodeName(){
		try {
			JSONArray jsonObject = JSONArray.fromObject(jsonString);
			
			List dataList = jsonObject.toList(jsonObject, Map.class);

			commonManagerService.modifyCodeName(dataList);
			
			result.setReturnResult(CommonDefine.SUCCESS);
			resultObj = JSONObject.fromObject(result);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}
	
	
	/**
	 * 更新codenames数据
	 * @return
	 */
	public String checkCodeName(){
		try {
			JSONObject jsonObject = JSONObject.fromObject(jsonString);
			
			Map data = (Map) jsonObject.toBean(jsonObject, Map.class);

			boolean xx = commonManagerService.checkCodeName(data);
			
			if(xx){
				result.setReturnResult(CommonDefine.SUCCESS);
			}else{
				result.setReturnResult(CommonDefine.FAILED);
			}
			resultObj = JSONObject.fromObject(result);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}

	/**
	 * 获取codeName
	 * @return
	 */
	public String getAllFilePath(){
		try {
			Map<String,Object> data = commonManagerService.getAllFilePath(params);
			resultObj = JSONObject.fromObject(data);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}
	
	/**
	 * 更新filePath数据
	 * @return
	 */
	public String modifyFilePath(){
		try {
			JSONArray jsonObject = JSONArray.fromObject(jsonString);
			
			List dataList = jsonObject.toList(jsonObject, Map.class);

			commonManagerService.modifyFilePath(dataList);
			
			result.setReturnResult(CommonDefine.SUCCESS);
			resultObj = JSONObject.fromObject(result);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}
	
	/**
	 * 获取codeName
	 * @return
	 */
	public String getAllContact(){
		try {
			Map<String,Object> data = commonManagerService.getAllContact(params);
			resultObj = JSONObject.fromObject(data);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}
	
	/**
	 * 新增codenames数据
	 * @return
	 */
	public String addContact(){
		try {
			JSONObject jsonObject = JSONObject.fromObject(jsonString);
			
			Map data = (Map) jsonObject.toBean(jsonObject, Map.class);
			//检查数据
			Map checkData = new HashMap();
			checkData.put("CONTACT_ID", data.get("CONTACT_ID"));
			
			if(commonManagerService.checkContact(checkData)){
				//新增数据
				commonManagerService.addContact(data);
				result.setReturnResult(CommonDefine.SUCCESS);
			}else{
				result.setReturnResult(CommonDefine.FAILED);
				result.setReturnMessage("ID重复，请重新输入！");
			}
			resultObj = JSONObject.fromObject(result);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}
	
	/**
	 * 更新filePath数据
	 * @return
	 */
	public String modifyContact(){
		try {
			JSONArray jsonObject = JSONArray.fromObject(jsonString);
			
			List dataList = jsonObject.toList(jsonObject, Map.class);

			commonManagerService.modifyContact(dataList);
			
			result.setReturnResult(CommonDefine.SUCCESS);
			resultObj = JSONObject.fromObject(result);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}
	
	/**
	 * 更新filePath数据
	 * @return
	 */
	public String deleteContact(){
		try {
			JSONArray jsonObject = JSONArray.fromObject(jsonString);
			
			List dataList = jsonObject.toList(jsonObject, Map.class);

			commonManagerService.deleteContact(dataList);
			
			result.setReturnResult(CommonDefine.SUCCESS);
			resultObj = JSONObject.fromObject(result);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}

	
	
	@IMethodLog(desc = "订单保存/提交")
	public String setOrder(){
		try {
			String editType="add";
			if(params.get("editType")!=null){
				editType=""+params.get("editType");
				params.remove("editType");
			}
			if("mod".equals(editType)){
				commonManagerService.setOrder(params,false);
			}else{
				commonManagerService.addOrder(params);
			}
			Map<String,Object> data = new HashMap<String, Object>();
			data.put("success", true);
			resultObj = JSONObject.fromObject(data);
		} catch (CommonException e) {
			Map<String,Object> data = new HashMap<String, Object>();
			data.put("success", false);
			data.put("msg", e.getErrorMessage());
			resultObj = JSONObject.fromObject(data);
		}
		return RESULT_OBJ;
	}
	@IMethodLog(desc = "订单列表查询")
	public String getAllOrders(){
		try {
			Map<String,Object> data = commonManagerService.getAllOrders(params);
			JsonConfig jsonConfig = new JsonConfig();
		    PropertyFilter filter = new PropertyFilter() {
		            public boolean apply(Object object, String fieldName, Object fieldValue) {
		            	return null == fieldValue;
		            }
		    };
		    jsonConfig.setJsonPropertyFilter(filter);
			resultObj = JSONObject.fromObject(data,jsonConfig);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}
	@IMethodLog(desc = "订单删除")
	public String delOrder(){
		try {
			commonManagerService.delOrder(params);
			result.setReturnResult(CommonDefine.SUCCESS);
			resultObj = JSONObject.fromObject(result);
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
			Map<String,Object> data = commonManagerService.getAllLogisticses(params);

			JsonConfig jsonConfig = new JsonConfig();
		    PropertyFilter filter = new PropertyFilter() {
		            public boolean apply(Object object, String fieldName, Object fieldValue) {
		            	return null == fieldValue;
		            }
		    };
		    jsonConfig.setJsonPropertyFilter(filter);
			resultObj = JSONObject.fromObject(data,jsonConfig);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}
	@IMethodLog(desc = "运单保存/提交/状态设置")
	public String setLogistics(){
		try {
			String editType="add";
			if(params.get("editType")!=null){
				editType=""+params.get("editType");
				params.remove("editType");
			}
			params.remove("EBP_CODE");
			if("modStatus".equals(editType)){
				commonManagerService.setLogistics(params,true);
			}else if("mod".equals(editType)){
				commonManagerService.setLogistics(params,false);
			}else{
				commonManagerService.addLogistics(params);
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
			commonManagerService.delLogistics(params);
			result.setReturnResult(CommonDefine.SUCCESS);
			resultObj = JSONObject.fromObject(result);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}
	
	@IMethodLog(desc = "清单列表查询")
	public String getAllInventorys(){
		try {
			Map<String,Object> data = commonManagerService.getAllInventorys(params);
			resultObj = JSONObject.fromObject(data);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}
	@IMethodLog(desc = "清单删除")
	public String delInventory(){
		try {
			commonManagerService.delInventory(params);
			result.setReturnResult(CommonDefine.SUCCESS);
			resultObj = JSONObject.fromObject(result);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}
	@IMethodLog(desc = "清单保存/提交")
	public String setInventory(){
		try {
			String editType="add";
			if(params.get("editType")!=null){
				editType=""+params.get("editType");
				params.remove("editType");
			}
			if("mod".equals(editType)){
				commonManagerService.setInventory(params,false);
			}else{
				commonManagerService.addInventory(params);
			}
			Map<String,Object> data = new HashMap<String, Object>();
			data.put("success", true);
			resultObj = JSONObject.fromObject(data);
		} catch (CommonException e) {
			Map<String,Object> data = new HashMap<String, Object>();
			data.put("success", false);
			data.put("msg", e.getErrorMessage());
			resultObj = JSONObject.fromObject(data);
		}
		return RESULT_OBJ;
	}
	
	
	@IMethodLog(desc = "批量提交运单")
	public String batchSubmit_logistics(){
		try {
			commonManagerService.batchSubmit_LOGISTICS(params);
			result.setReturnResult(CommonDefine.SUCCESS);
			resultObj = JSONObject.fromObject(result);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}
	
	@IMethodLog(desc = "批量提交运单状态")
	public String batchSubmit_logisticsStatus(){
		try {
			commonManagerService.batchSubmit_LOGISTICS_STATUS(params);
			result.setReturnResult(CommonDefine.SUCCESS);
			resultObj = JSONObject.fromObject(result);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}
	
	@IMethodLog(desc = "批量申请快递单号")
	public String applyExpressNo(){
		try {
			commonManagerService.applyExpressNo_LOGISTICS(params);
			result.setReturnResult(CommonDefine.SUCCESS);
			resultObj = JSONObject.fromObject(result);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}
	
	/**
	 * 将文件上传至服务器
	 * 
	 * @return
	 */
	@IMethodLog(desc = "上传商品/运单数据")
	public String importFile() {
		params.put("file", uploadFile);
		try {
			commonManagerService.importFile(params);
			result.setReturnResult(CommonDefine.SUCCESS);
			resultObj = JSONObject.fromObject(result);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}
	
	public void setRELATION_CATEGORY(String RELATION_CATEGORY){
		params.put("RELATION_CATEGORY", RELATION_CATEGORY);
	}
	public void setEditType(String editType){
		params.put("editType", editType);
	}
	public void setSKU_ID(Integer SKU_ID){
		params.put("SKU_ID", SKU_ID);
	}
	public void setGUID(String GUID){
		params.put("GUID", GUID);
	}
	public void setCUSTOM_CODE(String CUSTOM_CODE){
		params.put("CUSTOM_CODE", CUSTOM_CODE);
	}
	public void setAPP_TYPE(Integer APP_TYPE){
		params.put("APP_TYPE", APP_TYPE);
	}
	public void setAPP_TIME(String APP_TIME){
		params.put("APP_TIME", APP_TIME);
	}
	public void setAPP_STATUS(Integer APP_STATUS){
		params.put("APP_STATUS", APP_STATUS);
	}
	public void setAPP_UID(String APP_UID){
		params.put("APP_UID", APP_UID);
	}
	/*public void setAPP_UNAME(String APP_UNAME){
		params.put("APP_UNAME", APP_UNAME);
	}*/
	public void setPRE_NO(String PRE_NO){
		params.put("PRE_NO", PRE_NO);
	}
	public void setEBP_CODE(String EBP_CODE){
		params.put("EBP_CODE", EBP_CODE);
	}
	/*public void setEBP_NAME(String EBP_NAME){
		params.put("EBP_NAME", EBP_NAME);
	}*/
	public void setEBC_CODE(String EBC_CODE){
		params.put("EBC_CODE", EBC_CODE);
	}
	/*public void setEBC_NAME(String EBC_NAME){
		params.put("EBC_NAME", EBC_NAME);
	}*/
	public void setAGENT_CODE(String AGENT_CODE){
		params.put("AGENT_CODE", AGENT_CODE);
	}
	/*public void setAGENT_NAME(String AGENT_NAME){
		params.put("AGENT_NAME", AGENT_NAME);
	}*/
	public void setCLASSIFY_CODE(String CLASSIFY_CODE){
		params.put("CLASSIFY_CODE", CLASSIFY_CODE);
	}
	/*public void setCLASSIFY_NAME(String CLASSIFY_NAME){
		params.put("CLASSIFY_NAME", CLASSIFY_NAME);
	}*/
	public void setITEM_NO(String ITEM_NO){
		params.put("ITEM_NO", ITEM_NO);
	}
	public void setITEM_NAME(String ITEM_NAME){
		params.put("ITEM_NAME", ITEM_NAME);
	}
	public void setG_NO(String G_NO){
		params.put("G_NO", G_NO);
	}
	public void setG_CODE(String G_CODE){
		params.put("G_CODE", G_CODE);
	}
	public void setG_NAME(String G_NAME){
		params.put("G_NAME", G_NAME);
	}
	public void setG_MODEL(String G_MODEL){
		params.put("G_MODEL", G_MODEL);
	}
	public void setBAR_CODE(String BAR_CODE){
		params.put("BAR_CODE", BAR_CODE);
	}
	public void setBRAND(String BRAND){
		params.put("BRAND", BRAND);
	}
	public void setTAX_CODE(String TAX_CODE){
		params.put("TAX_CODE", TAX_CODE);
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
	public void setPRICE(Double PRICE){
		params.put("PRICE", PRICE);
	}
	public void setCURRENCY(String CURRENCY){
		params.put("CURRENCY", CURRENCY);
	}
	public void setGIFT_FLAG(Integer GIFT_FLAG){
		params.put("GIFT_FLAG", GIFT_FLAG);
	}
	public void setNOTE(String NOTE){
		params.put("NOTE", NOTE);
	}
	public void setRETURN_STATUS(Integer RETURN_STATUS){
		params.put("RETURN_STATUS", RETURN_STATUS);
	}
	
	//电子订单
	public void setORDERS_ID(Integer ORDERS_ID){
		params.put("ORDERS_ID", ORDERS_ID);
	}
	@SuppressWarnings("rawtypes")
	public void setGOODSList(String[] GOODSList){
		List<Map> detailMaps=new ArrayList<Map>();
		for(String item:GOODSList){
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
		params.put("GOODSList", detailMaps);
	}
	public void setORDER_NO(String ORDER_NO){
		params.put("ORDER_NO", ORDER_NO);
	}
	public void setGOODS_VALUE(Double GOODS_VALUE){
		params.put("GOODS_VALUE", GOODS_VALUE);
	}
	public void setFREIGHT(Double FREIGHT){
		params.put("FREIGHT", FREIGHT);
	}
	public void setCONSIGNEE(String CONSIGNEE){
		params.put("CONSIGNEE", CONSIGNEE);
	}
	public void setCONSIGNEE_ADDRESS(String CONSIGNEE_ADDRESS){
		params.put("CONSIGNEE_ADDRESS", CONSIGNEE_ADDRESS);
	}
	public void setCONSIGNEE_TELEPHONE(String CONSIGNEE_TELEPHONE){
		params.put("CONSIGNEE_TELEPHONE", CONSIGNEE_TELEPHONE);
	}
	public void setCONSIGNEE_COUNTRY(String CONSIGNEE_COUNTRY){
		params.put("CONSIGNEE_COUNTRY", CONSIGNEE_COUNTRY);
	}
	
	//物流运单
	public void setLOGISTICS_ID(Integer LOGISTICS_ID){
		params.put("LOGISTICS_ID", LOGISTICS_ID);
	}
	public void setLOGISTICS_CODE(String LOGISTICS_CODE){
		params.put("LOGISTICS_CODE", LOGISTICS_CODE);
	}
	public void setLOGISTICS_NO(String LOGISTICS_NO){
		params.put("LOGISTICS_NO", LOGISTICS_NO);
	}
	public void setLOGISTICS_STATUS(String LOGISTICS_STATUS){
		params.put("LOGISTICS_STATUS", LOGISTICS_STATUS);
	}
	public void setIE_FLAG(String IE_FLAG){
		params.put("IE_FLAG", IE_FLAG);
	}
	public void setTRAF_MODE(String TRAF_MODE){
		params.put("TRAF_MODE", TRAF_MODE);
	}
	public void setSHIP_NAME(String SHIP_NAME){
		params.put("SHIP_NAME", SHIP_NAME);
	}
	public void setVOYAGE_NO(String VOYAGE_NO){
		params.put("VOYAGE_NO", VOYAGE_NO);
	}
	public void setBILL_NO(String BILL_NO){
		params.put("BILL_NO", BILL_NO);
	}
	public void setINSURE_FEE(Double INSURE_FEE){
		params.put("INSURE_FEE", INSURE_FEE);
	}
	public void setWEIGHT(Double WEIGHT){
		params.put("WEIGHT", WEIGHT);
	}
	public void setNET_WEIGHT(Double NET_WEIGHT){
		params.put("NET_WEIGHT", NET_WEIGHT);
	}
	public void setPACK_NO(Integer PACK_NO){
		params.put("PACK_NO", PACK_NO);
	}
	public void setPARCEL_INFO(String PARCEL_INFO){
		params.put("PARCEL_INFO", PARCEL_INFO);
	}
	public void setGOODS_INFO(String GOODS_INFO){
		params.put("GOODS_INFO", GOODS_INFO);
	}
	public void setSHIPPER(String SHIPPER){
		params.put("SHIPPER", SHIPPER);
	}
	public void setSHIPPER_ADDRESS(String SHIPPER_ADDRESS){
		params.put("SHIPPER_ADDRESS", SHIPPER_ADDRESS);
	}
	public void setSHIPPER_TELEPHONE(String SHIPPER_TELEPHONE){
		params.put("SHIPPER_TELEPHONE", SHIPPER_TELEPHONE);
	}
	public void setSHIPPER_COUNTRY(String SHIPPER_COUNTRY){
		params.put("SHIPPER_COUNTRY", SHIPPER_COUNTRY);
	}
	
	public void setIN_USE(Boolean IN_USE){
		params.put("IN_USE", IN_USE);
	}
	
	//清单
	public void setINVENTORY_ID(Integer INVENTORY_ID){
		params.put("INVENTORY_ID", INVENTORY_ID);
	}
	public void setCOP_NO(String COP_NO){
		params.put("COP_NO", COP_NO);
	}
	public void setPORT_CODE(String PORT_CODE){
		params.put("PORT_CODE", PORT_CODE);
	}
	public void setIE_DATE(String IE_DATE){
		try{
			SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");
			Date date = df.parse(IE_DATE);
			params.put("IE_DATE", date);
		}catch (Exception e) {
		}
	}
	public void setOWNER_CODE(String OWNER_CODE){
		params.put("OWNER_CODE", OWNER_CODE);
	}
	public void setLOCT_NO(String LOCT_NO){
		params.put("LOCT_NO", LOCT_NO);
	}
	public void setLICENSE_NO(String LICENSE_NO){
		params.put("LICENSE_NO", LICENSE_NO);
	}
	public void setCOUNTRY(String COUNTRY){
		params.put("COUNTRY", COUNTRY);
	}
	public void setDESTINATION_PORT(String DESTINATION_PORT){
		params.put("DESTINATION_PORT", DESTINATION_PORT);
	}
	public void setFREIGHT_CURR(String FREIGHT_CURR){
		params.put("FREIGHT_CURR", FREIGHT_CURR);
	}
	public void setFREIGHT_MARK(Integer FREIGHT_MARK){
		params.put("FREIGHT_MARK", FREIGHT_MARK);
	}
	public void setINSURE_FEE_CURR(String INSURE_FEE_CURR){
		params.put("INSURE_FEE_CURR", INSURE_FEE_CURR);
	}
	public void setINSURE_FEE_MARK(Integer INSURE_FEE_MARK){
		params.put("INSURE_FEE_MARK", INSURE_FEE_MARK);
	}
	public void setWRAP_TYPE(String WRAP_TYPE){
		params.put("WRAP_TYPE", WRAP_TYPE);
	}
	public void setORDER_TYPE(String ORDER_TYPE){
		params.put("ORDER_TYPE", ORDER_TYPE);
	}
	public void setLOGISTICS_ORDER_ID(String LOGISTICS_ORDER_ID){
		params.put("LOGISTICS_ORDER_ID", LOGISTICS_ORDER_ID);
	}
	
	public void setRelationCategory(String relationCategory){
		params.put("relationCategory", relationCategory);
	}
	
	public File getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}
	
	public void setGuidList(List<String> guidList){
		params.put("guidList", guidList);
	}
	
	public void setFuzzy(String fuzzy){
		params.put("Fuzzy", fuzzy);
	}
	
	/*public void set(String ){
		params.put("", );
	}*/
}
