package com.foo.manager.commonManager.serviceImpl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foo.IService.ICommonManagerService;
import com.foo.common.CommonDefine;
import com.foo.common.CommonException;
import com.foo.common.MessageCodeDefine;
import com.foo.handler.ExceptionHandler;
import com.foo.manager.commonManager.service.CommonManagerService;
import com.foo.util.CommonUtil;
import com.foo.util.ConfigUtil;
import com.foo.util.FtpUtils;
import com.foo.util.POIExcelUtil;
import com.foo.util.XmlUtil;

@Service
@Transactional(rollbackFor = Exception.class)
public class CommonManagerServiceImpl extends CommonManagerService implements ICommonManagerService{

	@Override
	public Map<String, Object> getDataList(Map<String, Object> params) throws CommonException {
		
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		
		int total = 0;
		try{
			
		String tableName = params.get("tableName").toString();
			
		// 开始
		Integer start = params.get("start") == null ? 0 : Integer
				.valueOf(params.get("start").toString());
		// 结束
		Integer limit = params.get("limit") == null ? null : Integer
				.valueOf(params.get("limit").toString());
		
		total = commonManagerMapper.selectTableCount(tableName);
		rows = commonManagerMapper.selectTable(tableName, start, limit);

		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("total", total);
		result.put("rows", rows);
		
		return result;
		} catch (Exception e) {
			throw new CommonException(e,
					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR);
		}
		
	}
	
	
	@Override
	public List<Map> getSubMenuList(Integer userId, int menuParentId, boolean needAuthCheck) throws CommonException {

		List<Map> menuList = getSubMenu(userId, menuParentId, needAuthCheck);
		
		return menuList;

	}

	/**
	 * @param userId
	 * @param menuParentId
	 * @return
	 */
	private List<Map> getSubMenu(Integer userId, int menuParentId, boolean needAuthCheck) {

		List<Map> nodes = new ArrayList<Map>();
		// 查询全部菜单项
		List<Map> allMenuList = commonManagerMapper
				.getAllSubMenuList(menuParentId);
		
		//查询权限菜单项
		List<Map> authMenuList = null;
		//判断是否需要权限检测
		if(needAuthCheck){
			authMenuList=commonManagerMapper.getAuthSubMenuList(menuParentId, userId);
		}
		if (allMenuList != null) {
			// 标示符，防止重复
			for (Map obj : allMenuList) {
				//权限过滤
				if(needAuthCheck){
					obj.put("DISABLED", true);
					for(Map auth:authMenuList){
						if(obj.get("SYS_MENU_ID").equals(auth.get("SYS_MENU_ID"))){
							obj.put("DISABLED", false);
						}
					}
				}else{
					//所有权限
					obj.put("DISABLED", false);
				}
				//指定菜单项变灰
				if(obj.get("MENU_HREF")!=null&&"DISABLED".equals(obj.get("MENU_HREF").toString())){
					obj.put("DISABLED", true);
				}
				nodes.add(obj);
				if (menuParentId != 0) {
					nodes.addAll(getSubMenu(userId, Integer.parseInt(obj.get(
							"SYS_MENU_ID").toString()),needAuthCheck));
				}else{
//					//目录菜单可用
//					obj.put("DISABLED", false);
				}
			}
		}
		return nodes;
	}

	/**
	 * 获取菜单集合--首页显示用
	 * 
	 * @param userId
	 * @param menuId
	 * @return
	 */
	public List<Map> getMenuList(List<Integer> menuIds) throws CommonException {
		// 查询菜单项
		List<Map> menuList = new ArrayList<Map>();
		try {
			// 查询菜单项
			menuList = commonManagerMapper.getMenuList(menuIds);
			if (menuList != null) {
				int pos = 0;
				while (pos < menuList.size()) {
					Map posMenu = menuList.get(pos);
					posMenu.put("DISABLED", false);
					for (int i = 0; i < pos; i++) {
						Map cosMenu = menuList.get(i);
					}
					++pos;
				}
			}
		} catch (Exception e) {
			throw new CommonException(e,
					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR);
		}
		return menuList;
	}

	@Override
	public Map<String, Object> getAllSkus(Map<String, Object> params)
			throws CommonException {
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

		int total = 0;

		// 开始
		Integer start = params.get("start") == null ? null : Integer
				.valueOf(params.get("start").toString());
		// 结束
		Integer limit = params.get("limit") == null ? null : Integer
				.valueOf(params.get("limit").toString());

		params.remove("start");
		params.remove("limit");
		try {
			List<String> keys=new ArrayList<String>(params.keySet());
			List<Object> values=new ArrayList<Object>(params.values());
			rows = commonManagerMapper.selectTableListByNVList("t_sku", 
					keys,values,start, limit);

			total = commonManagerMapper.selectTableListCountByNVList("t_sku",
					keys,values);

			Map<String, Object> result = new HashMap<String, Object>();
			result.put("total", total);
			result.put("rows", rows);
			return result;
		} catch (Exception e) {
			throw new CommonException(e,
					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR);
		}
	}

	@Override
	public void delSku(Map<String, Object> params) throws CommonException {
		try {
			commonManagerMapper.delTableById("t_sku", "SKU_ID",
					params.get("SKU_ID"));
		} catch (Exception e) {
			throw new CommonException(e,
					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR);
		}
	}

	@Override
	public void setSku(Map<String, Object> sku, boolean statusOnly)
			throws CommonException {
		try {
			String uniqueCol="ITEM_NO";
			String primaryCol="SKU_ID";
			// 货号唯一性校验
			uniqueCheck(T_SKU,uniqueCol,sku.get(uniqueCol),primaryCol,sku.get(primaryCol),false);
			
			sku.remove("editType");

			commonManagerMapper.updateTableByNVList(T_SKU, primaryCol,
					sku.get(primaryCol), new ArrayList<String>(sku.keySet()),
					new ArrayList<Object>(sku.values()));

			submitXml_SKU(sku);
		} catch (CommonException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommonException(e,
					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR);
		}
	}

	@Override
	public void addSku(Map<String, Object> sku) throws CommonException {
		try {
			String uniqueCol="ITEM_NO";
			String primaryCol="SKU_ID";
			// 货号唯一性校验
			uniqueCheck(T_SKU,uniqueCol,sku.get(uniqueCol),null,null,false);
			
			sku.remove("editType");
			// 设置空id
			sku.put(primaryCol, null);
			// 设置guid
			sku.put("GUID", CommonUtil.generalGuid(CommonDefine.GUID_FOR_SKU,7,T_SKU));
			// 设置创建时间
			sku.put("CREAT_TIME", new Date());

			Map primary=new HashMap();
			primary.put("primaryId", null);
			commonManagerMapper.insertTableByNVList(T_SKU,
					new ArrayList<String>(sku.keySet()), 
					new ArrayList<Object>(sku.values()),
					primary);

			submitXml_SKU(sku);
		} catch (CommonException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommonException(e,
					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR);
		}
	}

	// 生成xml文件
	public boolean submitXml_SKU(Map<String, Object> data) {
		// 提交需要生成xml文件
		if (Integer.valueOf(CommonDefine.APP_STATUS_UPLOAD).equals(
				data.get("APP_STATUS"))) {
			System.out.println("submitXml_SKU");
			
			//更新sku数据，添加申报时间
			//插入申报时间
			String currentTime = new SimpleDateFormat(
					CommonDefine.RETRIEVAL_TIME_FORMAT).format(new Date());
			data.put("APP_TIME", currentTime);
			commonManagerMapper.updateSku(data);
			
			//获取guid
			String guid = data.get("GUID").toString();
			//获取需要生成报文的数据
			data = commonManagerMapper.selectDataForMessage201(guid);

			File file = XmlUtil.generalXml(data, null, CommonDefine.CEB201);

			FtpUtils ftpUtil = FtpUtils.getDefaultFtp();

			String generalXmlFilePath = ConfigUtil
					.getFileLocationPath(CommonDefine.FILE_CATEGORY_SPBA)
					.get("GENERAL_XML").toString();

			boolean result = ftpUtil.uploadFile(file.getPath(),
					generalXmlFilePath, file.getName());

			if (result) {
				file.delete();
			}
			return result;
		}
		return true;
	}
	
	// 生成xml文件
	public boolean submitXml_ORDER(Map<String, Object> data, Integer orderId) {
		// 提交需要生成xml文件,
		if (Integer.valueOf(CommonDefine.APP_STATUS_UPLOAD).equals(
				data.get("APP_STATUS"))) {
			System.out.println("submitXml_ORDER");
			
			//更新order数据，添加申报时间
			//插入申报时间
			String currentTime = new SimpleDateFormat(
					CommonDefine.RETRIEVAL_TIME_FORMAT).format(new Date());
			data.put("APP_TIME", currentTime);
			commonManagerMapper.updateOrder(data);

			//获取guid
			String guid = data.get("GUID").toString();
			//获取需要生成报文的数据
			data = commonManagerMapper.selectDataForMessage301(guid);
			
			//获取订单详细信息
			List<Map> subDataList = commonManagerMapper.selectSubDataForMessage301(orderId);

			File file = XmlUtil.generalXml(data, subDataList, CommonDefine.CEB301);

			FtpUtils ftpUtil = FtpUtils.getDefaultFtp();

			String generalXmlFilePath = ConfigUtil
					.getFileLocationPath(CommonDefine.FILE_CATEGORY_DZDD)
					.get("GENERAL_XML").toString();

			boolean result = ftpUtil.uploadFile(file.getPath(),
					generalXmlFilePath, file.getName());

			if (result) {
				file.delete();
			}
			return result;
		}
		return true;
	}
	
	// 生成xml文件
	public boolean submitXml_INVENTORY(Map<String, Object> data, Integer invertoryId) {
		// 提交需要生成xml文件,
		if (Integer.valueOf(CommonDefine.APP_STATUS_UPLOAD).equals(
				data.get("APP_STATUS"))) {
			System.out.println("submitXml_INVENTORY");
			
			//更新数据，添加申报时间
			//插入申报时间
			String currentTime = new SimpleDateFormat(
					CommonDefine.RETRIEVAL_TIME_FORMAT).format(new Date());
			data.put("APP_TIME", currentTime);
			commonManagerMapper.updateInventory(data);

			//获取guid
			String guid = data.get("GUID").toString();
			//获取需要生成报文的数据
			data = commonManagerMapper.selectDataForMessage601(guid);
			
			//获取订单详细信息
			List<Map> subDataList = commonManagerMapper.selectSubDataForMessage601(invertoryId);

			File file = XmlUtil.generalXml(data, subDataList, CommonDefine.CEB601);

			FtpUtils ftpUtil = FtpUtils.getDefaultFtp();

			String generalXmlFilePath = ConfigUtil
					.getFileLocationPath(CommonDefine.FILE_CATEGORY_CJQD)
					.get("GENERAL_XML").toString();

			boolean result = ftpUtil.uploadFile(file.getPath(),
					generalXmlFilePath, file.getName());

			if (result) {
				file.delete();
			}
			return result;
		}
		return true;
	}
	
	
	@Override
	public void batchSubmit_LOGISTICS(Map<String, Object> params) throws CommonException {
		List<String> guidList = (List<String>) params.get("guidList");
		List<Map<String,Object>> logisticsList = null;
		Map logistics = null;
		String tableName=T_LOGISTICS;
		String primaryCol="LOGISTICS_ID";
		String uniqueCol="";
		StringBuilder errorMessage = new StringBuilder("");
		for(String guid:guidList){
			try{
				//获取运单信息
				logisticsList = commonManagerMapper.selectTableListByCol(tableName, "GUID", guid, null, null);
				if(logisticsList !=null && logisticsList.size() == 1){
					logistics = logisticsList.get(0);
				}else{
					errorMessage.append(guid+"：运单数据不存在！;<br/>");
					continue;
				}
				//数据完备性检查
				if(logistics.get(primaryCol) == null){
					errorMessage.append(guid+"：提交错误（字段"+primaryCol+"为空！）;<br/>");
					continue;
				}
				// 唯一性校验
				uniqueCol="LOGISTICS_NO";
				//数据完备性检查
				if(logistics.get(uniqueCol) == null){
					errorMessage.append(guid+"：提交错误（字段"+uniqueCol+"为空！）;<br/>");
					continue;
				}
				uniqueCheck(tableName,uniqueCol,logistics.get(uniqueCol),primaryCol,logistics.get(primaryCol),false);
				
				uniqueCol="ORDER_NO";
				//数据完备性检查
				if(logistics.get(uniqueCol) == null){
					errorMessage.append(guid+"：提交错误（字段"+uniqueCol+"为空！）;<br/>");
					continue;
				}
				uniqueCheck(tableName,uniqueCol,logistics.get(uniqueCol),primaryCol,logistics.get(primaryCol),false);
				
				Object primaryId=logistics.get(primaryCol);
				
				// 修改APP_STATUS为upload
				logistics.put("APP_STATUS", CommonDefine.APP_STATUS_UPLOAD);
				//直接设置为120
				logistics.put("RETURN_STATUS", CommonDefine.RETURN_STATUS_120);
				
				submitXml_LOGISTICS4Korea(logistics);
				//设置申报完成，修改APP_STATUS为upload
//				logistics.put("APP_STATUS", CommonDefine.APP_STATUS_COMPLETE);
//				commonManagerMapper.updateLogistics(logistics, tableName);
				
			}catch(CommonException e){
				errorMessage.append(guid+"：提交错误（"+e.getErrorMessage()+"）;<br/>");
			}
		
		}
		//判断是否有错误信息，如果有抛出异常
		if(!errorMessage.toString().isEmpty()){
			//抛出错误信息
			throw new CommonException(new Exception(),
					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR, errorMessage.toString());
		}
	}
	
	
	@Override
	public void batchSubmit_LOGISTICS_STATUS(Map<String, Object> params) throws CommonException {
		List<String> guidList = (List<String>) params.get("guidList");
		List<Map<String,Object>> logisticsList = null;
		Map logistics = null;
		String tableName=T_LOGISTICS;
		String primaryCol="LOGISTICS_ID";
		StringBuilder errorMessage = new StringBuilder("");

		for (String guid : guidList) {
			// 获取运单信息
			logisticsList = commonManagerMapper.selectTableListByCol(tableName,
					"GUID", guid, null, null);
			if (logisticsList != null && logisticsList.size() == 1) {
				logistics = logisticsList.get(0);
			} else {
				errorMessage.append(guid + "：运单数据不存在！;<br/>");
				continue;
			}
			List<String> keys = Arrays.asList(new String[] {
					"LOGISTICS_STATUS", "RETURN_STATUS", "RETURN_TIME",
					"RETURN_INFO" });
			List<Object> values = Arrays.asList(new Object[] {
					logistics.get("LOGISTICS_STATUS"), null, null, null });
			commonManagerMapper.updateTableByNVList(T_LOGISTICS, primaryCol,
					logistics.get(primaryCol), keys, values);
			Object primaryId = logistics.get(primaryCol);

			// 生成xml报文
			// 修改APP_STATUS为upload
			logistics.put("APP_STATUS", CommonDefine.APP_STATUS_UPLOAD);
			logistics.put("LOGISTICS_STATUS", params.get("LOGISTICS_STATUS"));
			
			submitXml_LOGISTICS(logistics,
					Integer.valueOf(primaryId.toString()), CommonDefine.CEB503,
					CommonDefine.LOGISTICS_TYPE_NORMAL);

		}
		//判断是否有错误信息，如果有抛出异常
		if(!errorMessage.toString().isEmpty()){
			//抛出错误信息
			throw new CommonException(new Exception(),
					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR, errorMessage.toString());
		}
	}

	@Override
	public void applyExpressNo_LOGISTICS(Map<String, Object> params) throws CommonException {
		List<String> guidList = (List<String>) params.get("guidList");

		// 提交需要生成xml文件
		System.out.println("applyExpressNo_LOGISTICS"+CommonDefine.APPLY_EMS_NO);
		
		Map<String,String> leafNods = new HashMap<String,String>();
		
		leafNods.put("sysAccount", CommonUtil.getSystemConfigProperty("sysAccount"));
		leafNods.put("passWord", CommonUtil.encryptionMD5((CommonUtil.getSystemConfigProperty("passWord"))));
		leafNods.put("appKey", CommonUtil.getSystemConfigProperty("appkey_ems"));
		leafNods.put("businessType", CommonUtil.getSystemConfigProperty("businessType"));
		leafNods.put("billNoAmount", String.valueOf(guidList.size()));
		
		
		//xml报文
		String resultXmlString = XmlUtil.generalCommonXml("XMLInfo", leafNods);
		//获取返回数据
		String soapResponseData = sendHttpCMD(resultXmlString,CommonDefine.APPLY_EMS_NO);
			//摘取出返回信息
/*		soapResponseData = 
	        "<response>"+
	            "<result>1</result>"+
	            "<errorDesc>3123</errorDesc>"+
				 "<errorCode>11231</errorCode>"+
				  "<assignIds><assignId><billno>9999999999999</billno></assignId></assignIds>"+
	        "</response>";*/
		if(soapResponseData == null ||soapResponseData.isEmpty()){
			//抛出错误信息
			throw new CommonException(new Exception(),
					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR, "无回执信息！");
		}
		Map<String,Object> response = XmlUtil.parseXmlStringForReceipt_EMS(soapResponseData);
		if(response!=null&&response.size()>0){
			//更新数据库，或上报异常
			if (response.get("result") != null
					&& CommonDefine.FAILED == Integer.valueOf(response.get(
							"result").toString())) {
				// 抛出错误信息
				throw new CommonException(new Exception(),
						MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR, "【"
								+ response.get("errorCode").toString() + "】"
								+ response.get("errorDesc").toString()+"！");
			}else {
				//更新数据
				List<String> assignIds = (List<String>) response.get("assignIds");
				
				if(assignIds.size()!=guidList.size()){
					//抛出错误信息
					throw new CommonException(new Exception(),
							MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR, "返回订单号数量与所选项目数量不一致！");
				}
				//更新数据
				for(int i = 0;i<assignIds.size();i++){
					Map logistics = new HashMap();
					logistics.put("GUID", guidList.get(i));
					logistics.put("PARCEL_INFO", assignIds.get(i));
					commonManagerMapper.updateLogistics(logistics, T_LOGISTICS);
				}
			}
		}else{
			//抛出错误信息
			throw new CommonException(new Exception(),
					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR, soapResponseData);
		}
	}
	
	@Override
	public void addCodeName(Map data) {
		data.put("CREAT_TIME", new Date());
		Map primary=new HashMap();
		primary.put("primaryId", null);
		commonManagerMapper.insertTableByNVList("t_code_name",
				new ArrayList<String>(data.keySet()), 
				new ArrayList<Object>(data.values()),
				primary);
	}

	@Override
	public void modifyCodeName(List<Map> dataList) {
		// 更新数据
		for (Map data : dataList) {
			commonManagerMapper.updateTableByNVList("t_code_name",
					"CODE_NAME_ID", data.get("CODE_NAME_ID"),
					new ArrayList<String>(data.keySet()),
					new ArrayList<Object>(data.values()));
		}
	}

	@Override
	public boolean checkCodeName(Map data) throws CommonException {
		
		int count  = commonManagerMapper.selectTableListCountByNVList("t_code_name", new ArrayList<String>(data.keySet()),
				new ArrayList<Object>(data.values()));
		
		return !(count>0);
	}

	@Override
	public void setOrder(Map<String, Object> order, boolean statusOnly)
			throws CommonException {
		try {
			String uniqueCol="ORDER_NO";
			String primaryCol="ORDERS_ID";
			// 唯一性校验
			uniqueCheck(T_ORDERS,uniqueCol,order.get(uniqueCol),primaryCol,order.get(primaryCol),false);
			
			order.remove("editType");
			
			Object orderNo = order.get("ORDER_NO");
			List<Map> GOODSList=(List<Map>)order.get("GOODSList");
			order.remove("GOODSList");
			
			commonManagerMapper.updateTableByNVList(T_ORDERS, primaryCol,
					order.get(primaryCol), new ArrayList<String>(order.keySet()),
					new ArrayList<Object>(order.values()));

			Object primaryId=order.get(primaryCol);
			setGoodsList(GOODSList,orderNo,primaryId);
			//生成xml报文
			submitXml_ORDER(order,Integer.valueOf(primaryId.toString()));
		} catch (CommonException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommonException(e,
					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR);
		}
	}
	@Override
	public void addOrder(Map<String, Object> order) throws CommonException {
		try {
			String uniqueCol="ORDER_NO";
			String primaryCol="ORDERS_ID";
			// 唯一性校验
			uniqueCheck(T_ORDERS,uniqueCol,order.get(uniqueCol),null,null,false);
			
			order.remove("editType");
			// 设置空id
			order.put(primaryCol, null);
			
			// 设置guid
			order.put("GUID", CommonUtil.generalGuid(CommonDefine.GUID_FOR_ORDER,5,T_ORDERS));
			// 设置创建时间
			order.put("CREAT_TIME", new Date());
			
			Object orderNo = order.get("ORDER_NO");
			List<Map> GOODSList=(List<Map>)order.get("GOODSList");
			order.remove("GOODSList");
			
			Map primary=new HashMap();
			primary.put("primaryId", null);
			commonManagerMapper.insertTableByNVList(T_ORDERS,
					new ArrayList<String>(order.keySet()), 
					new ArrayList<Object>(order.values()),
					primary);
			
			Object primaryId=primary.get("primaryId");
			setGoodsList(GOODSList,orderNo,primaryId);
			//生成xml报文
			submitXml_ORDER(order,Integer.valueOf(primaryId.toString()));
		} catch (CommonException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommonException(e,
					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR);
		}
	}
	public void setGoodsList(List<Map> GOODSList,Object orderNo, Object ordersId){
		commonManagerMapper.delTableById("t_order_detail", "ORDERS_ID", ordersId);
		Date createTime=new Date();
		for(int i=0;i<GOODSList.size();i++){
			GOODSList.get(i).put("PRICE", GOODSList.get(i).get("ONE_PRICE"));
			GOODSList.get(i).put("NOTE", GOODSList.get(i).get("NOTE_OD"));
			Map good=new HashMap();
			good.put("ORDERS_ID", ordersId);
			good.put("ORDER_NO", orderNo);
			good.put("GNUM", i+1);
			good.put("CREAT_TIME", createTime);
			String[] copyColumns=new String[]{"ITEM_NO","QTY","PRICE","TOTAL","NOTE"};
			for(String col:copyColumns){
				good.put(col, GOODSList.get(i).get(col));
			}
			Map primary=new HashMap();
			primary.put("primaryId", null);
			commonManagerMapper.insertTableByNVList("t_order_detail",
					new ArrayList<String>(good.keySet()), 
					new ArrayList<Object>(good.values()),
					primary);
		}
	}
	@Override
	public Map<String, Object> getAllOrders(Map<String, Object> params)
			throws CommonException {
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

		int total = 0;

		// 开始
		Integer start = params.get("start") == null ? null : Integer
				.valueOf(params.get("start").toString());
		// 结束
		Integer limit = params.get("limit") == null ? null : Integer
				.valueOf(params.get("limit").toString());

		params.remove("start");
		params.remove("limit");
		try {
			String tableName = T_ORDERS;
			if(params.get("IN_USE")!=null){
				if(Boolean.FALSE.equals(params.get("IN_USE"))){
					tableName = V_ORDERS_UNUSE;
				}
				params.remove("IN_USE");
			}
			List<String> keys=new ArrayList<String>(params.keySet());
			List<Object> values=new ArrayList<Object>(params.values());
			rows = commonManagerMapper.selectTableListByNVList(tableName, 
					keys,values,start, limit);
			total = commonManagerMapper.selectTableListCountByNVList(tableName,
					keys,values);
			
			for(Map<String, Object> row:rows){
				row.put("GOODSList", getGoodsList(row));
			}

			Map<String, Object> result = new HashMap<String, Object>();
			result.put("total", total);
			result.put("rows", rows);
			return result;
		} catch (Exception e) {
			throw new CommonException(e,
					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR);
		}
	}
	public List<Map<String, Object>> getGoodsList(Map<String, Object> order){
		List<Map<String, Object>> goods=commonManagerMapper.selectTableListByCol("t_order_detail", "ORDERS_ID", order.get("ORDERS_ID"), null, null);
		for(Map<String, Object> good:goods){
			good.put("ONE_PRICE", good.get("PRICE"));
			good.remove("PRICE");
			good.put("NOTE_OD", good.get("NOTE"));
			good.remove("NOTE");
			List<Map<String, Object>> skus=commonManagerMapper.selectTableListByCol("t_sku", "ITEM_NO", good.get("ITEM_NO"), 0, 1);
			if(!skus.isEmpty()){
				for(String col:skus.get(0).keySet()){
					if(!good.containsKey(col))
						good.put(col, skus.get(0).get(col));
				}
			}
		}
		return goods;
	}
	@Override
	public void delOrder(Map<String, Object> params) throws CommonException {
		try {
			commonManagerMapper.delTableById("t_order_detail", "ORDER_NO",
					params.get("ORDER_NO"));
			commonManagerMapper.delTableById(T_ORDERS, "ORDERS_ID",
					params.get("ORDERS_ID"));
		} catch (Exception e) {
			throw new CommonException(e,
					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR);
		}
	}
	
	@Override
	public Map<String, Object> getAllLogisticses(Map<String, Object> params)
			throws CommonException {
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

		int total = 0;

		// 开始
		Integer start = params.get("start") == null ? null : Integer
				.valueOf(params.get("start").toString());
		// 结束
		Integer limit = params.get("limit") == null ? null : Integer
				.valueOf(params.get("limit").toString());

		params.remove("start");
		params.remove("limit");
		try {
			String tableName = V_LOGISTICS;
			if(params.get("IN_USE")!=null){
				if(Boolean.FALSE.equals(params.get("IN_USE"))){
					tableName = V_LOGISTICS_UNUSE;
				}
				params.remove("IN_USE");
			}
			
			List<String> keys=new ArrayList<String>(params.keySet());
			List<Object> values=new ArrayList<Object>(params.values());
			
			
			if(!params.containsKey("Fuzzy")){
			rows = commonManagerMapper.selectTableListByNVList(tableName, 
					keys,values,start, limit);
			total = commonManagerMapper.selectTableListCountByNVList(tableName,
					keys,values);
			}else{
				//模糊查询
				params.remove("Fuzzy");
				keys=new ArrayList<String>(params.keySet());
				values=new ArrayList<Object>(params.values());
				rows = commonManagerMapper.selectTableListByNVList_Fuzzy(tableName, 
						keys,values,start, limit);
				total = commonManagerMapper.selectTableListCountByNVList_Fuzzy(tableName,
						keys,values);
			}
			
			for(Map<String, Object> row:rows){
				Map<String, Object> additionInfo=getLogisticsOrder(row);
				if(additionInfo!=null)
					row.putAll(additionInfo);
			}

			Map<String, Object> result = new HashMap<String, Object>();
			result.put("total", total);
			result.put("rows", rows);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommonException(e,
					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR);
		}
	}
	public Map<String, Object> getLogisticsOrder(Map<String, Object> logistics){
		Map<String, Object> resultMap=null;
		Object ORDER_NO=logistics.get("ORDER_NO");
		if(ORDER_NO!=null){
			List<Map<String, Object>> orders=commonManagerMapper.selectTableListByCol(
					T_ORDERS, "ORDER_NO", ORDER_NO, null, null);
			if(orders!=null&&!orders.isEmpty()){
				Map<String, Object> orderMap=orders.get(0);
				resultMap=new HashMap<String, Object>();
				resultMap.put("EBP_CODE", orderMap.get("EBP_CODE"));
				resultMap.put("EBC_CODE", orderMap.get("EBC_CODE"));
				resultMap.put("AGENT_CODE", orderMap.get("AGENT_CODE"));
				resultMap.put("CONSIGNEE_COUNTRY_O", orderMap.get("CONSIGNEE_COUNTRY"));
				resultMap.put("GOODSList", getGoodsList(orderMap));
			}
		}
		return resultMap;
	}
	@Override
	public void delLogistics(Map<String, Object> params) throws CommonException {
		try {
			commonManagerMapper.delTableById("t_logistics", "LOGISTICS_ID",
					params.get("LOGISTICS_ID"));
		} catch (Exception e) {
			throw new CommonException(e,
					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR);
		}
	}
	@Override
	public void setLogistics(Map<String, Object> logistics, boolean statusOnly)
			throws CommonException {
		try {
			String primaryCol="LOGISTICS_ID";
			if(statusOnly){
				List<String> keys=Arrays.asList(new String[]{
						"LOGISTICS_STATUS","RETURN_STATUS","RETURN_TIME","RETURN_INFO"});
				List<Object> values=Arrays.asList(new Object[]{
						logistics.get("LOGISTICS_STATUS"),
						null,null,null});
				commonManagerMapper.updateTableByNVList(T_LOGISTICS, primaryCol,
						logistics.get(primaryCol), keys, values);
				Object primaryId=logistics.get(primaryCol);
				
				//生成xml报文
				//修改APP_STATUS为upload
				logistics.put("APP_STATUS", CommonDefine.APP_STATUS_UPLOAD);
				submitXml_LOGISTICS(logistics,
						Integer.valueOf(primaryId.toString()),
						CommonDefine.CEB503, CommonDefine.LOGISTICS_TYPE_NORMAL);
			}else{
				String uniqueCol="LOGISTICS_NO";
				// 唯一性校验
				uniqueCheck(T_LOGISTICS,uniqueCol,logistics.get(uniqueCol),primaryCol,logistics.get(primaryCol),false);
				uniqueCol="ORDER_NO";
				uniqueCheck(T_LOGISTICS,uniqueCol,logistics.get(uniqueCol),primaryCol,logistics.get(primaryCol),false);
				
				logistics.remove("editType");
				
				commonManagerMapper.updateTableByNVList(T_LOGISTICS, primaryCol,
						logistics.get(primaryCol), new ArrayList<String>(logistics.keySet()),
						new ArrayList<Object>(logistics.values()));
	
				Object primaryId=logistics.get(primaryCol);
	
				// 生成xml报文
				submitXml_LOGISTICS(logistics,
						Integer.valueOf(primaryId.toString()),
						CommonDefine.CEB501, CommonDefine.LOGISTICS_TYPE_NORMAL);
			}
		} catch (CommonException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommonException(e,
					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR);
		}
	}
	@Override
	public void addLogistics(Map<String, Object> logistics) throws CommonException {
		try {
			String uniqueCol="LOGISTICS_NO";
			String primaryCol="LOGISTICS_ID";
			// 唯一性校验
			uniqueCheck(T_LOGISTICS,uniqueCol,logistics.get(uniqueCol),null,null,false);
			uniqueCol="ORDER_NO";
			uniqueCheck(T_LOGISTICS,uniqueCol,logistics.get(uniqueCol),null,null,false);
			
			logistics.remove("editType");
			// 设置空id
			logistics.put(primaryCol, null);
			
			// 设置guid
			logistics.put("GUID", CommonUtil.generalGuid(CommonDefine.GUID_FOR_LOGISTICS,2,T_LOGISTICS));
			// 设置创建时间
			logistics.put("CREAT_TIME", new Date());
			
			Map primary=new HashMap();
			primary.put("primaryId", null);
			commonManagerMapper.insertTableByNVList(T_LOGISTICS,
					new ArrayList<String>(logistics.keySet()), 
					new ArrayList<Object>(logistics.values()),
					primary);
			
			Object primaryId=primary.get("primaryId");

			// 生成xml报文
			submitXml_LOGISTICS(logistics,
					Integer.valueOf(primaryId.toString()), CommonDefine.CEB501,
					CommonDefine.LOGISTICS_TYPE_NORMAL);
		} catch (CommonException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommonException(e,
					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR);
		}
	}
	
	@Override
	public Map<String, Object> getAllInventorys(Map<String, Object> params)
			throws CommonException {
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

		int total = 0;

		// 开始
		Integer start = params.get("start") == null ? null : Integer
				.valueOf(params.get("start").toString());
		// 结束
		Integer limit = params.get("limit") == null ? null : Integer
				.valueOf(params.get("limit").toString());

		params.remove("start");
		params.remove("limit");
		try {
			/*if(params.get("IN_USE")!=null){
				if(Boolean.FALSE.equals(params.get("IN_USE"))){
					tableName = "v_logistics_unuse";
				}
				params.remove("IN_USE");
			}*/
			
			List<String> keys=new ArrayList<String>(params.keySet());
			List<Object> values=new ArrayList<Object>(params.values());
			rows = commonManagerMapper.selectTableListByNVList(V_INVENTORY, 
					keys,values,start, limit);
			total = commonManagerMapper.selectTableListCountByNVList(V_INVENTORY,
					keys,values);
			
			for(Map<String, Object> row:rows){
				row.put("GOODSList", 
					commonManagerMapper.selectTableListByCol(
						"v_inventory_detail", "INVENTORY_ID", row.get("INVENTORY_ID"), null, null));
			}

			Map<String, Object> result = new HashMap<String, Object>();
			result.put("total", total);
			result.put("rows", rows);
			return result;
		} catch (Exception e) {
			throw new CommonException(e,
					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR);
		}
	}
	@Override
	public void delInventory(Map<String, Object> params) throws CommonException {
		try {
			commonManagerMapper.delTableById("t_inventory_detail", "INVENTORY_ID",
					params.get("INVENTORY_ID"));
			commonManagerMapper.delTableById("t_inventory", "INVENTORY_ID",
					params.get("INVENTORY_ID"));
		} catch (Exception e) {
			throw new CommonException(e,
					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR);
		}
	}
	@Override
	public void setInventory(Map<String, Object> inventory, boolean statusOnly)
			throws CommonException {
		try {
			String uniqueCol="COP_NO";
			String primaryCol="INVENTORY_ID";
			// 唯一性校验
			uniqueCheck(T_INVENTORY,uniqueCol,inventory.get(uniqueCol),primaryCol,inventory.get(primaryCol),false);
			uniqueCol="LOGISTICS_NO";
			// 唯一性校验
			uniqueCheck(T_INVENTORY,uniqueCol,inventory.get(uniqueCol),primaryCol,inventory.get(primaryCol),false);
			
			inventory.remove("editType");
			
			List<Map> GOODSList=(List<Map>)inventory.get("GOODSList");
			inventory.remove("GOODSList");
			
			commonManagerMapper.updateTableByNVList(T_INVENTORY, primaryCol,
					inventory.get(primaryCol), new ArrayList<String>(inventory.keySet()),
					new ArrayList<Object>(inventory.values()));

			Object primaryId=inventory.get(primaryCol);
			setInventoryGoodsList(GOODSList,primaryId,true);
			//生成xml报文
			submitXml_INVENTORY(inventory,Integer.valueOf(primaryId.toString()));
		} catch (CommonException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommonException(e,
					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR);
		}
	}
	@Override
	public void addInventory(Map<String, Object> inventory) throws CommonException {
		try {
			String uniqueCol="COP_NO";
			String primaryCol="INVENTORY_ID";
			// 唯一性校验
			uniqueCheck(T_INVENTORY,uniqueCol,inventory.get(uniqueCol),null,null,false);
			uniqueCol="LOGISTICS_NO";
			// 唯一性校验
			uniqueCheck(T_INVENTORY,uniqueCol,inventory.get(uniqueCol),null,null,false);
			
			inventory.remove("editType");
			// 设置空id
			inventory.put(primaryCol, null);
			
			// 设置guid
			inventory.put("GUID", CommonUtil.generalGuid(CommonDefine.GUID_FOR_INVENTORY,4,T_INVENTORY));
			// 设置创建时间
			inventory.put("CREAT_TIME", new Date());
			
			List<Map> GOODSList=(List<Map>)inventory.get("GOODSList");
			inventory.remove("GOODSList");
			
			Map primary=new HashMap();
			primary.put("primaryId", null);
			commonManagerMapper.insertTableByNVList(T_INVENTORY,
					new ArrayList<String>(inventory.keySet()), 
					new ArrayList<Object>(inventory.values()),
					primary);
			
			Object primaryId=primary.get("primaryId");
			setInventoryGoodsList(GOODSList,primaryId,false);
			//生成xml报文
			submitXml_INVENTORY(inventory,Integer.valueOf(primaryId.toString()));
		} catch (CommonException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommonException(e,
					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR);
		}
	}
	public void setInventoryGoodsList(List<Map> GOODSList,Object inventoryId,boolean isUpdate){

		String primaryCol="INVENTORY_DETAIL_ID";
		String ownerCol="INVENTORY_ID";
		Date createTime=new Date();
		for(int i=0;i<GOODSList.size();i++){
			Map rowMap=GOODSList.get(i);
			rowMap.put("PRICE", rowMap.get("ONE_PRICE"));
			rowMap.put("UNIT", rowMap.get("UNIT_I"));
			rowMap.put("UNIT1", rowMap.get("UNIT1_I"));
			rowMap.put("UNIT2", rowMap.get("UNIT2_I"));
			
			Map good=new HashMap();
			good.put(ownerCol, inventoryId);
			good.put("CREAT_TIME", createTime);
			String[] copyColumns=new String[]{primaryCol,
					"GNUM","ITEM_NO","QTY","QTY1","QTY2",
					"UNIT","UNIT1","UNIT2","PRICE","TOTAL"};
			for(String col:copyColumns){
				good.put(col, rowMap.get(col));
			}

			if(!isUpdate){
				good.remove(primaryCol);
				Map primary=new HashMap();
				primary.put("primaryId", null);
				commonManagerMapper.insertTableByNVList(T_INVENTORY_DETAIL,
						new ArrayList<String>(good.keySet()), 
						new ArrayList<Object>(good.values()),
						primary);
			}else{
				Object primaryValue=good.get(primaryCol);
				commonManagerMapper.updateTableByNVList(T_INVENTORY_DETAIL,
						primaryCol,primaryValue,
						new ArrayList<String>(good.keySet()), 
						new ArrayList<Object>(good.values()));
			}
			
		}
	}
	
	@Override
	public Map<String, Object> getAllFilePath(Map<String, Object> params)
			throws CommonException {
		try {

			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

			int total = 0;
			// 开始
			Integer start = params.get("start") == null ? null : Integer
					.valueOf(params.get("start").toString());
			// 结束
			Integer limit = params.get("limit") == null ? null : Integer
					.valueOf(params.get("limit").toString());
			// 查询所有
			rows = commonManagerMapper.selectTable("t_file_location_config", start,
					limit);
			total = commonManagerMapper.selectTableCount("t_file_location_config");
			
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("total", total);
			result.put("rows", rows);
			return result;
		} catch (Exception e) {
			ExceptionHandler.handleException(e);
			throw new CommonException(e,
					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR);
		}
	}
	
	
	@Override
	public void modifyFilePath(List<Map> dataList) {
		// 更新数据
		for (Map data : dataList) {
			commonManagerMapper.updateTableByNVList("t_file_location_config",
					"FILE_LOCATION_CONFIG_ID", data.get("FILE_LOCATION_CONFIG_ID"),
					new ArrayList<String>(data.keySet()),
					new ArrayList<Object>(data.values()));
		}
	}
	
	
	@Override
	public Map<String, Object> getAllContact(Map<String, Object> params)
			throws CommonException {
		try {

			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

			int total = 0;
			// 开始
			Integer start = params.get("start") == null ? null : Integer
					.valueOf(params.get("start").toString());
			// 结束
			Integer limit = params.get("limit") == null ? null : Integer
					.valueOf(params.get("limit").toString());
			// 查询所有
			rows = commonManagerMapper.selectTable("T_CONTACT", start,
					limit);
			total = commonManagerMapper.selectTableCount("T_CONTACT");
			
			for(Map xxx:rows){
				xxx.put("CONTACT_ID", xxx.get("CONTACT_ID").toString());
			}
			
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("total", total);
			result.put("rows", rows);
			return result;
		} catch (Exception e) {
			ExceptionHandler.handleException(e);
			throw new CommonException(e,
					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR);
		}
	}
	
	@Override
	public void addContact(Map data) {
		Map primary=new HashMap();
		primary.put("primaryId", null);
		
		String address = data.get("PROVINCE") + "_" + data.get("CITY")
				+ "_" + data.get("DISTRICT") + "_"
				+ data.get("SPECIFIC_ADDRESS");
		data.put("ADDRESS", address);
		
		commonManagerMapper.insertTableByNVList("T_CONTACT",
				new ArrayList<String>(data.keySet()), 
				new ArrayList<Object>(data.values()),
				primary);
	}
	
	@Override
	public void modifyContact(List<Map> dataList) {
		
		for (Map data : dataList) {
			String address = data.get("PROVINCE") + "_" + data.get("CITY")
					+ "_" + data.get("DISTRICT") + "_"
					+ data.get("SPECIFIC_ADDRESS");
			data.put("ADDRESS", address);
		}
		
		// 更新数据
		for (Map data : dataList) {
			commonManagerMapper.updateTableByNVList("T_CONTACT",
					"CONTACT_ID", data.get("CONTACT_ID"),
					new ArrayList<String>(data.keySet()),
					new ArrayList<Object>(data.values()));
		}
	}
	
	@Override
	public void deleteContact(List<Map> dataList) {
		// 更新数据
		for (Map data : dataList) {
			commonManagerMapper.delTableById("T_CONTACT",
					"CONTACT_ID", data.get("CONTACT_ID"));
		}
	}
	
	@Override
	public boolean checkContact(Map data) throws CommonException {
		
		int count  = commonManagerMapper.selectTableListCountByNVList("T_CONTACT", new ArrayList<String>(data.keySet()),
				new ArrayList<Object>(data.values()));
		
		return !(count>0);
	}


	@Override
	public void importFile(Map<String, Object> param) throws CommonException {
		String orderConfig = CommonUtil.getSystemConfigProperty("t_order_column");
		String logisticsConfig = CommonUtil.getSystemConfigProperty("t_logistics_column");
		
		//获取order数据
		List<Map<String,Object>> orderDataList = POIExcelUtil.readExcel((File) param.get("file"),orderConfig.split(","));
		for(Map orderData:orderDataList){
			orderData.put("GOODSList",new ArrayList<Map>());
			orderData.put("APP_STATUS", CommonDefine.APP_STATUS_UNUSE);
			//导入order
			addOrder(orderData);
		}
		//获取logistics数据
		List<Map<String,Object>> logisticsDataList = POIExcelUtil.readExcel((File) param.get("file"),logisticsConfig.split(","));
		for(Map logistics:logisticsDataList){
			logistics.put("APP_STATUS", CommonDefine.APP_STATUS_STORE);
			logistics.put("INSURE_FEE", 0d);
			logistics.put("PACK_NO", 1);
			//导入运单
			logistics.put("IE_FLAG", "E");
			addLogistics(logistics);
		}
	}

}
