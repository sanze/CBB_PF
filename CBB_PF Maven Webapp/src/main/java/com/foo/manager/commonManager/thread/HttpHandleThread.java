package com.foo.manager.commonManager.thread;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import javax.annotation.Resource;

import org.apache.commons.collections.map.LinkedMap;
import org.springframework.stereotype.Service;

import com.foo.common.CommonDefine;
import com.foo.common.CommonException;
import com.foo.dao.mysql.CommonManagerMapper;
import com.foo.dao.mysql.SNCommonManagerMapper;
import com.foo.manager.commonManager.model.BookOrderModel;
import com.foo.manager.commonManager.service.HttpServerManagerService;
import com.foo.util.CommonUtil;
import com.foo.util.HttpUtil;
import com.foo.util.SpringContextUtil;
import com.foo.util.XmlUtil;

@Service
public class HttpHandleThread implements Callable<Object>{
	@Resource
	private static CommonManagerMapper commonManagerMapper;
	@Resource
	private static SNCommonManagerMapper snCommonManagerMapper;
	
	private String requestType;
	private String content;
	private String signKey;
	
	public HttpHandleThread(String requestType,String content,String signKey) {
		if(commonManagerMapper == null){
			commonManagerMapper = (CommonManagerMapper) SpringContextUtil
					.getBean("commonManagerMapper");
		}
		if(snCommonManagerMapper == null){
			snCommonManagerMapper = (SNCommonManagerMapper) SpringContextUtil
					.getBean("SNCommonManagerMapper");
		}
		
		//订单信息--C061
//		this.requestType = HttpServerManagerService.requestType_Order;
//		this.content = "<inputData><ordersList><orders><orderImformation><orderHead><logisticsOrderId>LOS36100001120690200</logisticsOrderId><oriSys>LOS</oriSys><taskOrderid>36100001120690200</taskOrderid><thirdPartyCompany>0080010331</thirdPartyCompany><businessType>C061</businessType><announcedValue>100.00</announcedValue><valueUnit>CNY</valueUnit><expectDate>2015-09-07</expectDate><expectTime>11:38:10</expectTime></orderHead><orderItems><logisticsOrderId>LOS36100001120690200</logisticsOrderId><itemNumber>10</itemNumber><location>L971</location><locationDescription>天津口岸</locationDescription><storage>0001</storage><goodsCode>20166666</goodsCode><goodsDescription>长虹空调KFR35GWBP2DN1YF</goodsDescription><goodsNumber>5.000</goodsNumber><goodsUnit>S01</goodsUnit><comValue>100.00</comValue><valueUnit>CNY</valueUnit></orderItems><feedBackOrderCustomers><logisticsOrderId>LOS36100001120690200</logisticsOrderId><itemNumber>10</itemNumber><customerType>AG</customerType><customerId>70057297</customerId><name>C店商户苏宁</name><country>CN</country><city>南京市</city><address>南京市玄武区</address><zipCode>210012</zipCode><fixedLineTelephone>02566996699</fixedLineTelephone><mobilePhone>15295599233</mobilePhone></feedBackOrderCustomers></orderImformation></orders></ordersList></inputData>";
		//订单信息--C005
//		this.requestType = HttpServerManagerService.requestType_Order;
//		this.content = "<inputData><ordersList><orders><orderImformation><orderHead><logisticsOrderId>LOS36100001120690200</logisticsOrderId><oriSys>LOS</oriSys><taskOrderid>36100001120690200</taskOrderid><thirdPartyCompany>0080010331</thirdPartyCompany><businessType>C005</businessType><btcOrderId>BOL031707</btcOrderId><announcedValue>100.00</announcedValue><valueUnit>CNY</valueUnit><expectDate>2015-09-07</expectDate><expectTime>11:38:10</expectTime></orderHead><orderItems><logisticsOrderId>LOS36100001120690200</logisticsOrderId><itemNumber>10</itemNumber><location>L971</location><locationDescription>天津口岸</locationDescription><storage>0001</storage><goodsCode>20166666</goodsCode><goodsDescription>长虹空调KFR35GWBP2DN1YF</goodsDescription><goodsNumber>5.000</goodsNumber><goodsUnit>S01</goodsUnit><comValue>100.00</comValue><valueUnit>CNY</valueUnit></orderItems><feedBackOrderCustomers><logisticsOrderId>LOS36100001120690200</logisticsOrderId><itemNumber>10</itemNumber><customerType>AG</customerType><customerId>70057297</customerId><name>C店商户苏宁</name><country>CN</country><city>南京市</city><address>南京市玄武区</address><zipCode>210012</zipCode><fixedLineTelephone>02566996699</fixedLineTelephone><mobilePhone>15295599233</mobilePhone></feedBackOrderCustomers></orderImformation><orderExpBill><logisticOrderId>LOS36300000945410000</logisticOrderId><expressCode>62K900000210</expressCode><btcOrder>BTC01631803170701</btcOrder><btcItemOrder/><name>1asd2</name><telNumber>18513891234</telNumber><province>江苏省</province><city>南京市</city><district>栖霞区</district><adress>仙鹤门仙鹤名苑28-608</adress><firstPlantCrossing/><setLocation/><preparedTime>01:00:00</preparedTime><note/><shipCondition>配送</shipCondition><rePrintFlag/><valueAddTaxFlag/><shipTypeFlag>陆</shipTypeFlag><obligateFlag/><lastPlantCrossing/><route>郑州保税仓→南京小件→WL</route><lastLogisticStation>外设单位编码描述测试</lastLogisticStation><realDeliDate>2018-03-19</realDeliDate><realDeliTime>09:00:00</realDeliTime><paymmentMode>线上下单门店支付</paymmentMode><cashOnDeliveryValue>100.00</cashOnDeliveryValue><expressCompany>0080000013</expressCompany><expressCompanyExcode>444033803247</expressCompanyExcode><originCode>025</originCode><destCode>025</destCode><createTime>17:27:39</createTime><createDate>2018-03-17</createDate></orderExpBill><orderDeclare><orderDeclareHead><orderNo>BOL031707</orderNo><ieFlag>I</ieFlag><importType>1</importType><inOutDateStr>2018-03-17 17:07:17</inOutDateStr><logisCompanyCode>1505130005</logisCompanyCode><logisCompanyName>江苏苏宁物流有限公司</logisCompanyName><companyName>江苏苏宁易购电子商务有限公司</companyName><companyCode>PTE51001410280000005</companyCode><wayBill/><tradeCountry>502</tradeCountry><packNo>1</packNo><grossWeight>1.500</grossWeight><netWeight>1.500</netWeight><warpType>2</warpType><remark>37</remark><enteringPerson>9999</enteringPerson><enteringCompanyName>9999</enteringCompanyName><senderName>versace1563212333333范思哲海外专营店</senderName><consignee>无名</consignee><consigneeAddress>江苏南京市玄武区徐庄苏宁总部鼓楼1号</consigneeAddress><senderCountry>502</senderCountry><payerName>无名</payerName><payerPhoneNumber>13951783938</payerPhoneNumber><paperType>01</paperType><paperNumber>320102198001010059</paperNumber><freight>10.0000</freight><insuranceFee/><payTaxAmount>0.0000</payTaxAmount><worth>109.0000</worth><currCode>142</currCode><mainGName>移动端苏宁内测试购专</mainGName><isAuthorize>1</isAuthorize><paySerialNo/><payType/><payTime/><payMoney/><otherPayPrice/><goodsPriceIncludeTax/><senderProvince/><senderAddr/><senderZip/><senderTel/></orderDeclareHead><orderDeclareItems><logisticOrderId>LOS36300000945410000</logisticOrderId><expressCode>62K900000210</expressCode><orderNo>BOL031707</orderNo><orderItemNo>BOL031707</orderItemNo><goodsOrder>1</goodsOrder><codeTs>11</codeTs><goodsItemNo>000000000690105971</goodsItemNo><goodsName>移动端苏宁内测试购专</goodsName><goodsModel/><originCountry>701</originCountry><tradeCurr>142</tradeCurr><tradeTotal>99.0000</tradeTotal><declPrice>99.00</declPrice><declTotalPrice>99.0000</declTotalPrice><useTo>11</useTo><declareCount>1</declareCount><goodsUnit>011</goodsUnit><goodsGrossWeight>1.500</goodsGrossWeight><firstUnit>011</firstUnit><firstCount>1</firstCount><postalTax/></orderDeclareItems></orderDeclare></orders></ordersList></inputData>";
		//库存
//		this.requestType = HttpServerManagerService.requestType_inventory;
//		this.content = "<inputList><inventoryOrder><inventoryDetailHead><messageId>1df3154d823c49f7a13bbb69801d94cd</messageId><logisticsOrderId>LOS36100001120690200</logisticsOrderId><businessType>C061</businessType><statusCode>1131</statusCode><offsetFlag/><note>天津外部公司测试</note></inventoryDetailHead><inventoryDetailItems><itemNumber>10</itemNumber><goodsCode>20166666</goodsCode><goodsDescription>Rafferty&apos;s Garden 婴儿辅食 南瓜苹果玉米泥 120克/袋 海外进口 蔬果泥 </goodsDescription><goodsNumber>99</goodsNumber><goodsUnit>G</goodsUnit><inventoryType>A</inventoryType><keyValueAdd/></inventoryDetailItems></inventoryOrder></inputList>";
		//装载
//		this.requestType = HttpServerManagerService.requestType_load;
//		this.content = "<LoadHead><loadContents><loadContent><loadContentId>1</loadContentId><outorderId>6666666666</outorderId></loadContent><loadContent><loadContentId>2</loadContentId><outorderId>7777777777</outorderId></loadContent></loadContents><loadHeadId>12</loadHeadId><loadId>1736474588</loadId><total>2</total><tracyNum>3</tracyNum><TotalWeight>2.5</TotalWeight><CarEcNo>苏A234234</CarEcNo></LoadHead>";
		
		this.requestType = requestType;
		this.content = content;
		this.signKey = signKey;
	}

	@Override
	public Object call() throws CommonException {
		
		System.out.println("【开始】数据处理："+signKey);
		
		String result = "";
//		System.out.println("【content】："+content);
		if(requestType.equals(HttpServerManagerService.requestType_Order)){
			//处理订单数据
			result = handleXml_Order(content);
		}
		else if(requestType.equals(HttpServerManagerService.requestType_inventory)){
			//处理库存数据
			result = handleXml_Inventory(content);
		}
		else if(requestType.equals(HttpServerManagerService.requestType_load)){
			//处理装载数据
			result = handleXml_Load(content);
		}
		else if(requestType.equals(HttpServerManagerService.requestType_listRelease)){
			//处理装载数据
			result = handleXml_ListRelease(content);
		}
		
		System.out.println("【结束】数据处理："+signKey);
		
		return result;
	}
	
	
	//处理订单数据
	private String handleXml_Order(String xmlString){
		
		String xmlReturnString = "";

		System.out.println("---------------------------【FPAPI_ORDER】-------------------------------");
		//入库
//		商品的入库流程如下：
//		收到订单报文中<businessType>等于C061时，将报文中<orderItems>中内容插入t_new_import_receipt表，一个<orderItems>对应一行记录。没有进一步操作。
//		<goodsCode>插入SKU
//		<goodsDescription>插入DESCRIPTION
//		<goodsNumber>插入EXPECT_QTY 
//		<logisticsOrderId>插入RECEIPT_NO
//		CREAT_DATE填当前日期的YYYYMMDD
		
		Map head = null;
		
		Map result = new HashMap();

		try{
			//判断businessType
			head = XmlUtil.parseXmlFPAPI_SingleNodes(xmlString,"//inputData/ordersList/orders/orderImformation/orderHead/child::*");
			
			String businessType = null;

			if (head != null && head.containsKey("businessType")
					&& head.get("businessType") != null) {
				businessType = (String) head.get("businessType");
			}
			
			// 获取资源文件
			ResourceBundle bundle = CommonUtil.getMessageMappingResource("CEB_SN");
			
			String checkBusinessType_C061 = CommonUtil.getSystemConfigProperty("checkOrderBusinessType_C061");
			String checkBusinessType_C005 = CommonUtil.getSystemConfigProperty("checkOrderBusinessType_C005");
			//报文入库
			if(checkBusinessType_C061.equals(businessType)){
				//C061报文处理
				result = handleOrderC061(xmlString,bundle);
				
			}else if(checkBusinessType_C005.equals(businessType)){
				//C005报文处理
				result = handleOrderC005(head,xmlString,bundle);
			}
		}catch(Exception e){
			e.printStackTrace();
			result.put("isSuccess", false);
			result.put("errorMsg", "系统异常");
		}

		//构建返回数据
		List<LinkedMap> dataList = new ArrayList<LinkedMap>();
		for(int i=0;i<1;i++){
			LinkedMap resultData = new LinkedMap();
			resultData.put("logisticsOrderId", head!=null?head.get("logisticsOrderId"):"");
			resultData.put("success", result.get("isSuccess"));
			resultData.put("errorCode", "");
			resultData.put("errorMsg", result.get("errorMsg"));
			dataList.add(resultData);
		}
		String root = CommonUtil.getSystemConfigProperty("orderReceiptRoot");
		String firstElement = CommonUtil.getSystemConfigProperty("orderFirstElement");
		
		xmlReturnString = XmlUtil.generalReceiptXml_FP(root,firstElement,dataList);

		return xmlReturnString;
	}
	
	//处理订单数据
	private String handleXml_Inventory(String xmlString) {

		String xmlReturnString = "";

		System.out
				.println("---------------------------【FPAPI_INVENTORY】-------------------------------");
		
//		收到库存明细同步报文中<businessType>等于C061时，两步操作。
		
//		第一步将报文中<inventoryDetailItems>的<goodsNumber>的值写入t_new_import_receipt表已有数据的ACTUAL_QTY中。
//		1.inventoryDetailHead.logisticsOrderId = RECEIPT_NO
//		2.inventoryDetailItems.goodsCode = SKU

//		第二步将报文中内容插入账册表t_new_import_books
//		<goodsCode>插入SKU
//		<goodsDescription>插入DESCRIPTION
//		<goodsNumber>插入QTY 
//		ADD_REDUCE_FLAG固定填1
//		<logisticsOrderId>插入ORDER_NO
//		CREAT_DATE填当前日期的YYYYMMDD
//		没有进一步操作。
		
		Map head = null;
		boolean isSuccess = true;

		try{
			//判断businessType
			head = XmlUtil.parseXmlFPAPI_SingleNodes(xmlString,"//inputList/inventoryOrder/inventoryDetailHead/child::*");
			
			String businessType = null;
			String logisticsOrderId = head.get("logisticsOrderId")!=null?head.get("logisticsOrderId").toString():"";
			
			if (head != null && head.containsKey("businessType")
					&& head.get("businessType") != null) {
				businessType = (String) head.get("businessType");
			}
			
			String checkBusinessType = CommonUtil.getSystemConfigProperty("checkInventoryBusinessType_C061");
			
			// 获取资源文件
			ResourceBundle bundle = CommonUtil.getMessageMappingResource("CEB_SN");
			//报文入库
			if(checkBusinessType.equals(businessType)){
				List<Map> items =  XmlUtil.parseXmlFPAPI_MulitpleNodes(xmlString,"//inputList/inventoryOrder/inventoryDetailItems");

				SimpleDateFormat sf = CommonUtil.getDateFormatter(CommonDefine.COMMON_FORMAT_1);
				
				List<String> colNames = new ArrayList<String>();
				List<Object> colValues = new ArrayList<Object>();
				
				for (Map item : items) {
					Map data = new HashMap();
					//转换
					for(Object key:item.keySet()){
						if(bundle.containsKey("inventory_"+key.toString())){
							data.put(bundle.getObject("inventory_"+key.toString()), item.get(key));
						}
					}
					//更新t_new_import_receipt.ACTUAL_QTY, 条件SKU和RECEIPT_NO
					String sku = data.get("SKU").toString();
					
					colNames.clear();
					colValues.clear();
					colNames.add("RECEIPT_NO");
					colNames.add("SKU");
					colValues.add(logisticsOrderId);
					colValues.add(sku);
					
					List<Map<String, Object>> rows = commonManagerMapper.selectTableListByNVList("t_new_import_receipt", colNames, colValues,null,null);
					
					if(rows.size()>0){
						for(Map row:rows){
							colNames.clear();
							colValues.clear();
							colNames.add("ACTUAL_QTY");
							colValues.add(data.get("QTY"));
							//更新数据
							commonManagerMapper.updateTableByNVList("t_new_import_receipt", "RECEIPT_ID", row.get("RECEIPT_ID"), colNames, colValues);
						}
						// 数据入库
						data.put("ORDER_NO", logisticsOrderId);
						data.put("ADD_REDUCE_FLAG", "1");
						data.put("CREAT_DATE", sf.format(new Date()));
						data.put("CREAT_TIME", new Date());
						Map primary = new HashMap();
						primary.put("primaryId", null);
						commonManagerMapper.insertTableByNVList("t_new_import_books",
								new ArrayList<String>(data.keySet()),
								new ArrayList<Object>(data.values()), primary);
					}else{
						isSuccess = false;
					}
				}
			}
		}catch(Exception e){
			isSuccess = false;
		}

		// 返回数据构建
		List<LinkedMap> dataList = new ArrayList<LinkedMap>();

		for (int i = 0; i < 1; i++) {
			LinkedMap resultData = new LinkedMap();
			resultData.put("messageId",
					head.get("messageId"));
			resultData.put("success", isSuccess);
			resultData.put("errorCode", "");
			resultData.put("errorMsg", "");
			dataList.add(resultData);
		}
		String root = CommonUtil.getSystemConfigProperty("inventoryReceiptRoot");
		String firstElement = CommonUtil
				.getSystemConfigProperty("inventoryFirstElement");

		xmlReturnString = XmlUtil.generalReceiptXml_FP(root, firstElement,
				dataList);

		return xmlReturnString;
	}
	
	
	// 处理装载数据
	private String handleXml_Load(String xmlString) {

		String xmlReturnString = "";

		System.out
				.println("---------------------------【FPAPI_Load】-------------------------------");
		// 入库
		// <loadId>插入t_new_import_load.LOAD_NO
		// <total>插入t_new_import_load.TOTAL
		// <TotalWeight>插入t_new_import_load.WEIGHT
		// <CarEcNo>插入t_new_import_load.CAR_NO
		// <tracyNum>插入t_new_import_load.TRACY_NUM
		// STATUS固定填1
		//
		// t_new_import_load.LOAD_ID插入t_new_import_load_detail.LOAD_ID
		// <outorderId>插入t_new_import_load_detail.ORDER_NO

		Map head = null;
		boolean isSuccess = true;

		try {
			// 判断businessType
			head = XmlUtil.parseXmlFPAPI_SingleNodes(xmlString,
					"//LoadHead/child::*");
			// 获取资源文件
			ResourceBundle bundle = CommonUtil
					.getMessageMappingResource("CEB_SN");

			SimpleDateFormat sf = CommonUtil
					.getDateFormatter(CommonDefine.COMMON_FORMAT_1);
			// 装载单入库
			// 转换
			Map mainData = new HashMap();
			for (Object key : head.keySet()) {
				if (bundle.containsKey("load_" + key.toString())) {
					mainData.put(bundle.getObject("load_" + key.toString()),
							head.get(key));
				}
			}
			mainData.put("STATUS", "1");
			mainData.put("CREAT_DATE", sf.format(new Date()));
			mainData.put("CREAT_TIME", new Date());
			Map primary = new HashMap();
			primary.put("primaryId", null);
			commonManagerMapper.insertTableByNVList("t_new_import_load",
					new ArrayList<String>(mainData.keySet()),
					new ArrayList<Object>(mainData.values()), primary);

			// 装载单详细信息入库
			List<Map> items = XmlUtil.parseXmlFPAPI_MulitpleNodes(xmlString,
					"//LoadHead/loadContents/loadContent");

			for (Map item : items) {
				Map data = new HashMap();
				// 转换
				for (Object key : item.keySet()) {
					if (bundle.containsKey("load_" + key.toString())) {
						data.put(bundle.getObject("load_" + key.toString()),
								item.get(key));
					}
				}
				
				// String uniqueCol="SKU";
				// String primaryCol="RECEIPT_ID";
				// // 货号唯一性校验
				// uniqueCheck("t_new_import_receipt",uniqueCol,data.get(uniqueCol),primaryCol,data.get(primaryCol),false);
				// 数据入库
				data.put("LOAD_ID", primary.get("primaryId"));
//				data.put("CREAT_DATE", sf.format(new Date()));
				data.put("CREAT_TIME", new Date());
				Map primarySub = new HashMap();
				primarySub.put("primaryId", null);
				commonManagerMapper.insertTableByNVList("t_new_import_load_detail",
						new ArrayList<String>(data.keySet()),
						new ArrayList<Object>(data.values()), primarySub);
			}
		} catch (Exception e) {
			e.printStackTrace();
			isSuccess = false;
		}

		// 构建返回数据
		List<LinkedMap> dataList = new ArrayList<LinkedMap>();
		for (int i = 0; i < 1; i++) {
			LinkedMap resultData = new LinkedMap();
			resultData.put("loadHeadId",
					head != null ? head.get("loadHeadId") : "");
			resultData.put("loadId",
					head != null ? head.get("loadId") : "");
			resultData.put("returnMessage", isSuccess?"接收成功":"接收失败");
//			resultData.put("errorCode", "");
//			resultData.put("errorMsg", "");
			dataList.add(resultData);
		}
		String root = CommonUtil.getSystemConfigProperty("loadReceiptRoot");

		xmlReturnString = XmlUtil.generalReceiptXml_FP(root,null,
				dataList);

		return xmlReturnString;
	}
	
	private String handleXml_ListRelease(String xmlString) {

		String xmlReturnString = "";

		System.out
				.println("---------------------------【FPAPI_ListRelease】-------------------------------");

		Map head = null;
		boolean isSuccess = true;

		try {
			//包含数据orderNo、invtNo，hgReturnstatus，sjReturnStatus
			head = XmlUtil.parseXmlFPAPI_SingleNodes(xmlString,
					"//InventoryReturn/child::*");
			
			//向苏宁回传订单状态
			System.out.println(head);
		} catch (Exception e) {
			e.printStackTrace();
			isSuccess = false;
		}

		// 构建返回数据
		List<LinkedMap> dataList = new ArrayList<LinkedMap>();
		for (int i = 0; i < 1; i++) {
			LinkedMap resultData = new LinkedMap();
			resultData.put("loadHeadId",
					head != null ? head.get("loadHeadId") : "");
			resultData.put("loadId",
					head != null ? head.get("loadId") : "");
			resultData.put("returnMessage", isSuccess?"接收成功":"接收失败");
//			resultData.put("errorCode", "");
//			resultData.put("errorMsg", "");
			dataList.add(resultData);
		}
		String root = CommonUtil.getSystemConfigProperty("loadReceiptRoot");

		xmlReturnString = XmlUtil.generalReceiptXml_FP(root,null,
				dataList);

		return xmlReturnString;
	}

	
	private Map handleOrderC061(String xmlString,ResourceBundle bundle){
		Map result = new HashMap();
		result.put("isSuccess", true);
		
		SimpleDateFormat sf = CommonUtil.getDateFormatter(CommonDefine.COMMON_FORMAT_1);
		List<Map> items =  XmlUtil.parseXmlFPAPI_MulitpleNodes(xmlString,"//inputData/ordersList/orders/orderImformation/orderItems");

		for (Map item : items) {
			Map data = new HashMap();
			//转换
			for(Object key:item.keySet()){
				if(bundle.containsKey("order_"+key.toString())){
					data.put(bundle.getObject("order_"+key.toString()), item.get(key));
				}
			}
//			String uniqueCol="SKU";
//			String primaryCol="RECEIPT_ID";
//			// 货号唯一性校验
//			uniqueCheck("t_new_import_receipt",uniqueCol,data.get(uniqueCol),primaryCol,data.get(primaryCol),false);
			// 数据入库
			data.put("CREAT_DATE", sf.format(new Date()));
			data.put("CREAT_TIME", new Date());
			Map primary = new HashMap();
			primary.put("primaryId", null);
			commonManagerMapper.insertTableByNVList("t_new_import_receipt",
					new ArrayList<String>(data.keySet()),
					new ArrayList<Object>(data.values()), primary);
		}
		return result;
		
	}
	
	private Map handleOrderC005(Map head,String xmlString,ResourceBundle bundle){
		Map result = new HashMap();
		
		result.put("isSuccess", true);
		
		SimpleDateFormat sf = CommonUtil.getDateFormatter(CommonDefine.COMMON_FORMAT_1);
		//t_new_import_inventory表中查找数据
		List<String> colNames = new ArrayList<String>();
		List<Object> colValues = new ArrayList<Object>();
		colNames.add("LOGISTICS_NO");
		colValues.add(head.get("logisticsOrderId"));
		List<Map<String, Object>> rows = commonManagerMapper.selectTableListByNVList("t_new_import_inventory", colNames, colValues,null,null);
		
		//如果存在，直接向苏宁返回“出库单已存在”，没有进一步操作。
		if(rows.size()>0){
			//返回错误，出库单已存在--直接返回true，当做成功
			result.put("isSuccess", true);
//			result.put("errorMsg", "出库单已存在");
		}else{
			List<Map> items =  XmlUtil.parseXmlFPAPI_MulitpleNodes(xmlString,"//inputData/ordersList/orders/orderImformation/orderItems");
			
			//循环商品，看是否库存不足
			boolean isSkuEnough = true;
			List<BookOrderModel> bookOrders = new ArrayList<BookOrderModel>();
			for(Map item:items){
				String goodsCode = item.get("goodsCode")!=null?item.get("goodsCode").toString():"";
				String goodsNumber = item.get("goodsNumber")!=null?item.get("goodsNumber").toString():"";
				
				double goodsNumberDouble = !goodsNumber.isEmpty()?Double.valueOf(goodsNumber):0;
				//t_new_import_books表中查找数据
				//ADD_REDUCE_FLAG=1
				//SKU=<orderItems><goodsCode>
				//QTY>=<orderItems><goodsNumber>
				List<Map> dataList = snCommonManagerMapper.selectBookNumber(goodsCode, goodsNumberDouble);
				
				if(dataList.size() == 0){
					isSkuEnough = false;
					break;
				}else{
					BookOrderModel bookOrder = new BookOrderModel();
					bookOrder.setHead(head);
					bookOrder.setOrderitem(item);
					bookOrder.setBookItems(dataList);
					bookOrders.add(bookOrder);
				}
			}
			if(isSkuEnough){
				List<Map> skuList;
				for(BookOrderModel xxx:bookOrders){
					skuList = xxx.getBookItems();
					//QTY最小的，CREAT_TIME最久远的记录减去<orderItems><goodsNumber>。
					Collections.sort(skuList, new Comparator<Map>() {
			            public int compare(Map o1, Map o2) {
			            	double qty1 = Double.valueOf(o1.get("QTY").toString());
			            	double qty2 = Double.valueOf(o2.get("QTY").toString());
//			            	SimpleDateFormat sf = CommonUtil.getDateFormatter(CommonDefine.COMMON_FORMAT);
//			            	Date creatTime1 = null;
//			            	Date creatTime2 = null;
//							try {
//								creatTime1 = sf.parse(o1.get("CREAT_TIME").toString());
//								creatTime2 = sf.parse(o2.get("CREAT_TIME").toString());
//							} catch (ParseException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
			            	if(qty1>qty2){
			            		return 1;
			            	}else{
			            		return 0;
			            	}
			            }
			        });
					//插入新记录
					Map recorder = skuList.get(0);
					colNames.clear();
					colValues.clear();
					double updateQty = Double.valueOf(recorder.get("QTY")
							.toString())
							- Double.valueOf(xxx.getOrderitem()
									.get("goodsNumber").toString());
					recorder.put("QTY", updateQty);
					for(Object key:recorder.keySet()){
						colNames.add(key.toString());
						colValues.add(recorder.get(key));
					}
					//更新数据
					commonManagerMapper.updateTableByNVList(
							"t_new_import_books", "BOOKS_ID",
							recorder.get("BOOKS_ID"), colNames,
							colValues);	
					
					//插入新记录
					Map primary = new HashMap();
					primary.put("primaryId", null);
					colNames.clear();
					colValues.clear();
					colNames.add("SKU");
					colNames.add("DESCRIPTION");
					colNames.add("QTY");
					colNames.add("GOODS_SERIALNO");
					colNames.add("DECL_NO");
					colNames.add("CON_MODEL");
					colNames.add("CON_NUM");
					colNames.add("CON_NO");
					colNames.add("ADD_REDUCE_FLAG");
					colNames.add("ORDER_NO");
					colNames.add("CREAT_DATE");
					colNames.add("CREAT_TIME");
					colValues.add(xxx.getOrderitem().get("goodsCode"));
					colValues.add(xxx.getOrderitem().get("goodsDescription"));
					colValues.add(xxx.getOrderitem().get("goodsNumber"));
					colValues.add(recorder.get("GOODS_SERIALNO"));
					colValues.add(recorder.get("DECL_NO"));
					colValues.add(recorder.get("CON_MODEL"));
					colValues.add(recorder.get("CON_NUM"));
					colValues.add(recorder.get("CON_NO"));
					colValues.add("2");
					colValues.add(xxx.getHead().get("btcOrderId"));
					colValues.add(sf.format(new Date()));
					colValues.add(new Date());
					commonManagerMapper.insertTableByNVList("t_new_import_books", colNames, colValues, primary);
				}
				//第四步  插入t_new_import_inventory表和t_new_import_inventory_detail表
				String id = insertInventory(xmlString);
				
				//第五步 将t_new_import_inventory和t_new_import_inventory_detail表中部分数据组成xml，先保存本地，在通过接口发送
				String xmlStringData = generalRequestXml4TJ(id,bundle);
				
				//第五步 向天津外运发送清单数据
				Map reponse = postToTJ(xmlStringData);
				
				//回传数据处理
				
				
			}else{
				//返回错误，库存不足
				result.put("isSuccess", false);
				result.put("errorMsg", "库存不足");
			}
		}
		
		return result;
	}
	
	private Map postToTJ(String xmlData){
		String partner_id = CommonUtil.getSystemConfigProperty("partner_id");
		String business_type = "STOCK_INVENTORY_INFO_ADD";
		String data_type = CommonUtil.getSystemConfigProperty("data_type");
		//请求数据
		String requestData = "partner_id="+partner_id+"&business_type="+business_type+"&data_type="+data_type+"&data="+xmlData;

		//发送http请求
		Map<String, Object> head = new HashMap<String, Object>();
		head.put("Content-Type", CommonUtil.getSystemConfigProperty("Content_Type"));
		//获取url
		String url = CommonUtil.getSystemConfigProperty("TJ_requestUrl");
		
		Map result = HttpUtil.doPost4TJ(url, head, requestData);
		
		return result;
	}
	
	private String generalRequestXml4TJ(String id,ResourceBundle bundle){
		int idInt = Integer.valueOf(id);
		//step 1 生成xml
		LinkedHashMap InventoryHead = new LinkedHashMap();
		if(snCommonManagerMapper.selectInventoryHead(idInt)!=null){
			LinkedHashMap item = snCommonManagerMapper.selectInventoryHead(idInt).get(0);
			//转换
			if(item!=null){
				for(Object key:item.keySet()){
					if(bundle.containsKey("TJ_HEAD_"+key.toString())){
						InventoryHead.put(bundle.getObject("TJ_HEAD_"+key.toString()), item.get(key));
					}else{
						InventoryHead.put(key.toString(), item.get(key));
					}
				}
			}
		}
		
		LinkedHashMap InventoryList = new LinkedHashMap();
		if(snCommonManagerMapper.selectInventoryList(idInt)!=null){
			LinkedHashMap item = snCommonManagerMapper.selectInventoryList(idInt).get(0);
			//转换
			if(item!=null){
				for(Object key:item.keySet()){
					if(bundle.containsKey("TJ_LIST_"+key.toString())){
						InventoryList.put(bundle.getObject("TJ_LIST_"+key.toString()), item.get(key));
					}else{
						InventoryList.put(key.toString(), item.get(key));
					}
				}
			}
		}
		
		
		LinkedHashMap IODeclContainerList = new LinkedHashMap();
		if(snCommonManagerMapper.selectIODeclContainerList(idInt)!=null){
			LinkedHashMap item = snCommonManagerMapper.selectIODeclContainerList(idInt).get(0);
			//转换
			if(item!=null){
				for(Object key:item.keySet()){
					if(bundle.containsKey("TJ_IO_"+key.toString())){
						IODeclContainerList.put(bundle.getObject("TJ_IO_"+key.toString()), item.get(key));
					}else{
						IODeclContainerList.put(key.toString(), item.get(key));
					}
				}
			}
			
		}
		LinkedHashMap IODeclOrderRelationList = new LinkedHashMap();
		if(snCommonManagerMapper.selectIODeclOrderRelationList(idInt)!=null){
			LinkedHashMap item = snCommonManagerMapper.selectIODeclOrderRelationList(idInt).get(0);
			//转换
			if(item!=null){
				for(Object key:item.keySet()){
					if(bundle.containsKey("TJ_IO_"+key.toString())){
						IODeclOrderRelationList.put(bundle.getObject("TJ_IO_"+key.toString()), item.get(key));
					}else{
						IODeclOrderRelationList.put(key.toString(), item.get(key));
					}
				}
			}
		}
		
		LinkedHashMap BaseTransfer = snCommonManagerMapper.selectBaseTransfer();

		String resultXmlString = XmlUtil.generalRequestXml4TJ_621(InventoryHead,
				InventoryList, IODeclContainerList, IODeclOrderRelationList,
				BaseTransfer,bundle);
		
		return resultXmlString;
	}
	
	//插入t_new_import_inventory表和t_new_import_inventory_detail表
	private String insertInventory(String xmlString){
		Map head = XmlUtil.parseXmlFPAPI_SingleNodes(xmlString,"//inputData/ordersList/orders/orderImformation/orderHead/child::*");
		Map orderDeclareHead = XmlUtil.parseXmlFPAPI_SingleNodes(xmlString,"//inputData/ordersList/orders/orderDeclare/orderDeclareHead/child::*");
		List<Map> orderDeclareItems =  XmlUtil.parseXmlFPAPI_MulitpleNodes(xmlString,"//inputData/ordersList/orders/orderDeclare/orderDeclareItems");
		
		SimpleDateFormat sf = CommonUtil.getDateFormatter(CommonDefine.RETRIEVAL_TIME_FORMAT);
		
		SimpleDateFormat sf1 = CommonUtil.getDateFormatter(CommonDefine.COMMON_FORMAT_1);
		
		List<String> colNames = new ArrayList<String>();
		List<Object> colValues = new ArrayList<Object>();
		Map primary = new HashMap();
		primary.put("primaryId", null);
		
		colNames.add("GUID");
		colValues.add(CommonUtil.generalGuid(CommonDefine.GUID_FOR_LOGISTICS_SN_1,10,"t_new_import_inventory"));
		
		colNames.add("CUSTOM_CODE");
		colValues.add("0213");
		
		colNames.add("APP_TYPE");
		colValues.add("1");
		
		colNames.add("APP_TIME");
		colValues.add(sf.format(new Date()));
		
		colNames.add("APP_STATUS");
		colValues.add("2");
		
		colNames.add("COP_NO");
		colValues.add(head.get("taskOrderid"));
		
		colNames.add("PRE_NO");
		colValues.add("");
		
		colNames.add("EBC_CODE");
		colValues.add("3201966A69");
		
		colNames.add("EBC_NAME");
		colValues.add("江苏苏宁易购电子商务有限公司");
		
		colNames.add("EBP_CODE");
		colValues.add("3201966A69");
		
		colNames.add("EBP_NAME");
		colValues.add("江苏苏宁易购电子商务有限公司");
		
		colNames.add("ORDER_NO");
		colValues.add(head.get("btcOrderId"));
		
		colNames.add("LOGISTICS_NO");
		colValues.add(head.get("logisticsOrderId"));
		
		colNames.add("LOGISTICS_CODE");
		colValues.add("3201961A28");
		
		colNames.add("LOGISTICS_NAME");
		colValues.add("江苏苏宁物流有限公司");
		
		colNames.add("ASSURE_CODE");
		colValues.add("3201966A69");
		
		colNames.add("EMS_NO");
		colValues.add("WL0213201600000003");
		
		colNames.add("INVT_NO");
		colValues.add("");
		
		colNames.add("DECL_TIME");
		colValues.add(sf1.format(new Date()));
		
		colNames.add("PORT_CODE");
		colValues.add("0213");
		
		colNames.add("IE_DATE");
		colValues.add(null);
		
		colNames.add("BUYER_NAME");
		colValues.add(orderDeclareHead.get("consignee"));
		
		colNames.add("BUYER_IDTYPE");
		colValues.add("1");
		
		colNames.add("BUYER_IDNUMBER");
		colValues.add(orderDeclareHead.get("paperNumber"));
		
		colNames.add("BUYER_TELEPHONE");
		colValues.add(orderDeclareHead.get("payerPhoneNumber"));
		
		colNames.add("CONSIGNEE_ADDRESS");
		colValues.add(orderDeclareHead.get("consigneeAddress"));
		
		colNames.add("AGENT_CODE");
		colValues.add("1207980025");
		
		colNames.add("AGENT_NAME");
		colValues.add("天津中外运报关有限公司");
		
		colNames.add("AERA_CODE");
		colValues.add("1207610251");
		
		colNames.add("AERA_NAME");
		colValues.add("天津中外运国际物流发展有限公司");
		
		colNames.add("TRADE_MODE");
		colValues.add("1210");
		
		colNames.add("TRAF_MODE");
		colValues.add("Y");
		
		colNames.add("TRAF_NO");
		colValues.add("");
		
		colNames.add("LOCT_NO");
		colValues.add("");
		
		colNames.add("LICENSE_NO");
		colValues.add("");
		
		colNames.add("COUNTRY");
		colValues.add("142");
		
		colNames.add("CURRENCY");
		colValues.add("142");

		colNames.add("FREIGHT");
		if (orderDeclareHead.get("freight") != null
				&& !orderDeclareHead.get("freight").toString().isEmpty()) {
			colValues.add(orderDeclareHead.get("freight"));
		}else{
			colValues.add(null);
		}
		
		
		colNames.add("INSURE_FEE");
		if (orderDeclareHead.get("insuranceFee") != null
				&& !orderDeclareHead.get("insuranceFee").toString().isEmpty()) {
			colValues.add(orderDeclareHead.get("insuranceFee"));
		}else{
			colValues.add(null);
		}
		
		colNames.add("WRAP_TYPE");
		colValues.add(orderDeclareHead.get("warpType"));
		
		colNames.add("PACK_NO");
		colValues.add("1");
		
		colNames.add("GROSS_WEIGHT");
		if (orderDeclareHead.get("grossWeight") != null
				&& !orderDeclareHead.get("grossWeight").toString().isEmpty()) {
			colValues.add(orderDeclareHead.get("grossWeight"));
		}else{
			colValues.add(null);
		}
		
		colNames.add("NET_WEIGHT");
		if (orderDeclareHead.get("netWeight") != null
				&& !orderDeclareHead.get("netWeight").toString().isEmpty()) {
			colValues.add(orderDeclareHead.get("netWeight"));
		}else{
			colValues.add(null);
		}
		
		colNames.add("PAY_SERIAL_NO");
		if (orderDeclareHead.get("paySerialNo") != null
				&& !orderDeclareHead.get("paySerialNo").toString().isEmpty()) {
			colValues.add(orderDeclareHead.get("paySerialNo"));
		}else{
			colValues.add(null);
		}
		
		colNames.add("WORTH");
		if (orderDeclareHead.get("worth") != null
				&& !orderDeclareHead.get("worth").toString().isEmpty()) {
			colValues.add(orderDeclareHead.get("worth"));
		}else{
			colValues.add(null);
		}
		
		colNames.add("NOTE");
		colValues.add("");
		
		colNames.add("CREAT_TIME");
		colValues.add(new Date());
		
		commonManagerMapper.insertTableByNVList("t_new_import_inventory", colNames, colValues, primary);
		
		for(Map orderDeclareItem:orderDeclareItems){
			
			colNames.clear();
			colValues.clear();
			Map primary_sub = new HashMap();
			primary_sub.put("primaryId", null);
			
			colNames.add("INVENTORY_ID");
			colValues.add(primary.get("primaryId"));
			
			colNames.add("GNUM");
			colValues.add(orderDeclareItem.get("goodsOrder"));
			
			colNames.add("ITEM_NO");
			colValues.add(orderDeclareItem.get("goodsItemNo"));
			
			colNames.add("ITEM_NAME");
			colValues.add(orderDeclareItem.get("goodsName"));
			
			colNames.add("G_CODE");
			colValues.add("");
			
			colNames.add("G_NAME");
			colValues.add(orderDeclareItem.get("goodsName"));
			
			colNames.add("G_MODEL");
			colValues.add(orderDeclareItem.get("goodsModel"));
			
			colNames.add("BARCODE");
			colValues.add("");
			
			colNames.add("COUNTRY");
			colValues.add(orderDeclareItem.get("originCountry"));
			
			colNames.add("CURRENCY");
			colValues.add(orderDeclareItem.get("tradeCurr"));
			
			colNames.add("QTY");
			if (orderDeclareItem.get("declareCount") != null
					&& !orderDeclareItem.get("declareCount").toString().isEmpty()) {
				colValues.add(orderDeclareItem.get("declareCount"));
			}else{
				colValues.add(null);
			}
			
			colNames.add("QTY1");
			if (orderDeclareItem.get("firstCount") != null
					&& !orderDeclareItem.get("firstCount").toString().isEmpty()) {
				colValues.add(orderDeclareItem.get("firstCount"));
			}else{
				colValues.add(null);
			}
			
			colNames.add("QTY2");
			if (orderDeclareItem.get("secondCount") != null
					&& !orderDeclareItem.get("secondCount").toString().isEmpty()) {
				colValues.add(orderDeclareItem.get("secondCount"));
			}else{
				colValues.add(null);
			}
			
			colNames.add("UNIT");
			colValues.add(orderDeclareItem.get("goodsUnit"));
			
			colNames.add("UNIT1");
			colValues.add(orderDeclareItem.get("firstUnit"));
			
			colNames.add("UNIT2");
			colValues.add(orderDeclareItem.get("secondUnit"));
			
			colNames.add("PRICE");
			if (orderDeclareItem.get("declPrice") != null
					&& !orderDeclareItem.get("declPrice").toString().isEmpty()) {
				colValues.add(orderDeclareItem.get("declPrice"));
			}else{
				colValues.add(null);
			}
			
			colNames.add("TOTAL_PRICE");
			if (orderDeclareItem.get("declTotalPrice") != null
					&& !orderDeclareItem.get("declTotalPrice").toString().isEmpty()) {
				colValues.add(orderDeclareItem.get("declTotalPrice"));
			}else{
				colValues.add(null);
			}

			colNames.add("CREAT_TIME");
			colValues.add(new Date());
			
			commonManagerMapper.insertTableByNVList("t_new_import_inventory_detail", colNames, colValues, primary_sub);
			
		}
		
		return primary.get("primaryId").toString();
	}

	public static void main(String arg[]){
		
		String logistics_interface = "<LoadHead><loadContents><loadContent><loadContentId>1</loadContentId><outorderId>6666666666</outorderId></loadContent><loadContent><loadContentId>2</loadContentId><outorderId>7777777777</outorderId></loadContent></loadContents><loadHeadId>12</loadHeadId><loadId>1736474588</loadId><total>2</total><tracyNum>3</tracyNum><TotalWeight>2.5</TotalWeight><CarEcNo>苏A234234</CarEcNo></LoadHead>";
 		
 		String data_digest = CommonUtil.makeSign(logistics_interface);
 		
 		System.out.println(data_digest);
 		try {
			System.out
					.println("logistics_interface="
							+ URLEncoder.encode(logistics_interface, "utf-8")
							+ "&data_digest=" + URLEncoder.encode(data_digest,"utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			System.out.println(URLEncoder.encode("helloworld","utf-8"));
			System.out.println(URLEncoder.encode("voQc3u6+f6pSflMPdw4ySQ==","utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("QTY", "10");
        map1.put("CREAT_TIME", "2018-09-14 21:46:17");
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("QTY", "8");
        map2.put("CREAT_TIME", "2018-09-14 23:46:17");
        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put("QTY", "12");
        map3.put("CREAT_TIME", "2018-09-14 22:46:17");
        list.add(map1);
        list.add(map3);
        list.add(map2);
        //排序前 
        for (Map<String, Object> map : list) {
            System.out.println(map);
        }
		
		
		Collections.sort(list, new Comparator<Map>() {
            public int compare(Map o1, Map o2) {
            	
            	SimpleDateFormat sf = CommonUtil.getDateFormatter(CommonDefine.COMMON_FORMAT);
            	
            	int qty1 = Integer.valueOf(o1.get("QTY").toString());
            	Date creatTime1 = null;
				try {
					creatTime1 = sf.parse(o1.get("CREAT_TIME").toString());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            	int qty2 = Integer.valueOf(o2.get("QTY").toString());
            	Date creatTime2 = null;
				try {
					creatTime2 = sf.parse(o2.get("CREAT_TIME").toString());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            	if(qty1 > qty2 || creatTime1.before(creatTime2)){
            		return 1;
            	}else{
            		return 0;
            	}
            }
        });
		
		System.out.println("-------------------");
        for (Map<String, Object> map : list) {
            System.out.println(map);
        }
	}
	
	
}
