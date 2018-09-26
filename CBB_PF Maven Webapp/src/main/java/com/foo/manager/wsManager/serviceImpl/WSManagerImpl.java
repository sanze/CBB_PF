package com.foo.manager.wsManager.serviceImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.jws.WebService;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

import com.foo.common.CommonDefine;
import com.foo.manager.wsManager.service.WSManagerService;
import com.foo.util.CommonUtil;
import com.foo.util.XmlUtil;

@WebService
public class WSManagerImpl extends WSManagerService{
	
	private static String FILE_TYPE_SNT101 = "SNT101";
	private static String FILE_TYPE_SNT102 = "SNT102";
	private static String FILE_TYPE_SNT201 = "SNT201";
	private static String FILE_TYPE_SNT202 = "SNT202";
	
	//订单信息 总署进口版本
	private static String FILE_TYPE_SNT301 = "SNT301";
	private static String FILE_TYPE_SNT302 = "SNT302";
	private static String FILE_TYPE_SNT401 = "SNT401";
	private static String FILE_TYPE_SNT402 = "SNT402";
	
	
	private static String CUSTOM_CODE = "2308";
	

	@Override
	public String ParseXml(String xmlString) {
		
		//上传log
		uploadWsLog(1,xmlString);
		
		String xmlReturnString = "";
		
		//截取fileType
		String fileType = xmlString.substring(
				xmlString.indexOf("<fileType>") + 10,
				xmlString.indexOf("</fileType>"));
		
		//验证xml
		String returnInfo = validateXml(xmlString,fileType);
		
		//验证不通过，返回错误信息
		if(!returnInfo.isEmpty()){
			Map data = new HashMap();
			
			Map content = null;
			if(FILE_TYPE_SNT101.equals(fileType)){
				content = generate102ReturnMap(data,"",returnInfo,CommonDefine.FAILED);
				xmlReturnString = XmlUtil.generalReceiptXml_WS(FILE_TYPE_SNT102,content);
			}else if(FILE_TYPE_SNT201.equals(fileType)){
				content = generate202ReturnMap(returnInfo,CommonDefine.FAILED);
				xmlReturnString = XmlUtil.generalReceiptXml_WS(FILE_TYPE_SNT202,content);
			}else if(FILE_TYPE_SNT301.equals(fileType)){
				content = generate302ReturnMap(data,"",returnInfo,CommonDefine.FAILED);
				xmlReturnString = XmlUtil.generalReceiptXml_WS(FILE_TYPE_SNT302,content);
			}else if(FILE_TYPE_SNT401.equals(fileType)){
				content = generate402ReturnMap(returnInfo,CommonDefine.FAILED);
				xmlReturnString = XmlUtil.generalReceiptXml_WS(FILE_TYPE_SNT402,content);
			}
			 
			//上传log
			uploadWsLog(2,xmlReturnString);
			return xmlReturnString;
		}
		
		//解析数据，获取fileType，xml
		Map<String,String> result = XmlUtil.parseXmlRoot_WS(xmlString);
		
		String xmlContent = result.get("xml").toString();

		//处理报文
		xmlReturnString = handleXml(fileType,xmlContent);

		//上传log
		uploadWsLog(2,xmlReturnString);
		
		return xmlReturnString;
	}
	
	//处理具体报文方法
	private String handleXml(String fileType,String xmlString){
		
		String xmlReturnString = "";
		
		if(FILE_TYPE_SNT101.equals(fileType)){
			xmlReturnString = handleXml_SNT101(xmlString);
		}else if(FILE_TYPE_SNT201.equals(fileType)){
			xmlReturnString = handleXml_SNT201(xmlString);
		}else if(FILE_TYPE_SNT301.equals(fileType)){
			xmlReturnString = handleXml_SNT301(xmlString);
		}else if(FILE_TYPE_SNT401.equals(fileType)){
			xmlReturnString = handleXml_SNT401(xmlString);
		}
		
		
		return xmlReturnString;
	}
	
	
	//处理SNT101报文
	private String handleXml_SNT101(String xmlString){
		
		String xmlReturnString = "";
		
		Map<String,Object> data =  XmlUtil.parseXmlSNT101_WS(xmlString);
		
		Map head = (Map) data.get("OrderHead");
		
		List<Map> OrderList = (List<Map>) data.get("OrderList");
		
		//检查订单是否在数据库中存在
		String OrderNo = head.get("OrderNo").toString();
		int count = commonManagerMapper.selectTableListCountByCol("t_nj_orders", "ORDER_NO", OrderNo);
		
		if(count == 0){
			//生成商品数据
			generateSkuDataToDb(head,OrderList);
			//生成订单数据
			generateOrderDataToDb(head,OrderList);
			//生成运单数据
			String logisticsNo = generateLogisticsDataToDb(head);
			
			//更新订单数据中的运单号
			updateLogisticsNoToOrder(OrderNo,logisticsNo);
			
			//返回运单状态
			Map content = generate102ReturnMap(head,logisticsNo,"",CommonDefine.SUCCESS);
			
			xmlReturnString = XmlUtil.generalReceiptXml_WS(FILE_TYPE_SNT102,content);
			
//			xmlReturnString = "";
		}else{
			String logisticsNo = getLogisticsNo(OrderNo);
			//更新订单数据中的运单号
			updateLogisticsNoToOrder(OrderNo,logisticsNo);
			//返回运单状态
			Map content = generate102ReturnMap(head,logisticsNo,"",CommonDefine.SUCCESS);
			
			xmlReturnString = XmlUtil.generalReceiptXml_WS(FILE_TYPE_SNT102,content);
		}

		return xmlReturnString;
	}
	
	
	//处理SNT201报文
	private String handleXml_SNT201(String xmlString) {

		String xmlReturnString = "";

		Map<String, Object> data = XmlUtil.parseXmlSNT201_WS(xmlString);

		Map head = (Map) data.get("Head");

		// 检查运单是否在数据库中存在
		String LogisticsNo = head.get("LogisticsNo").toString();
		String OrderNo = head.get("OrderNo").toString();

		List<String> colNames = new ArrayList<String>();
		colNames.add("LOGISTICS_NO");
		colNames.add("ORDER_NO");
		List<Object> colValues = new ArrayList<Object>();
		colValues.add(LogisticsNo);
		colValues.add(OrderNo);
		// 请求中的订单号和运单号，必须是对应的。
		int count = commonManagerMapper.selectTableListCountByNVList(
				"t_nj_logistics", colNames, colValues);

		if (count == 0) {
			// 返回运单状态
			Map content = generate202ReturnMap("运单号和订单号不匹配！",
					CommonDefine.FAILED);

			xmlReturnString = XmlUtil.generalReceiptXml_WS(FILE_TYPE_SNT202,
					content);

		} else {
			String EbpCode = head.get("EbpCode").toString();
			String EbcCode = head.get("EbcCode").toString();
			// 请求中的电商平台编号，电商企业编号与订单号，必须在订单表中对应
			colNames.clear();
			colNames.add("ORDER_NO");
			colNames.add("EBC_CODE");
			colNames.add("EBP_CODE");
			colValues.clear();
			colValues.add(OrderNo);
			colValues.add(EbcCode);
			colValues.add(EbpCode);
			count = commonManagerMapper.selectTableListCountByNVList(
					"t_nj_orders", colNames, colValues);
			if (count == 0) {
				// 返回运单状态
				Map content = generate202ReturnMap("订单号和电商平台编号、电商企业编号不匹配！",
						CommonDefine.FAILED);

				xmlReturnString = XmlUtil.generalReceiptXml_WS(
						FILE_TYPE_SNT202, content);
			} else {
				// 查询数据
				Map content = njCommonManagerMapper
						.selectDataForMessageSNT202(LogisticsNo);
				
				// 获取资源文件
				ResourceBundle bundle = CommonUtil
						.getMessageMappingResource("CEB_NJ");
				
				// 写死3201W改成从配置文件中读取
				content.put(
						"DestinationPort",
						bundle.getString("DestinationPort_value") != null ? bundle
								.getString("DestinationPort_value") : "3201W");
				// 写死5改成从配置文件中读取
				content.put(
						"WrapType",
						bundle.getString("WrapType_value") != null ? bundle
								.getString("WrapType_value") : "5");
				
				content.put("returnStatus", CommonDefine.SUCCESS);
				content.put("returnInfo", "");
				// 返回数据
				xmlReturnString = XmlUtil.generalReceiptXml_WS(
						FILE_TYPE_SNT202, content);
			}

		}
		return xmlReturnString;
	}
	
	
	
	//处理SNT301报文
	private String handleXml_SNT301(String xmlString){
		
		String xmlReturnString = "";
		
		Map<String,Object> data =  XmlUtil.parseXmlSNT301_WS(xmlString);
		
		Map head = (Map) data.get("OrderHead");
		
		List<Map> OrderList = (List<Map>) data.get("OrderList");
		
		//检查订单是否在数据库中存在
		String OrderNo = head.get("orderNo").toString();
		int count = commonManagerMapper.selectTableListCountByCol(T_IMPORT_ORDERS, "ORDER_NO", OrderNo);
		
		if(count == 0){
			//生成订单数据
			generateOrderDataToDb_import(head,OrderList);
			//生成运单数据
			String logisticsNo = generateLogisticsDataToDb_import(head);
			
			//更新订单数据中的运单号
			updateLogisticsNoToOrder(OrderNo,logisticsNo);
			
			//返回运单状态
			Map content = generate302ReturnMap(head,logisticsNo,"",CommonDefine.SUCCESS);
			
			xmlReturnString = XmlUtil.generalReceiptXml_WS(FILE_TYPE_SNT302,content);
			
//			xmlReturnString = "";
		}else{
			String logisticsNo = getLogisticsNo(OrderNo);
			//更新订单数据中的运单号
			updateLogisticsNoToOrder(OrderNo,logisticsNo);
			//返回运单状态
			Map content = generate302ReturnMap(head,logisticsNo,"",CommonDefine.SUCCESS);
			
			xmlReturnString = XmlUtil.generalReceiptXml_WS(FILE_TYPE_SNT302,content);
		}

		return xmlReturnString;
	}
	
	//处理SNT401报文
	private String handleXml_SNT401(String xmlString) {

		String xmlReturnString = "";

		Map<String, Object> data = XmlUtil.parseXmlSNT401_WS(xmlString);

		Map head = (Map) data.get("Head");

		// 检查运单是否在数据库中存在
		String LogisticsNo = head.get("LogisticsNo").toString();
		String OrderNo = head.get("OrderNo").toString();

		List<String> colNames = new ArrayList<String>();
		colNames.add("LOGISTICS_NO");
		colNames.add("ORDER_NO");
		List<Object> colValues = new ArrayList<Object>();
		colValues.add(LogisticsNo);
		colValues.add(OrderNo);
		// 请求中的订单号和运单号，必须是对应的。
		int count = commonManagerMapper.selectTableListCountByNVList(
				T_IMPORT_LOGISTICS, colNames, colValues);

		if (count == 0) {
			// 返回运单状态
			Map content = generate402ReturnMap("运单号和订单号不匹配！",
					CommonDefine.FAILED);

			xmlReturnString = XmlUtil.generalReceiptXml_WS(FILE_TYPE_SNT402,
					content);

		} else {
			String EbpCode = head.get("EbpCode").toString();
			String EbcCode = head.get("EbcCode").toString();
			// 请求中的电商平台编号，电商企业编号与订单号，必须在订单表中对应
			colNames.clear();
			colNames.add("ORDER_NO");
			colNames.add("EBC_CODE");
			colNames.add("EBP_CODE");
			colValues.clear();
			colValues.add(OrderNo);
			colValues.add(EbcCode);
			colValues.add(EbpCode);
			count = commonManagerMapper.selectTableListCountByNVList(
					T_IMPORT_ORDERS, colNames, colValues);
			if (count == 0) {
				// 返回运单状态
				Map content = generate402ReturnMap("订单号和电商平台编号、电商企业编号不匹配！",
						CommonDefine.FAILED);

				xmlReturnString = XmlUtil.generalReceiptXml_WS(
						FILE_TYPE_SNT402, content);
			} else {
				// 查询数据
				Map content = importCommonManagerMapper
						.selectDataForMessageSNT402(LogisticsNo);
				
				// 获取资源文件
				ResourceBundle bundle = CommonUtil
						.getMessageMappingResource("CEB_IMPORT");
				
				content.put("returnStatus", CommonDefine.SUCCESS);
				content.put("returnInfo", "");
				// 返回数据
				xmlReturnString = XmlUtil.generalReceiptXml_WS(
						FILE_TYPE_SNT402, content);
			}

		}
		return xmlReturnString;
	}
	
	
	
	//更新订单中的运单号
	private void updateLogisticsNoToOrder(String orderNo, String logisticsNo) {

		Map data = new HashMap();
		data.put("LOGISTICS_NO", logisticsNo);
		commonManagerMapper.updateTableByNVList(T_NJ_ORDERS, "ORDER_NO",
				orderNo, new ArrayList<String>(data.keySet()),
				new ArrayList<Object>(data.values()));
	}
	
	//更新订单中的运单号
	private void updateLogisticsNoToOrder_import(String orderNo, String logisticsNo) {

		Map data = new HashMap();
		data.put("LOGISTICS_NO", logisticsNo);
		commonManagerMapper.updateTableByNVList(T_IMPORT_ORDERS, "ORDER_NO",
				orderNo, new ArrayList<String>(data.keySet()),
				new ArrayList<Object>(data.values()));
	}
	
	//组织返回数据
	private Map generate102ReturnMap(Map head,String logisticsNo,String returnInfo,int flag){

		String currentTime = new SimpleDateFormat(
				CommonDefine.RETRIEVAL_TIME_FORMAT).format(new Date());
		
		Map content = new LinkedHashMap();
		content.put("EbpCode", head.get("EbpCode"));
		content.put("EbcCode", head.get("EbcCode"));
		content.put("OrderNo", head.get("OrderNo"));
		content.put("returnStatus", flag);
		content.put("returnTime", currentTime);
		content.put("returnInfo", returnInfo);
		content.put("LogisticsNo", logisticsNo);
		
		return content;
	}
	
	
	//组织返回数据
	private Map generate202ReturnMap(String returnInfo,int flag){
		
		Map content = new LinkedHashMap();
		content.put("returnStatus", flag);
		content.put("returnInfo", returnInfo);
		
		return content;
	}

	//组织返回数据
	private Map generate302ReturnMap(Map head,String logisticsNo,String returnInfo,int flag){

		String currentTime = new SimpleDateFormat(
				CommonDefine.RETRIEVAL_TIME_FORMAT).format(new Date());
		
		Map content = new LinkedHashMap();
		content.put("ebpCode", head.get("ebpCode"));
		content.put("ebcCode", head.get("ebcCode"));
		content.put("orderNo", head.get("orderNo"));
		content.put("returnStatus", flag);
		content.put("returnTime", currentTime);
		content.put("returnInfo", returnInfo);
		content.put("LogisticsNo", logisticsNo);
		
		return content;
	}
	
	//组织返回数据
	private Map generate402ReturnMap(String returnInfo,int flag){
		
		Map content = new LinkedHashMap();
		content.put("returnStatus", flag);
		content.put("returnInfo", returnInfo);
		
		return content;
	}
	
	//获取中外运订单编号
	private String getLogisticsNo(String OrderNo){
		String logisticsNo = "";
		//获取SNTNo
		List logisticsDataList = commonManagerMapper
				.selectTableListByCol("t_nj_logistics", "ORDER_NO",
						OrderNo, null, null);
		
		if(logisticsDataList!=null && logisticsDataList.size()>0){
			Map logisticsData = (Map)logisticsDataList.get(0);
			if(logisticsData.get("LOGISTICS_NO") !=null){
				logisticsNo = logisticsData.get("LOGISTICS_NO").toString();
			}
		}
		return logisticsNo;
	}
	
	//在数据库中插入商品数据，返回id
	private void generateSkuDataToDb(Map head,List<Map> data){

		//在head中取需要的列名
		String[] needColumn = new String[]{
				"EBP_CODE","EBC_CODE","BIZ_TYPE","CURRENCY"
		};
		String tableName=T_NJ_SKU;
		String uniqueCol="ITEM_NO";
		String primaryCol="SKU_ID";
		
		//变换列名
		head = changeDbColumn(head);
		
		//特殊字段
		if(head.containsKey("ORDER_TYPE")){
			head.put("BIZ_TYPE", head.get("ORDER_TYPE"));
		}
		
		//在data中取需要的列名
		String[] dataNeedColumn = new String[]{
				"G_NO","G_NAME","ITEM_NO","PRICE","BAR_CODE","NOTE"
		};
		for(Map sku:data){
			
			//检查商品是否在数据库中存在
			String ItemNo = sku.get("ItemNo").toString();
			int count = commonManagerMapper.selectTableListCountByCol("T_NJ_SKU", "ITEM_NO", ItemNo);
			
			if(count == 0){
				Map newHead = new HashMap();
				//变换列名
				sku = changeDbColumn(sku);
				//添加字段
				for(String column:needColumn){
					newHead.put(column, head.get(column));
				}
				//添加商品信息字段
				for(String column:dataNeedColumn){
					newHead.put(column, sku.get(column));
				}
				// 设置空id
				newHead.put(primaryCol, null);
				
				// 设置额外列
				newHead.put("CUSTOM_CODE", CUSTOM_CODE);
				newHead.put("RECEIVER_ID", CUSTOM_CODE);
				newHead.put("GUID", CommonUtil.generalGuid4NJ(CommonDefine.CEB201,head.get("EBC_CODE").toString(),CUSTOM_CODE));
				
				newHead.put("APP_UID", head.get("EBC_CODE"));
				newHead.put("AGENT_CODE", head.get("EBC_CODE"));
				
				newHead.put("APP_STATUS", CommonDefine.APP_STATUS_UNUSE);
				// 设置创建时间
				newHead.put("CREAT_TIME", new Date());
				
				Map primary=new HashMap();
				primary.put("primaryId", null);

				//插入数据
				commonManagerMapper.insertTableByNVList(tableName,
						new ArrayList<String>(newHead.keySet()), 
						new ArrayList<Object>(newHead.values()),
						primary);
			}
		}
		
	}
	
	//在数据库中插入订单数据
	private void  generateOrderDataToDb(Map head,List<Map> data){
		
		//在head中取需要的列名
		String[] needColumn = new String[]{
				"EBP_CODE","EBC_CODE","ORDER_TYPE","ORDER_NO","GOODS_VALUE",
				"TAX_FEE","FREIGHT","CURRENCY","NOTE"
		};
		
		String tableName=T_NJ_ORDERS;
		String uniqueCol="ORDER_NO";
		String primaryCol="ORDERS_ID";
		
		//获取联系人Id
		Object consigneeId = handleAddress(head);
		//变换列名
		head = changeDbColumn(head);
		
		Map newHead = new HashMap();
		for(String column:needColumn){
			newHead.put(column, head.get(column));
		}

		//设置额外列
		newHead.put("CONSIGNEE_ID", consigneeId);
		//生成guid  CUSTOM_CODE固定2308
		newHead.put("CUSTOM_CODE", CUSTOM_CODE);
		newHead.put("RECEIVER_ID", CUSTOM_CODE);
		newHead.put("GUID", CommonUtil.generalGuid4NJ(CommonDefine.CEB301,head.get("EBC_CODE").toString(),CUSTOM_CODE));
		//用户名称、申报企业名称填电商企业名称
		newHead.put("AGENT_CODE", newHead.get("EBC_CODE"));
		newHead.put("APP_UID", newHead.get("EBC_CODE"));
		
		newHead.put("APP_STATUS", CommonDefine.APP_STATUS_UNUSE);
		
		newHead.put("CREAT_TIME", new Date());
		
		Map primary=new HashMap();
		primary.put(primaryCol, null);
		
		commonManagerMapper.insertTableByNVList(tableName,
				new ArrayList<String>(newHead.keySet()), 
				new ArrayList<Object>(newHead.values()),
				primary);
		
		//转换列名
		data = changeDbColumn(data);
		Object primaryId=primary.get("primaryId");
		setGoodsList(data,head.get("ORDER_NO").toString(),primaryId);

	}
	
	
	//在数据库中插入订单数据
	private void  generateOrderDataToDb_import(Map head,List<Map> data){
		
		//在head中取需要的列名
		String[] needColumn = new String[]{
				"EBP_CODE","EBC_CODE","ORDER_NO",
				"FREIGHT","NOTE"
		};
		
		String tableName=T_IMPORT_ORDERS;
		String uniqueCol="ORDER_NO";
		String primaryCol="ORDERS_ID";
		
		//获取联系人Id
		Object consigneeId = handleAddress_import(head);
		//变换列名
		head = changeDbColumn_import(head);
		
		Map newHead = new HashMap();
		for(String column:needColumn){
			newHead.put(column, head.get(column));
		}

		//设置额外列
		newHead.put("CONSIGNEE_ID", consigneeId);
		//生成guid  CUSTOM_CODE固定2308
//		newHead.put("CUSTOM_CODE", CUSTOM_CODE);
//		newHead.put("RECEIVER_ID", CUSTOM_CODE);
		newHead.put("GUID", CommonUtil.generalGuid4NJ(CommonDefine.CEB311,head.get("EBC_CODE").toString(),CUSTOM_CODE));
		
		newHead.put("APP_STATUS", CommonDefine.APP_STATUS_UNUSE);
		
		newHead.put("CREAT_TIME", new Date());
		
		Map primary=new HashMap();
		primary.put(primaryCol, null);
		
		commonManagerMapper.insertTableByNVList(tableName,
				new ArrayList<String>(newHead.keySet()), 
				new ArrayList<Object>(newHead.values()),
				primary);
		
		//转换列名
		data = changeDbColumn_import(data);
		Object primaryId=primary.get("primaryId");
		setGoodsList_import(data,head.get("ORDER_NO").toString(),primaryId);

	}

	//在数据库中插入运单数据，返回id
	private String generateLogisticsDataToDb(Map head){
		
		String orderType = head.get("OrderType").toString();
		String orderNo = head.get("OrderNo").toString();

		//在head中取需要的列名
		String[] needColumn = new String[]{
				"ORDER_NO","INSURE_FEE","NET_WEIGHT","GOODS_INFO","FREIGHT"
		};
		String tableName=T_NJ_LOGISTICS;
		String uniqueCol="LOGISTICS_NO";
		String primaryCol="LOGISTICS_ID";
		
		//获取联系人Id
		Object consigneeId = handleAddress(head);
		
		//变换列名
		head = changeDbColumn(head);
		
		//特殊字段
		if(head.containsKey("NET_WEIGHT_SP")){
			head.put("NET_WEIGHT", head.get("NET_WEIGHT_SP"));
		}
		
		Map newHead = new HashMap();
		for(String column:needColumn){
			newHead.put(column, head.get(column));
		}
		
		// 设置空id
		newHead.put(primaryCol, null);
		
		// 设置额外列
		newHead.put("CUSTOM_CODE", CUSTOM_CODE);
		newHead.put("RECEIVER_ID", CUSTOM_CODE);
		newHead.put("GUID", CommonUtil.generalGuid4NJ(CommonDefine.CEB501,head.get("EBC_CODE").toString(),CUSTOM_CODE));
		//件数 运单关联的订单中所有商品数量之和
		int packNo = njCommonManagerMapper.selectSumPackNoForOrder(orderNo);
		
		newHead.put("PACK_NO", packNo);
		//进出口标记
		if("1".equals(orderType) || "3".equals(orderType)){
			newHead.put("IE_FLAG", "I");
		}else if("2".equals(orderType) || "4".equals(orderType)){
			newHead.put("IE_FLAG", "E");
		}
		newHead.put("CONSIGNEE_ID", consigneeId);
		
//		newHead.put("APP_UID", head.get("EBC_CODE"));
		// 设置创建时间
		newHead.put("CREAT_TIME", new Date());
		
		String logisticsNo = CommonUtil.generalLogisticsNo(12, "LOGISTICS_NO", "t_nj_logistics");
		
		//生成运单号
		newHead.put("IS_AUTO", CommonDefine.SUCCESS);
		newHead.put("LOGISTICS_NO", logisticsNo);
		
		Map primary=new HashMap();
		primary.put("primaryId", null);

		//插入数据
		commonManagerMapper.insertTableByNVList(tableName,
				new ArrayList<String>(newHead.keySet()), 
				new ArrayList<Object>(newHead.values()),
				primary);
		
		return logisticsNo;
		
	}
	
	
	//在数据库中插入运单数据，返回id
	private String generateLogisticsDataToDb_import(Map head){
		
		String orderNo = head.get("orderNo").toString();

		//在head中取需要的列名
		String[] needColumn = new String[]{
				"ORDER_NO","INSURE_FEE","WEIGHT","NET_WEIGHT","GOODS_INFO","FREIGHT"
		};
		String tableName=T_IMPORT_LOGISTICS;
		String uniqueCol="LOGISTICS_NO";
		String primaryCol="LOGISTICS_ID";
		
		//获取联系人Id
		Object consigneeId = handleAddress_import(head);
		
		//变换列名
		head = changeDbColumn_import(head);
		
		//特殊字段
		if(head.containsKey("NET_WEIGHT_SP")){
			head.put("NET_WEIGHT", head.get("NET_WEIGHT_SP"));
		}
		
		Map newHead = new HashMap();
		for(String column:needColumn){
			newHead.put(column, head.get(column));
		}
		
		// 设置空id
		newHead.put(primaryCol, null);
		
		// 设置额外列
//		newHead.put("CUSTOM_CODE", CUSTOM_CODE);
//		newHead.put("RECEIVER_ID", CUSTOM_CODE);
		newHead.put("GUID", CommonUtil.generalGuid4NJ(CommonDefine.CEB511,"",""));
		//件数 运单关联的订单中所有商品数量之和
		int packNo = importCommonManagerMapper.selectSumPackNoForOrder(orderNo);
		
		newHead.put("PACK_NO", packNo);

		newHead.put("CONSIGNEE_ID", consigneeId);
		
//		newHead.put("APP_UID", head.get("EBC_CODE"));
		// 设置创建时间
		newHead.put("CREAT_TIME", new Date());
		
		String logisticsNo = CommonUtil.generalLogisticsNo(12, "LOGISTICS_NO", T_IMPORT_LOGISTICS);
		
		//生成运单号
		newHead.put("IS_AUTO", CommonDefine.SUCCESS);
		newHead.put("LOGISTICS_NO", logisticsNo);
		
		Map primary=new HashMap();
		primary.put("primaryId", null);

		//插入数据
		commonManagerMapper.insertTableByNVList(tableName,
				new ArrayList<String>(newHead.keySet()), 
				new ArrayList<Object>(newHead.values()),
				primary);
		
		return logisticsNo;
		
	}
	
	//地址处理
	private Object handleAddress(Map head){
		
		Object id = null;
		
		List<String> colNames=new ArrayList<String>();
		colNames.add("CODE");
		colNames.add("NAME");
		colNames.add("TEL");
		colNames.add("COUNTRY");
		colNames.add("PROVINCE");
		colNames.add("CITY");
		colNames.add("DISTRICT");
		colNames.add("SPECIFIC_ADDRESS");
		List<Object> colValues=new ArrayList<Object>();
		colValues.add(head.get("ConsigneeCode"));
		colValues.add(head.get("Consignee"));
		colValues.add(head.get("ConsigneeTelephone"));
		colValues.add(head.get("ConsigneeCountry"));
		colValues.add(head.get("ConsigneeProvince"));
		colValues.add(head.get("ConsigneeCity"));
		colValues.add(head.get("ConsigneeDistrict"));
		colValues.add(head.get("ConsigneeAddress"));
		
		//查找t_contact表，是否是已存在的地址
		List<Map<String,Object>> data = commonManagerMapper.selectTableListByNVList("t_contact", colNames, colValues, null, null);
		
		if(data.size()>0){
			id = data.get(0).get("CONTACT_ID");
		}else{
			//插入联系人数据
			Map primary=new HashMap();
			primary.put("primaryId", null);
			
			String address = head.get("ConsigneeProvince") + "_" + head.get("ConsigneeCity")
					+ "_" + head.get("ConsigneeDistrict") + "_"
					+ head.get("ConsigneeAddress");
			
			Map contactData = new HashMap();
			
			for(int i = 0;i<colNames.size();i++){
				contactData.put(colNames.get(i), colValues.get(i));
			}
			contactData.put("ADDRESS", address);
			
			commonManagerMapper.insertTableByNVList("T_CONTACT",
					new ArrayList<String>(contactData.keySet()), 
					new ArrayList<Object>(contactData.values()),
					primary);
			
			Object primaryId=primary.get("primaryId");
			id = primaryId;
			
		}
		return id;
	}
	
	
	//地址处理
	private Object handleAddress_import(Map head){
		
		Object id = null;
		
		List<String> colNames=new ArrayList<String>();
		colNames.add("CODE");
		colNames.add("NAME");
		colNames.add("TEL");
		colNames.add("COUNTRY");
		colNames.add("PROVINCE");
		colNames.add("CITY");
		colNames.add("DISTRICT");
		colNames.add("SPECIFIC_ADDRESS");
		colNames.add("NAME_P");
		colNames.add("ADDRESS_P");
		List<Object> colValues=new ArrayList<Object>();
		colValues.add(head.get("consigneeCode"));
		colValues.add(head.get("consignee"));
		colValues.add(head.get("consigneeTelephone"));
		colValues.add(head.get("consigneeCountry"));
		colValues.add(head.get("consigneeProvince"));
		colValues.add(head.get("consigneeCity"));
		colValues.add(head.get("consigneeDistrict"));
		colValues.add(head.get("consigneeAddress"));
		colValues.add(head.get("consigneeP"));
		colValues.add(head.get("consigneeAddressP"));
		
		//查找t_contact表，是否是已存在的地址
		List<Map<String,Object>> data = commonManagerMapper.selectTableListByNVList("t_contact", colNames, colValues, null, null);
		
		if(data.size()>0){
			id = data.get(0).get("CONTACT_ID");
		}else{
			//插入联系人数据
			Map primary=new HashMap();
			primary.put("primaryId", null);
			
			String address = head.get("consigneeProvince") + "_" + head.get("consigneeCity")
					+ "_" + head.get("consigneeDistrict") + "_"
					+ head.get("consigneeAddress");
			
			Map contactData = new HashMap();
			
			for(int i = 0;i<colNames.size();i++){
				contactData.put(colNames.get(i), colValues.get(i));
			}
			contactData.put("ADDRESS", address);
			
			commonManagerMapper.insertTableByNVList("T_CONTACT",
					new ArrayList<String>(contactData.keySet()), 
					new ArrayList<Object>(contactData.values()),
					primary);
			
			Object primaryId=primary.get("primaryId");
			id = primaryId;
			
		}
		return id;
	}
	
	
	//插入订单详细信息数据
	private void setGoodsList(List<Map> GOODSList,Object orderNo, Object ordersId){
		commonManagerMapper.delTableById(T_NJ_ORDER_DETAIL, "ORDERS_ID", ordersId);
		Date createTime=new Date();
		for(int i=0;i<GOODSList.size();i++){
//			GOODSList.get(i).put("PRICE", GOODSList.get(i).get("ONE_PRICE"));
//			GOODSList.get(i).put("NOTE", GOODSList.get(i).get("NOTE_OD"));
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
			commonManagerMapper.insertTableByNVList(T_NJ_ORDER_DETAIL,
					new ArrayList<String>(good.keySet()), 
					new ArrayList<Object>(good.values()),
					primary);
		}
	}
	
	//插入订单详细信息数据
	private void setGoodsList_import(List<Map> GOODSList,Object orderNo, Object ordersId){
		commonManagerMapper.delTableById(T_IMPORT_ORDER_DETAIL, "ORDERS_ID", ordersId);
		Date createTime=new Date();
		for(int i=0;i<GOODSList.size();i++){
//			GOODSList.get(i).put("PRICE", GOODSList.get(i).get("ONE_PRICE"));
//			GOODSList.get(i).put("NOTE", GOODSList.get(i).get("NOTE_OD"));
			Map good=new HashMap();
			good.put("ORDERS_ID", ordersId);
			good.put("ORDER_NO", orderNo);
			good.put("GNUM", i+1);
			good.put("CREAT_TIME", createTime);
			String[] copyColumns=new String[]{"ITEM_NO","QTY","PRICE","TOTAL","NOTE","ITEM_NAME","G_NAME","BARCODE"};
			for(String col:copyColumns){
				good.put(col, GOODSList.get(i).get(col));
			}
			Map primary=new HashMap();
			primary.put("primaryId", null);
			commonManagerMapper.insertTableByNVList(T_IMPORT_ORDER_DETAIL,
					new ArrayList<String>(good.keySet()), 
					new ArrayList<Object>(good.values()),
					primary);
		}
	}

	
	//校验xml文件
	public String validateXml(String sourceString,String fileType) {

		String result = "";
		
		if(fileType == null || fileType.isEmpty()){
			result = "fileType不正确！";
			return result;
		}
		try {
			// 建立schema工厂
			SchemaFactory schemaFactory = SchemaFactory
					.newInstance("http://www.w3.org/2001/XMLSchema");
			// 建立验证文档文件对象，利用此文件对象所封装的文件进行schema验证
			Source sourceSchema = new StreamSource(Thread
					.currentThread()
					.getContextClassLoader()
					.getResourceAsStream(
							"xmlDataSource/"+fileType+".xsd"));
			// 利用schema工厂，接收验证文档文件对象生成Schema对象
			Schema schema = schemaFactory.newSchema(sourceSchema);
			// 通过Schema产生针对于此Schema的验证器，利用schenaFile进行验证
			Validator validator = schema.newValidator();
//			 // 创建默认的XML错误处理器
//	          XMLErrorHandler errorHandler = new XMLErrorHandler();
//	          validator.setErrorHandler(errorHandler);
			// 得到验证的数据源
			Source sourceFile = new StreamSource(new  ByteArrayInputStream(sourceString.getBytes("UTF-8")));
			// 开始验证，成功输出success!!!，失败输出fail
			validator.validate(sourceFile);

		} catch (SAXException e) {
			result = e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

}
