package com.foo.manager.commonManager.thread;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
public class HttpHandleThread_SNOrder implements Callable<Object> {
	@Resource
	private static CommonManagerMapper commonManagerMapper;
	@Resource
	private static SNCommonManagerMapper snCommonManagerMapper;

	private String requestType;
	private String content;
	private String signKey;

	public HttpHandleThread_SNOrder(String requestType, String content, String signKey) {
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
//		 this.content =
//		 "<inputData><ordersList><orders><orderImformation><orderHead><logisticsOrderId>LOS36100001120690200</logisticsOrderId><oriSys>LOS</oriSys><taskOrderid>36100001120690200</taskOrderid><thirdPartyCompany>0080010331</thirdPartyCompany><businessType>C061</businessType><btcOrderId>BOL031707</btcOrderId><announcedValue>100.00</announcedValue><valueUnit>CNY</valueUnit><expectDate>2015-09-07</expectDate><expectTime>11:38:10</expectTime></orderHead><orderItems><logisticsOrderId>LOS36100001120690200</logisticsOrderId><itemNumber>10</itemNumber><location>L971</location><locationDescription>天津口岸</locationDescription><storage>0001</storage><goodsCode>20166666</goodsCode><goodsDescription>长虹空调KFR35GWBP2DN1YF</goodsDescription><goodsNumber>5.000</goodsNumber><goodsUnit>S01</goodsUnit><comValue>100.00</comValue><valueUnit>CNY</valueUnit></orderItems><feedBackOrderCustomers><logisticsOrderId>LOS36100001120690200</logisticsOrderId><itemNumber>10</itemNumber><customerType>AG</customerType><customerId>70057297</customerId><name>C店商户苏宁</name><country>CN</country><city>南京市</city><address>南京市玄武区</address><zipCode>210012</zipCode><fixedLineTelephone>02566996699</fixedLineTelephone><mobilePhone>15295599233</mobilePhone></feedBackOrderCustomers></orderImformation><orderExpBill><logisticOrderId>LOS36300000945410000</logisticOrderId><expressCode>62K900000210</expressCode><btcOrder>BTC01631803170701</btcOrder><btcItemOrder/><name>1asd2</name><telNumber>18513891234</telNumber><province>江苏省</province><city>南京市</city><district>栖霞区</district><adress>仙鹤门仙鹤名苑28-608</adress><firstPlantCrossing/><setLocation/><preparedTime>01:00:00</preparedTime><note/><shipCondition>配送</shipCondition><rePrintFlag/><valueAddTaxFlag/><shipTypeFlag>陆</shipTypeFlag><obligateFlag/><lastPlantCrossing/><route>郑州保税仓→南京小件→WL</route><lastLogisticStation>外设单位编码描述测试</lastLogisticStation><realDeliDate>2018-03-19</realDeliDate><realDeliTime>09:00:00</realDeliTime><paymmentMode>线上下单门店支付</paymmentMode><cashOnDeliveryValue>100.00</cashOnDeliveryValue><expressCompany>0080000013</expressCompany><expressCompanyExcode>444033803247</expressCompanyExcode><originCode>025</originCode><destCode>025</destCode><createTime>17:27:39</createTime><createDate>2018-03-17</createDate></orderExpBill><orderDeclare><orderDeclareHead><orderNo>BOL031707</orderNo><ieFlag>I</ieFlag><importType>1</importType><inOutDateStr>2018-03-17 17:07:17</inOutDateStr><logisCompanyCode>1505130005</logisCompanyCode><logisCompanyName>江苏苏宁物流有限公司</logisCompanyName><companyName>江苏苏宁易购电子商务有限公司</companyName><companyCode>PTE51001410280000005</companyCode><wayBill/><tradeCountry>502</tradeCountry><packNo>1</packNo><grossWeight>1.500</grossWeight><netWeight>1.500</netWeight><warpType>2</warpType><remark>37</remark><enteringPerson>9999</enteringPerson><enteringCompanyName>9999</enteringCompanyName><senderName>versace1563212333333范思哲海外专营店</senderName><consignee>无名</consignee><consigneeAddress>江苏南京市玄武区徐庄苏宁总部鼓楼1号</consigneeAddress><senderCountry>502</senderCountry><payerName>无名</payerName><payerPhoneNumber>13951783938</payerPhoneNumber><paperType>01</paperType><paperNumber>320102198001010059</paperNumber><freight>10.0000</freight><insuranceFee/><payTaxAmount>0.0000</payTaxAmount><worth>109.0000</worth><currCode>142</currCode><mainGName>移动端苏宁内测试购专</mainGName><isAuthorize>1</isAuthorize><paySerialNo/><payType/><payTime/><payMoney/><otherPayPrice/><goodsPriceIncludeTax/><senderProvince/><senderAddr/><senderZip/><senderTel/></orderDeclareHead><orderDeclareItems><logisticOrderId>LOS36300000945410000</logisticOrderId><expressCode>62K900000210</expressCode><orderNo>BOL031707</orderNo><orderItemNo>BOL031707</orderItemNo><goodsOrder>1</goodsOrder><codeTs>11</codeTs><goodsItemNo>000000000690105971</goodsItemNo><goodsName>移动端苏宁内测试购专</goodsName><goodsModel/><originCountry>701</originCountry><tradeCurr>142</tradeCurr><tradeTotal>99.0000</tradeTotal><declPrice>99.00</declPrice><declTotalPrice>99.0000</declTotalPrice><useTo>11</useTo><declareCount>1</declareCount><goodsUnit>011</goodsUnit><goodsGrossWeight>1.500</goodsGrossWeight><firstUnit>011</firstUnit><firstCount>1</firstCount><postalTax/></orderDeclareItems></orderDeclare></orders><orders><orderImformation><orderHead><logisticsOrderId>xxxxxxxxxxxxxxxxxxxx</logisticsOrderId><oriSys>LOS</oriSys><taskOrderid>36100001120690200</taskOrderid><thirdPartyCompany>0080010331</thirdPartyCompany><businessType>C061</businessType><btcOrderId>BOL031708</btcOrderId><announcedValue>100.00</announcedValue><valueUnit>CNY</valueUnit><expectDate>2015-09-07</expectDate><expectTime>11:38:10</expectTime></orderHead><orderItems><logisticsOrderId>xxxxxxxxxxxxxxxxxxxx</logisticsOrderId><itemNumber>10</itemNumber><location>L971</location><locationDescription>天津口岸</locationDescription><storage>0001</storage><goodsCode>20166666</goodsCode><goodsDescription>长虹空调KFR35GWBP2DN1YF</goodsDescription><goodsNumber>5.000</goodsNumber><goodsUnit>S01</goodsUnit><comValue>100.00</comValue><valueUnit>CNY</valueUnit></orderItems><feedBackOrderCustomers><logisticsOrderId>xxxxxxxxxxxxxxxxxxxx</logisticsOrderId><itemNumber>10</itemNumber><customerType>AG</customerType><customerId>70057297</customerId><name>C店商户苏宁</name><country>CN</country><city>南京市</city><address>南京市玄武区</address><zipCode>210012</zipCode><fixedLineTelephone>02566996699</fixedLineTelephone><mobilePhone>15295599233</mobilePhone></feedBackOrderCustomers></orderImformation><orderExpBill><logisticOrderId>LOS36300000945410000</logisticOrderId><expressCode>62K900000210</expressCode><btcOrder>BTC01631803170701</btcOrder><btcItemOrder/><name>1asd2</name><telNumber>18513891234</telNumber><province>江苏省</province><city>南京市</city><district>栖霞区</district><adress>仙鹤门仙鹤名苑28-608</adress><firstPlantCrossing/><setLocation/><preparedTime>01:00:00</preparedTime><note/><shipCondition>配送</shipCondition><rePrintFlag/><valueAddTaxFlag/><shipTypeFlag>陆</shipTypeFlag><obligateFlag/><lastPlantCrossing/><route>郑州保税仓→南京小件→WL</route><lastLogisticStation>外设单位编码描述测试</lastLogisticStation><realDeliDate>2018-03-19</realDeliDate><realDeliTime>09:00:00</realDeliTime><paymmentMode>线上下单门店支付</paymmentMode><cashOnDeliveryValue>100.00</cashOnDeliveryValue><expressCompany>0080000013</expressCompany><expressCompanyExcode>444033803247</expressCompanyExcode><originCode>025</originCode><destCode>025</destCode><createTime>17:27:39</createTime><createDate>2018-03-17</createDate></orderExpBill><orderDeclare><orderDeclareHead><orderNo>BOL031708</orderNo><ieFlag>I</ieFlag><importType>1</importType><inOutDateStr>2018-03-17 17:07:17</inOutDateStr><logisCompanyCode>1505130005</logisCompanyCode><logisCompanyName>江苏苏宁物流有限公司</logisCompanyName><companyName>江苏苏宁易购电子商务有限公司</companyName><companyCode>PTE51001410280000005</companyCode><wayBill/><tradeCountry>502</tradeCountry><packNo>1</packNo><grossWeight>1.500</grossWeight><netWeight>1.500</netWeight><warpType>2</warpType><remark>37</remark><enteringPerson>9999</enteringPerson><enteringCompanyName>9999</enteringCompanyName><senderName>versace1563212333333范思哲海外专营店</senderName><consignee>无名</consignee><consigneeAddress>江苏南京市玄武区徐庄苏宁总部鼓楼1号</consigneeAddress><senderCountry>502</senderCountry><payerName>无名</payerName><payerPhoneNumber>13951783938</payerPhoneNumber><paperType>01</paperType><paperNumber>320102198001010059</paperNumber><freight>10.0000</freight><insuranceFee/><payTaxAmount>0.0000</payTaxAmount><worth>109.0000</worth><currCode>142</currCode><mainGName>移动端苏宁内测试购专</mainGName><isAuthorize>1</isAuthorize><paySerialNo/><payType/><payTime/><payMoney/><otherPayPrice/><goodsPriceIncludeTax/><senderProvince/><senderAddr/><senderZip/><senderTel/></orderDeclareHead><orderDeclareItems><logisticOrderId>LOS36300000945410000</logisticOrderId><expressCode>62K900000210</expressCode><orderNo>BOL031708</orderNo><orderItemNo>BOL031708</orderItemNo><goodsOrder>1</goodsOrder><codeTs>11</codeTs><goodsItemNo>000000000690105971</goodsItemNo><goodsName>移动端苏宁内测试购专</goodsName><goodsModel/><originCountry>701</originCountry><tradeCurr>142</tradeCurr><tradeTotal>99.0000</tradeTotal><declPrice>99.00</declPrice><declTotalPrice>99.0000</declTotalPrice><useTo>11</useTo><declareCount>1</declareCount><goodsUnit>011</goodsUnit><goodsGrossWeight>1.500</goodsGrossWeight><firstUnit>011</firstUnit><firstCount>1</firstCount><postalTax/></orderDeclareItems></orderDeclare></orders></ordersList></inputData>";
		// 订单信息--C005
//		 this.requestType = HttpServerManagerService.requestType_Order;
//		 this.content =
//		 "<inputData><ordersList><orders><orderImformation><orderHead><logisticsOrderId>LOS36100001120690200</logisticsOrderId><oriSys>LOS</oriSys><taskOrderid>36100001120690200</taskOrderid><thirdPartyCompany>0080010331</thirdPartyCompany><businessType>C005</businessType><btcOrderId>BOL031707</btcOrderId><btcItemNumber>1</btcItemNumber><announcedValue>100.00</announcedValue><valueUnit>CNY</valueUnit><expectDate>2015-09-07</expectDate><expectTime>11:38:10</expectTime></orderHead><orderItems><logisticsOrderId>LOS36100001120690200</logisticsOrderId><itemNumber>10</itemNumber><location>L971</location><locationDescription>天津口岸</locationDescription><storage>0001</storage><goodsCode>20166666</goodsCode><goodsDescription>长虹空调KFR35GWBP2DN1YF</goodsDescription><goodsNumber>5.000</goodsNumber><goodsUnit>S01</goodsUnit><comValue>100.00</comValue><valueUnit>CNY</valueUnit></orderItems><feedBackOrderCustomers><logisticsOrderId>LOS36100001120690200</logisticsOrderId><itemNumber>10</itemNumber><customerType>AG</customerType><customerId>70057297</customerId><name>C店商户苏宁</name><country>CN</country><city>南京市</city><address>南京市玄武区</address><zipCode>210012</zipCode><fixedLineTelephone>02566996699</fixedLineTelephone><mobilePhone>15295599233</mobilePhone></feedBackOrderCustomers></orderImformation><orderExpBill><logisticOrderId>LOS36300000945410000</logisticOrderId><expressCode>62K900000210</expressCode><btcOrder>BTC01631803170701</btcOrder><btcItemOrder/><name>1asd2</name><telNumber>18513891234</telNumber><province>江苏省</province><city>南京市</city><district>栖霞区</district><adress>仙鹤门仙鹤名苑28-608</adress><firstPlantCrossing/><setLocation/><preparedTime>01:00:00</preparedTime><note/><shipCondition>配送</shipCondition><rePrintFlag/><valueAddTaxFlag/><shipTypeFlag>陆</shipTypeFlag><obligateFlag/><lastPlantCrossing/><route>郑州保税仓→南京小件→WL</route><lastLogisticStation>外设单位编码描述测试</lastLogisticStation><realDeliDate>2018-03-19</realDeliDate><realDeliTime>09:00:00</realDeliTime><paymmentMode>线上下单门店支付</paymmentMode><cashOnDeliveryValue>100.00</cashOnDeliveryValue><expressCompany>0080000013</expressCompany><expressCompanyExcode>444033803247</expressCompanyExcode><originCode>025</originCode><destCode>025</destCode><createTime>17:27:39</createTime><createDate>2018-03-17</createDate></orderExpBill><orderDeclare><orderDeclareHead><orderNo>BOL031707</orderNo><ieFlag>I</ieFlag><importType>1</importType><inOutDateStr>2018-03-17 17:07:17</inOutDateStr><logisCompanyCode>1505130005</logisCompanyCode><logisCompanyName>江苏苏宁物流有限公司</logisCompanyName><companyName>江苏苏宁易购电子商务有限公司</companyName><companyCode>PTE51001410280000005</companyCode><wayBill/><tradeCountry>502</tradeCountry><packNo>1</packNo><grossWeight>1.500</grossWeight><netWeight>1.500</netWeight><warpType>2</warpType><remark>37</remark><enteringPerson>9999</enteringPerson><enteringCompanyName>9999</enteringCompanyName><senderName>versace1563212333333范思哲海外专营店</senderName><consignee>无名</consignee><consigneeAddress>江苏南京市玄武区徐庄苏宁总部鼓楼1号</consigneeAddress><senderCountry>502</senderCountry><payerName>无名</payerName><payerPhoneNumber>13951783938</payerPhoneNumber><paperType>01</paperType><paperNumber>320102198001010059</paperNumber><freight>10.0000</freight><insuranceFee/><payTaxAmount>0.0000</payTaxAmount><worth>109.0000</worth><currCode>142</currCode><mainGName>移动端苏宁内测试购专</mainGName><isAuthorize>1</isAuthorize><paySerialNo/><payType/><payTime/><payMoney/><otherPayPrice/><goodsPriceIncludeTax/><senderProvince/><senderAddr/><senderZip/><senderTel/></orderDeclareHead><orderDeclareItems><logisticOrderId>LOS36300000945410000</logisticOrderId><expressCode>62K900000210</expressCode><orderNo>BOL031707</orderNo><orderItemNo>BOL031707</orderItemNo><goodsOrder>1</goodsOrder><codeTs>11</codeTs><goodsItemNo>000000000690105971</goodsItemNo><goodsName>移动端苏宁内测试购专</goodsName><goodsModel/><originCountry>701</originCountry><tradeCurr>142</tradeCurr><tradeTotal>99.0000</tradeTotal><declPrice>99.00</declPrice><declTotalPrice>99.0000</declTotalPrice><useTo>11</useTo><declareCount>1</declareCount><goodsUnit>011</goodsUnit><goodsGrossWeight>1.500</goodsGrossWeight><firstUnit>011</firstUnit><firstCount>1</firstCount><postalTax/></orderDeclareItems></orderDeclare></orders><orders><orderImformation><orderHead><logisticsOrderId>xxxxxxxxxxxxxxxxxxxx</logisticsOrderId><oriSys>LOS</oriSys><taskOrderid>36100001120690200</taskOrderid><thirdPartyCompany>0080010331</thirdPartyCompany><businessType>C005</businessType><btcOrderId>BOL031708</btcOrderId><btcItemNumber>2</btcItemNumber><announcedValue>100.00</announcedValue><valueUnit>CNY</valueUnit><expectDate>2015-09-07</expectDate><expectTime>11:38:10</expectTime></orderHead><orderItems><logisticsOrderId>xxxxxxxxxxxxxxxxxxxx</logisticsOrderId><itemNumber>10</itemNumber><location>L971</location><locationDescription>天津口岸</locationDescription><storage>0001</storage><goodsCode>20166666</goodsCode><goodsDescription>长虹空调KFR35GWBP2DN1YF</goodsDescription><goodsNumber>5.000</goodsNumber><goodsUnit>S01</goodsUnit><comValue>100.00</comValue><valueUnit>CNY</valueUnit></orderItems><feedBackOrderCustomers><logisticsOrderId>xxxxxxxxxxxxxxxxxxxx</logisticsOrderId><itemNumber>10</itemNumber><customerType>AG</customerType><customerId>70057297</customerId><name>C店商户苏宁</name><country>CN</country><city>南京市</city><address>南京市玄武区</address><zipCode>210012</zipCode><fixedLineTelephone>02566996699</fixedLineTelephone><mobilePhone>15295599233</mobilePhone></feedBackOrderCustomers></orderImformation><orderExpBill><logisticOrderId>LOS36300000945410000</logisticOrderId><expressCode>62K900000210</expressCode><btcOrder>BTC01631803170701</btcOrder><btcItemOrder/><name>1asd2</name><telNumber>18513891234</telNumber><province>江苏省</province><city>南京市</city><district>栖霞区</district><adress>仙鹤门仙鹤名苑28-608</adress><firstPlantCrossing/><setLocation/><preparedTime>01:00:00</preparedTime><note/><shipCondition>配送</shipCondition><rePrintFlag/><valueAddTaxFlag/><shipTypeFlag>陆</shipTypeFlag><obligateFlag/><lastPlantCrossing/><route>郑州保税仓→南京小件→WL</route><lastLogisticStation>外设单位编码描述测试</lastLogisticStation><realDeliDate>2018-03-19</realDeliDate><realDeliTime>09:00:00</realDeliTime><paymmentMode>线上下单门店支付</paymmentMode><cashOnDeliveryValue>100.00</cashOnDeliveryValue><expressCompany>0080000013</expressCompany><expressCompanyExcode>444033803247</expressCompanyExcode><originCode>025</originCode><destCode>025</destCode><createTime>17:27:39</createTime><createDate>2018-03-17</createDate></orderExpBill><orderDeclare><orderDeclareHead><orderNo>BOL031708</orderNo><ieFlag>I</ieFlag><importType>1</importType><inOutDateStr>2018-03-17 17:07:17</inOutDateStr><logisCompanyCode>1505130005</logisCompanyCode><logisCompanyName>江苏苏宁物流有限公司</logisCompanyName><companyName>江苏苏宁易购电子商务有限公司</companyName><companyCode>PTE51001410280000005</companyCode><wayBill/><tradeCountry>502</tradeCountry><packNo>1</packNo><grossWeight>1.500</grossWeight><netWeight>1.500</netWeight><warpType>2</warpType><remark>37</remark><enteringPerson>9999</enteringPerson><enteringCompanyName>9999</enteringCompanyName><senderName>versace1563212333333范思哲海外专营店</senderName><consignee>无名</consignee><consigneeAddress>江苏南京市玄武区徐庄苏宁总部鼓楼1号</consigneeAddress><senderCountry>502</senderCountry><payerName>无名</payerName><payerPhoneNumber>13951783938</payerPhoneNumber><paperType>01</paperType><paperNumber>320102198001010059</paperNumber><freight>10.0000</freight><insuranceFee/><payTaxAmount>0.0000</payTaxAmount><worth>109.0000</worth><currCode>142</currCode><mainGName>移动端苏宁内测试购专</mainGName><isAuthorize>1</isAuthorize><paySerialNo/><payType/><payTime/><payMoney/><otherPayPrice/><goodsPriceIncludeTax/><senderProvince/><senderAddr/><senderZip/><senderTel/></orderDeclareHead><orderDeclareItems><logisticOrderId>LOS36300000945410000</logisticOrderId><expressCode>62K900000210</expressCode><orderNo>BOL031708</orderNo><orderItemNo>BOL031708</orderItemNo><goodsOrder>1</goodsOrder><codeTs>11</codeTs><goodsItemNo>000000000690105971</goodsItemNo><goodsName>移动端苏宁内测试购专</goodsName><goodsModel/><originCountry>701</originCountry><tradeCurr>142</tradeCurr><tradeTotal>99.0000</tradeTotal><declPrice>99.00</declPrice><declTotalPrice>99.0000</declTotalPrice><useTo>11</useTo><declareCount>1</declareCount><goodsUnit>011</goodsUnit><goodsGrossWeight>1.500</goodsGrossWeight><firstUnit>011</firstUnit><firstCount>1</firstCount><postalTax/></orderDeclareItems></orderDeclare></orders></ordersList></inputData>";

		
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
		}

		System.out.println("【结束】数据处理：" + signKey);

		return result;
	}

	// 处理订单数据
	private String handleXml_Order(String xmlString) {

		String xmlReturnString = "";

		System.out
				.println("---------------------------【FPAPI_ORDER】-------------------------------");
		
		List<String> ordersDataXML = XmlUtil.getNodesXmlData(xmlString, "//inputData/ordersList/orders");
		
		List<Map> resultList = new ArrayList<Map>();
		
		for(String xmlData:ordersDataXML){
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
						.parseXmlFPAPI_SingleNodes(xmlData,
								"//orders/orderImformation/orderHead/child::*");

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
					result = handleOrderC061(xmlData, bundle);

				} else if (checkBusinessType_C005.equals(businessType)) {
					//多线程调测代码
//					synchronized(HttpHandleThread.class){
//						// C005报文处理
//						Map xxx = commonManagerMapper.selectTableById(
//								"t_new_import_books", "BOOKS_ID", 1);
//						
//						System.out.println("更新前qty："+xxx.get("QTY"));
//						xxx.put("QTY",
//								Double.parseDouble(xxx.get("QTY").toString()) + 1);
//
//						commonManagerMapper.updateTableByNVList(
//								"t_new_import_books", "BOOKS_ID", 1,
//								new ArrayList<String>(xxx.keySet()),
//								new ArrayList<Object>(xxx.values()));
//						Thread.sleep(500);
//					}
					
//					result = handleOrderC005(head, xmlData, bundle);
					synchronized(HttpHandleThread_SNOrder.class){
						result = handleOrderC005_new(head, xmlData, bundle);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				result.put("isSuccess", "false");
				result.put("errorMsg", "系统异常");
			}
			result.put("logisticsOrderId", head.get("logisticsOrderId"));
			
			resultList.add(result);
		}
		

		// 构建返回数据
		List<LinkedMap> dataList = new ArrayList<LinkedMap>();
		for (Map result:resultList) {
			LinkedMap resultData = new LinkedMap();
			resultData.put("logisticsOrderId",result.get("logisticsOrderId"));
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


	private Map handleOrderC061(String xmlString, ResourceBundle bundle) {
		Map result = new HashMap();
		result.put("isSuccess", true);

		SimpleDateFormat sf = CommonUtil
				.getDateFormatter(CommonDefine.COMMON_FORMAT_1);
		List<Map> items = XmlUtil.parseXmlFPAPI_MulitpleNodes(xmlString,
				"//orders/orderImformation/orderItems");

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

	private Map handleOrderC005_new(Map head, String xmlString,
			ResourceBundle bundle) {
		Map result = new HashMap();

		result.put("isSuccess", "true");

		SimpleDateFormat sf = CommonUtil
				.getDateFormatter(CommonDefine.COMMON_FORMAT_1);
		// t_new_import_inventory表中查找数据
		List<String> colNames = new ArrayList<String>();
		List<Object> colValues = new ArrayList<Object>();
		colNames.add("ORDER_NO");
		colValues.add(head.get("btcOrderId"));
		List<Map<String, Object>> inventoryList = commonManagerMapper
				.selectTableListByNVList("t_new_import_inventory", colNames,
						colValues, null, null);
		
		Map inventory = null;
		int inventoryStatus = 0;
		
		//step1:根据ORDER_NO号查询t_new_import_inventory.status
		if(inventoryList != null && inventoryList.size()>0){
			inventory = inventoryList.get(0);
			inventoryStatus = Integer.parseInt(inventory.get("STATUS").toString());
		}
		
		if (0 == inventoryStatus || 2 == inventoryStatus) {
			String inventoryId = (inventory!=null?inventory.get("INVENTORY_ID").toString():null);
			if(0 == inventoryStatus){
				// 未生成清单，数据入库，更新或插入
				// 插入主数据
				inventoryId = insertOrUpdateInventory_main(xmlString, inventory);
				// 插入子数据
				String subId = insertOrUpdateInventoryDetail_new(xmlString, inventoryId);
			}
			// 获取商品种类数据量
			int itemNumber = Integer.parseInt(head.get("btcItemNumber")
					.toString());
			//获取商品种类
			List<Map<String, Object>> subDataList = commonManagerMapper
					.selectTableListByCol("t_new_import_inventory_detail",
							"ORDER_NO", head.get("btcOrderId"), null, null);
			//更新主表的netWeight和grossWeight
			double qtyCount = 0;
			for(Map<String, Object> subData:subDataList){
				if(subData.get("QTY1")!=null && !subData.get("QTY1").toString().isEmpty()){
					qtyCount = qtyCount+Double.parseDouble(subData.get("QTY1").toString());
				}
			}
			//t_new_import_inventory表更新net_weight,gross_weight
			colNames.clear();
			colValues.clear();
			colNames.add("GROSS_WEIGHT");
			colNames.add("NET_WEIGHT");
			colValues.add(qtyCount);
			colValues.add(qtyCount);
			commonManagerMapper.updateTableByNVList("t_new_import_inventory", "INVENTORY_ID", inventoryId, colNames, colValues);
			//判断库存是否足够
			if(itemNumber == subDataList.size() || 2 == inventoryStatus){
				//判断库存是否足够
				List<BookOrderModel> bookOrders = isSkuEnough(head,subDataList);
				boolean isSkuEnough = (bookOrders !=null);
				//扣库存，发报文
				if (isSkuEnough) {
					//更新账册表
					updateBookRecord(bookOrders);
					//t_new_import_inventory表更新status
					colNames.clear();
					colValues.clear();
					colNames.add("STATUS");
					colValues.add("1");
					commonManagerMapper.updateTableByNVList("t_new_import_inventory", "INVENTORY_ID", inventoryId, colNames, colValues);
					// 将t_new_import_inventory和t_new_import_inventory_detail表中部分数据组成xml，先保存本地，再通过接口发送
					String xmlStringData = generalRequestXml4TJ(inventoryId, bundle);

					// 第五步 向天津外运发送清单数据
					Map reponse = postToTJ(xmlStringData,CommonUtil
							.getSystemConfigProperty("TJ_business_type"));
					// 回传数据处理
					String status = reponse.get("status") != null ? reponse.get(
							"status").toString() : "";
					String reason = reponse.get("reason") != null ? reponse.get(
							"reason").toString() : "";
					if ("fail".equals(status)) {
						result.put("isSuccess", "true");
						result.put("errorMsg", reason);
					}
				}else{
					//库存不足
					//t_new_import_inventory表更新status
					colNames.clear();
					colValues.clear();
					colNames.add("STATUS");
					colValues.add("2");
					commonManagerMapper.updateTableByNVList("t_new_import_inventory", "INVENTORY_ID", inventoryId, colNames, colValues);
					//流程结束
					result.put("isSuccess", "true");
					result.put("errorMsg", "库存不足");
				}
			}else{
				//流程结束
				result.put("isSuccess", "true");
				result.put("errorMsg", "订单商品种类不全");
			}
		}else {
			//已生成清单，返回苏宁成功，结束流程
			result.put("isSuccess", "true");
			result.put("errorMsg", "已生成清单");
		}

		return result;
	}
	
	//判断库存是否足够
	private List<BookOrderModel> isSkuEnough(Map head,List<Map<String, Object>> inventoryDetailList){
		boolean isSkuEnough = true;
		List<BookOrderModel> bookOrders = new ArrayList<BookOrderModel>();
		for (Map item : inventoryDetailList) {
			String goodsCode = item.get("ITEM_NO") != null ? item.get(
					"ITEM_NO").toString() : "";
			String goodsNumber = item.get("QTY") != null ? item
					.get("QTY").toString() : "";

			double goodsNumberDouble = !goodsNumber.isEmpty() ? Double
					.valueOf(goodsNumber) : 0;
			// t_new_import_books表中查找数据
			// ADD_REDUCE_FLAG=1
			// SKU=<orderItems><goodsCode>
			// QTY>=<orderItems><goodsNumber>
			//增加一个条件，RECORD_NO不等于空
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
		if(isSkuEnough){
			return bookOrders;
		}else{
			return null;
		}
	}
	
	//更新账册表
	private void updateBookRecord(List<BookOrderModel> bookOrders){
		List<String> colNames = new ArrayList<String>();
		List<Object> colValues = new ArrayList<Object>();
		for (BookOrderModel xxx : bookOrders) {
			List<Map> skuList = xxx.getBookItems();
			// RECORD_NO最小的记录减去<orderItems><goodsNumber>。
			Collections.sort(skuList, new Comparator<Map>() {
				public int compare(Map o1, Map o2) {
//					double qty1 = Double.valueOf(o1.get("QTY")
//							.toString());
//					double qty2 = Double.valueOf(o2.get("QTY")
//							.toString());
					String recordNo1 = o1.get("RECORD_NO")!=null?o1.get("RECORD_NO").toString():"";
					String recordNo2 = o2.get("RECORD_NO")!=null?o2.get("RECORD_NO").toString():"";
//					if (qty1 > qty2) {
						if(recordNo1.compareTo(recordNo2)<0){
							return 0;
						}else{
							return 1;
						}
//					} else {
//						return 0;
//					}
				}
			});

			// 插入新记录
			Map recorder = skuList.get(0);
			
			colNames.clear();
			colValues.clear();
			double updateQty = Double.valueOf(recorder.get("QTY")
					.toString())
					- Double.valueOf(xxx.getOrderitem()
							.get("QTY").toString());
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
			Map newBook = new HashMap();
			
			newBook.put("SKU", xxx.getOrderitem().get("ITEM_NO"));
			newBook.put("DESCRIPTION", xxx.getOrderitem().get("ITEM_NAME"));
			newBook.put("QTY", xxx.getOrderitem().get("QTY"));
			
			newBook.put("GOODS_SERIALNO", recorder.get("GOODS_SERIALNO"));
			newBook.put("DECL_NO", recorder.get("DECL_NO"));
			newBook.put("CON_MODEL", recorder.get("CON_MODEL"));
			newBook.put("CON_NUM", recorder.get("CON_NUM"));
			newBook.put("CON_NO", recorder.get("CON_NO"));
			newBook.put("RECORD_NO", recorder.get("RECORD_NO"));
			newBook.put("ADD_REDUCE_FLAG", "2");
			newBook.put("QTY1", recorder.get("QTY1"));
			newBook.put("QTY2", recorder.get("QTY2"));
			newBook.put("ORDER_NO", xxx.getHead().get("btcOrderId"));
			
			newBook.put("PRICE", recorder.get("PRICE"));
			newBook.put("CURRENCY", recorder.get("CURRENCY"));
			
			
			newBook.put("CREAT_DATE", CommonUtil
					.getDateFormatter(CommonDefine.COMMON_FORMAT_1).format(new Date()));
			newBook.put("CREAT_TIME", new Date());

			commonManagerMapper.insertTableByNVList(
					"t_new_import_books", new ArrayList<String>(newBook.keySet()),
					new ArrayList<Object>(newBook.values()), primary);
		}
	}

	// 插入t_new_import_inventory表
	private String insertOrUpdateInventory_main(String xmlString,Map inventory) {
		Map head = XmlUtil
				.parseXmlFPAPI_SingleNodes(xmlString,
						"//orders/orderImformation/orderHead/child::*");
		Map orderDeclareHead = XmlUtil
				.parseXmlFPAPI_SingleNodes(xmlString,
						"//orders/orderDeclare/orderDeclareHead/child::*");
		
		Map orderExpBill = XmlUtil
				.parseXmlFPAPI_SingleNodes(xmlString,
						"//orders/orderExpBill/child::*");
		
		Map orderDeclareItems = XmlUtil.parseXmlFPAPI_SingleNodes(
				xmlString,
				"//orders/orderDeclare/orderDeclareItems/child::*");

		SimpleDateFormat sf = CommonUtil
				.getDateFormatter(CommonDefine.RETRIEVAL_TIME_FORMAT);

		SimpleDateFormat sf1 = CommonUtil
				.getDateFormatter(CommonDefine.COMMON_FORMAT_1);

		
		if(inventory == null){
			inventory = new HashMap();
		}
		
		inventory.put("GUID", CommonUtil.generalGuid(
				CommonDefine.GUID_FOR_LOGISTICS_SN_1, 10,
				"t_new_import_inventory"));
		inventory.put("CUSTOM_CODE", "0213");
		inventory.put("APP_TYPE", "1");
		inventory.put("APP_TIME", sf.format(new Date()));
		inventory.put("APP_STATUS", "2");
		inventory.put("COP_NO", head.get("taskOrderid"));
		inventory.put("PRE_NO", "");
//		inventory.put("EBC_CODE", "3201966A69");
//		inventory.put("EBC_NAME", "江苏苏宁易购电子商务有限公司");
		inventory.put("EBC_CODE", orderDeclareHead.get("eCommerceCode"));
		inventory.put("EBC_NAME", orderDeclareHead.get("eCommerceName"));
		inventory.put("EBP_CODE", "3201966A69");
		inventory.put("EBP_NAME", "江苏苏宁易购电子商务有限公司");
		inventory.put("ORDER_NO", head.get("btcOrderId"));
		inventory.put("LOGISTICS_NO", orderExpBill.get("expressCompanyExcode"));
		
		if(orderExpBill.get("expressCompany")!=null){
			if("SN".equals(orderExpBill.get("expressCompany").toString())){
				inventory.put("LOGISTICS_CODE", "3201961A28");
				inventory.put("LOGISTICS_NAME", "江苏苏宁物流有限公司");
			}
			if("SF".equals(orderExpBill.get("expressCompany").toString())){
				inventory.put("LOGISTICS_CODE", "1210680001");
				inventory.put("LOGISTICS_NAME", "天津顺丰速递有限公司");
			}
		}
		inventory.put("ASSURE_CODE", "3201966A69");
		inventory.put("EMS_NO", "T0213W000152");
		inventory.put("INVT_NO", "");
		inventory.put("DECL_TIME", sf1.format(new Date()));
		inventory.put("PORT_CODE", "0213");
		inventory.put("IE_DATE", null);
		inventory.put("BUYER_NAME", orderDeclareHead.get("payerName"));
		inventory.put("BUYER_IDTYPE", "1");
		inventory.put("BUYER_IDNUMBER", orderDeclareHead.get("paperNumber"));
		inventory.put("BUYER_TELEPHONE", orderDeclareHead.get("payerPhoneNumber"));
		inventory.put("CONSIGNEE_ADDRESS", orderDeclareHead.get("consigneeAddress"));
		inventory.put("AGENT_CODE", "1207980025");
		inventory.put("AGENT_NAME", "天津中外运报关有限公司");
		inventory.put("AERA_CODE", "1207610251");
		inventory.put("AERA_NAME", "天津中外运国际物流发展有限公司");
		inventory.put("TRADE_MODE", "1210");
		inventory.put("TRAF_MODE", "Y");
		inventory.put("TRAF_NO", "");
		inventory.put("LOCT_NO", "");
		inventory.put("LICENSE_NO", "");
		inventory.put("COUNTRY", "142");
		inventory.put("CURRENCY", "142");
		if (orderDeclareHead.get("freight") != null
				&& !orderDeclareHead.get("freight").toString().isEmpty()) {
			inventory.put("FREIGHT", orderDeclareHead.get("freight"));
		} else {
			inventory.put("FREIGHT", null);
		}
		if (orderDeclareHead.get("insuranceFee") != null
				&& !orderDeclareHead.get("insuranceFee").toString().isEmpty()) {
			inventory.put("INSURE_FEE", orderDeclareHead.get("insuranceFee"));
		} else {
			inventory.put("INSURE_FEE", 0);
		}
		inventory.put("WRAP_TYPE", orderDeclareHead.get("warpType"));
		inventory.put("PACK_NO", "1");
//		if (orderDeclareHead.get("grossWeight") != null
//				&& !orderDeclareHead.get("grossWeight").toString().isEmpty()) {
//			order.put("GROSS_WEIGHT", orderDeclareHead.get("grossWeight"));
//		} else {
//			order.put("GROSS_WEIGHT", null);
//		}
//		if (orderDeclareHead.get("netWeight") != null
//				&& !orderDeclareHead.get("netWeight").toString().isEmpty()) {
//			order.put("NET_WEIGHT", orderDeclareHead.get("netWeight"));
//		} else {
//			order.put("NET_WEIGHT", null);
//		}
		if (orderDeclareHead.get("paySerialNo") != null
				&& !orderDeclareHead.get("paySerialNo").toString().isEmpty()) {
			inventory.put("PAY_SERIAL_NO", orderDeclareHead.get("paySerialNo"));
		} else {
			inventory.put("PAY_SERIAL_NO", null);
		}
		if (orderDeclareItems.get("tradeTotal") != null
				&& !orderDeclareItems.get("tradeTotal").toString().isEmpty()) {
			inventory.put("WORTH", orderDeclareItems.get("tradeTotal"));
		} else {
			inventory.put("WORTH", null);
		}
		inventory.put("NOTE", "");
//		order.put("LOS_NO", head.get("logisticsOrderId"));
		inventory.put("CREAT_TIME", new Date());
		
		inventory.put("ITEM_NUMBER", head.get("btcItemNumber"));
		inventory.put("STATUS", "0");
		
		String id;
		//插入或更新
		if (inventory.containsKey("INVENTORY_ID")) {
			id = inventory.get("INVENTORY_ID").toString();
//			commonManagerMapper.updateTableByNVList("t_new_import_inventory",
//					"INVENTORY_ID", inventory.get("INVENTORY_ID"),
//					new ArrayList<String>(inventory.keySet()),
//					new ArrayList<Object>(inventory.values()));
		} else {
			Map primary = new HashMap();
			primary.put("primaryId", null);
			commonManagerMapper.insertTableByNVList("t_new_import_inventory",
					new ArrayList<String>(inventory.keySet()),
					new ArrayList<Object>(inventory.values()), primary);
			id = primary.get("primaryId").toString();
		}
		
		return id;
	}
	
	// 插入t_new_import_inventory_detail表
	private String insertOrUpdateInventoryDetail_new(String xmlString,String mainId) {
		Map head = XmlUtil
				.parseXmlFPAPI_SingleNodes(xmlString,
						"//orders/orderImformation/orderHead/child::*");
		Map orderDeclareHead = XmlUtil
				.parseXmlFPAPI_SingleNodes(xmlString,
						"//orders/orderDeclare/orderDeclareHead/child::*");
		
		Map orderExpBill = XmlUtil
				.parseXmlFPAPI_SingleNodes(xmlString,
						"//orders/orderExpBill/child::*");
		
		Map orderDeclareItems = XmlUtil.parseXmlFPAPI_SingleNodes(
				xmlString,
				"//orders/orderDeclare/orderDeclareItems/child::*");

		SimpleDateFormat sf = CommonUtil
				.getDateFormatter(CommonDefine.RETRIEVAL_TIME_FORMAT);

		SimpleDateFormat sf1 = CommonUtil
				.getDateFormatter(CommonDefine.COMMON_FORMAT_1);
		
		//查询
		List<String> colNames = new ArrayList<String>();
		List<Object> colValues = new ArrayList<Object>();
		colNames.add("ORDER_NO");
		colNames.add("LOS_NO");
		colValues.add(head.get("btcOrderId"));
		colValues.add(head.get("logisticsOrderId"));
		List<Map<String, Object>> rows = commonManagerMapper
				.selectTableListByNVList("t_new_import_inventory_detail", colNames,
						colValues, null, null);
		
		Map subOrder = new HashMap();
		if(rows != null && rows.size()>0){
			subOrder = rows.get(0);
		}
		//插入t_new_import_inventory_detail表
		subOrder.put("INVENTORY_ID", mainId);
		subOrder.put("GNUM", orderDeclareItems.get("goodsOrder"));
		subOrder.put("ITEM_NO", orderDeclareItems.get("goodsItemNo"));
		subOrder.put("ITEM_NAME", orderDeclareItems.get("goodsName"));
		subOrder.put("G_CODE", "");
		subOrder.put("G_NAME", orderDeclareItems.get("goodsName"));
		subOrder.put("G_MODEL", orderDeclareItems.get("goodsModel"));
		subOrder.put("BARCODE", "");
		subOrder.put("COUNTRY", orderDeclareItems.get("originCountry"));
		subOrder.put("TRADE_COUNTRY", orderDeclareHead.get("tradeCountry"));
		subOrder.put("CURRENCY", orderDeclareItems.get("tradeCurr"));
		if (orderDeclareItems.get("declareCount") != null
				&& !orderDeclareItems.get("declareCount").toString()
						.isEmpty()) {
			subOrder.put("QTY", orderDeclareItems.get("declareCount"));
		} else {
			subOrder.put("QTY", null);
		}
		// 填<orderDeclareItems><goodsGrossWeight>的值乘以<orderDeclareItems><declareCount>的值。
		if (orderDeclareItems.get("goodsGrossWeight") != null
				&& !orderDeclareItems.get("goodsGrossWeight").toString()
						.isEmpty()
				&& orderDeclareItems.get("declareCount") != null
				&& !orderDeclareItems.get("declareCount").toString().isEmpty()) {
			subOrder.put(
					"QTY1",
					Double.parseDouble(orderDeclareItems
							.get("goodsGrossWeight").toString())
							* Double.parseDouble(orderDeclareItems
									.get("declareCount").toString()));
		} else {
			subOrder.put("QTY1", null);
		}
		if (orderDeclareItems.get("secondCount") != null
				&& !orderDeclareItems.get("secondCount").toString()
						.isEmpty()) {
			subOrder.put("QTY2", orderDeclareItems.get("secondCount"));
		} else {
			subOrder.put("QTY2", null);
		}
		subOrder.put("UNIT", orderDeclareItems.get("goodsUnit"));
		subOrder.put("UNIT1", orderDeclareItems.get("firstUnit"));
		subOrder.put("UNIT2", orderDeclareItems.get("secondUnit"));
		
		if (orderDeclareItems.get("declPrice") != null
				&& !orderDeclareItems.get("declPrice").toString().isEmpty()) {
			subOrder.put("PRICE", orderDeclareItems.get("declPrice"));
		} else {
			subOrder.put("PRICE", null);
		}
				
		if (subOrder.get("PRICE") != null && subOrder.get("QTY") != null) {
			subOrder.put(
					"TOTAL_PRICE",
					Double.parseDouble(subOrder.get("PRICE").toString())
							* Double.parseDouble(subOrder.get("QTY").toString()));
		} else {
			subOrder.put("TOTAL_PRICE", null);
		}
		
		subOrder.put("CREAT_TIME", new Date());
		
		subOrder.put("LOS_NO", head.get("logisticsOrderId"));
		subOrder.put("ORDER_NO", head.get("btcOrderId"));

		String id;
		//插入或更新
		if (subOrder.containsKey("INVENTORY_DETAIL_ID")) {
			id = subOrder.get("INVENTORY_DETAIL_ID").toString();
//			commonManagerMapper.updateTableByNVList("t_new_import_inventory_detail",
//					"INVENTORY_DETAIL_ID", subOrder.get("INVENTORY_DETAIL_ID"),
//					new ArrayList<String>(subOrder.keySet()),
//					new ArrayList<Object>(subOrder.values()));
		} else {
			Map primary = new HashMap();
			primary.put("primaryId", null);
			commonManagerMapper.insertTableByNVList("t_new_import_inventory_detail",
					new ArrayList<String>(subOrder.keySet()),
					new ArrayList<Object>(subOrder.values()), primary);
			id = primary.get("primaryId").toString();
		}

		return id;
	}
	
	public static Map postToTJ(String xmlData,String businessType) {
		String partner_id = CommonUtil.getSystemConfigProperty("TJ_partner_id");
		String data_type = CommonUtil.getSystemConfigProperty("TJ_data_type");
		// 请求数据
		String requestData = null;
		try {
			requestData = "partner_id=" + partner_id + "&business_type="
					+ businessType + "&data_type=" + data_type + "&data="
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
	
	public static String generalRequestXml4TJ(String id, ResourceBundle bundle) {
		if (snCommonManagerMapper == null) {
			snCommonManagerMapper = (SNCommonManagerMapper) SpringContextUtil
					.getBean("SNCommonManagerMapper");
		}
		int idInt = Integer.valueOf(id);
		// step 1 生成xml
		LinkedHashMap InventoryHead = new LinkedHashMap();
		//查询数据
		List<LinkedHashMap> headList = snCommonManagerMapper.selectInventoryHead(idInt);
		if (headList != null) {
			LinkedHashMap item = headList.get(0);
			if(item.get("LOGISTICS_CODE")!=null){
				if("1210680001".equals(item.get("LOGISTICS_CODE").toString())){
					item.put("ciqLogisticsCode", "Q120000201609000060");
				}
				if("3201961A28".equals(item.get("LOGISTICS_CODE").toString())){
					item.put("ciqLogisticsCode", "Q120000201808000038");
				}
			}
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

		List<LinkedHashMap> InventoryList = new ArrayList<LinkedHashMap>();
		//查询数据
		List<LinkedHashMap> ItemList = snCommonManagerMapper.selectInventoryList(idInt);
		if(ItemList!=null){
			for(LinkedHashMap item:ItemList){
				//查询book表的关联数据
				List<LinkedHashMap> bookInfoList = snCommonManagerMapper
						.selectInventoryListRelateBookInfo(
								item.get("ORDER_NO").toString(),
								item.get("ITEM_NO").toString());
				
				for(LinkedHashMap bookInfo:bookInfoList){
					//book信息添加
					item.put("RECORD_NO", bookInfo.get("RECORD_NO"));
					item.put("GOODS_SERIALNO", bookInfo.get("GOODS_SERIALNO"));
					item.put("DECL_NO", bookInfo.get("DECL_NO"));
					
					LinkedHashMap Inventory = new LinkedHashMap();
					
					Double qty = item.get("QTY")!=null?Double.parseDouble(item.get("QTY").toString()):null;
					Double qty1 = bookInfo.get("QTY1")!=null?Double.parseDouble(bookInfo.get("QTY1").toString()):null;
					Double qty2 = bookInfo.get("QTY2")!=null?Double.parseDouble(bookInfo.get("QTY2").toString()):null;
					//填t_new_import_inventory_detail.qty乘以t_new_import_books.qty1
					if(qty == null||qty1==null){
						item.put("QTY1", null);
					}else{
						item.put("QTY1", qty*qty1);
					}
					//填t_new_import_inventory_detail.qty乘以t_new_import_books.qty2
					if(qty == null||qty2==null){
						item.put("QTY2", null);
					}else{
						item.put("QTY2", qty*qty2);
					}
					
					//判断作废
//					//如果t_new_import_sku.unit2为空，则<qty2>保持不变，继续填t_new_import_inventory_detail.qty2。
//					//如果t_new_import_sku.unit2不为空，则<qty2>改成t_new_import_inventory_detail.qty1
//					if(item.get("UNIT2") == null || item.get("UNIT2").toString().isEmpty()){
//						//不变
//					}else{
//						item.put("QTY2", item.get("QTY1"));
//					}
					// 转换
					if (item != null) {
						for (Object key : item.keySet()) {
							if (bundle.containsKey("TJ_LIST_" + key.toString())) {
								Inventory.put(
										bundle.getObject("TJ_LIST_" + key.toString()),
										item.get(key));
							} else {
								Inventory.put(key.toString(), item.get(key));
							}
						}
					}
					
					InventoryList.add(Inventory);
				}

			}
		}

		LinkedHashMap IODeclContainerList = new LinkedHashMap();
		List<LinkedHashMap> IODeclContainerListTemp = snCommonManagerMapper.selectIODeclContainerList(idInt);
		if (IODeclContainerListTemp != null) {
			LinkedHashMap item = IODeclContainerListTemp.get(0);
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
		List<LinkedHashMap> IODeclOrderRelationListTemp = snCommonManagerMapper.selectIODeclOrderRelationList(idInt);
		if (IODeclOrderRelationListTemp != null) {
			LinkedHashMap item = IODeclOrderRelationListTemp.get(0);
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

	public static void main(String arg[]) {

		String logistics_interface = "<inputData><ordersList><orders><orderImformation><orderHead><logisticsOrderId>LOS36100001120690200</logisticsOrderId><oriSys>LOS</oriSys><taskOrderid>36100001120690200</taskOrderid><thirdPartyCompany>0080010331</thirdPartyCompany><businessType>C061</businessType><btcOrderId>BOL031707</btcOrderId><announcedValue>100.00</announcedValue><valueUnit>CNY</valueUnit><expectDate>2015-09-07</expectDate><expectTime>11:38:10</expectTime></orderHead><orderItems><logisticsOrderId>LOS36100001120690200</logisticsOrderId><itemNumber>10</itemNumber><location>L971</location><locationDescription>天津口岸</locationDescription><storage>0001</storage><goodsCode>20166666</goodsCode><goodsDescription>长虹空调KFR35GWBP2DN1YF</goodsDescription><goodsNumber>5.000</goodsNumber><goodsUnit>S01</goodsUnit><comValue>100.00</comValue><valueUnit>CNY</valueUnit></orderItems><feedBackOrderCustomers><logisticsOrderId>LOS36100001120690200</logisticsOrderId><itemNumber>10</itemNumber><customerType>AG</customerType><customerId>70057297</customerId><name>C店商户苏宁</name><country>CN</country><city>南京市</city><address>南京市玄武区</address><zipCode>210012</zipCode><fixedLineTelephone>02566996699</fixedLineTelephone><mobilePhone>15295599233</mobilePhone></feedBackOrderCustomers></orderImformation><orderExpBill><logisticOrderId>LOS36300000945410000</logisticOrderId><expressCode>62K900000210</expressCode><btcOrder>BTC01631803170701</btcOrder><btcItemOrder/><name>1asd2</name><telNumber>18513891234</telNumber><province>江苏省</province><city>南京市</city><district>栖霞区</district><adress>仙鹤门仙鹤名苑28-608</adress><firstPlantCrossing/><setLocation/><preparedTime>01:00:00</preparedTime><note/><shipCondition>配送</shipCondition><rePrintFlag/><valueAddTaxFlag/><shipTypeFlag>陆</shipTypeFlag><obligateFlag/><lastPlantCrossing/><route>郑州保税仓→南京小件→WL</route><lastLogisticStation>外设单位编码描述测试</lastLogisticStation><realDeliDate>2018-03-19</realDeliDate><realDeliTime>09:00:00</realDeliTime><paymmentMode>线上下单门店支付</paymmentMode><cashOnDeliveryValue>100.00</cashOnDeliveryValue><expressCompany>0080000013</expressCompany><expressCompanyExcode>444033803247</expressCompanyExcode><originCode>025</originCode><destCode>025</destCode><createTime>17:27:39</createTime><createDate>2018-03-17</createDate></orderExpBill><orderDeclare><orderDeclareHead><orderNo>BOL031707</orderNo><ieFlag>I</ieFlag><importType>1</importType><inOutDateStr>2018-03-17 17:07:17</inOutDateStr><logisCompanyCode>1505130005</logisCompanyCode><logisCompanyName>江苏苏宁物流有限公司</logisCompanyName><companyName>江苏苏宁易购电子商务有限公司</companyName><companyCode>PTE51001410280000005</companyCode><wayBill/><tradeCountry>502</tradeCountry><packNo>1</packNo><grossWeight>1.500</grossWeight><netWeight>1.500</netWeight><warpType>2</warpType><remark>37</remark><enteringPerson>9999</enteringPerson><enteringCompanyName>9999</enteringCompanyName><senderName>versace1563212333333范思哲海外专营店</senderName><consignee>无名</consignee><consigneeAddress>江苏南京市玄武区徐庄苏宁总部鼓楼1号</consigneeAddress><senderCountry>502</senderCountry><payerName>无名</payerName><payerPhoneNumber>13951783938</payerPhoneNumber><paperType>01</paperType><paperNumber>320102198001010059</paperNumber><freight>10.0000</freight><insuranceFee/><payTaxAmount>0.0000</payTaxAmount><worth>109.0000</worth><currCode>142</currCode><mainGName>移动端苏宁内测试购专</mainGName><isAuthorize>1</isAuthorize><paySerialNo/><payType/><payTime/><payMoney/><otherPayPrice/><goodsPriceIncludeTax/><senderProvince/><senderAddr/><senderZip/><senderTel/></orderDeclareHead><orderDeclareItems><logisticOrderId>LOS36300000945410000</logisticOrderId><expressCode>62K900000210</expressCode><orderNo>BOL031707</orderNo><orderItemNo>BOL031707</orderItemNo><goodsOrder>1</goodsOrder><codeTs>11</codeTs><goodsItemNo>000000000690105971</goodsItemNo><goodsName>移动端苏宁内测试购专</goodsName><goodsModel/><originCountry>701</originCountry><tradeCurr>142</tradeCurr><tradeTotal>99.0000</tradeTotal><declPrice>99.00</declPrice><declTotalPrice>99.0000</declTotalPrice><useTo>11</useTo><declareCount>1</declareCount><goodsUnit>011</goodsUnit><goodsGrossWeight>1.500</goodsGrossWeight><firstUnit>011</firstUnit><firstCount>1</firstCount><postalTax/></orderDeclareItems></orderDeclare></orders><orders><orderImformation><orderHead><logisticsOrderId>xxxxxxxxxxxxxxxxxxxx</logisticsOrderId><oriSys>LOS</oriSys><taskOrderid>36100001120690200</taskOrderid><thirdPartyCompany>0080010331</thirdPartyCompany><businessType>C061</businessType><btcOrderId>BOL031708</btcOrderId><announcedValue>100.00</announcedValue><valueUnit>CNY</valueUnit><expectDate>2015-09-07</expectDate><expectTime>11:38:10</expectTime></orderHead><orderItems><logisticsOrderId>xxxxxxxxxxxxxxxxxxxx</logisticsOrderId><itemNumber>10</itemNumber><location>L971</location><locationDescription>天津口岸</locationDescription><storage>0001</storage><goodsCode>20166666</goodsCode><goodsDescription>长虹空调KFR35GWBP2DN1YF</goodsDescription><goodsNumber>5.000</goodsNumber><goodsUnit>S01</goodsUnit><comValue>100.00</comValue><valueUnit>CNY</valueUnit></orderItems><feedBackOrderCustomers><logisticsOrderId>xxxxxxxxxxxxxxxxxxxx</logisticsOrderId><itemNumber>10</itemNumber><customerType>AG</customerType><customerId>70057297</customerId><name>C店商户苏宁</name><country>CN</country><city>南京市</city><address>南京市玄武区</address><zipCode>210012</zipCode><fixedLineTelephone>02566996699</fixedLineTelephone><mobilePhone>15295599233</mobilePhone></feedBackOrderCustomers></orderImformation><orderExpBill><logisticOrderId>LOS36300000945410000</logisticOrderId><expressCode>62K900000210</expressCode><btcOrder>BTC01631803170701</btcOrder><btcItemOrder/><name>1asd2</name><telNumber>18513891234</telNumber><province>江苏省</province><city>南京市</city><district>栖霞区</district><adress>仙鹤门仙鹤名苑28-608</adress><firstPlantCrossing/><setLocation/><preparedTime>01:00:00</preparedTime><note/><shipCondition>配送</shipCondition><rePrintFlag/><valueAddTaxFlag/><shipTypeFlag>陆</shipTypeFlag><obligateFlag/><lastPlantCrossing/><route>郑州保税仓→南京小件→WL</route><lastLogisticStation>外设单位编码描述测试</lastLogisticStation><realDeliDate>2018-03-19</realDeliDate><realDeliTime>09:00:00</realDeliTime><paymmentMode>线上下单门店支付</paymmentMode><cashOnDeliveryValue>100.00</cashOnDeliveryValue><expressCompany>0080000013</expressCompany><expressCompanyExcode>444033803247</expressCompanyExcode><originCode>025</originCode><destCode>025</destCode><createTime>17:27:39</createTime><createDate>2018-03-17</createDate></orderExpBill><orderDeclare><orderDeclareHead><orderNo>BOL031708</orderNo><ieFlag>I</ieFlag><importType>1</importType><inOutDateStr>2018-03-17 17:07:17</inOutDateStr><logisCompanyCode>1505130005</logisCompanyCode><logisCompanyName>江苏苏宁物流有限公司</logisCompanyName><companyName>江苏苏宁易购电子商务有限公司</companyName><companyCode>PTE51001410280000005</companyCode><wayBill/><tradeCountry>502</tradeCountry><packNo>1</packNo><grossWeight>1.500</grossWeight><netWeight>1.500</netWeight><warpType>2</warpType><remark>37</remark><enteringPerson>9999</enteringPerson><enteringCompanyName>9999</enteringCompanyName><senderName>versace1563212333333范思哲海外专营店</senderName><consignee>无名</consignee><consigneeAddress>江苏南京市玄武区徐庄苏宁总部鼓楼1号</consigneeAddress><senderCountry>502</senderCountry><payerName>无名</payerName><payerPhoneNumber>13951783938</payerPhoneNumber><paperType>01</paperType><paperNumber>320102198001010059</paperNumber><freight>10.0000</freight><insuranceFee/><payTaxAmount>0.0000</payTaxAmount><worth>109.0000</worth><currCode>142</currCode><mainGName>移动端苏宁内测试购专</mainGName><isAuthorize>1</isAuthorize><paySerialNo/><payType/><payTime/><payMoney/><otherPayPrice/><goodsPriceIncludeTax/><senderProvince/><senderAddr/><senderZip/><senderTel/></orderDeclareHead><orderDeclareItems><logisticOrderId>LOS36300000945410000</logisticOrderId><expressCode>62K900000210</expressCode><orderNo>BOL031708</orderNo><orderItemNo>BOL031708</orderItemNo><goodsOrder>1</goodsOrder><codeTs>11</codeTs><goodsItemNo>000000000690105971</goodsItemNo><goodsName>移动端苏宁内测试购专</goodsName><goodsModel/><originCountry>701</originCountry><tradeCurr>142</tradeCurr><tradeTotal>99.0000</tradeTotal><declPrice>99.00</declPrice><declTotalPrice>99.0000</declTotalPrice><useTo>11</useTo><declareCount>1</declareCount><goodsUnit>011</goodsUnit><goodsGrossWeight>1.500</goodsGrossWeight><firstUnit>011</firstUnit><firstCount>1</firstCount><postalTax/></orderDeclareItems></orderDeclare></orders></ordersList></inputData>";

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
//
//		try {
//			System.out.println(URLEncoder.encode("helloworld", "utf-8"));
//			System.out.println(URLEncoder.encode("voQc3u6+f6pSflMPdw4ySQ==",
//					"utf-8"));
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//		Map<String, Object> map1 = new HashMap<String, Object>();
//		map1.put("RECORD_NO", "335");
//		map1.put("CREAT_TIME", "2018-09-14 21:46:17");
//		Map<String, Object> map2 = new HashMap<String, Object>();
//		map2.put("RECORD_NO", "145");
//		map2.put("CREAT_TIME", "2018-09-14 23:46:17");
//		Map<String, Object> map3 = new HashMap<String, Object>();
//		map3.put("RECORD_NO", "285");
//		map3.put("CREAT_TIME", "2018-09-14 22:46:17");
//		Map<String, Object> map4 = new HashMap<String, Object>();
//		map4.put("RECORD_NO", "265");
//		map4.put("CREAT_TIME", "2018-09-14 22:46:17");
//		list.add(map1);
//		list.add(map2);
//		list.add(map3);
//		list.add(map4);
//		// 排序前
//		for (Map<String, Object> map : list) {
//			System.out.println(map);
//		}
//
//		Collections.sort(list, new Comparator<Map>() {
//			public int compare(Map o1, Map o2) {
////				double qty1 = Double.valueOf(o1.get("QTY")
////						.toString());
////				double qty2 = Double.valueOf(o2.get("QTY")
////						.toString());
//				String recordNo1 = o1.get("RECORD_NO")!=null?o1.get("RECORD_NO").toString():"";
//				String recordNo2 = o2.get("RECORD_NO")!=null?o2.get("RECORD_NO").toString():"";
////				if (qty1 > qty2) {
//					if(recordNo1.compareTo(recordNo2)<0){
//						return 0;
//					}else{
//						return 1;
//					}
////				} else {
////					return 0;
////				}
//			}
//		});

//		System.out.println("-------------------");
//		for (Map<String, Object> map : list) {
//			System.out.println(map);
//		}
//
//		String s = "<![CDATA[南京市玄武区]]>";
//		Pattern p = Pattern.compile(".*<!\\[CDATA\\[(.*)\\]\\]>.*");
//		Matcher m = p.matcher(s);
//
//		if (m.matches()) {
//			System.out.println(m.group(1));
//		}else{
//			System.out.println(s);
//		}
//		
//		System.out.println(CommonUtil.getDateFormatter(CommonDefine.COMMON_FORMAT_2)
//					.format(new Date()));
		
//		String response = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soapenv:Envelope xmlns:soapenv=\"http://www.w3.org/2003/05/soap-envelope\"><soapenv:Body><ns:sendOrderResponse xmlns:ns=\"http://ws.com\"><ns:return><?xml version=\"1.0\" encoding=\"UTF-8\"?><DATA><ORDER><ORDER_CODE>W100133410</ORDER_CODE><CD>OK</CD><INFO>11811000073</INFO></ORDER></DATA></ns:return></ns:sendOrderResponse></soapenv:Body></soapenv:Envelope>";
//		
//		String xxxx = StringEscapeUtils.escapeXml(response);
		
//		String returnXmlData = XmlUtil
//				.getResponseFromXmlString_CJ(xxxx);
		
//		String returnXmlData = XmlUtil.getTotalMidValue(response,"<ns:return>","</ns:return>");
//		
//		Map orderResult = XmlUtil.parseXmlFPAPI_SingleNodes(returnXmlData, "//DATA/ORDER/child::*");
		
//		System.out.println(xxxx);
		
//		System.out.println(orderResult);
		
		try {
			String xxx = "<returnInfo>鐪嬩簡鏀炬帴鍙ｆ噿寰楄璐瑰彂鍔ㄦ満瀵屽惈鐨勫ソ鍙戜簡灏卞簾浜嗗彲瑙佸晩閫傚綋鑰冭檻鍙戞媺鏄鎺ュ彂鏀綞</returnInfo>";
			
			xxx = "<returnInfo>鐪嬩簡鏀炬帴鍙ｆ噿寰楄璐瑰彂鍔ㄦ満瀵屽惈鐨勫ソ鍙戜簡灏卞簾浜嗗彲瑙佸晩閫傚綋鑰冭檻鍙戞媺鏄鎺ュ彂鏀�/returnInfo>";

			
//			String xxx = "清单新增申报成功[电商企业编码：3201966A69订单编号：AAAA124613101110701] 对应电商企业为[3201966A69]订单号为[AAAA124613101110701]的订单信息不存在 对应物流企业为[1210680001]物流单号为[245119218897]的运单信息不存在 对应电商平台为[3201966A69]订单号为[AAAA124613101110701]支付信息不存在";
			
//			String uft_gbk = new String(xxx.getBytes("UTF-8"),"GBK");
			
			String gbk_utf = new String(xxx.getBytes("GBK"),"UTF-8");
			
			
//			System.out.println(uft_gbk);
			System.out.println(gbk_utf);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
