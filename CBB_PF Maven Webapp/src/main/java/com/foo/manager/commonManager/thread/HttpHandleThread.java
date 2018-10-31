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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
public class HttpHandleThread implements Callable<Object> {
	@Resource
	private static CommonManagerMapper commonManagerMapper;
	@Resource
	private static SNCommonManagerMapper snCommonManagerMapper;

	private String requestType;
	private String content;
	private String signKey;

	public HttpHandleThread(String requestType, String content, String signKey) {
		if (commonManagerMapper == null) {
			commonManagerMapper = (CommonManagerMapper) SpringContextUtil
					.getBean("commonManagerMapper");
		}
		if (snCommonManagerMapper == null) {
			snCommonManagerMapper = (SNCommonManagerMapper) SpringContextUtil
					.getBean("SNCommonManagerMapper");
		}

		// 订单信息--C061
		// this.requestType = HttpServerManagerService.requestType_Order;
		// this.content =
		// "<inputData><ordersList><orders><orderImformation><orderHead><logisticsOrderId>LOS36100001120690200</logisticsOrderId><oriSys>LOS</oriSys><taskOrderid>36100001120690200</taskOrderid><thirdPartyCompany>0080010331</thirdPartyCompany><businessType>C061</businessType><announcedValue>100.00</announcedValue><valueUnit>CNY</valueUnit><expectDate>2015-09-07</expectDate><expectTime>11:38:10</expectTime></orderHead><orderItems><logisticsOrderId>LOS36100001120690200</logisticsOrderId><itemNumber>10</itemNumber><location>L971</location><locationDescription>天津口岸</locationDescription><storage>0001</storage><goodsCode>20166666</goodsCode><goodsDescription>长虹空调KFR35GWBP2DN1YF</goodsDescription><goodsNumber>5.000</goodsNumber><goodsUnit>S01</goodsUnit><comValue>100.00</comValue><valueUnit>CNY</valueUnit></orderItems><feedBackOrderCustomers><logisticsOrderId>LOS36100001120690200</logisticsOrderId><itemNumber>10</itemNumber><customerType>AG</customerType><customerId>70057297</customerId><name>C店商户苏宁</name><country>CN</country><city>南京市</city><address>南京市玄武区</address><zipCode>210012</zipCode><fixedLineTelephone>02566996699</fixedLineTelephone><mobilePhone>15295599233</mobilePhone></feedBackOrderCustomers></orderImformation></orders></ordersList></inputData>";
		// 订单信息--C005
//		 this.requestType = HttpServerManagerService.requestType_Order;
//		 this.content =
//		 "<inputData><ordersList><orders><orderImformation><orderHead><logisticsOrderId>LOS36100001120690200</logisticsOrderId><oriSys>LOS</oriSys><taskOrderid>36100001120690200</taskOrderid><thirdPartyCompany>0080010331</thirdPartyCompany><businessType>C005</businessType><btcOrderId>BOL031707</btcOrderId><announcedValue>100.00</announcedValue><valueUnit>CNY</valueUnit><expectDate>2015-09-07</expectDate><expectTime>11:38:10</expectTime></orderHead><orderItems><logisticsOrderId>LOS36100001120690200</logisticsOrderId><itemNumber>10</itemNumber><location>L971</location><locationDescription>天津口岸</locationDescription><storage>0001</storage><goodsCode>20166666</goodsCode><goodsDescription>长虹空调KFR35GWBP2DN1YF</goodsDescription><goodsNumber>5.000</goodsNumber><goodsUnit>S01</goodsUnit><comValue>100.00</comValue><valueUnit>CNY</valueUnit></orderItems><feedBackOrderCustomers><logisticsOrderId>LOS36100001120690200</logisticsOrderId><itemNumber>10</itemNumber><customerType>AG</customerType><customerId>70057297</customerId><name>C店商户苏宁</name><country>CN</country><city>南京市</city><address>南京市玄武区</address><zipCode>210012</zipCode><fixedLineTelephone>02566996699</fixedLineTelephone><mobilePhone>15295599233</mobilePhone></feedBackOrderCustomers></orderImformation><orderExpBill><logisticOrderId>LOS36300000945410000</logisticOrderId><expressCode>62K900000210</expressCode><btcOrder>BTC01631803170701</btcOrder><btcItemOrder/><name>1asd2</name><telNumber>18513891234</telNumber><province>江苏省</province><city>南京市</city><district>栖霞区</district><adress>仙鹤门仙鹤名苑28-608</adress><firstPlantCrossing/><setLocation/><preparedTime>01:00:00</preparedTime><note/><shipCondition>配送</shipCondition><rePrintFlag/><valueAddTaxFlag/><shipTypeFlag>陆</shipTypeFlag><obligateFlag/><lastPlantCrossing/><route>郑州保税仓→南京小件→WL</route><lastLogisticStation>外设单位编码描述测试</lastLogisticStation><realDeliDate>2018-03-19</realDeliDate><realDeliTime>09:00:00</realDeliTime><paymmentMode>线上下单门店支付</paymmentMode><cashOnDeliveryValue>100.00</cashOnDeliveryValue><expressCompany>0080000013</expressCompany><expressCompanyExcode>444033803247</expressCompanyExcode><originCode>025</originCode><destCode>025</destCode><createTime>17:27:39</createTime><createDate>2018-03-17</createDate></orderExpBill><orderDeclare><orderDeclareHead><orderNo>BOL031707</orderNo><ieFlag>I</ieFlag><importType>1</importType><inOutDateStr>2018-03-17 17:07:17</inOutDateStr><logisCompanyCode>1505130005</logisCompanyCode><logisCompanyName>江苏苏宁物流有限公司</logisCompanyName><companyName>江苏苏宁易购电子商务有限公司</companyName><companyCode>PTE51001410280000005</companyCode><wayBill/><tradeCountry>502</tradeCountry><packNo>1</packNo><grossWeight>1.500</grossWeight><netWeight>1.500</netWeight><warpType>2</warpType><remark>37</remark><enteringPerson>9999</enteringPerson><enteringCompanyName>9999</enteringCompanyName><senderName>versace1563212333333范思哲海外专营店</senderName><consignee>无名</consignee><consigneeAddress>江苏南京市玄武区徐庄苏宁总部鼓楼1号</consigneeAddress><senderCountry>502</senderCountry><payerName>无名</payerName><payerPhoneNumber>13951783938</payerPhoneNumber><paperType>01</paperType><paperNumber>320102198001010059</paperNumber><freight>10.0000</freight><insuranceFee/><payTaxAmount>0.0000</payTaxAmount><worth>109.0000</worth><currCode>142</currCode><mainGName>移动端苏宁内测试购专</mainGName><isAuthorize>1</isAuthorize><paySerialNo/><payType/><payTime/><payMoney/><otherPayPrice/><goodsPriceIncludeTax/><senderProvince/><senderAddr/><senderZip/><senderTel/></orderDeclareHead><orderDeclareItems><logisticOrderId>LOS36300000945410000</logisticOrderId><expressCode>62K900000210</expressCode><orderNo>BOL031707</orderNo><orderItemNo>BOL031707</orderItemNo><goodsOrder>1</goodsOrder><codeTs>11</codeTs><goodsItemNo>000000000690105971</goodsItemNo><goodsName>移动端苏宁内测试购专</goodsName><goodsModel/><originCountry>701</originCountry><tradeCurr>142</tradeCurr><tradeTotal>99.0000</tradeTotal><declPrice>99.00</declPrice><declTotalPrice>99.0000</declTotalPrice><useTo>11</useTo><declareCount>1</declareCount><goodsUnit>011</goodsUnit><goodsGrossWeight>1.500</goodsGrossWeight><firstUnit>011</firstUnit><firstCount>1</firstCount><postalTax/></orderDeclareItems></orderDeclare></orders></ordersList></inputData>";
		// 库存
//		 this.requestType = HttpServerManagerService.requestType_inventory;
//		 this.content =
//		 "<inputList><inventoryOrder><inventoryDetailHead><messageId>1df3154d823c49f7a13bbb69801d94cd</messageId><logisticsOrderId>1215001180620011114YG</logisticsOrderId><businessType>C005</businessType><statusCode>1131</statusCode><offsetFlag/><note>天津外部公司测试</note></inventoryDetailHead><inventoryDetailItems><itemNumber>10</itemNumber><goodsCode>20166666</goodsCode><goodsDescription>Rafferty&apos;s Garden 婴儿辅食 南瓜苹果玉米泥 120克/袋 海外进口 蔬果泥 </goodsDescription><goodsNumber>99</goodsNumber><goodsUnit>G</goodsUnit><inventoryType>A</inventoryType><keyValueAdd/></inventoryDetailItems></inventoryOrder></inputList>";
		// 装载
		// this.requestType = HttpServerManagerService.requestType_load;
		// this.content =
		// "<LoadHead><loadContents><loadContent><loadContentId>1</loadContentId><outorderId>6666666666</outorderId></loadContent><loadContent><loadContentId>2</loadContentId><outorderId>7777777777</outorderId></loadContent></loadContents><loadHeadId>12</loadHeadId><loadId>1736474588</loadId><total>2</total><tracyNum>3</tracyNum><TotalWeight>2.5</TotalWeight><CarEcNo>苏A234234</CarEcNo></LoadHead>";

		// requestType_listRelease
		// this.requestType = HttpServerManagerService.requestType_listRelease;
		// this.content =
		// "<InventoryReturn><orderNo>20180930001test</orderNo><invtNo/><returnStatus>800</returnStatus></InventoryReturn>";

		// sn_sku
		// this.requestType = HttpServerManagerService.requestType_sn_sku;
		// this.content =
		// "{content={\"cmmdtyInfo\":[{\"kunner\":\"RH007\",\"sncmmdtyCode\":\"000000010591492016\",\"cmmdtyName\":\"FANCL/芳珂 HTC胶原蛋白饮料 10日装*3 2018版\",\"measureUnit\":\"S01\",\"attributes\":\"\",\"cmmdtyType\":\"Z001\",\"cmmdtyGrp\":\"胶原蛋白\",\"brandCode\":\"芳珂(FANCL)\",\"cmmdtyLength\":\"1.000\",\"cmmdtyWidth\":\"1.000\",\"cmmdtyHeight\":\"1.000\",\"cmmdtyVolume\":\"0.001\",\"grossCmmdtyWeight\":\"1.000\",\"netCmmdtyWeight\":\"1.000\",\"totalShelfLife\":\"180\",\"coldChain\":\"\",\"bigsmall\":\"2\",\"cmmdtyFreightGrp\":\"90\",\"mprbs\":\"\",\"cmmdtyModel\":\"10日装*3\",\"cmmdtyOrigin\":\"日本\",\"weightFlag\":\"\",\"taste\":\"\",\"size\":\"\",\"colour\":\"\",\"keepEnvironment\":\"01\",\"cmmdtyFeatures\":\"14\",\"physicalForm\":\"01\",\"shape\":\"01\",\"keepMethod\":\"\",\"pcs\":\"\",\"cmmdtyHierrarchy\":\"000290DUU\"}],\"customerInfo\":[{\"kunner\":\"RH007\",\"sncmmdtyCode\":\"000000010591492016\",\"isshelflife\":\"X\",\"remainingShelfLife\":\"90\",\"riskLife\":\"60\",\"damageLife\":\"33\",\"prototypeManage\":null,\"badManage\":null,\"cmanage\":null,\"batchFlag\":\"\",\"deleteFlag\":null,\"versionNo\":\"00001\"}],\"barCodeInfo\":[{\"kunner\":\"RH007\",\"sncmmdtyCode\":\"000000010591492016\",\"externalEanCode\":\"SN10591492016\",\"cmmdtyEanCateg\":\"DE\"}]},businessType=rcs_ztky_commodity_info_distribute}";
		// this.content =
		// "{\"cmmdtyInfo\":[{\"kunner\":\"RH007\",\"sncmmdtyCode\":\" 000000010591492016\",\"cmmdtyName\":\" FANCL/芳珂 HTC胶原蛋白饮料 10日装*3 2018版\",\"measureUnit\":\"S01\",\"attributes\":\"\",\"cmmdtyType\":\" Z001\",\"cmmdtyGrp\":\"\",\"brandCode\":\"\",\"cmmdtyLength\":\"\",\"cmmdtyWidth\":\"\",\"cmmdtyHeight\":\"\",\"cmmdtyVolume\":\"\",\"grossCmmdtyWeight\":\"\",\"netCmmdtyWeight\":\"\",\"totalShelfLife\":\"\",\"coldChain\":\"\",\"bigsmall\":\"\",\"cmmdtyFreightGrp\":\"\",\"mprbs\":\"\",\"cmmdtyModel\":\"\",\"cmmdtyOrigin\":\"\",\"weightFlag\":\"\",\"taste\":\"\",\"size\":\"\",\"colour\":\"\",\"keepEnvironment\":\"\",\"cmmdtyFeatures\":\"\",\"physicalForm\":\"\",\"shape\":\"\",\"keepMethod\":\"\",\"pcs\":\"\",\"cmmdtyHierrarchy\":\"\"}],\"customerInfo\":[{\"kunner\":\"\",\"sncmmdtyCode\":\"\",\"isshelflife\":\"\",\"remainingShelfLife\":\"\",\"riskLife\":\"\",\"damageLife\":\"\",\"prototypeManage\":\"\",\"badManage\":\"\",\"cmanage\":\"\",\"batchFlag\":\"\",\"deleteFlag\":\"\",\"versionNo\":\"\"}],\"barCodeInfo\":[{\"kunner\":\"\",\"sncmmdtyCode\":\"\",\"externalEanCode\":\" SN10591492016 \",\"cmmdtyEanCateg\":\"\"}]}";

		// sn_receipt
		// this.requestType = HttpServerManagerService.requestType_sn_receipt;
		// this.content =
		// "{\"orderInfo\":{\"ownerUserId\":\"RH100\",\"fpsOrderId\":\"123456XXXXX\",\"storeCode\":\"WM10xxxxxx\",\"orderCode\":\"W107xxxxxx\",\"orderType\":\"601\",\"orderNumber\":\"A22xxxx\",\"outsourcingFlag\":\"01\",\"orderSource\":\"305\",\"remark\":\"商品情况：未开包未使用包装完好;;\",\"returnReason\":\"商品情况：未开包未使用包装完好;;\",\"orderCreateTime\":\"2018-01-03 11:47:07\",\"expectStartTime\":\"2018-01-03 11:47:06\",\"expectEndTime\":\"2018-01-03 11:47:06\",\"orderFlag\":\"9\",\"tmsServiceCode\":\"S02\",\"tmsServiceName\":\"苏宁物流\",\"tmsOrderCode\":\"896102xxxxxx\",\"prevOrderCode\":\"W107xxxxxx\",\"receiverInfo\":{\"receiverProvince\":\"江苏\",\"receiverCity\":\"南京市\",\"receiverArea\":\"雨花台区\",\"receiverTown\":\"全区\",\"receiverAddress\":\"龙藏大道2号\",\"receiverName\":\"沈xx\",\"receiverMobile\":\"18666xxxxxx\",\"receiverPhone\":\"15172xxxxxx\"},\"senderInfo\":{\"senderAddress\":\"雨花经济开发区龙藏大道与凤舞路交叉口\",\"senderProvince\":\"浙江省\",\"senderCity\":\"杭州市\",\"senderArea\":\"滨江区\",\"senderTown\":\"全区\",\"senderCode\":\"7016xxxx\",\"senderName\":\"左xx\",\"senderMobile\":\"15172xxxxxx\",\"senderPhone\":\"15172xxxxxx\"},\"orderItemList\":[{\"orderItemId\":\"420000002xxxxxx\",\"userId\":\"7016xxxx\",\"userName\":\"安利（中国）日用品有限公司\",\"ownerUserId\":\"7016xxxx\",\"ownerUserName\":\"安利（中国）日用品有限公司\",\"itemId\":\"917080415493xxxxxx\",\"itemName\":\"雅蜜润肤沐浴露 750ML\",\"inventoryType\":\"1\",\"itemQuantity\":\"750\",\"produceCode\":\"7359xxxx\",\"condition\":\"A\"},{\"orderItemId\":\"420000002xxxxxx\",\"userId\":\"7016xxxx\",\"userName\":\"安利（中国）日用品有限公司\",\"ownerUserId\":\"70168xxx\",\"ownerUserName\":\"安利（中国）日用品有限公司\",\"itemId\":\"917080415493xxxxxx\",\"itemName\":\"雅蜜润肤沐浴露 750ML\",\"inventoryType\":\"1\",\"itemQuantity\":\"750\",\"produceCode\":\"7359xxxx\",\"condition\":\"A\"}]}}";

		// sn_warehousing
		// this.requestType =
		// HttpServerManagerService.requestType_sn_warehousing;
		// this.content =
		// "{\"orderInfo\":{\"fpsOrderId\":\"4111110000197\",\"ownerUserId\":\"70057018\",\"storeCode\":\"WM10000079\",\"orderCode\":\"W100112051\",\"orderType\":\"601\",\"orderNumber\":\"ZT201808212027001\",\"outsourcingFlag\":\"01\",\"orderSource\":\"301\",\"orderCreateTime\":\"2018-08-21 22:06:54\",\"returnReason\":\"\",\"expectStartTime\":\"2018-08-29 15:13:38\",\"expectEndTime\":\"2018-08-29 15:13:38\",\"remark\":\"\",\"receiverInfo\":{},\"senderInfo\":{\"senderAddress\":\"江苏南京市软件大道203号\",\"senderCode\":\"70057018\",\"senderName\":\"C店-平行仓商家E 新1\",\"senderMobile\":\"025-66996699-880665\",\"senderPhone\":\"025-66996699-880665\"},\"orderItemList\":[{\"orderItemId\":\"410111000019511\",\"userId\":\"70057018\",\"userName\":\"C店-平行仓商家E 新1\",\"ownerUserId\":\"70057018\",\"ownerUserName\":\"C店-平行仓商家E 新1\",\"itemId\":\"000000001002512101\",\"itemName\":\"千丰浴霸壁挂式二灯双灯三灯浴霸卫生间浴室取暖灯泡挂墙式灯暖 小玲珑2米线护眼黄泡两灯挂浴霸 漏电保护\",\"itemCode\":\"000000001002512101\",\"inventoryType\":\"301\",\"itemQuantity\":\"1\",\"produceCode\":\"\",\"condition\":\"\",\"batchCode\":\"\"}]}}";

//		sn_deliverGoodsNotify
//		 this.requestType = HttpServerManagerService.requestType_sn_deliverGoodsNotify;
//		 this.content = "{\"orderInfo\":{\"ownerUserId\":\"7016xxxx\",\"storeCode\":\"WM10xxxxxx\",\"fpsOrderId\":\"12345xxxxxx\",\"orderCode\":\"W107xxxxxx\",\"orderType\":\"201\",\"orderNumber\":\"W89xxxx\",\"outsourcingFlag\":\"01\",\"customsMode\":\"02\",\"bol\":\"HM121231xxxxxx\",\"bolCount\":\"2\",\"orderSource\":\"S12\",\"tmsServiceCode\":\"S02\",\"tmsServiceName\":\"顺丰\",\"tmsOrderCode\":\"1234556667\",\"orderFlag\":\"\",\"orderCreateTime\":\"2018-01-03 12:47:11\",\"deliveryType\":\"PTPS\",\"deliverRequirements\":{\"scheduleDay\":\"2018-01-04\",\"scheduleStart\":\"18:00:00\",\"scheduleEnd\":\"18:00:00\"},\"batchSendCtrlParam\":{},\"extendFields\":\"{}\",\"receiverInfo\":{\"receiverProvince\":\"江苏省\",\"receiverCity\":\"宿迁市\",\"receiverArea\":\"宿豫区\",\"receiverTown\":\"全区\",\"receiverMobile\":\"0527-8431xxxx\",\"receiverPhone\":\"0527-8431xxxx\",\"receiverName\":\"谷绍娟\",\"receiverAddress\":\"江苏省宿迁市宿豫区江山大道xxxxxx\"},\"senderInfo\":{\"senderProvince\":\"江苏\",\"senderCity\":\"南京市\",\"senderArea\":\"雨花台区\",\"senderTown\":\"全区\",\"senderAddress\":\"xx大道1号\",\"senderName\":\"董x\",\"senderPhone\":\"025-66996699-87xxxx\",\"senderMobile\":\"025-66996699-87xxxx\"},\"orderItemList\":[{\"itemName\":\"电源xxx\",\"itemQuantity\":\"1\",\"orderItemId\":\"SL201801030000xxxxxx\",\"condition\":\"A\",\"ownerUserId\":\"7016xxxx\",\"itemId\":\"917080409503xxxxxx\",\"inventoryType\":\"1\",\"userId\":\"7016xxxx\",\"itemCode\":\"WTI09xxxx\"}]}}";

		this.requestType = requestType;
		this.content = content;
		this.signKey = signKey;
	}

	@Override
	public Object call() throws CommonException {

		System.out.println("【开始】数据处理：" + signKey);

		String result = "";
		// System.out.println("【content】："+content);
		if (requestType.equals(HttpServerManagerService.requestType_Order)) {
			// 处理订单数据
			result = handleXml_Order(content);
		} else if (requestType
				.equals(HttpServerManagerService.requestType_inventory)) {
			// 处理库存数据
			result = handleXml_Inventory(content);
		} else if (requestType
				.equals(HttpServerManagerService.requestType_load)) {
			// 处理装载数据
			result = handleXml_Load(content);
		} else if (requestType
				.equals(HttpServerManagerService.requestType_listRelease)) {
			// 处理装载数据
			result = handleXml_ListRelease(content);
		} else if (requestType
				.equals(HttpServerManagerService.requestType_sn_sku)) {
			// 苏宁海外仓商品
			result = handleXml_sn_sku(content);
		} else if (requestType
				.equals(HttpServerManagerService.requestType_sn_receipt)) {
			// 苏宁海外仓回执
			result = handleXml_sn_receipt(content);
		} else if (requestType
				.equals(HttpServerManagerService.requestType_sn_warehousing)) {
			// 川佐返回收货结果
			result = handleXml_sn_warehousing(content);
		}else if (requestType
				.equals(HttpServerManagerService.requestType_sn_deliverGoodsNotify)) {
			// 苏宁发货通知
			result = handleXml_sn_deliverGoodsNotify(content);
		}

		System.out.println("【结束】数据处理：" + signKey);

		return result;
	}

	// 处理订单数据
	private String handleXml_Order(String xmlString) {

		String xmlReturnString = "";

		System.out
				.println("---------------------------【FPAPI_ORDER】-------------------------------");
		// 入库
		// 商品的入库流程如下：
		// 收到订单报文中<businessType>等于C061时，将报文中<orderItems>中内容插入t_new_import_receipt表，一个<orderItems>对应一行记录。没有进一步操作。
		// <goodsCode>插入SKU
		// <goodsDescription>插入DESCRIPTION
		// <goodsNumber>插入EXPECT_QTY
		// <logisticsOrderId>插入RECEIPT_NO
		// CREAT_DATE填当前日期的YYYYMMDD

		Map head = null;

		Map result = new HashMap();

		try {
			// 判断businessType
			head = XmlUtil
					.parseXmlFPAPI_SingleNodes(xmlString,
							"//inputData/ordersList/orders/orderImformation/orderHead/child::*");

			String businessType = null;

			if (head != null && head.containsKey("businessType")
					&& head.get("businessType") != null) {
				businessType = (String) head.get("businessType");
			}

			// 获取资源文件
			ResourceBundle bundle = CommonUtil
					.getMessageMappingResource("CEB_SN");

			String checkBusinessType_C061 = CommonUtil
					.getSystemConfigProperty("checkOrderBusinessType_C061");
			String checkBusinessType_C005 = CommonUtil
					.getSystemConfigProperty("checkOrderBusinessType_C005");
			// 报文入库
			if (checkBusinessType_C061.equals(businessType)) {
				// C061报文处理
				result = handleOrderC061(xmlString, bundle);

			} else if (checkBusinessType_C005.equals(businessType)) {
				// C005报文处理
				result = handleOrderC005(head, xmlString, bundle);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("isSuccess", "false");
			result.put("errorMsg", "系统异常");
		}

		// 构建返回数据
		List<LinkedMap> dataList = new ArrayList<LinkedMap>();
		for (int i = 0; i < 1; i++) {
			LinkedMap resultData = new LinkedMap();
			resultData.put("logisticsOrderId",
					head != null ? head.get("logisticsOrderId") : "");
			resultData.put("success", result.get("isSuccess").toString());
			resultData.put("errorCode", "");
			resultData.put("errorMsg", result.get("errorMsg"));
			dataList.add(resultData);
		}
		String root = CommonUtil.getSystemConfigProperty("orderReceiptRoot");
		String firstElement = CommonUtil
				.getSystemConfigProperty("orderFirstElement");

		xmlReturnString = XmlUtil.generalReceiptXml_FP(root, firstElement,
				dataList);

		return xmlReturnString;
	}

	// 处理订单数据
	private String handleXml_Inventory(String xmlString) {

		String xmlReturnString = "";

		System.out
				.println("---------------------------【FPAPI_INVENTORY】-------------------------------");

		// 收到库存明细同步报文中<businessType>等于C061时，两步操作。

		// 第一步将报文中<inventoryDetailItems>的<goodsNumber>的值写入t_new_import_receipt表已有数据的ACTUAL_QTY中。
		// 1.inventoryDetailHead.logisticsOrderId = RECEIPT_NO
		// 2.inventoryDetailItems.goodsCode = SKU

		// 第二步将报文中内容插入账册表t_new_import_books
		// <goodsCode>插入SKU
		// <goodsDescription>插入DESCRIPTION
		// <goodsNumber>插入QTY
		// ADD_REDUCE_FLAG固定填1
		// <logisticsOrderId>插入ORDER_NO
		// CREAT_DATE填当前日期的YYYYMMDD
		// 没有进一步操作。

		Map head = null;
		boolean isSuccess = true;

		try {
			// 判断businessType
			head = XmlUtil.parseXmlFPAPI_SingleNodes(xmlString,
					"//inputList/inventoryOrder/inventoryDetailHead/child::*");

			String businessType = null;
			String logisticsOrderId = head.get("logisticsOrderId") != null ? head
					.get("logisticsOrderId").toString() : "";

			if (head != null && head.containsKey("businessType")
					&& head.get("businessType") != null) {
				businessType = (String) head.get("businessType");
			}

			String checkBusinessTypeC061 = CommonUtil
					.getSystemConfigProperty("checkInventoryBusinessType_C061");
			String checkBusinessTypeC005 = CommonUtil
					.getSystemConfigProperty("checkInventoryBusinessType_C005");

			// 获取资源文件
			ResourceBundle bundle = CommonUtil
					.getMessageMappingResource("CEB_SN");
			// 报文入库--C061
			if (checkBusinessTypeC061.equals(businessType)) {
				List<Map> items = XmlUtil.parseXmlFPAPI_MulitpleNodes(
						xmlString,
						"//inputList/inventoryOrder/inventoryDetailItems");

				SimpleDateFormat sf = CommonUtil
						.getDateFormatter(CommonDefine.COMMON_FORMAT_1);

				List<String> colNames = new ArrayList<String>();
				List<Object> colValues = new ArrayList<Object>();

				for (Map item : items) {
					Map data = new HashMap();
					// 转换
					for (Object key : item.keySet()) {
						if (bundle.containsKey("inventory_" + key.toString())) {
							data.put(
									bundle.getObject("inventory_"
											+ key.toString()), item.get(key));
						}
					}
					// 更新t_new_import_receipt.ACTUAL_QTY, 条件SKU和RECEIPT_NO
					String sku = data.get("SKU").toString();

					colNames.clear();
					colValues.clear();
					colNames.add("RECEIPT_NO");
					colNames.add("SKU");
					colValues.add(logisticsOrderId);
					colValues.add(sku);

					List<Map<String, Object>> rows = commonManagerMapper
							.selectTableListByNVList("t_new_import_receipt",
									colNames, colValues, null, null);

					if (rows.size() > 0) {
						for (Map row : rows) {
							colNames.clear();
							colValues.clear();
							colNames.add("ACTUAL_QTY");
							colValues.add(data.get("QTY"));
							// 更新数据
							commonManagerMapper.updateTableByNVList(
									"t_new_import_receipt", "RECEIPT_ID",
									row.get("RECEIPT_ID"), colNames, colValues);
						}
						// 数据入库
						data.put("ORDER_NO", logisticsOrderId);
						data.put("ADD_REDUCE_FLAG", "1");
						data.put("CREAT_DATE", sf.format(new Date()));
						data.put("CREAT_TIME", new Date());
						Map primary = new HashMap();
						primary.put("primaryId", null);
						commonManagerMapper.insertTableByNVList(
								"t_new_import_books", new ArrayList<String>(
										data.keySet()), new ArrayList<Object>(
										data.values()), primary);
					} else {
						isSuccess = false;
					}
				}
				// 订单状态回传接口--SN
				Map data = new HashMap();

				String dateFormatString = CommonUtil.getDateFormatter(
						CommonDefine.COMMON_FORMAT).format(new Date());

				data.put("messageId", getMessageId());
				data.put("logisticsOrderId", logisticsOrderId);
				data.put("logisticsExpressId", "");
				data.put("statusCode", "1120");
				data.put("logisticsStation", "tianjin");
				data.put("finishedDate", dateFormatString.split(" ")[0]);
				data.put("finishedTime", dateFormatString.split(" ")[1]);
				data.put("operator", "sinotrans");
				data.put("telNumber", "");
				data.put("shipmentCode", "");
				data.put("weight", "");
				data.put("weightUnit", "");
				data.put("note", "");
				data.put("consignee", "");
				data.put("airwayBillNo", "");
				data.put("flightNo", "");
				data.put("flightDate", "");
				data.put("keyValueAdd", "");
				data.put("thirdPartyCompany", "");

				// 向苏宁回传订单状态
				List<Map> dataList = new ArrayList<Map>();

				dataList.add(data);

				Map requestParam = new HashMap();
				requestParam
						.put("logistic_provider_id",
								CommonUtil
										.getSystemConfigProperty("SN_inputStatusList_logistic_provider_id"));
				requestParam
						.put("msg_type",
								CommonUtil
										.getSystemConfigProperty("SN_inputStatusList_msg_type"));
				requestParam
						.put("url",
								CommonUtil
										.getSystemConfigProperty("SN_inputStatusList_requestUrl"));

				String content = XmlUtil
						.generalReceiptXml_LoadRec_FP(
								CommonUtil
										.getSystemConfigProperty("inputStatusListRoot"),
								CommonUtil
										.getSystemConfigProperty("inputStatusListFirstElement"),
								dataList);

				// 发送请求
				String result = send2SN(requestParam, content);
			} else if (checkBusinessTypeC005.equals(businessType)) {
				String statusCode = head.get("statusCode") != null ? head.get(
						"statusCode").toString() : "";
				if ("1060".equals(statusCode)) {
					// 什么也不做
				} else if ("1131".equals(statusCode)) {
					// 撤销操作
					// t_new_import_inventory表中查找数据
					List<String> colNames = new ArrayList<String>();
					List<Object> colValues = new ArrayList<Object>();
					colNames.add("ADD_REDUCE_FLAG");
					colNames.add("ORDER_NO");
					colValues.add("2");
					colValues.add(logisticsOrderId);
					List<Map<String, Object>> rows = commonManagerMapper
							.selectTableListByNVList("t_new_import_books",
									colNames, colValues, null, null);
					//
					for (Map<String, Object> data : rows) {
						// 查找母数据
						colNames.clear();
						colValues.clear();

						colNames.add("ADD_REDUCE_FLAG");
						colNames.add("GOODS_SERIALNO");
						colValues.add("1");
						colValues.add(data.get("GOODS_SERIALNO"));

						Map<String, Object> mainData = commonManagerMapper
								.selectTableListByNVList("t_new_import_books",
										colNames, colValues, null, null).get(0);

						double updateQty = Double.valueOf(mainData.get("QTY")
								.toString())
								+ Double.valueOf(data.get("QTY").toString());
						mainData.put("QTY", updateQty);
						// 更新母数据
						commonManagerMapper.updateTableByNVList(
								"t_new_import_books", "BOOKS_ID",
								mainData.get("BOOKS_ID"),
								new ArrayList<String>(mainData.keySet()),
								new ArrayList<Object>(mainData.values()));
						// 更新子数据
						data.put("QTY", 0d);
						commonManagerMapper.updateTableByNVList(
								"t_new_import_books", "BOOKS_ID", data
										.get("BOOKS_ID"),
								new ArrayList<String>(data.keySet()),
								new ArrayList<Object>(data.values()));

					}
					// 向天津发送清单撤销消息
					/*<InventoryCancel>
						<orderNo></orderNo>
						<ebpCode></ebpCode>
					</InventoryCancel>*/
					Map InventoryCancelData = new HashMap();
					
					InventoryCancelData.put("orderNo", logisticsOrderId);
					InventoryCancelData.put("ebpCode", "3201966A69");
					
					String xmlStringData = XmlUtil.generalCommonXml(
							"InventoryCancel", InventoryCancelData);

					// 第五步 向天津外运发送清单数据
					Map reponse = postToTJ(xmlStringData);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			isSuccess = false;
		}

		// 返回数据构建
		List<LinkedMap> dataList = new ArrayList<LinkedMap>();

		for (int i = 0; i < 1; i++) {
			LinkedMap resultData = new LinkedMap();
			resultData.put("messageId", head.get("messageId"));
			resultData.put("success", String.valueOf(isSuccess));
			resultData.put("errorCode", "");
			resultData.put("errorMsg", "");
			dataList.add(resultData);
		}
		String root = CommonUtil
				.getSystemConfigProperty("inventoryReceiptRoot");
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
				// data.put("CREAT_DATE", sf.format(new Date()));
				data.put("CREAT_TIME", new Date());
				Map primarySub = new HashMap();
				primarySub.put("primaryId", null);
				commonManagerMapper.insertTableByNVList(
						"t_new_import_load_detail",
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
			resultData.put("loadHeadId", head != null ? head.get("loadHeadId")
					: "");
			resultData.put("loadId", head != null ? head.get("loadId") : "");
			resultData.put("returnMessage", isSuccess ? "接收成功" : "接收失败");
			// resultData.put("errorCode", "");
			// resultData.put("errorMsg", "");
			dataList.add(resultData);
		}
		String root = CommonUtil.getSystemConfigProperty("loadReceiptRoot");

		xmlReturnString = XmlUtil.generalReceiptXml_FP(root, null, dataList);

		return xmlReturnString;
	}

	private String handleXml_ListRelease(String xmlString) {

		String xmlReturnString = "";

		System.out
				.println("---------------------------【FPAPI_ListRelease】-------------------------------");

		Map head = null;
		boolean isSuccess = true;

		SimpleDateFormat sf = CommonUtil
				.getDateFormatter(CommonDefine.COMMON_FORMAT);

		try {
			// 包含数据orderNo、invtNo，returnStatus
			head = XmlUtil.parseXmlFPAPI_SingleNodes(xmlString,
					"//InventoryReturn/child::*");

			Map data = new HashMap();

			data.put("messageId", getMessageId());
			data.put("logisticsOrderId", head.get("orderNo"));
			data.put("logisticsExpressId", "");
			data.put("statusCode", head.get("returnStatus"));
			data.put("logisticsStation", "tianjin");
			data.put("finishedDate", sf.format(new Date()).split(" ")[0]);
			data.put("finishedTime", sf.format(new Date()).split(" ")[1]);
			data.put("operator", "sinotrans");
			data.put("telNumber", "");
			data.put("shipmentCode", "");
			data.put("weight", "");
			data.put("weightUnit", "");
			data.put("note", "");
			data.put("consignee", "");
			data.put("airwayBillNo", "");
			data.put("flightNo", "");
			data.put("flightDate", "");
			data.put("keyValueAdd", "");
			data.put("thirdPartyCompany", "");

			// 向苏宁回传订单状态
			List<Map> dataList = new ArrayList<Map>();

			dataList.add(data);

			Map requestParam = new HashMap();
			requestParam
					.put("logistic_provider_id",
							CommonUtil
									.getSystemConfigProperty("SN_inputStatusList_logistic_provider_id"));
			requestParam.put("msg_type", CommonUtil
					.getSystemConfigProperty("SN_inputStatusList_msg_type"));
			requestParam.put("url", CommonUtil
					.getSystemConfigProperty("SN_inputStatusList_requestUrl"));

			String content = XmlUtil.generalReceiptXml_LoadRec_FP(CommonUtil
					.getSystemConfigProperty("inputStatusListRoot"), CommonUtil
					.getSystemConfigProperty("inputStatusListFirstElement"),
					dataList);
			// 发送请求
			String result = send2SN(requestParam, content);

		} catch (Exception e) {
			e.printStackTrace();
			isSuccess = false;
		}
		// 无需内容，直接返回200
		xmlReturnString = "";

		return xmlReturnString;
	}

	private String send2SN(Map requestParam, String content) {
		// 向苏宁发送报文
		// 请求数据
		String data_digest = CommonUtil.makeSign(content);
		String logistic_provider_id = requestParam.get("logistic_provider_id")
				.toString();
		String msg_type = requestParam.get("msg_type").toString();
		String url = requestParam.get("url").toString();

		String requestUrl = "";
		try {
			requestUrl = url + "?" + "logistics_interface="
					+ URLEncoder.encode(content, "utf-8") + "&data_digest="
					+ URLEncoder.encode(data_digest, "utf-8")
					+ "&logistic_provider_id=" + logistic_provider_id
					+ "&msg_type=" + msg_type;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 发送http请求
		Map<String, Object> head = new HashMap<String, Object>();
		head.put("Content-Type",
				CommonUtil.getSystemConfigProperty("SN_Content_Type"));

		String result = HttpUtil.doPost4TJ(requestUrl, head, "", false);

		return result;
	}

	// 处理订单数据
	private String handleXml_sn_sku(String jsonString) {

		// String jsonReturnString = "";

		System.out
				.println("---------------------------【FPAPI_SN_SKU】-------------------------------");
		// 入库
		JSONObject jsonObject = JSONObject.fromObject(jsonString);

		JSONArray cmmdtyInfo = (JSONArray) jsonObject.get("cmmdtyInfo");
		// JSONArray customerInfo = (JSONArray) jsonObject.get("customerInfo");
		// JSONArray barCodeInfo = (JSONArray) jsonObject.get("barCodeInfo");
		// 获取资源文件
		ResourceBundle bundle = CommonUtil.getMessageMappingResource("CEB_SN");

		JSONObject result = new JSONObject();
		JSONArray resultArray = new JSONArray();
		for (Iterator it = cmmdtyInfo.iterator(); it.hasNext();) {
			JSONObject item = (JSONObject) it.next();
			Map primary = new HashMap();
			primary.put("primaryId", null);

			Map data = new HashMap();

			for (Object key : item.keySet()) {
				if (bundle
						.containsKey("SN_SKU_" + key.toString().toUpperCase())) {

					if (item.get(key) == null
							|| item.get(key).toString().isEmpty()) {
						data.put(
								bundle.getObject("SN_SKU_"
										+ key.toString().toUpperCase()), null);
					} else {
						data.put(
								bundle.getObject("SN_SKU_"
										+ key.toString().toUpperCase()),
								item.get(key));
					}
				}
			}

			// 数据入库
			// data.put("CREAT_DATE", sf.format(new Date()));
			data.put("CREAT_TIME", new Date());
			commonManagerMapper.insertTableByNVList("T_SN_SKU",
					new ArrayList<String>(data.keySet()),
					new ArrayList<Object>(data.values()), primary);

			JSONObject resultItem = new JSONObject();

			resultItem.put("kunner", data.get("OWNER"));
			resultItem.put("sncmmdty_code", data.get("SKU"));
			resultItem.put("process_stat", "03");
			resultItem.put("notes", "");
			resultArray.add(resultItem);

		}
		result.put("resultInfo", resultArray);
		return result.toString();
	}

	// 处理入库通知
	private String handleXml_sn_receipt(String jsonString) throws CommonException{

		// String jsonReturnString = "";
		SimpleDateFormat sf = CommonUtil
				.getDateFormatter(CommonDefine.COMMON_FORMAT_1);

		System.out
				.println("---------------------------【FPAPI_SN_RECEIPT】-------------------------------");
		// 入库
		JSONObject jsonObject = JSONObject.fromObject(jsonString);

		JSONObject orderInfo = (JSONObject) jsonObject.get("orderInfo");
		JSONObject senderInfo = (JSONObject) orderInfo.get("senderInfo");

		JSONArray orderItemList = (JSONArray) orderInfo.get("orderItemList");
		// JSONArray customerInfo = (JSONArray) jsonObject.get("customerInfo");
		// JSONArray barCodeInfo = (JSONArray) jsonObject.get("barCodeInfo");
		// 获取资源文件
		ResourceBundle bundle = CommonUtil.getMessageMappingResource("CEB_SN");

		JSONObject result = new JSONObject();
		JSONArray resultArray = new JSONArray();

		// 入库T_SN_RECEIPT
		Map primary = new HashMap();
		primary.put("primaryId", null);

		Map data = new HashMap();
		for (Object key : orderInfo.keySet()) {
			if (bundle
					.containsKey("SN_RECEIPT_" + key.toString().toUpperCase())) {

				if (orderInfo.get(key) == null
						|| orderInfo.get(key).toString().isEmpty()) {
					data.put(
							bundle.getObject("SN_RECEIPT_"
									+ key.toString().toUpperCase()), null);
				} else {
					data.put(
							bundle.getObject("SN_RECEIPT_"
									+ key.toString().toUpperCase()),
							orderInfo.get(key));
				}

			}
		}
		for (Object key : senderInfo.keySet()) {
			if (bundle
					.containsKey("SN_RECEIPT_" + key.toString().toUpperCase())) {

				if (senderInfo.get(key) == null
						|| senderInfo.get(key).toString().isEmpty()) {
					data.put(
							bundle.getObject("SN_RECEIPT_"
									+ key.toString().toUpperCase()), null);
				} else {
					data.put(
							bundle.getObject("SN_RECEIPT_"
									+ key.toString().toUpperCase()),
							senderInfo.get(key));
				}

			}
		}
		data.put("CREAT_DATE", sf.format(new Date()));
		data.put("CREAT_TIME", new Date());
		commonManagerMapper.insertTableByNVList("T_SN_RECEIPT",
				new ArrayList<String>(data.keySet()), new ArrayList<Object>(
						data.values()), primary);

		//发送给川佐
		List<Map> orderItemListForCJ = new ArrayList<Map>();
		
		for (Iterator it = orderItemList.iterator(); it.hasNext();) {

			JSONObject item = (JSONObject) it.next();
			Map primary_sub = new HashMap();
			primary_sub.put("primaryId", null);

			Map dataSub = new HashMap();

			Map dataSubForCJ = new HashMap();

			for (Object key : item.keySet()) {
				if (bundle.containsKey("SN_RECEIPT_DETAIL_"
						+ key.toString().toUpperCase())) {

					if (item.get(key) == null
							|| item.get(key).toString().isEmpty()) {
						dataSub.put(
								bundle.getObject("SN_RECEIPT_DETAIL_"
										+ key.toString().toUpperCase()), null);
					} else {
						dataSub.put(
								bundle.getObject("SN_RECEIPT_DETAIL_"
										+ key.toString().toUpperCase()),
								item.get(key));
					}
				}
			}

			// 数据入库
			dataSub.put("RECEIPT_ID", primary.get("primaryId"));
			// data.put("CREAT_DATE", sf.format(new Date()));
			dataSub.put("CREAT_TIME", new Date());
			commonManagerMapper.insertTableByNVList("T_SN_RECEIPT_DETAIL",
					new ArrayList<String>(dataSub.keySet()),
					new ArrayList<Object>(dataSub.values()), primary_sub);

			//发送给川佐数据组装
			dataSubForCJ.put("ORDER_ITEM_ID", dataSub.get("ORDER_ITEM_ID"));
			dataSubForCJ.put("SKU", dataSub.get("SKU"));
			dataSubForCJ.put("ITEM_NAME", dataSub.get("ITEM_NAME"));
			dataSubForCJ.put("EXPECT_QTY", dataSub.get("EXPECT_QTY"));
			dataSubForCJ.put("PRODUCE_CODE", dataSub.get("PRODUCE_CODE"));
			orderItemListForCJ.add(dataSubForCJ);

		}
		//给川佐发送入库通知
		Map order = new HashMap();
		order.put("OWNER", data.get("OWNER"));
		order.put("ORDER_CODE", data.get("ORDER_CODE"));
		order.put("ORDER_TYPE", data.get("ORDER_TYPE"));
		order.put("EXPECT_START_TIME", data.get("EXPECT_START_TIME"));
		order.put("EXPECT_END_TIME", data.get("EXPECT_END_TIME"));
		order.put("TMS_ORDER_CODE", data.get("TMS_ORDER_CODE"));
		order.put("SENDER_ADDRESS", data.get("SENDER_ADDRESS"));
		order.put("SENDER_CODE", data.get("SENDER_CODE"));
		order.put("SENDER_NAME", data.get("SENDER_NAME"));
		order.put("SENDER_MOBILE", data.get("SENDER_MOBILE"));
		order.put("SENDER_PHONE", data.get("SENDER_PHONE"));
		order.put("CUST", CommonUtil
				.getSystemConfigProperty("CJ_cust"));
		
		String xmlStringData = XmlUtil.generalWarehousingNoticeXml_CJ(
				"DATA", order, orderItemListForCJ);
		
		String requestXmlData = XmlUtil.generalSoapXml_CJ(xmlStringData);
		
		System.out.println(requestXmlData);
		//向川佐发送入库通知单
		String response = HttpUtil.sendHttpCMD_CJ(requestXmlData,CommonUtil
				.getSystemConfigProperty("CJ_requestUrl").toString());

		//获取返回信息
		String returnXmlData = XmlUtil
				.getResponseFromXmlString_CJ(response);
		
		//正常测试报文
//		String returnXmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><DATA><ORDER><ORDER_CODE>3434222e333</ORDER_CODE><CD>OK</CD><INFO><![CDATA[]]></INFO><ITEMS><ITEM><ORDER_ITEM_ID>420000002xxxxxx</ORDER_ITEM_ID><SKU>P0000KMM</SKU><ACTUAL_QTY>1</ACTUAL_QTY><ACTUAL_QTY_DEFECT>5590</ACTUAL_QTY_DEFECT></ITEM><ITEM><ORDER_ITEM_ID>1234567891</ORDER_ITEM_ID><SKU>P0001KMM</SKU><ACTUAL_QTY>1</ACTUAL_QTY><ACTUAL_QTY_DEFECT>5591</ACTUAL_QTY_DEFECT></ITEM></ITEMS></ORDER></DATA>";
		//异常测试报文
//		String returnXmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><DATA><ORDER><ORDER_CODE>W107xxxxxx</ORDER_CODE><CD>102</CD><INFO>发货人编号在系统中未找到。</INFO></ORDER></DATA>";

		//解析返回报文
		//正常报文
		Map orderResult = XmlUtil.parseXmlFPAPI_SingleNodes(returnXmlData, "//DATA/ORDER/child::*");
		
		//正常返回
		if(orderResult.containsKey("CD") && "OK".equals(orderResult.get("CD").toString())){
			List<Map> itemsResult = XmlUtil.parseXmlFPAPI_MulitpleNodes(returnXmlData, "//DATA/ORDER/ITEMS/ITEM");
			//将ACTUAL_QTY和ACTUAL_QTY_DEFECT字段写入t_sn_receipt_detail
			for(Map itemReult:itemsResult){
				List<String> colNames = new ArrayList<String>();
				List<Object> colValues = new ArrayList<Object>();
				colNames.add("ACTUAL_QTY");
				colNames.add("ACTUAL_QTY_DEFECT");
				colValues.add(itemReult.get("ACTUAL_QTY"));
				colValues.add(itemReult.get("ACTUAL_QTY_DEFECT"));
				commonManagerMapper.updateTableByNVList("t_sn_receipt_detail", "ORDER_ITEM_ID", itemReult.get("ORDER_ITEM_ID"), colNames, colValues);
			}
			//返回给苏宁
		result.put("success", "true");
		result.put("errorCode", "");
		result.put("errorMsg", "");
		}else{
			//异常返回
			//返回给苏宁的错误代码只填B0007,错误原因透传。
			result.put("success", "false");
			result.put("errorCode", "B0007");
			result.put("errorMsg", orderResult.get("INFO"));
		}
		result.put("orderCode", orderResult.get("ORDER_CODE"));

		return result.toString();
	}

	// 川佐收货确认----未完成、暂不入库，直接转发给苏宁
	private String handleXml_sn_warehousing(String jsonString) {

		// String jsonReturnString = "";
		SimpleDateFormat sf = CommonUtil
				.getDateFormatter(CommonDefine.RETRIEVAL_TIME_FORMAT);

		System.out
				.println("---------------------------【FPAPI_SN_WAREHOUSING】-------------------------------");
		// 入库
		JSONObject jsonObject = JSONObject.fromObject(jsonString);

		JSONObject orderInfo = (JSONObject) jsonObject.get("orderInfo");
		JSONObject senderInfo = (JSONObject) orderInfo.get("senderInfo");

		JSONArray orderItemList = (JSONArray) orderInfo.get("orderItemList");

		// 查询数据库明细表中数据
		List<String> colNames = new ArrayList<String>();
		List<Object> colValues = new ArrayList<Object>();
		colNames.add("ORDER_CODE");
		colValues.add(orderInfo.get("orderCode"));
		List<Map<String, Object>> items = commonManagerMapper
				.selectTableListByNVList("t_sn_receipt", colNames, colValues,
						null, null);

		Map item = null;
		if (items.size() > 0) {
			item = items.get(0);
		} else {
			return "";
		}

		// ------------------- orderItem ------------
		JSONArray orderItem = new JSONArray();

		int i = 0;
		for (Iterator it = orderItemList.iterator(); it.hasNext();) {

			JSONObject data = (JSONObject) it.next();
			// //查询数据库明细表中数据
			// List<String> colNames = new ArrayList<String>();
			// List<Object> colValues = new ArrayList<Object>();
			// colNames.add("ORDER_ITEM_ID");
			// colNames.add("SKU");
			// colValues.add(data.get("orderItemId"));
			// colValues.add(data.get("itemId"));
			// commonManagerMapper.selectTableListByNVList("t_sn_receipt_detail",
			// colNames, colValues, null, null);

			JSONObject orderSingleItem = new JSONObject();
			// 明细表ORDER_ITEM_ID
			orderSingleItem.put("orderItemId", data.get("orderItemId"));
			// OWNER
			orderSingleItem.put("ownUserId", item.get("OWNER"));
			orderSingleItem.put("stockNumber", "DSWMS"+CommonUtil.getDateFormatter(CommonDefine.COMMON_FORMAT_2)
					.format(new Date()));
			orderSingleItem.put("isCompleted", true);
			// 明细表SKU
			orderSingleItem.put("itemId", data.get("itemId"));

			// ------------------- itemInventorySingleItem ------------
			JSONObject itemInventorySingleItem = new JSONObject();
			itemInventorySingleItem.put("inventoryType", "1");
			// 明细表 ACTUAL_QTY
			itemInventorySingleItem.put("quantity", data.get("itemQuantity"));
			itemInventorySingleItem.put("produceDate", "");
			// 明细表 PRODUCE_CODE
			itemInventorySingleItem.put("produceCode", data.get("produceCode"));
			itemInventorySingleItem.put("condition", "");
			// ------------------- produceCodeSingleItem ------------
			JSONObject produceCodeSingleItem = new JSONObject();
			// 填顺序值，从0开始
			produceCodeSingleItem.put("flag", i);
			// 明细表 PRODUCE_CODE
			produceCodeSingleItem.put("produceCode", data.get("produceCode"));
			produceCodeSingleItem.put("expirationDate", "2099-01-01");
			// 明细表 ACTUAL_QTY
			produceCodeSingleItem.put("quantity", data.get("itemQuantity"));

			// ------------------- itemInventory ------------
			JSONArray itemInventory = new JSONArray();
			itemInventory.add(itemInventorySingleItem);
			// ------------------- produceCodeItem ------------
			JSONArray produceCodeItem = new JSONArray();
			produceCodeItem.add(produceCodeSingleItem);

			// ------------------- itemInventoryList ------------
			JSONObject itemInventoryList = new JSONObject();
			itemInventoryList.put("itemInventory", itemInventory);
			// ------------------- produceCodeItems ------------
			JSONObject produceCodeItems = new JSONObject();
			produceCodeItems.put("produceCodeItem", produceCodeItem);

			orderSingleItem.put("itemInventoryList", itemInventoryList);
			orderSingleItem.put("produceCodeItems", produceCodeItems);

			orderItem.add(orderSingleItem);
			
			i++;
		}

		// 返回苏宁数据
		// ------------------- orderItems ------------
		JSONObject orderItems = new JSONObject();
		orderItems.put("orderItem", orderItem);

		// ------------------- orderConfirmInfo ------------
		JSONObject orderConfirmInfo = new JSONObject();
		// STORE_CODE
		orderConfirmInfo.put("storeOrderCode", orderInfo.get("storeCode"));
		// FPS_ORDER_ID
		orderConfirmInfo.put("fpsOrderId", orderInfo.get("fpsOrderId"));
		// ORDER_TYPE
		orderConfirmInfo.put("orderType", orderInfo.get("orderType"));
		// ORDER_CODE
		orderConfirmInfo.put("orderCode", orderInfo.get("orderCode"));
		// ORDER_NUMBER
		orderConfirmInfo.put("orderNumber", orderInfo.get("orderNumber"));
		//
		orderConfirmInfo.put("outBizCode",
				CommonUtil.getDateFormatter(CommonDefine.RETRIEVAL_TIME_FORMAT)
						.format(new Date()));
		orderConfirmInfo.put("orderItems", orderItems);
		orderConfirmInfo.put(
				"orderConfirmTime",
				CommonUtil.getDateFormatter(CommonDefine.COMMON_FORMAT).format(
						new Date()));
		orderConfirmInfo.put("confirmType", "0");

		JSONObject content = new JSONObject();
		content.put("orderConfirmInfo", orderConfirmInfo);

		Map requestParam = new HashMap();
		requestParam
				.put("logistic_provider_id",
						CommonUtil
								.getSystemConfigProperty("SN_orderConfirmInfo_logistic_provider_id"));
		requestParam.put("msg_type", CommonUtil
				.getSystemConfigProperty("SN_orderConfirmInfo_msg_type"));
		requestParam.put("url", CommonUtil
				.getSystemConfigProperty("SN_orderConfirmInfo_requestUrl"));

		// 发送请求
		String result = send2SN(requestParam, content.toString());

		return "";
	}
	
	private String handleXml_sn_deliverGoodsNotify(String jsonString) {

		// String jsonReturnString = "";
		SimpleDateFormat sf = CommonUtil
				.getDateFormatter(CommonDefine.COMMON_FORMAT_1);

		System.out
				.println("---------------------------【FPAPI_SN_deliverGoodsNotify】-------------------------------");
		// 入库
		JSONObject jsonObject = JSONObject.fromObject(jsonString);

		JSONObject orderInfo = (JSONObject) jsonObject.get("orderInfo");
		JSONObject senderInfo = (JSONObject) orderInfo.get("senderInfo");
		JSONObject receiverInfo = (JSONObject) orderInfo.get("receiverInfo");

		JSONArray orderItemList = (JSONArray) orderInfo.get("orderItemList");
		// JSONArray customerInfo = (JSONArray) jsonObject.get("customerInfo");
		// JSONArray barCodeInfo = (JSONArray) jsonObject.get("barCodeInfo");
		// 获取资源文件
		ResourceBundle bundle = CommonUtil.getMessageMappingResource("CEB_SN");

		JSONObject result = new JSONObject();
		JSONArray resultArray = new JSONArray();

		// 入库T_SN_ORDER
//		commonManagerMapper.selectTableListByCol("T_SN_ORDER", "ORDER_CODE", orderInfo.get(key), startNumber, pageSize);
	
		Map primary = new HashMap();
		primary.put("primaryId", null);

		Map data = new HashMap();
		for (Object key : orderInfo.keySet()) {
			if (bundle
					.containsKey("SN_ORDER_" + key.toString().toUpperCase())) {

				if (orderInfo.get(key) == null
						|| orderInfo.get(key).toString().isEmpty()) {
					data.put(
							bundle.getObject("SN_ORDER_"
									+ key.toString().toUpperCase()), null);
				} else {
					data.put(
							bundle.getObject("SN_ORDER_"
									+ key.toString().toUpperCase()),
							orderInfo.get(key));
				}

			}
		}
		for (Object key : senderInfo.keySet()) {
			if (bundle
					.containsKey("SN_ORDER_" + key.toString().toUpperCase())) {

				if (senderInfo.get(key) == null
						|| senderInfo.get(key).toString().isEmpty()) {
					data.put(
							bundle.getObject("SN_ORDER_"
									+ key.toString().toUpperCase()), null);
				} else {
					data.put(
							bundle.getObject("SN_ORDER_"
									+ key.toString().toUpperCase()),
							senderInfo.get(key));
				}

			}
		}
		for (Object key : receiverInfo.keySet()) {
			if (bundle
					.containsKey("SN_ORDER_" + key.toString().toUpperCase())) {

				if (receiverInfo.get(key) == null
						|| receiverInfo.get(key).toString().isEmpty()) {
					data.put(
							bundle.getObject("SN_ORDER_"
									+ key.toString().toUpperCase()), null);
				} else {
					data.put(
							bundle.getObject("SN_ORDER_"
									+ key.toString().toUpperCase()),
									receiverInfo.get(key));
				}

			}
		}
		//bolCount如果为空或等于0，BOL_COUNT填0。如果bolCount大于0，BOL_COUNT填bolCount减1
		int bolCount = 0;
		if(data.get("BOL_COUNT") == null ||data.get("BOL_COUNT").toString().isEmpty()){
			
		}else{
			bolCount = Integer.valueOf(data.get("BOL_COUNT").toString());
			bolCount = bolCount==0?0:bolCount-1;
		}
		data.put("BOL_COUNT", bolCount);
		data.put("CREAT_DATE", sf.format(new Date()));
		data.put("CREAT_TIME", new Date());
		commonManagerMapper.insertTableByNVList("T_SN_ORDER",
				new ArrayList<String>(data.keySet()), new ArrayList<Object>(
						data.values()), primary);

		for (Iterator it = orderItemList.iterator(); it.hasNext();) {

			JSONObject item = (JSONObject) it.next();
			Map primary_sub = new HashMap();
			primary_sub.put("primaryId", null);

			Map dataSub = new HashMap();

			for (Object key : item.keySet()) {
				if (bundle.containsKey("SN_ORDER_DETAIL_"
						+ key.toString().toUpperCase())) {

					if (item.get(key) == null
							|| item.get(key).toString().isEmpty()) {
						dataSub.put(
								bundle.getObject("SN_ORDER_DETAIL_"
										+ key.toString().toUpperCase()), null);
					} else {
						dataSub.put(
								bundle.getObject("SN_ORDER_DETAIL_"
										+ key.toString().toUpperCase()),
								item.get(key));
					}
				}
			}

			// 数据入库
			dataSub.put("ORDER_ID", primary.get("primaryId"));
			// data.put("CREAT_DATE", sf.format(new Date()));
			dataSub.put("CREAT_TIME", new Date());
			commonManagerMapper.insertTableByNVList("T_SN_ORDER_DETAIL",
					new ArrayList<String>(dataSub.keySet()),
					new ArrayList<Object>(dataSub.values()), primary_sub);

		}
		result.put("orderCode", data.get("ORDER_CODE"));
		result.put("success", "true");
		result.put("errorCode", "");
		result.put("errorMsg", "");

		return result.toString();
	}
	
	
	private Map handleOrderC061(String xmlString, ResourceBundle bundle) {
		Map result = new HashMap();
		result.put("isSuccess", true);

		SimpleDateFormat sf = CommonUtil
				.getDateFormatter(CommonDefine.COMMON_FORMAT_1);
		List<Map> items = XmlUtil.parseXmlFPAPI_MulitpleNodes(xmlString,
				"//inputData/ordersList/orders/orderImformation/orderItems");

		for (Map item : items) {
			Map data = new HashMap();
			// 转换
			for (Object key : item.keySet()) {
				if (bundle.containsKey("order_" + key.toString())) {
					data.put(bundle.getObject("order_" + key.toString()),
							item.get(key));
				}
			}
			// String uniqueCol="SKU";
			// String primaryCol="RECEIPT_ID";
			// // 货号唯一性校验
			// uniqueCheck("t_new_import_receipt",uniqueCol,data.get(uniqueCol),primaryCol,data.get(primaryCol),false);
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

	private Map handleOrderC005(Map head, String xmlString,
			ResourceBundle bundle) {
		Map result = new HashMap();

		result.put("isSuccess", true);

		SimpleDateFormat sf = CommonUtil
				.getDateFormatter(CommonDefine.COMMON_FORMAT_1);
		// t_new_import_inventory表中查找数据
		List<String> colNames = new ArrayList<String>();
		List<Object> colValues = new ArrayList<Object>();
		colNames.add("LOGISTICS_NO");
		colValues.add(head.get("logisticsOrderId"));
		List<Map<String, Object>> rows = commonManagerMapper
				.selectTableListByNVList("t_new_import_inventory", colNames,
						colValues, null, null);

		// 如果存在，直接向苏宁返回“出库单已存在”，没有进一步操作。
		if (rows.size() > 0) {
			// 返回错误，出库单已存在--直接返回true，当做成功
			result.put("isSuccess", true);
			// result.put("errorMsg", "出库单已存在");
		} else {
			List<Map> items = XmlUtil
					.parseXmlFPAPI_MulitpleNodes(xmlString,
							"//inputData/ordersList/orders/orderImformation/orderItems");

			// 循环商品，看是否库存不足
			boolean isSkuEnough = true;
			List<BookOrderModel> bookOrders = new ArrayList<BookOrderModel>();
			for (Map item : items) {
				String goodsCode = item.get("goodsCode") != null ? item.get(
						"goodsCode").toString() : "";
				String goodsNumber = item.get("goodsNumber") != null ? item
						.get("goodsNumber").toString() : "";

				double goodsNumberDouble = !goodsNumber.isEmpty() ? Double
						.valueOf(goodsNumber) : 0;
				// t_new_import_books表中查找数据
				// ADD_REDUCE_FLAG=1
				// SKU=<orderItems><goodsCode>
				// QTY>=<orderItems><goodsNumber>
				List<Map> dataList = snCommonManagerMapper.selectBookNumber(
						goodsCode, goodsNumberDouble);

				if (dataList.size() == 0) {
					isSkuEnough = false;
					break;
				} else {
					BookOrderModel bookOrder = new BookOrderModel();
					bookOrder.setHead(head);
					bookOrder.setOrderitem(item);
					bookOrder.setBookItems(dataList);
					bookOrders.add(bookOrder);
				}
			}
			if (isSkuEnough) {
				List<Map> skuList;
				for (BookOrderModel xxx : bookOrders) {
					skuList = xxx.getBookItems();
					// QTY最小的，CREAT_TIME最久远的记录减去<orderItems><goodsNumber>。
					Collections.sort(skuList, new Comparator<Map>() {
						public int compare(Map o1, Map o2) {
							double qty1 = Double.valueOf(o1.get("QTY")
									.toString());
							double qty2 = Double.valueOf(o2.get("QTY")
									.toString());
							// SimpleDateFormat sf =
							// CommonUtil.getDateFormatter(CommonDefine.COMMON_FORMAT);
							// Date creatTime1 = null;
							// Date creatTime2 = null;
							// try {
							// creatTime1 =
							// sf.parse(o1.get("CREAT_TIME").toString());
							// creatTime2 =
							// sf.parse(o2.get("CREAT_TIME").toString());
							// } catch (ParseException e) {
							// // TODO Auto-generated catch block
							// e.printStackTrace();
							// }
							if (qty1 > qty2) {
								return 1;
							} else {
								return 0;
							}
						}
					});
					// 插入新记录
					Map recorder = skuList.get(0);
					colNames.clear();
					colValues.clear();
					double updateQty = Double.valueOf(recorder.get("QTY")
							.toString())
							- Double.valueOf(xxx.getOrderitem()
									.get("goodsNumber").toString());
					recorder.put("QTY", updateQty);
					for (Object key : recorder.keySet()) {
						colNames.add(key.toString());
						colValues.add(recorder.get(key));
					}
					// 更新数据
					commonManagerMapper.updateTableByNVList(
							"t_new_import_books", "BOOKS_ID",
							recorder.get("BOOKS_ID"), colNames, colValues);

					// 插入新记录
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
					colNames.add("RECORD_NO");
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
					colValues.add(recorder.get("RECORD_NO"));
					colValues.add("2");
					colValues.add(xxx.getHead().get("btcOrderId"));
					colValues.add(sf.format(new Date()));
					colValues.add(new Date());
					commonManagerMapper.insertTableByNVList(
							"t_new_import_books", colNames, colValues, primary);
				}
				// 第四步 插入t_new_import_inventory表和t_new_import_inventory_detail表
				String id = insertInventory(xmlString);

				// 第五步
				// 将t_new_import_inventory和t_new_import_inventory_detail表中部分数据组成xml，先保存本地，再通过接口发送
				String xmlStringData = generalRequestXml4TJ(id, bundle);

				// 第五步 向天津外运发送清单数据
				Map reponse = postToTJ(xmlStringData);
				// 回传数据处理
				String status = reponse.get("status") != null ? reponse.get(
						"status").toString() : "";
				String reason = reponse.get("reason") != null ? reponse.get(
						"reason").toString() : "";
				if ("fail".equals(status)) {
					result.put("isSuccess", "false");
					result.put("errorMsg", reason);
				}
			} else {
				// 返回错误，库存不足
				result.put("isSuccess", "false");
				result.put("errorMsg", "库存不足");
			}
		}

		return result;
	}

	private Map postToTJ(String xmlData) {
		String partner_id = CommonUtil.getSystemConfigProperty("TJ_partner_id");
		String business_type = CommonUtil
				.getSystemConfigProperty("TJ_business_type");
		String data_type = CommonUtil.getSystemConfigProperty("TJ_data_type");
		// 请求数据
		String requestData = null;
		try {
			requestData = "partner_id=" + partner_id + "&business_type="
					+ business_type + "&data_type=" + data_type + "&data="
					+ URLEncoder.encode(xmlData, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 发送http请求
		Map<String, Object> head = new HashMap<String, Object>();
		head.put("Content-Type",
				CommonUtil.getSystemConfigProperty("TJ_Content_Type"));
		head.put("Accept", CommonUtil.getSystemConfigProperty("TJ_Accept"));
		// 获取url
		String url = CommonUtil.getSystemConfigProperty("TJ_requestUrl");

		String result = HttpUtil.doPost4TJ(url, head, requestData, false);

		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

		resultMap = XmlUtil.parseXmlFPAPI_SingleNodes(result,
				"//responses/child::*");

		return resultMap;
	}

	private static int num = 1;

	private static synchronized String getMessageId() {

		SimpleDateFormat sf = CommonUtil
				.getDateFormatter(CommonDefine.RETRIEVAL_TIME_FORMAT);

		String messageId = sf.format(new Date()) + String.format("%0" + 10 + "d", num);

		num++;

		return messageId;

	}

	private String generalRequestXml4TJ(String id, ResourceBundle bundle) {
		int idInt = Integer.valueOf(id);
		// step 1 生成xml
		LinkedHashMap InventoryHead = new LinkedHashMap();
		if (snCommonManagerMapper.selectInventoryHead(idInt) != null) {
			LinkedHashMap item = snCommonManagerMapper.selectInventoryHead(
					idInt).get(0);
			// 转换
			if (item != null) {
				for (Object key : item.keySet()) {
					if (bundle.containsKey("TJ_HEAD_" + key.toString())) {
						InventoryHead.put(
								bundle.getObject("TJ_HEAD_" + key.toString()),
								item.get(key));
					} else {
						InventoryHead.put(key.toString(), item.get(key));
					}
				}
			}
		}

		LinkedHashMap InventoryList = new LinkedHashMap();
		if (snCommonManagerMapper.selectInventoryList(idInt) != null) {
			LinkedHashMap item = snCommonManagerMapper.selectInventoryList(
					idInt).get(0);
			// 转换
			if (item != null) {
				for (Object key : item.keySet()) {
					if (bundle.containsKey("TJ_LIST_" + key.toString())) {
						InventoryList.put(
								bundle.getObject("TJ_LIST_" + key.toString()),
								item.get(key));
					} else {
						InventoryList.put(key.toString(), item.get(key));
					}
				}
			}
		}

		LinkedHashMap IODeclContainerList = new LinkedHashMap();
		if (snCommonManagerMapper.selectIODeclContainerList(idInt) != null) {
			LinkedHashMap item = snCommonManagerMapper
					.selectIODeclContainerList(idInt).get(0);
			// 转换
			if (item != null) {
				for (Object key : item.keySet()) {
					if (bundle.containsKey("TJ_IO_" + key.toString())) {
						IODeclContainerList.put(
								bundle.getObject("TJ_IO_" + key.toString()),
								item.get(key));
					} else {
						IODeclContainerList.put(key.toString(), item.get(key));
					}
				}
			}

		}
		LinkedHashMap IODeclOrderRelationList = new LinkedHashMap();
		if (snCommonManagerMapper.selectIODeclOrderRelationList(idInt) != null) {
			LinkedHashMap item = snCommonManagerMapper
					.selectIODeclOrderRelationList(idInt).get(0);
			// 转换
			if (item != null) {
				for (Object key : item.keySet()) {
					if (bundle.containsKey("TJ_IO_" + key.toString())) {
						IODeclOrderRelationList.put(
								bundle.getObject("TJ_IO_" + key.toString()),
								item.get(key));
					} else {
						IODeclOrderRelationList.put(key.toString(),
								item.get(key));
					}
				}
			}
		}

		LinkedHashMap BaseTransfer = snCommonManagerMapper.selectBaseTransfer();

		String resultXmlString = XmlUtil.generalRequestXml4TJ_621(
				InventoryHead, InventoryList, IODeclContainerList,
				IODeclOrderRelationList, BaseTransfer, bundle);

		return resultXmlString;
	}

	// 插入t_new_import_inventory表和t_new_import_inventory_detail表
	private String insertInventory(String xmlString) {
		Map head = XmlUtil
				.parseXmlFPAPI_SingleNodes(xmlString,
						"//inputData/ordersList/orders/orderImformation/orderHead/child::*");
		Map orderDeclareHead = XmlUtil
				.parseXmlFPAPI_SingleNodes(xmlString,
						"//inputData/ordersList/orders/orderDeclare/orderDeclareHead/child::*");
		
		Map orderExpBill = XmlUtil
				.parseXmlFPAPI_SingleNodes(xmlString,
						"//inputData/ordersList/orders/orderExpBill/child::*");
		
		List<Map> orderDeclareItems = XmlUtil.parseXmlFPAPI_MulitpleNodes(
				xmlString,
				"//inputData/ordersList/orders/orderDeclare/orderDeclareItems");

		SimpleDateFormat sf = CommonUtil
				.getDateFormatter(CommonDefine.RETRIEVAL_TIME_FORMAT);

		SimpleDateFormat sf1 = CommonUtil
				.getDateFormatter(CommonDefine.COMMON_FORMAT_1);

		List<String> colNames = new ArrayList<String>();
		List<Object> colValues = new ArrayList<Object>();
		Map primary = new HashMap();
		primary.put("primaryId", null);

		colNames.add("GUID");
		colValues.add(CommonUtil.generalGuid(
				CommonDefine.GUID_FOR_LOGISTICS_SN_1, 10,
				"t_new_import_inventory"));

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
		colValues.add(orderExpBill.get("expressCompanyExcode"));

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
		colValues.add(orderDeclareHead.get("payerName"));

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
		} else {
			colValues.add(null);
		}

		colNames.add("INSURE_FEE");
		if (orderDeclareHead.get("insuranceFee") != null
				&& !orderDeclareHead.get("insuranceFee").toString().isEmpty()) {
			colValues.add(orderDeclareHead.get("insuranceFee"));
		} else {
			colValues.add(0);
		}

		colNames.add("WRAP_TYPE");
		colValues.add(orderDeclareHead.get("warpType"));

		colNames.add("PACK_NO");
		colValues.add("1");

		colNames.add("GROSS_WEIGHT");
		if (orderDeclareHead.get("grossWeight") != null
				&& !orderDeclareHead.get("grossWeight").toString().isEmpty()) {
			colValues.add(orderDeclareHead.get("grossWeight"));
		} else {
			colValues.add(null);
		}

		colNames.add("NET_WEIGHT");
		if (orderDeclareHead.get("netWeight") != null
				&& !orderDeclareHead.get("netWeight").toString().isEmpty()) {
			colValues.add(orderDeclareHead.get("netWeight"));
		} else {
			colValues.add(null);
		}

		colNames.add("PAY_SERIAL_NO");
		if (orderDeclareHead.get("paySerialNo") != null
				&& !orderDeclareHead.get("paySerialNo").toString().isEmpty()) {
			colValues.add(orderDeclareHead.get("paySerialNo"));
		} else {
			colValues.add(null);
		}

		colNames.add("WORTH");
		if (orderDeclareHead.get("worth") != null
				&& !orderDeclareHead.get("worth").toString().isEmpty()) {
			colValues.add(orderDeclareHead.get("worth"));
		} else {
			colValues.add(null);
		}

		colNames.add("NOTE");
		colValues.add("");

		colNames.add("CREAT_TIME");
		colValues.add(new Date());

		commonManagerMapper.insertTableByNVList("t_new_import_inventory",
				colNames, colValues, primary);

		for (Map orderDeclareItem : orderDeclareItems) {

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

			colNames.add("TRADE_COUNTRY");
			colValues.add(orderDeclareHead.get("tradeCountry"));

			colNames.add("CURRENCY");
			colValues.add(orderDeclareItem.get("tradeCurr"));

			colNames.add("QTY");
			if (orderDeclareItem.get("declareCount") != null
					&& !orderDeclareItem.get("declareCount").toString()
							.isEmpty()) {
				colValues.add(orderDeclareItem.get("declareCount"));
			} else {
				colValues.add(null);
			}

			colNames.add("QTY1");
			if (orderDeclareItem.get("firstCount") != null
					&& !orderDeclareItem.get("firstCount").toString().isEmpty()) {
				colValues.add(orderDeclareItem.get("firstCount"));
			} else {
				colValues.add(null);
			}

			colNames.add("QTY2");
			if (orderDeclareItem.get("secondCount") != null
					&& !orderDeclareItem.get("secondCount").toString()
							.isEmpty()) {
				colValues.add(orderDeclareItem.get("secondCount"));
			} else {
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
			} else {
				colValues.add(null);
			}

			colNames.add("TOTAL_PRICE");
			if (orderDeclareItem.get("declTotalPrice") != null
					&& !orderDeclareItem.get("declTotalPrice").toString()
							.isEmpty()) {
				colValues.add(orderDeclareItem.get("declTotalPrice"));
			} else {
				colValues.add(null);
			}

			colNames.add("CREAT_TIME");
			colValues.add(new Date());

			commonManagerMapper.insertTableByNVList(
					"t_new_import_inventory_detail", colNames, colValues,
					primary_sub);

		}

		return primary.get("primaryId").toString();
	}

	public static void main(String arg[]) {

		String logistics_interface = "<LoadHead><loadContents><loadContent><loadContentId>1</loadContentId><outorderId>6666666666</outorderId></loadContent><loadContent><loadContentId>2</loadContentId><outorderId>7777777777</outorderId></loadContent></loadContents><loadHeadId>12</loadHeadId><loadId>1736474588</loadId><total>2</total><tracyNum>3</tracyNum><TotalWeight>2.5</TotalWeight><CarEcNo>苏A234234</CarEcNo></LoadHead>";

		String data_digest = CommonUtil.makeSign(logistics_interface);

		System.out.println(data_digest);
		try {
			System.out
					.println("logistics_interface="
							+ URLEncoder.encode(logistics_interface, "utf-8")
							+ "&data_digest="
							+ URLEncoder.encode(data_digest, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			System.out.println(URLEncoder.encode("helloworld", "utf-8"));
			System.out.println(URLEncoder.encode("voQc3u6+f6pSflMPdw4ySQ==",
					"utf-8"));
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
		// 排序前
		for (Map<String, Object> map : list) {
			System.out.println(map);
		}

		Collections.sort(list, new Comparator<Map>() {
			public int compare(Map o1, Map o2) {

				SimpleDateFormat sf = CommonUtil
						.getDateFormatter(CommonDefine.COMMON_FORMAT);

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

				if (qty1 > qty2 || creatTime1.before(creatTime2)) {
					return 1;
				} else {
					return 0;
				}
			}
		});

		System.out.println("-------------------");
		for (Map<String, Object> map : list) {
			System.out.println(map);
		}

		String s = "<![CDATA[南京市玄武区]]>";
		Pattern p = Pattern.compile(".*<!\\[CDATA\\[(.*)\\]\\]>.*");
		Matcher m = p.matcher(s);

		if (m.matches()) {
			System.out.println(m.group(1));
		}else{
			System.out.println(s);
		}

		System.out.println(CommonUtil.getDateFormatter(CommonDefine.COMMON_FORMAT_2)
					.format(new Date()));

	}

}
