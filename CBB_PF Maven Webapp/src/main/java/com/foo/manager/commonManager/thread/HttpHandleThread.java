package com.foo.manager.commonManager.thread;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang3.StringEscapeUtils;
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
//		 this.content =
//		 "<inputData><ordersList><orders><orderImformation><orderHead><logisticsOrderId>LOS36100001120690200</logisticsOrderId><oriSys>LOS</oriSys><taskOrderid>36100001120690200</taskOrderid><thirdPartyCompany>0080010331</thirdPartyCompany><businessType>C061</businessType><btcOrderId>BOL031707</btcOrderId><announcedValue>100.00</announcedValue><valueUnit>CNY</valueUnit><expectDate>2015-09-07</expectDate><expectTime>11:38:10</expectTime></orderHead><orderItems><logisticsOrderId>LOS36100001120690200</logisticsOrderId><itemNumber>10</itemNumber><location>L971</location><locationDescription>天津口岸</locationDescription><storage>0001</storage><goodsCode>20166666</goodsCode><goodsDescription>长虹空调KFR35GWBP2DN1YF</goodsDescription><goodsNumber>5.000</goodsNumber><goodsUnit>S01</goodsUnit><comValue>100.00</comValue><valueUnit>CNY</valueUnit></orderItems><feedBackOrderCustomers><logisticsOrderId>LOS36100001120690200</logisticsOrderId><itemNumber>10</itemNumber><customerType>AG</customerType><customerId>70057297</customerId><name>C店商户苏宁</name><country>CN</country><city>南京市</city><address>南京市玄武区</address><zipCode>210012</zipCode><fixedLineTelephone>02566996699</fixedLineTelephone><mobilePhone>15295599233</mobilePhone></feedBackOrderCustomers></orderImformation><orderExpBill><logisticOrderId>LOS36300000945410000</logisticOrderId><expressCode>62K900000210</expressCode><btcOrder>BTC01631803170701</btcOrder><btcItemOrder/><name>1asd2</name><telNumber>18513891234</telNumber><province>江苏省</province><city>南京市</city><district>栖霞区</district><adress>仙鹤门仙鹤名苑28-608</adress><firstPlantCrossing/><setLocation/><preparedTime>01:00:00</preparedTime><note/><shipCondition>配送</shipCondition><rePrintFlag/><valueAddTaxFlag/><shipTypeFlag>陆</shipTypeFlag><obligateFlag/><lastPlantCrossing/><route>郑州保税仓→南京小件→WL</route><lastLogisticStation>外设单位编码描述测试</lastLogisticStation><realDeliDate>2018-03-19</realDeliDate><realDeliTime>09:00:00</realDeliTime><paymmentMode>线上下单门店支付</paymmentMode><cashOnDeliveryValue>100.00</cashOnDeliveryValue><expressCompany>0080000013</expressCompany><expressCompanyExcode>444033803247</expressCompanyExcode><originCode>025</originCode><destCode>025</destCode><createTime>17:27:39</createTime><createDate>2018-03-17</createDate></orderExpBill><orderDeclare><orderDeclareHead><orderNo>BOL031707</orderNo><ieFlag>I</ieFlag><importType>1</importType><inOutDateStr>2018-03-17 17:07:17</inOutDateStr><logisCompanyCode>1505130005</logisCompanyCode><logisCompanyName>江苏苏宁物流有限公司</logisCompanyName><companyName>江苏苏宁易购电子商务有限公司</companyName><companyCode>PTE51001410280000005</companyCode><wayBill/><tradeCountry>502</tradeCountry><packNo>1</packNo><grossWeight>1.500</grossWeight><netWeight>1.500</netWeight><warpType>2</warpType><remark>37</remark><enteringPerson>9999</enteringPerson><enteringCompanyName>9999</enteringCompanyName><senderName>versace1563212333333范思哲海外专营店</senderName><consignee>无名</consignee><consigneeAddress>江苏南京市玄武区徐庄苏宁总部鼓楼1号</consigneeAddress><senderCountry>502</senderCountry><payerName>无名</payerName><payerPhoneNumber>13951783938</payerPhoneNumber><paperType>01</paperType><paperNumber>320102198001010059</paperNumber><freight>10.0000</freight><insuranceFee/><payTaxAmount>0.0000</payTaxAmount><worth>109.0000</worth><currCode>142</currCode><mainGName>移动端苏宁内测试购专</mainGName><isAuthorize>1</isAuthorize><paySerialNo/><payType/><payTime/><payMoney/><otherPayPrice/><goodsPriceIncludeTax/><senderProvince/><senderAddr/><senderZip/><senderTel/></orderDeclareHead><orderDeclareItems><logisticOrderId>LOS36300000945410000</logisticOrderId><expressCode>62K900000210</expressCode><orderNo>BOL031707</orderNo><orderItemNo>BOL031707</orderItemNo><goodsOrder>1</goodsOrder><codeTs>11</codeTs><goodsItemNo>000000000690105971</goodsItemNo><goodsName>移动端苏宁内测试购专</goodsName><goodsModel/><originCountry>701</originCountry><tradeCurr>142</tradeCurr><tradeTotal>99.0000</tradeTotal><declPrice>99.00</declPrice><declTotalPrice>99.0000</declTotalPrice><useTo>11</useTo><declareCount>1</declareCount><goodsUnit>011</goodsUnit><goodsGrossWeight>1.500</goodsGrossWeight><firstUnit>011</firstUnit><firstCount>1</firstCount><postalTax/></orderDeclareItems></orderDeclare></orders><orders><orderImformation><orderHead><logisticsOrderId>xxxxxxxxxxxxxxxxxxxx</logisticsOrderId><oriSys>LOS</oriSys><taskOrderid>36100001120690200</taskOrderid><thirdPartyCompany>0080010331</thirdPartyCompany><businessType>C061</businessType><btcOrderId>BOL031708</btcOrderId><announcedValue>100.00</announcedValue><valueUnit>CNY</valueUnit><expectDate>2015-09-07</expectDate><expectTime>11:38:10</expectTime></orderHead><orderItems><logisticsOrderId>xxxxxxxxxxxxxxxxxxxx</logisticsOrderId><itemNumber>10</itemNumber><location>L971</location><locationDescription>天津口岸</locationDescription><storage>0001</storage><goodsCode>20166666</goodsCode><goodsDescription>长虹空调KFR35GWBP2DN1YF</goodsDescription><goodsNumber>5.000</goodsNumber><goodsUnit>S01</goodsUnit><comValue>100.00</comValue><valueUnit>CNY</valueUnit></orderItems><feedBackOrderCustomers><logisticsOrderId>xxxxxxxxxxxxxxxxxxxx</logisticsOrderId><itemNumber>10</itemNumber><customerType>AG</customerType><customerId>70057297</customerId><name>C店商户苏宁</name><country>CN</country><city>南京市</city><address>南京市玄武区</address><zipCode>210012</zipCode><fixedLineTelephone>02566996699</fixedLineTelephone><mobilePhone>15295599233</mobilePhone></feedBackOrderCustomers></orderImformation><orderExpBill><logisticOrderId>LOS36300000945410000</logisticOrderId><expressCode>62K900000210</expressCode><btcOrder>BTC01631803170701</btcOrder><btcItemOrder/><name>1asd2</name><telNumber>18513891234</telNumber><province>江苏省</province><city>南京市</city><district>栖霞区</district><adress>仙鹤门仙鹤名苑28-608</adress><firstPlantCrossing/><setLocation/><preparedTime>01:00:00</preparedTime><note/><shipCondition>配送</shipCondition><rePrintFlag/><valueAddTaxFlag/><shipTypeFlag>陆</shipTypeFlag><obligateFlag/><lastPlantCrossing/><route>郑州保税仓→南京小件→WL</route><lastLogisticStation>外设单位编码描述测试</lastLogisticStation><realDeliDate>2018-03-19</realDeliDate><realDeliTime>09:00:00</realDeliTime><paymmentMode>线上下单门店支付</paymmentMode><cashOnDeliveryValue>100.00</cashOnDeliveryValue><expressCompany>0080000013</expressCompany><expressCompanyExcode>444033803247</expressCompanyExcode><originCode>025</originCode><destCode>025</destCode><createTime>17:27:39</createTime><createDate>2018-03-17</createDate></orderExpBill><orderDeclare><orderDeclareHead><orderNo>BOL031708</orderNo><ieFlag>I</ieFlag><importType>1</importType><inOutDateStr>2018-03-17 17:07:17</inOutDateStr><logisCompanyCode>1505130005</logisCompanyCode><logisCompanyName>江苏苏宁物流有限公司</logisCompanyName><companyName>江苏苏宁易购电子商务有限公司</companyName><companyCode>PTE51001410280000005</companyCode><wayBill/><tradeCountry>502</tradeCountry><packNo>1</packNo><grossWeight>1.500</grossWeight><netWeight>1.500</netWeight><warpType>2</warpType><remark>37</remark><enteringPerson>9999</enteringPerson><enteringCompanyName>9999</enteringCompanyName><senderName>versace1563212333333范思哲海外专营店</senderName><consignee>无名</consignee><consigneeAddress>江苏南京市玄武区徐庄苏宁总部鼓楼1号</consigneeAddress><senderCountry>502</senderCountry><payerName>无名</payerName><payerPhoneNumber>13951783938</payerPhoneNumber><paperType>01</paperType><paperNumber>320102198001010059</paperNumber><freight>10.0000</freight><insuranceFee/><payTaxAmount>0.0000</payTaxAmount><worth>109.0000</worth><currCode>142</currCode><mainGName>移动端苏宁内测试购专</mainGName><isAuthorize>1</isAuthorize><paySerialNo/><payType/><payTime/><payMoney/><otherPayPrice/><goodsPriceIncludeTax/><senderProvince/><senderAddr/><senderZip/><senderTel/></orderDeclareHead><orderDeclareItems><logisticOrderId>LOS36300000945410000</logisticOrderId><expressCode>62K900000210</expressCode><orderNo>BOL031708</orderNo><orderItemNo>BOL031708</orderItemNo><goodsOrder>1</goodsOrder><codeTs>11</codeTs><goodsItemNo>000000000690105971</goodsItemNo><goodsName>移动端苏宁内测试购专</goodsName><goodsModel/><originCountry>701</originCountry><tradeCurr>142</tradeCurr><tradeTotal>99.0000</tradeTotal><declPrice>99.00</declPrice><declTotalPrice>99.0000</declTotalPrice><useTo>11</useTo><declareCount>1</declareCount><goodsUnit>011</goodsUnit><goodsGrossWeight>1.500</goodsGrossWeight><firstUnit>011</firstUnit><firstCount>1</firstCount><postalTax/></orderDeclareItems></orderDeclare></orders></ordersList></inputData>";
		// 订单信息--C005
//		 this.requestType = HttpServerManagerService.requestType_Order;
//		 this.content =
//		 "<inputData><ordersList><orders><orderImformation><orderHead><logisticsOrderId>LOS36100001120690200</logisticsOrderId><oriSys>LOS</oriSys><taskOrderid>36100001120690200</taskOrderid><thirdPartyCompany>0080010331</thirdPartyCompany><businessType>C005</businessType><btcOrderId>BOL031707</btcOrderId><btcItemNumber>1</btcItemNumber><announcedValue>100.00</announcedValue><valueUnit>CNY</valueUnit><expectDate>2015-09-07</expectDate><expectTime>11:38:10</expectTime></orderHead><orderItems><logisticsOrderId>LOS36100001120690200</logisticsOrderId><itemNumber>10</itemNumber><location>L971</location><locationDescription>天津口岸</locationDescription><storage>0001</storage><goodsCode>20166666</goodsCode><goodsDescription>长虹空调KFR35GWBP2DN1YF</goodsDescription><goodsNumber>5.000</goodsNumber><goodsUnit>S01</goodsUnit><comValue>100.00</comValue><valueUnit>CNY</valueUnit></orderItems><feedBackOrderCustomers><logisticsOrderId>LOS36100001120690200</logisticsOrderId><itemNumber>10</itemNumber><customerType>AG</customerType><customerId>70057297</customerId><name>C店商户苏宁</name><country>CN</country><city>南京市</city><address>南京市玄武区</address><zipCode>210012</zipCode><fixedLineTelephone>02566996699</fixedLineTelephone><mobilePhone>15295599233</mobilePhone></feedBackOrderCustomers></orderImformation><orderExpBill><logisticOrderId>LOS36300000945410000</logisticOrderId><expressCode>62K900000210</expressCode><btcOrder>BTC01631803170701</btcOrder><btcItemOrder/><name>1asd2</name><telNumber>18513891234</telNumber><province>江苏省</province><city>南京市</city><district>栖霞区</district><adress>仙鹤门仙鹤名苑28-608</adress><firstPlantCrossing/><setLocation/><preparedTime>01:00:00</preparedTime><note/><shipCondition>配送</shipCondition><rePrintFlag/><valueAddTaxFlag/><shipTypeFlag>陆</shipTypeFlag><obligateFlag/><lastPlantCrossing/><route>郑州保税仓→南京小件→WL</route><lastLogisticStation>外设单位编码描述测试</lastLogisticStation><realDeliDate>2018-03-19</realDeliDate><realDeliTime>09:00:00</realDeliTime><paymmentMode>线上下单门店支付</paymmentMode><cashOnDeliveryValue>100.00</cashOnDeliveryValue><expressCompany>0080000013</expressCompany><expressCompanyExcode>444033803247</expressCompanyExcode><originCode>025</originCode><destCode>025</destCode><createTime>17:27:39</createTime><createDate>2018-03-17</createDate></orderExpBill><orderDeclare><orderDeclareHead><orderNo>BOL031707</orderNo><ieFlag>I</ieFlag><importType>1</importType><inOutDateStr>2018-03-17 17:07:17</inOutDateStr><logisCompanyCode>1505130005</logisCompanyCode><logisCompanyName>江苏苏宁物流有限公司</logisCompanyName><companyName>江苏苏宁易购电子商务有限公司</companyName><companyCode>PTE51001410280000005</companyCode><wayBill/><tradeCountry>502</tradeCountry><packNo>1</packNo><grossWeight>1.500</grossWeight><netWeight>1.500</netWeight><warpType>2</warpType><remark>37</remark><enteringPerson>9999</enteringPerson><enteringCompanyName>9999</enteringCompanyName><senderName>versace1563212333333范思哲海外专营店</senderName><consignee>无名</consignee><consigneeAddress>江苏南京市玄武区徐庄苏宁总部鼓楼1号</consigneeAddress><senderCountry>502</senderCountry><payerName>无名</payerName><payerPhoneNumber>13951783938</payerPhoneNumber><paperType>01</paperType><paperNumber>320102198001010059</paperNumber><freight>10.0000</freight><insuranceFee/><payTaxAmount>0.0000</payTaxAmount><worth>109.0000</worth><currCode>142</currCode><mainGName>移动端苏宁内测试购专</mainGName><isAuthorize>1</isAuthorize><paySerialNo/><payType/><payTime/><payMoney/><otherPayPrice/><goodsPriceIncludeTax/><senderProvince/><senderAddr/><senderZip/><senderTel/></orderDeclareHead><orderDeclareItems><logisticOrderId>LOS36300000945410000</logisticOrderId><expressCode>62K900000210</expressCode><orderNo>BOL031707</orderNo><orderItemNo>BOL031707</orderItemNo><goodsOrder>1</goodsOrder><codeTs>11</codeTs><goodsItemNo>000000000690105971</goodsItemNo><goodsName>移动端苏宁内测试购专</goodsName><goodsModel/><originCountry>701</originCountry><tradeCurr>142</tradeCurr><tradeTotal>99.0000</tradeTotal><declPrice>99.00</declPrice><declTotalPrice>99.0000</declTotalPrice><useTo>11</useTo><declareCount>1</declareCount><goodsUnit>011</goodsUnit><goodsGrossWeight>1.500</goodsGrossWeight><firstUnit>011</firstUnit><firstCount>1</firstCount><postalTax/></orderDeclareItems></orderDeclare></orders><orders><orderImformation><orderHead><logisticsOrderId>xxxxxxxxxxxxxxxxxxxx</logisticsOrderId><oriSys>LOS</oriSys><taskOrderid>36100001120690200</taskOrderid><thirdPartyCompany>0080010331</thirdPartyCompany><businessType>C005</businessType><btcOrderId>BOL031708</btcOrderId><btcItemNumber>2</btcItemNumber><announcedValue>100.00</announcedValue><valueUnit>CNY</valueUnit><expectDate>2015-09-07</expectDate><expectTime>11:38:10</expectTime></orderHead><orderItems><logisticsOrderId>xxxxxxxxxxxxxxxxxxxx</logisticsOrderId><itemNumber>10</itemNumber><location>L971</location><locationDescription>天津口岸</locationDescription><storage>0001</storage><goodsCode>20166666</goodsCode><goodsDescription>长虹空调KFR35GWBP2DN1YF</goodsDescription><goodsNumber>5.000</goodsNumber><goodsUnit>S01</goodsUnit><comValue>100.00</comValue><valueUnit>CNY</valueUnit></orderItems><feedBackOrderCustomers><logisticsOrderId>xxxxxxxxxxxxxxxxxxxx</logisticsOrderId><itemNumber>10</itemNumber><customerType>AG</customerType><customerId>70057297</customerId><name>C店商户苏宁</name><country>CN</country><city>南京市</city><address>南京市玄武区</address><zipCode>210012</zipCode><fixedLineTelephone>02566996699</fixedLineTelephone><mobilePhone>15295599233</mobilePhone></feedBackOrderCustomers></orderImformation><orderExpBill><logisticOrderId>LOS36300000945410000</logisticOrderId><expressCode>62K900000210</expressCode><btcOrder>BTC01631803170701</btcOrder><btcItemOrder/><name>1asd2</name><telNumber>18513891234</telNumber><province>江苏省</province><city>南京市</city><district>栖霞区</district><adress>仙鹤门仙鹤名苑28-608</adress><firstPlantCrossing/><setLocation/><preparedTime>01:00:00</preparedTime><note/><shipCondition>配送</shipCondition><rePrintFlag/><valueAddTaxFlag/><shipTypeFlag>陆</shipTypeFlag><obligateFlag/><lastPlantCrossing/><route>郑州保税仓→南京小件→WL</route><lastLogisticStation>外设单位编码描述测试</lastLogisticStation><realDeliDate>2018-03-19</realDeliDate><realDeliTime>09:00:00</realDeliTime><paymmentMode>线上下单门店支付</paymmentMode><cashOnDeliveryValue>100.00</cashOnDeliveryValue><expressCompany>0080000013</expressCompany><expressCompanyExcode>444033803247</expressCompanyExcode><originCode>025</originCode><destCode>025</destCode><createTime>17:27:39</createTime><createDate>2018-03-17</createDate></orderExpBill><orderDeclare><orderDeclareHead><orderNo>BOL031708</orderNo><ieFlag>I</ieFlag><importType>1</importType><inOutDateStr>2018-03-17 17:07:17</inOutDateStr><logisCompanyCode>1505130005</logisCompanyCode><logisCompanyName>江苏苏宁物流有限公司</logisCompanyName><companyName>江苏苏宁易购电子商务有限公司</companyName><companyCode>PTE51001410280000005</companyCode><wayBill/><tradeCountry>502</tradeCountry><packNo>1</packNo><grossWeight>1.500</grossWeight><netWeight>1.500</netWeight><warpType>2</warpType><remark>37</remark><enteringPerson>9999</enteringPerson><enteringCompanyName>9999</enteringCompanyName><senderName>versace1563212333333范思哲海外专营店</senderName><consignee>无名</consignee><consigneeAddress>江苏南京市玄武区徐庄苏宁总部鼓楼1号</consigneeAddress><senderCountry>502</senderCountry><payerName>无名</payerName><payerPhoneNumber>13951783938</payerPhoneNumber><paperType>01</paperType><paperNumber>320102198001010059</paperNumber><freight>10.0000</freight><insuranceFee/><payTaxAmount>0.0000</payTaxAmount><worth>109.0000</worth><currCode>142</currCode><mainGName>移动端苏宁内测试购专</mainGName><isAuthorize>1</isAuthorize><paySerialNo/><payType/><payTime/><payMoney/><otherPayPrice/><goodsPriceIncludeTax/><senderProvince/><senderAddr/><senderZip/><senderTel/></orderDeclareHead><orderDeclareItems><logisticOrderId>LOS36300000945410000</logisticOrderId><expressCode>62K900000210</expressCode><orderNo>BOL031708</orderNo><orderItemNo>BOL031708</orderItemNo><goodsOrder>1</goodsOrder><codeTs>11</codeTs><goodsItemNo>000000000690105971</goodsItemNo><goodsName>移动端苏宁内测试购专</goodsName><goodsModel/><originCountry>701</originCountry><tradeCurr>142</tradeCurr><tradeTotal>99.0000</tradeTotal><declPrice>99.00</declPrice><declTotalPrice>99.0000</declTotalPrice><useTo>11</useTo><declareCount>1</declareCount><goodsUnit>011</goodsUnit><goodsGrossWeight>1.500</goodsGrossWeight><firstUnit>011</firstUnit><firstCount>1</firstCount><postalTax/></orderDeclareItems></orderDeclare></orders></ordersList></inputData>";
		// 库存
//		 this.requestType = HttpServerManagerService.requestType_inventory;
//		 this.content =
//		 "<inputList><inventoryOrder><inventoryDetailHead><messageId>001550fb0407445c8199971b85cf39d2</messageId><logisticsOrderId>LOS36300259871403000</logisticsOrderId><businessType>C061</businessType><statusCode>1060</statusCode><offsetFlag></offsetFlag><finishedDate>2018-11-25</finishedDate><finishedTime>17:21:40</finishedTime></inventoryDetailHead><inventoryDetailItems><itemNumber>10</itemNumber><goodsCode>000000000686902504</goodsCode><goodsDescription>花王 Merries 大号婴儿纸尿裤 L58片 (L码增量装)</goodsDescription><goodsNumber>1</goodsNumber><goodsUnit>S01</goodsUnit><inventoryType>A</inventoryType></inventoryDetailItems></inventoryOrder><inventoryOrder><inventoryDetailHead><messageId>0038eee2fddf4305a31ac2aa2b8333e5</messageId><logisticsOrderId>LOS36300260644470300</logisticsOrderId><businessType>C061</businessType><statusCode>1060</statusCode><offsetFlag></offsetFlag><finishedDate>2018-11-25</finishedDate><finishedTime>18:24:16</finishedTime></inventoryDetailHead><inventoryDetailItems><itemNumber>10</itemNumber><goodsCode>000000000686897623</goodsCode><goodsDescription>尤妮佳 Moony 小号婴儿纸尿裤 S号S84片 （4kg-8kg）</goodsDescription><goodsNumber>2</goodsNumber><goodsUnit>S01</goodsUnit><inventoryType>A</inventoryType></inventoryDetailItems></inventoryOrder><inventoryOrder><inventoryDetailHead><messageId>006e59bb562a4372844915f90734a1a3</messageId><logisticsOrderId>LOS36300261078420600</logisticsOrderId><businessType>C061</businessType><statusCode>1060</statusCode><offsetFlag></offsetFlag><finishedDate>2018-11-25</finishedDate><finishedTime>18:24:11</finishedTime></inventoryDetailHead><inventoryDetailItems><itemNumber>10</itemNumber><goodsCode>000000000686897623</goodsCode><goodsDescription>尤妮佳 Moony 小号婴儿纸尿裤 S号S84片 （4kg-8kg）</goodsDescription><goodsNumber>2</goodsNumber><goodsUnit>S01</goodsUnit><inventoryType>A</inventoryType></inventoryDetailItems></inventoryOrder></inputList>";
		// 装载
		// this.requestType = HttpServerManagerService.requestType_load;
		// this.content =
		// "<LoadHead><loadContents><loadContent><loadContentId>1</loadContentId><outorderId>6666666666</outorderId></loadContent><loadContent><loadContentId>2</loadContentId><outorderId>7777777777</outorderId></loadContent></loadContents><loadHeadId>12</loadHeadId><loadId>1736474588</loadId><total>2</total><tracyNum>3</tracyNum><TotalWeight>2.5</TotalWeight><CarEcNo>苏A234234</CarEcNo></LoadHead>";

		// 出库单状态报文
//		 this.requestType = HttpServerManagerService.requestType_listRelease;
//		 this.content =
//		 "<InventoryReturnList><InventoryReturn><orderNo>AAAA0016172051200208</orderNo><invtNo>02132018I651591534</invtNo><returnStatus>3070</returnStatus><returnInfo></returnInfo></InventoryReturn><InventoryReturn><orderNo>AAAA0028172236190126</orderNo><invtNo>02132018I651643914</invtNo><returnStatus>3070</returnStatus><returnInfo></returnInfo></InventoryReturn></InventoryReturnList>";

		// sn_sku--商品数据分发，苏宁发起
		// this.requestType = HttpServerManagerService.requestType_sn_sku;
		// this.content =
		// "{content={\"cmmdtyInfo\":[{\"kunner\":\"RH007\",\"sncmmdtyCode\":\"000000010591492016\",\"cmmdtyName\":\"FANCL/芳珂 HTC胶原蛋白饮料 10日装*3 2018版\",\"measureUnit\":\"S01\",\"attributes\":\"\",\"cmmdtyType\":\"Z001\",\"cmmdtyGrp\":\"胶原蛋白\",\"brandCode\":\"芳珂(FANCL)\",\"cmmdtyLength\":\"1.000\",\"cmmdtyWidth\":\"1.000\",\"cmmdtyHeight\":\"1.000\",\"cmmdtyVolume\":\"0.001\",\"grossCmmdtyWeight\":\"1.000\",\"netCmmdtyWeight\":\"1.000\",\"totalShelfLife\":\"180\",\"coldChain\":\"\",\"bigsmall\":\"2\",\"cmmdtyFreightGrp\":\"90\",\"mprbs\":\"\",\"cmmdtyModel\":\"10日装*3\",\"cmmdtyOrigin\":\"日本\",\"weightFlag\":\"\",\"taste\":\"\",\"size\":\"\",\"colour\":\"\",\"keepEnvironment\":\"01\",\"cmmdtyFeatures\":\"14\",\"physicalForm\":\"01\",\"shape\":\"01\",\"keepMethod\":\"\",\"pcs\":\"\",\"cmmdtyHierrarchy\":\"000290DUU\"}],\"customerInfo\":[{\"kunner\":\"RH007\",\"sncmmdtyCode\":\"000000010591492016\",\"isshelflife\":\"X\",\"remainingShelfLife\":\"90\",\"riskLife\":\"60\",\"damageLife\":\"33\",\"prototypeManage\":null,\"badManage\":null,\"cmanage\":null,\"batchFlag\":\"\",\"deleteFlag\":null,\"versionNo\":\"00001\"}],\"barCodeInfo\":[{\"kunner\":\"RH007\",\"sncmmdtyCode\":\"000000010591492016\",\"externalEanCode\":\"SN10591492016\",\"cmmdtyEanCateg\":\"DE\"}]},businessType=rcs_ztky_commodity_info_distribute}";
		// this.content =
		// "{\"cmmdtyInfo\":[{\"kunner\":\"RH007\",\"sncmmdtyCode\":\" 000000010591492016\",\"cmmdtyName\":\" FANCL/芳珂 HTC胶原蛋白饮料 10日装*3 2018版\",\"measureUnit\":\"S01\",\"attributes\":\"\",\"cmmdtyType\":\" Z001\",\"cmmdtyGrp\":\"\",\"brandCode\":\"\",\"cmmdtyLength\":\"\",\"cmmdtyWidth\":\"\",\"cmmdtyHeight\":\"\",\"cmmdtyVolume\":\"\",\"grossCmmdtyWeight\":\"\",\"netCmmdtyWeight\":\"\",\"totalShelfLife\":\"\",\"coldChain\":\"\",\"bigsmall\":\"\",\"cmmdtyFreightGrp\":\"\",\"mprbs\":\"\",\"cmmdtyModel\":\"\",\"cmmdtyOrigin\":\"\",\"weightFlag\":\"\",\"taste\":\"\",\"size\":\"\",\"colour\":\"\",\"keepEnvironment\":\"\",\"cmmdtyFeatures\":\"\",\"physicalForm\":\"\",\"shape\":\"\",\"keepMethod\":\"\",\"pcs\":\"\",\"cmmdtyHierrarchy\":\"\"}],\"customerInfo\":[{\"kunner\":\"\",\"sncmmdtyCode\":\"\",\"isshelflife\":\"\",\"remainingShelfLife\":\"\",\"riskLife\":\"\",\"damageLife\":\"\",\"prototypeManage\":\"\",\"badManage\":\"\",\"cmanage\":\"\",\"batchFlag\":\"\",\"deleteFlag\":\"\",\"versionNo\":\"\"}],\"barCodeInfo\":[{\"kunner\":\"\",\"sncmmdtyCode\":\"\",\"externalEanCode\":\" SN10591492016 \",\"cmmdtyEanCateg\":\"\"}]}";

		//sn_receipt--入库通知，苏宁发起
		
//		 this.requestType = HttpServerManagerService.requestType_sn_receipt;
//		 this.content =
//		 "{\"orderInfo\":{\"ownerUserId\":\"RH100\",\"fpsOrderId\":\"123456XXXXX\",\"storeCode\":\"WM10xxxxxx\",\"orderCode\":\"W107xxxxxx\",\"orderType\":\"601\",\"orderNumber\":\"A22xxxx\",\"outsourcingFlag\":\"01\",\"orderSource\":\"305\",\"remark\":\"商品情况：未开包未使用包装完好;;\",\"returnReason\":\"商品情况：未开包未使用包装完好;;\",\"orderCreateTime\":\"2018-01-03 11:47:07\",\"expectStartTime\":\"2018-01-03 11:47:06\",\"expectEndTime\":\"2018-01-03 11:47:06\",\"orderFlag\":\"9\",\"tmsServiceCode\":\"S02\",\"tmsServiceName\":\"苏宁物流\",\"tmsOrderCode\":\"896102xxxxxx\",\"prevOrderCode\":\"W107xxxxxx\",\"receiverInfo\":{\"receiverProvince\":\"江苏\",\"receiverCity\":\"南京市\",\"receiverArea\":\"雨花台区\",\"receiverTown\":\"全区\",\"receiverAddress\":\"龙藏大道2号\",\"receiverName\":\"沈xx\",\"receiverMobile\":\"18666xxxxxx\",\"receiverPhone\":\"15172xxxxxx\"},\"senderInfo\":{\"senderAddress\":\"雨花经济开发区龙藏大道与凤舞路交叉口\",\"senderProvince\":\"浙江省\",\"senderCity\":\"杭州市\",\"senderArea\":\"滨江区\",\"senderTown\":\"全区\",\"senderCode\":\"7016xxxx\",\"senderName\":\"左xx\",\"senderMobile\":\"15172xxxxxx\",\"senderPhone\":\"15172xxxxxx\"},\"orderItemList\":[{\"orderItemId\":\"420000002xxxxxx\",\"userId\":\"7016xxxx\",\"userName\":\"安利（中国）日用品有限公司\",\"ownerUserId\":\"7016xxxx\",\"ownerUserName\":\"安利（中国）日用品有限公司\",\"itemId\":\"917080415493xxxxxx\",\"itemName\":\"雅蜜润肤沐浴露 750ML\",\"inventoryType\":\"1\",\"itemQuantity\":\"750\",\"produceCode\":\"7359xxxx\",\"condition\":\"A\"},{\"orderItemId\":\"420000002xxxxxx\",\"userId\":\"7016xxxx\",\"userName\":\"安利（中国）日用品有限公司\",\"ownerUserId\":\"70168xxx\",\"ownerUserName\":\"安利（中国）日用品有限公司\",\"itemId\":\"917080415493xxxxxx\",\"itemName\":\"雅蜜润肤沐浴露 750ML\",\"inventoryType\":\"1\",\"itemQuantity\":\"750\",\"produceCode\":\"7359xxxx\",\"condition\":\"A\"}]}}";

		//cj_entryOrderConfirm--入库确认，川佐发起
//		 this.requestType =
//		 HttpServerManagerService.requestType_cj_entryOrderConfirm;
		// this.content =
		// "{\"orderInfo\":{\"fpsOrderId\":\"4111110000197\",\"ownerUserId\":\"70057018\",\"storeCode\":\"WM10000079\",\"orderCode\":\"W100112051\",\"orderType\":\"601\",\"orderNumber\":\"ZT201808212027001\",\"outsourcingFlag\":\"01\",\"orderSource\":\"301\",\"orderCreateTime\":\"2018-08-21 22:06:54\",\"returnReason\":\"\",\"expectStartTime\":\"2018-08-29 15:13:38\",\"expectEndTime\":\"2018-08-29 15:13:38\",\"remark\":\"\",\"receiverInfo\":{},\"senderInfo\":{\"senderAddress\":\"江苏南京市软件大道203号\",\"senderCode\":\"70057018\",\"senderName\":\"C店-平行仓商家E 新1\",\"senderMobile\":\"025-66996699-880665\",\"senderPhone\":\"025-66996699-880665\"},\"orderItemList\":[{\"orderItemId\":\"410111000019511\",\"userId\":\"70057018\",\"userName\":\"C店-平行仓商家E 新1\",\"ownerUserId\":\"70057018\",\"ownerUserName\":\"C店-平行仓商家E 新1\",\"itemId\":\"000000001002512101\",\"itemName\":\"千丰浴霸壁挂式二灯双灯三灯浴霸卫生间浴室取暖灯泡挂墙式灯暖 小玲珑2米线护眼黄泡两灯挂浴霸 漏电保护\",\"itemCode\":\"000000001002512101\",\"inventoryType\":\"301\",\"itemQuantity\":\"1\",\"produceCode\":\"\",\"condition\":\"\",\"batchCode\":\"\"}]}}";
//		this.content = "<entryorderconfirm><order_code>W107xxxxxx</order_code><itemlist><item><order_item_id>420000002xxxxxx</order_item_id><sku>917080415493xxxxxx</sku><actual_qty>12</actual_qty><actual_qty_defect>2</actual_qty_defect></item><item><order_item_id>420000002xxxxxx11</order_item_id><sku>917080415493xxxxxx</sku><actual_qty>10</actual_qty><actual_qty_defect>4</actual_qty_defect></item></itemlist></entryorderconfirm>";

		//sn_deliverGoodsNotify--销售发货通知，苏宁发起
//		 this.requestType = HttpServerManagerService.requestType_sn_deliverGoodsNotify;
//		 this.content = "{\"orderInfo\":{\"ownerUserId\":\"7016xxxx\",\"storeCode\":\"WM10xxxxxx\",\"fpsOrderId\":\"12345xxxxxx\",\"orderCode\":\"W107xxxxxx\",\"orderType\":\"201\",\"orderNumber\":\"W89xxxx\",\"outsourcingFlag\":\"01\",\"customsMode\":\"04\",\"bol\":\"HM121231xxxxxx\",\"bolCount\":\"2\",\"orderSource\":\"S12\",\"tmsServiceCode\":\"S02\",\"tmsServiceName\":\"顺丰\",\"tmsOrderCode\":\"1234556667\",\"orderFlag\":\"\",\"destcode\":\"xxxxx\",\"orderCreateTime\":\"2018-01-03 12:47:11\",\"deliveryType\":\"PTPS\",\"deliverRequirements\":{\"scheduleDay\":\"2018-01-04\",\"scheduleStart\":\"18:00:00\",\"scheduleEnd\":\"18:00:00\"},\"batchSendCtrlParam\":{},\"extendFields\":\"{}\",\"receiverInfo\":{\"receiverProvince\":\"江苏省\",\"receiverCity\":\"宿迁市\",\"receiverArea\":\"宿豫区\",\"receiverTown\":\"全区\",\"receiverMobile\":\"0527-8431xxxx\",\"receiverPhone\":\"0527-8431xxxx\",\"receiverName\":\"谷绍娟\",\"receiverAddress\":\"江苏省宿迁市宿豫区江山大道xxxxxx\"},\"senderInfo\":{\"senderProvince\":\"江苏\",\"senderCity\":\"南京市\",\"senderArea\":\"雨花台区\",\"senderTown\":\"全区\",\"senderAddress\":\"xx大道1号\",\"senderName\":\"董x\",\"senderPhone\":\"025-66996699-87xxxx\",\"senderMobile\":\"025-66996699-87xxxx\"},\"orderItemList\":[{\"itemName\":\"电源xxx\",\"itemQuantity\":\"1\",\"orderItemId\":\"SL201801030000xxxxxx\",\"condition\":\"A\",\"ownerUserId\":\"7016xxxx\",\"itemId\":\"917080409503xxxxxx\",\"inventoryType\":\"1\",\"userId\":\"7016xxxx\",\"itemCode\":\"WTI09xxxx\"}]}}";

		//cj_deliveryOrderConfirm--销售发货确认，川佐发起
//		 this.requestType =
//		 HttpServerManagerService.requestType_cj_deliveryOrderConfirm;
//		this.content = "<deliveryorderconfirm><order_code>W107xxxxxx</order_code><tms_order_code>ems456</tms_order_code><package_weight>1.2</package_weight><package_length>1.1</package_length><package_width>1.3</package_width><package_height>1.5</package_height><itemlist><item><order_item_id>420000002xxxxxx</order_item_id><sku>917080415493xxxxxx</sku><qty>12</qty><produce_code>2</produce_code></item><item><order_item_id>420000002xxxxxx11</order_item_id><sku>917080415493xxxxxx</sku><qty>10</qty><produce_code>4</produce_code></item></itemlist></deliveryorderconfirm>";

		//cj_deliveryOrderStatus--出库单状态流程，川佐发起
//		 this.requestType =
//		 HttpServerManagerService.requestType_cj_deliveryOrderStatus;
//		this.content = "<deliveryorderstatus><order_code>W107xxxxxx</order_code><status_code>WMS_PACKAGE</status_code><status_remark>打包完成</status_remark></deliveryorderstatus>";

		
		this.requestType = requestType;
		this.content = content;
		this.signKey = signKey;
	}
	
//	@Transactional(rollbackFor = Exception.class)
//	public void test(){
//		List<String> colNames = new ArrayList<String>();
//		List<Object> colValues = new ArrayList<Object>();
//		
//		Map data = new HashMap();
//		
//		Map primary = new HashMap();
//		primary.put("primaryId", null);
//		
//		data.put("COL1", "111111111111");
//		commonManagerMapper.insertTableByNVList("tmp",
//				new ArrayList<String>(data.keySet()), new ArrayList<Object>(
//						data.values()), primary);
//		
//		data.put("COL1", "111111111111;;;;;");
//		
//		commonManagerMapper.insertTableByNVList("tmp",
//				new ArrayList<String>(data.keySet()), new ArrayList<Object>(
//						data.values()), primary);
//		
//		
//	}

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
			// 处理出库单状态
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
				.equals(HttpServerManagerService.requestType_cj_entryOrderConfirm)) {
			// 川佐返回收货结果
			result = handleXml_cj_entryOrderConfirm(content);
		}else if (requestType
				.equals(HttpServerManagerService.requestType_sn_deliverGoodsNotify)) {
			// 苏宁发货通知
			result = handleXml_sn_deliverGoodsNotify(content);
		} else if (requestType
				.equals(HttpServerManagerService.requestType_cj_deliveryOrderConfirm)) {
			// 川佐出库确认
			result = handleXml_cj_deliveryOrderConfirm(content);
		} else if (requestType
				.equals(HttpServerManagerService.requestType_cj_deliveryOrderStatus)) {
			// 川佐出库状态
			result = handleXml_cj_deliveryOrderStatus(content);
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
					synchronized(HttpHandleThread.class){
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


	// 处理订单数据
	private String handleXml_Inventory(String xmlString) {

		String xmlReturnString = "";

		System.out
				.println("---------------------------【FPAPI_INVENTORY】-------------------------------");

		List<String> inventoryOrderDataXML = XmlUtil.getNodesXmlData(xmlString, "//inputList/inventoryOrder");
		
		List<Map> resultList = new ArrayList<Map>();
		
		// 向苏宁回传订单状态
		List<Map> dataList = new ArrayList<Map>();

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
		String checkBusinessTypeC061 = CommonUtil
				.getSystemConfigProperty("checkInventoryBusinessType_C061");
		String checkBusinessTypeC005 = CommonUtil
				.getSystemConfigProperty("checkInventoryBusinessType_C005");
		
		// 获取资源文件
		ResourceBundle bundle = CommonUtil
				.getMessageMappingResource("CEB_SN");
		
		SimpleDateFormat sf = CommonUtil
				.getDateFormatter(CommonDefine.COMMON_FORMAT_1);
		
		for(String xmlData:inventoryOrderDataXML){

		Map head = null;

			Map singleResult = new HashMap();
			singleResult.put("isSuccess", "true");
		try {
			// 判断businessType
				head = XmlUtil.parseXmlFPAPI_SingleNodes(xmlData,
						"//inventoryDetailHead/child::*");
				
				singleResult.put("messageId", head.get("messageId"));

			String businessType = null;
			String logisticsOrderId = head.get("logisticsOrderId") != null ? head
					.get("logisticsOrderId").toString() : "";

			if (head != null && head.containsKey("businessType")
					&& head.get("businessType") != null) {
				businessType = (String) head.get("businessType");
			}

			// 报文入库--C061
			if (checkBusinessTypeC061.equals(businessType)) {
				List<Map> items = XmlUtil.parseXmlFPAPI_MulitpleNodes(
							xmlData,
							"//inventoryDetailItems");

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
							singleResult.put("isSuccess", "false");
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

				dataList.add(data);

			} else if (checkBusinessTypeC005.equals(businessType)) {
				String statusCode = head.get("statusCode") != null ? head.get(
						"statusCode").toString() : "";
				if ("1060".equals(statusCode)) {
					// 什么也不做
				} else if ("1131".equals(statusCode)) {
//					// 撤销操作
//					// t_new_import_inventory表中查找数据
//					List<String> colNames = new ArrayList<String>();
//					List<Object> colValues = new ArrayList<Object>();
//					colNames.add("ADD_REDUCE_FLAG");
//					colNames.add("ORDER_NO");
//					colValues.add("2");
//					colValues.add(logisticsOrderId);
//					List<Map<String, Object>> rows = commonManagerMapper
//							.selectTableListByNVList("t_new_import_books",
//									colNames, colValues, null, null);
//					//
//					for (Map<String, Object> data : rows) {
//						// 查找母数据
//						colNames.clear();
//						colValues.clear();
//
//						colNames.add("ADD_REDUCE_FLAG");
//						colNames.add("GOODS_SERIALNO");
//						colValues.add("1");
//						colValues.add(data.get("GOODS_SERIALNO"));
//
//						Map<String, Object> mainData = commonManagerMapper
//								.selectTableListByNVList("t_new_import_books",
//										colNames, colValues, null, null).get(0);
//
//						double updateQty = Double.valueOf(mainData.get("QTY")
//								.toString())
//								+ Double.valueOf(data.get("QTY").toString());
//						mainData.put("QTY", updateQty);
//						// 更新母数据
//						commonManagerMapper.updateTableByNVList(
//								"t_new_import_books", "BOOKS_ID",
//								mainData.get("BOOKS_ID"),
//								new ArrayList<String>(mainData.keySet()),
//								new ArrayList<Object>(mainData.values()));
//						// 更新子数据
//						data.put("QTY", 0d);
//						commonManagerMapper.updateTableByNVList(
//								"t_new_import_books", "BOOKS_ID", data
//										.get("BOOKS_ID"),
//								new ArrayList<String>(data.keySet()),
//								new ArrayList<Object>(data.values()));
//
//					}
//					// 向天津发送清单撤销消息
//					/*<InventoryCancel>
//						<orderNo></orderNo>
//						<ebpCode></ebpCode>
//					</InventoryCancel>*/
//					Map InventoryCancelData = new HashMap();
//					
//					InventoryCancelData.put("orderNo", logisticsOrderId);
//					InventoryCancelData.put("ebpCode", "3201966A69");
//					
//					String xmlStringData = XmlUtil.generalCommonXml(
//							"InventoryCancel", InventoryCancelData);
					//
//					// 第五步 向天津外运发送清单数据
//					Map reponse = postToTJ(xmlStringData);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
				singleResult.put("isSuccess", "false");
			}
			
			resultList.add(singleResult);
		}

		// 订单状态回传接口
		if (dataList.size() > 0) {
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
//			System.out.println(content);
			// 发送请求
			send2SN(requestParam, content);
		}

		// 返回数据构建
		List<LinkedMap> dataListReturn = new ArrayList<LinkedMap>();

		for (int i = 0; i < resultList.size(); i++) {
			LinkedMap resultData = new LinkedMap();
			resultData.put("messageId", resultList.get(i).get("messageId"));
			resultData.put("success", resultList.get(i).get("isSuccess"));
			resultData.put("errorCode", "");
			resultData.put("errorMsg", "");
			dataListReturn.add(resultData);
		}
		String root = CommonUtil
				.getSystemConfigProperty("inventoryReceiptRoot");
		String firstElement = CommonUtil
				.getSystemConfigProperty("inventoryFirstElement");

		xmlReturnString = XmlUtil.generalReceiptXml_FP(root, firstElement,
				dataListReturn);

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

	//出库单状态
	private String handleXml_ListRelease(String xmlString) {

		String xmlReturnString = "";

		System.out
				.println("---------------------------【FPAPI_ListRelease】-------------------------------");

//		boolean isSuccess = true;

		SimpleDateFormat sf = CommonUtil
				.getDateFormatter(CommonDefine.COMMON_FORMAT);
		// 向苏宁回传订单状态--7.3章节
		List<Map> dataList = new ArrayList<Map>();
		try {
			
			// 包含数据orderNo、invtNo，returnStatus
			List<Map> dataListArray = XmlUtil.parseXmlFPAPI_MulitpleNodes(xmlString, "//InventoryReturnList/InventoryReturn");

			for(Map head:dataListArray){
				
				// 查询数据库明细表中数据
				List<String> colNames = new ArrayList<String>();
				List<Object> colValues = new ArrayList<Object>();
				colNames.add("INVT_NO");
				colValues.add(head.get("invtNo"));
				//更新t_new_import_inventory.invtNo到值
				commonManagerMapper.updateTableByNVList("t_new_import_inventory", "ORDER_NO", head.get("orderNo"), colNames, colValues);
				
				//用orderNo在t_new_import_inventory_detail找到对应的LOS_NO，填在给苏宁的回执报文中logisticsOrderId
				List<Map<String,Object>> searchDataList = commonManagerMapper.selectTableListByCol("t_new_import_inventory_detail", "ORDER_NO", head.get("orderNo"), null, null);
				
				for(Map item:searchDataList){
					
					Map data = new HashMap();
					data.put("messageId", getMessageId());
					data.put("logisticsOrderId", item!=null?item.get("LOS_NO"):"");
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
					
					dataList.add(data);
				}
			}
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
			
			//System.out.println(content);
			
			// 发送请求
			String result = send2SN(requestParam, content);

		} catch (Exception e) {
			e.printStackTrace();
//			isSuccess = false;
		}
		// 无需内容，直接返回200
		xmlReturnString = "";

		return xmlReturnString;
	}

	public static String send2SN(Map requestParam, String content) {
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
		data.put("RECEIPT_STATUS", "0");
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
		
		String xmlStringData = XmlUtil.generalCommonXml_CJ(
				"DATA", order, orderItemListForCJ);
		
		String requestXmlData = XmlUtil.generalSoapXml_CJ(xmlStringData,"sendInStockOrder");
		
		System.out.println(requestXmlData);
		//向川佐发送入库通知单
		String response = HttpUtil.sendHttpCMD_CJ(requestXmlData,CommonUtil
				.getSystemConfigProperty("CJ_sendInStockOrder_requestUrl").toString());

		//获取返回信息
//		String returnXmlData = XmlUtil
//				.getResponseFromXmlString_CJ(response);
		
		String returnXmlData = XmlUtil.getTotalMidValue(StringEscapeUtils.unescapeXml(response),"<ns:return>","</ns:return>");
		
		//正常测试报文
//		String returnXmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><DATA><ORDER><ORDER_CODE>3434222e333</ORDER_CODE><CD>OK</CD><INFO><![CDATA[]]></INFO><ITEMS><ITEM><ORDER_ITEM_ID>420000002xxxxxx</ORDER_ITEM_ID><SKU>P0000KMM</SKU><ACTUAL_QTY>1</ACTUAL_QTY><ACTUAL_QTY_DEFECT>5590</ACTUAL_QTY_DEFECT></ITEM><ITEM><ORDER_ITEM_ID>1234567891</ORDER_ITEM_ID><SKU>P0001KMM</SKU><ACTUAL_QTY>1</ACTUAL_QTY><ACTUAL_QTY_DEFECT>5591</ACTUAL_QTY_DEFECT></ITEM></ITEMS></ORDER></DATA>";
		//异常测试报文
//		String returnXmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><DATA><ORDER><ORDER_CODE>W107xxxxxx</ORDER_CODE><CD>102</CD><INFO>发货人编号在系统中未找到。</INFO></ORDER></DATA>";

		//解析返回报文
		//正常报文
		Map orderResult = XmlUtil.parseXmlFPAPI_SingleNodes(returnXmlData, "//DATA/ORDER/child::*");
		
		//正常返回
		if(orderResult.containsKey("CD") && "OK".equals(orderResult.get("CD").toString())){
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

/*	// 川佐收货确认----未完成、暂不入库，直接转发给苏宁
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
	}*/
	
	
	//川佐返回入库确认信息
	private String handleXml_cj_entryOrderConfirm(String xmlDataString) {

		// String jsonReturnString = "";
		SimpleDateFormat sf = CommonUtil
				.getDateFormatter(CommonDefine.RETRIEVAL_TIME_FORMAT);

		System.out
				.println("---------------------------【FPAPI_cj_entryOrderConfirm】-------------------------------");
		
		Map orderInfo = XmlUtil.parseXmlFPAPI_SingleNodes(xmlDataString, "//entryorderconfirm/child::*");
		List<Map> orderItemList = XmlUtil.parseXmlFPAPI_MulitpleNodes(xmlDataString, "//entryorderconfirm/itemlist/item");

		//返回苏宁入库确认--7.3章节
		// 查询数据库明细表中数据
		List<String> colNames = new ArrayList<String>();
		List<Object> colValues = new ArrayList<Object>();
		colNames.add("ORDER_CODE");
		colValues.add(orderInfo.get("order_code"));
		List<Map<String, Object>> items = commonManagerMapper
				.selectTableListByNVList("t_sn_receipt", colNames, colValues,
						null, null);

		//查询主信息
		Map item = null;
		if (items.size() > 0) {
			item = items.get(0);
		} else {
			return "<entryorderconfirmreturn><status>fail</status></entryorderconfirmreturn>";
		}
		
		//更新主表RECEIPT_STATUS
		colNames.clear();
		colValues.clear();
		colNames.add("RECEIPT_STATUS");
		colValues.add("1");
		commonManagerMapper.updateTableByNVList("t_sn_receipt", "ORDER_CODE", orderInfo.get("order_code"), colNames, colValues);
		
		//更新明细表
		//将ACTUAL_QTY和ACTUAL_QTY_DEFECT字段写入t_sn_receipt_detail
		for(Map orderItem:orderItemList){
			colNames.clear();
			colValues.clear();
			colNames.add("ACTUAL_QTY");
			colNames.add("ACTUAL_QTY_DEFECT");
			colValues.add(orderItem.get("actual_qty"));
			colValues.add(orderItem.get("actual_qty_defect"));
			commonManagerMapper.updateTableByNVList("t_sn_receipt_detail", "ORDER_ITEM_ID", orderItem.get("order_item_id"), colNames, colValues);
		}

		// ------------------- orderItem ------------
		JSONArray orderItemListArray = new JSONArray();

		int i = 0;
		for (Map orderItem:orderItemList) {
			 //查询数据库明细表中数据
			colNames.clear();
			colValues.clear();
			colNames.add("ORDER_ITEM_ID");
			colNames.add("SKU");
			colValues.add(orderItem.get("order_item_id"));
			colValues.add(orderItem.get("sku"));
			Map data = commonManagerMapper.selectTableListByNVList("t_sn_receipt_detail",
					colNames, colValues, null, null).get(0);

			JSONObject orderSingleItem = new JSONObject();
			// 明细表ORDER_ITEM_ID
			orderSingleItem.put("orderItemId", data.get("ORDER_ITEM_ID"));
			// OWNER
			orderSingleItem.put("ownUserId", item.get("OWNER"));
			orderSingleItem.put("stockNumber", "DSWMS"+CommonUtil.getDateFormatter(CommonDefine.COMMON_FORMAT_2)
					.format(new Date()));
			orderSingleItem.put("isCompleted", true);
			// 明细表SKU
			orderSingleItem.put("itemId", data.get("SKU"));

			// ------------------- itemInventorySingleItem ------------
			JSONObject itemInventorySingleItem = new JSONObject();
			itemInventorySingleItem.put("inventoryType", "1");
			// 明细表 ACTUAL_QTY
			itemInventorySingleItem.put("quantity", data.get("ACTUAL_QTY"));
			itemInventorySingleItem.put("produceDate", "");
			// 明细表 PRODUCE_CODE
			itemInventorySingleItem.put("produceCode", data.get("PRODUCE_CODE"));
			itemInventorySingleItem.put("condition", "");
			// ------------------- produceCodeSingleItem ------------
			JSONObject produceCodeSingleItem = new JSONObject();
			// 填顺序值，从0开始
			produceCodeSingleItem.put("flag", i);
			// 明细表 PRODUCE_CODE
			produceCodeSingleItem.put("produceCode", data.get("PRODUCE_CODE"));
			produceCodeSingleItem.put("expirationDate", "2099-01-01");
			// 明细表 ACTUAL_QTY
			produceCodeSingleItem.put("quantity", data.get("ACTUAL_QTY"));

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

			orderItemListArray.add(orderSingleItem);
			
			i++;
		}

		// 返回苏宁数据
		// ------------------- orderItems ------------
		JSONObject orderItems = new JSONObject();
		orderItems.put("orderItem", orderItemListArray);

		// ------------------- orderConfirmInfo ------------
		JSONObject orderConfirmInfo = new JSONObject();
		// STORE_CODE
		orderConfirmInfo.put("storeOrderCode", item.get("STORE_CODE"));
		// FPS_ORDER_ID
		orderConfirmInfo.put("fpsOrderId", item.get("FPS_ORDER_ID"));
		// ORDER_TYPE
		orderConfirmInfo.put("orderType", item.get("ORDER_TYPE"));
		// ORDER_CODE
		orderConfirmInfo.put("orderCode", item.get("ORDER_CODE"));
		// ORDER_NUMBER
		orderConfirmInfo.put("orderNumber", item.get("ORDER_NUMBER"));
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

//		System.out.println(content.toString());
		// 发送请求
		String result = send2SN(requestParam, content.toString());

		return "<entryorderconfirmreturn><status>success</status></entryorderconfirmreturn>";
	}
	
	//川佐返回出库确认
	private String handleXml_cj_deliveryOrderConfirm(String xmlDataString) {

		// String jsonReturnString = "";
		SimpleDateFormat sf = CommonUtil
				.getDateFormatter(CommonDefine.RETRIEVAL_TIME_FORMAT);

		System.out
				.println("---------------------------【FPAPI_cj_deliveryOrderConfirm】-------------------------------");
		
		Map orderInfo = XmlUtil.parseXmlFPAPI_SingleNodes(xmlDataString, "//deliveryorderconfirm/child::*");
		List<Map> orderItemList = XmlUtil.parseXmlFPAPI_MulitpleNodes(xmlDataString, "//deliveryorderconfirm/itemlist/item");

		//返回苏宁入库确认--7.7章节
		// 查询数据库明细表中数据
		List<String> colNames = new ArrayList<String>();
		List<Object> colValues = new ArrayList<Object>();
		colNames.add("ORDER_CODE");
		colValues.add(orderInfo.get("order_code"));
		List<Map<String, Object>> items = commonManagerMapper
				.selectTableListByNVList("t_sn_order", colNames, colValues,
						null, null);

		//查询主信息
		Map item = null;
		if (items.size() > 0) {
			item = items.get(0);
		} else {
			return "<deliveryorderconfirmreturn><status>fail</status></deliveryorderconfirmreturn>";
		}
		
		//更新主表
		colNames.clear();
		colValues.clear();
		colNames.add("ORDER_STATUS");
		colNames.add("TMS_ORDER_CODE");
		colNames.add("PACKAGE_HEIGHT");
		colNames.add("PACKAGE_WEIGHT");
		colNames.add("PACKAGE_LENGTH");
		colNames.add("PACKAGE_WIDTH");
		colValues.add("1");
		colValues.add(orderInfo.get("tms_order_code"));
		colValues.add(orderInfo.get("package_weight"));
		colValues.add(orderInfo.get("package_length"));
		colValues.add(orderInfo.get("package_width"));
		colValues.add(orderInfo.get("package_height"));
		commonManagerMapper.updateTableByNVList("t_sn_order", "ORDER_CODE", orderInfo.get("order_code"), colNames, colValues);
		
		//更新明细表
		//将QTY和PRODUCE_CODE字段写入t_sn_order_detail
		for(Map orderItem:orderItemList){
			colNames.clear();
			colValues.clear();
			colNames.add("QTY");
			colNames.add("PRODUCE_CODE");
			colValues.add(orderItem.get("qty"));
			colValues.add(orderItem.get("produce_code"));
			commonManagerMapper.updateTableByNVList("t_sn_order_detail", "ORDER_ITEM_ID", orderItem.get("order_item_id"), colNames, colValues);
		}

		// ------------------- orderItem ------------
		JSONArray orderItemListArray = new JSONArray();
		JSONArray tmsOrdersListArray = new JSONArray();

		int i = 1;
		for (Map orderItem:orderItemList) {
			 //查询数据库明细表中数据
			colNames.clear();
			colValues.clear();
			colNames.add("ORDER_ITEM_ID");
			colNames.add("SKU");
			colValues.add(orderItem.get("order_item_id"));
			colValues.add(orderItem.get("sku"));
			Map data = commonManagerMapper.selectTableListByNVList("t_sn_order_detail",
					colNames, colValues, null, null).get(0);

			JSONObject orderSingleItem = new JSONObject();
			// 明细表ORDER_ITEM_ID
			orderSingleItem.put("orderItemId", data.get("ORDER_ITEM_ID"));
			// OWNER
			orderSingleItem.put("ownUserId", item.get("OWNER"));
			orderSingleItem.put("stockNumber", "DSWMS"+CommonUtil.getDateFormatter(CommonDefine.COMMON_FORMAT_2)
					.format(new Date()));
			orderSingleItem.put("isCompleted", true);
			// 明细表SKU
			orderSingleItem.put("itemId", data.get("SKU"));
			// 明细表ITEM_CODE
			orderSingleItem.put("itemCode", data.get("ITEM_CODE"));	

			// ------------------- itemInventorySingleItem ------------
			JSONObject itemInventorySingleItem = new JSONObject();
			itemInventorySingleItem.put("inventoryType", "1");
			// 明细表 QTY
			itemInventorySingleItem.put("quantity", data.get("QTY"));

			// ------------------- produceCodeSingleItem ------------
			JSONObject produceCodeSingleItem = new JSONObject();
			// 填顺序值，从0开始
			produceCodeSingleItem.put("flag", i);
			// 明细表 PRODUCE_CODE
			produceCodeSingleItem.put("produceCode", data.get("PRODUCE_CODE"));
			produceCodeSingleItem.put("expirationDate", "2099-01-01");
			// 明细表 QTY
			produceCodeSingleItem.put("quantity", data.get("QTY"));
			
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
			
			orderItemListArray.add(orderSingleItem);
			
			
			
			// ------------------- tmsOrderSingleItem ------------
			JSONObject tmsOrderSingleItem = new JSONObject();
			// 主表
			tmsOrderSingleItem.put("packageWeight", orderInfo.get("package_weight"));
			tmsOrderSingleItem.put("packageLength", orderInfo.get("package_length"));
			tmsOrderSingleItem.put("packageHeight", orderInfo.get("package_height"));
			tmsOrderSingleItem.put("packageWidth", orderInfo.get("package_width"));
			tmsOrderSingleItem.put("tmsOrderCode", orderInfo.get("tms_order_code"));
			
			tmsOrderSingleItem.put("tmsCode", item.get("TMS_SERVICE_CODE"));

			// ------------------- tmsItemSingleItem ------------
			JSONObject tmsItemSingleItem = new JSONObject();
			tmsItemSingleItem.put("orderItemId", data.get("ORDER_ITEM_ID"));
			tmsItemSingleItem.put("itemId", data.get("SKU"));
			tmsItemSingleItem.put("itemCode", data.get("ITEM_CODE"));
			tmsItemSingleItem.put("itemQuantity", data.get("QTY"));
			// ------------------- packageMaterialSingleItem ------------
			JSONObject packageMaterialSingleItem = new JSONObject();
			packageMaterialSingleItem.put("materialQuantity", "");
			packageMaterialSingleItem.put("materialType", "");
			// ------------------- tmsItem ------------
			JSONArray tmsItem = new JSONArray();
			tmsItem.add(tmsItemSingleItem);
			// ------------------- packageMaterial ------------
			JSONArray packageMaterial = new JSONArray();
			packageMaterial.add(packageMaterialSingleItem);
			// ------------------- tmsItems ------------
			JSONObject tmsItems = new JSONObject();
			tmsItems.put("tmsItem", tmsItem);
			// ------------------- packageMaterialList ------------
			JSONObject packageMaterialList = new JSONObject();
			packageMaterialList.put("packageMaterial", packageMaterial);
			
			tmsOrderSingleItem.put("tmsItems", tmsItems);
			tmsOrderSingleItem.put("packageMaterialList", packageMaterialList);
			tmsOrdersListArray.add(tmsOrderSingleItem);
			
			i++;
		}

		// 返回苏宁数据
		// ------------------- orderItems ------------
		JSONObject orderItems = new JSONObject();
		orderItems.put("orderItem", orderItemListArray);
		// ------------------- tmsOrders ------------
		JSONObject tmsOrders = new JSONObject();
		tmsOrders.put("tmsOrder", tmsOrdersListArray);

		// ------------------- orderConfirmInfo ------------
		JSONObject orderConfirmInfo = new JSONObject();
		// STORE_CODE
		orderConfirmInfo.put("storeOrderCode", item.get("STORE_CODE"));
		// FPS_ORDER_ID
		orderConfirmInfo.put("fpsOrderId", item.get("FPS_ORDER_ID"));
		// ORDER_TYPE
		orderConfirmInfo.put("orderType", item.get("ORDER_TYPE"));
		// ORDER_CODE
		orderConfirmInfo.put("orderCode", item.get("ORDER_CODE"));
		// ORDER_NUMBER
		orderConfirmInfo.put("orderNumber", item.get("ORDER_NUMBER"));
		//
		orderConfirmInfo.put("outBizCode",
				CommonUtil.getDateFormatter(CommonDefine.RETRIEVAL_TIME_FORMAT)
						.format(new Date()));
		orderConfirmInfo.put("orderItems", orderItems);
		orderConfirmInfo.put("tmsOrders", tmsOrders);
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
								.getSystemConfigProperty("SN_deliveryOrderConfirmInfo_logistic_provider_id"));
		requestParam.put("msg_type", CommonUtil
				.getSystemConfigProperty("SN_deliveryOrderConfirmInfo_msg_type"));
		requestParam.put("url", CommonUtil
				.getSystemConfigProperty("SN_deliveryOrderConfirmInfo_requestUrl"));

//		System.out.println(content.toString());
		// 发送请求
		String result = send2SN(requestParam, content.toString());

		return "<deliveryorderconfirmreturn><status>success</status></deliveryorderconfirmreturn>";
	}
	
	
	//川佐返回出库状态
	private String handleXml_cj_deliveryOrderStatus(String xmlDataString) {

		// String jsonReturnString = "";
		SimpleDateFormat sf = CommonUtil
				.getDateFormatter(CommonDefine.RETRIEVAL_TIME_FORMAT);

		System.out
				.println("---------------------------【FPAPI_cj_deliveryOrderStatus】-------------------------------");
		
		Map orderInfo = XmlUtil.parseXmlFPAPI_SingleNodes(xmlDataString, "//deliveryorderstatus/child::*");

		//返回苏宁入库确认
		// 查询数据库明细表中数据
		List<String> colNames = new ArrayList<String>();
		List<Object> colValues = new ArrayList<Object>();
		colNames.add("ORDER_CODE");
		colValues.add(orderInfo.get("order_code"));
		List<Map<String, Object>> items = commonManagerMapper
				.selectTableListByNVList("t_sn_order", colNames, colValues,
						null, null);

		//查询主信息
		Map item = null;
		if (items.size() > 0) {
			item = items.get(0);
		} else {
			return "<deliveryorderstatusreturn><status>fail</status></deliveryorderstatusreturn>";
		}
		
		//更新主表--暂无

		//给苏宁报文--7.10章节
		JSONObject snRequest = new JSONObject();
		
		JSONArray statusList = new JSONArray();
		
		colNames.clear();
		colValues.clear();
		colNames.add("STATUS_CODE");
		colValues.add(orderInfo.get("status_code"));
		List<Map<String, Object>> statusChangeList = commonManagerMapper
				.selectTableListByNVList("T_SN_STATUS_CHANGE", colNames, colValues,
						null, null);
		for(Map<String, Object> statusChange:statusChangeList){
			
		JSONObject statusSingleItem = new JSONObject();
		
			String statusType = statusChange.get("STATUS_TYPE")!=null?statusChange.get("STATUS_TYPE").toString():"";
			
			if("WMS".equals(statusType)){
				statusSingleItem.put("expressNo", orderInfo.get("order_code"));
				statusSingleItem.put("expressCompanyCode", "WMS");
			}else if("TMS".equals(statusType)){
				statusSingleItem.put("expressNo", item.get("TMS_ORDER_CODE"));
				statusSingleItem.put("expressCompanyCode", item.get("TMS_SERVICE_CODE"));
			}		
			statusSingleItem.put("mailStatus", statusChange.get("CN_CODE"));
			statusSingleItem.put("address", statusChange.get("CN_REMARK"));
			statusSingleItem.put("statusType", statusChange.get("STATUS_TYPE"));
		statusSingleItem.put("time",
				CommonUtil.getDateFormatter(CommonDefine.COMMON_FORMAT)
						.format(new Date()));
		
		statusList.add(statusSingleItem);
		}
		snRequest.put("statusList", statusList);

		JSONObject content = new JSONObject();
		content.put("snRequest", snRequest);

		Map requestParam = new HashMap();
		requestParam
				.put("logistic_provider_id",
						CommonUtil
								.getSystemConfigProperty("SN_deliveryOrderStatusInfo_logistic_provider_id"));
		requestParam.put("msg_type", CommonUtil
				.getSystemConfigProperty("SN_deliveryOrderStatusInfo_msg_type"));
		requestParam.put("url", CommonUtil
				.getSystemConfigProperty("SN_deliveryOrderStatusInfo_requestUrl"));

		System.out.println(content.toString());
		// 发送请求
		String result = send2SN(requestParam, content.toString());

		//插入t_sn_return_status表
		// 获取资源文件
		ResourceBundle bundle = CommonUtil.getMessageMappingResource("CEB_SN");
		Map primary = new HashMap();
		primary.put("primaryId", null);
		Map data = new HashMap();
		for (Object key : orderInfo.keySet()) {
			if (bundle
					.containsKey("SN_RETURN_STATUS_" + key.toString().toUpperCase())) {

				if (orderInfo.get(key) == null
						|| orderInfo.get(key).toString().isEmpty()) {
					data.put(
							bundle.getObject("SN_RETURN_STATUS_"
									+ key.toString().toUpperCase()), null);
				} else {
					data.put(
							bundle.getObject("SN_RETURN_STATUS_"
									+ key.toString().toUpperCase()),
							orderInfo.get(key));
				}
			}
		}
		data.put("STATUS_TIME", CommonUtil
				.getDateFormatter(CommonDefine.COMMON_FORMAT).format(new Date()));
		data.put("CREAT_TIME", new Date());
		commonManagerMapper.insertTableByNVList("T_SN_RETURN_STATUS",
				new ArrayList<String>(data.keySet()), new ArrayList<Object>(
						data.values()), primary);

		return "<deliveryorderstatusreturn><status>success</status></deliveryorderstatusreturn>";
	}
	
	private String handleXml_sn_deliverGoodsNotify(String jsonString)  throws CommonException{

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
		data.put("ORDER_STATUS", "0");
		data.put("BOL_COUNT", bolCount);
		data.put("CREAT_DATE", sf.format(new Date()));
		data.put("CREAT_TIME", new Date());
		commonManagerMapper.insertTableByNVList("T_SN_ORDER",
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
			
			//发送给川佐数据组装
			dataSubForCJ.put("ORDER_ITEM_ID", dataSub.get("ORDER_ITEM_ID"));
			dataSubForCJ.put("SKU", dataSub.get("SKU"));
			dataSubForCJ.put("ITEM_NAME", dataSub.get("ITEM_NAME"));
			dataSubForCJ.put("QTY", dataSub.get("QTY"));
			orderItemListForCJ.add(dataSubForCJ);

		}
		//根据苏宁报文中CUSTOMS_MODE字段走不同的流程。先测CUSTOMS_MODE等于04时，直接发给佐川。等于02时走另外的流程。
		String customsMode = data.get("CUSTOMS_MODE")!=null?data.get("CUSTOMS_MODE").toString():"";
		
		//直接发给川佐
		if("04".equals(customsMode)){
			
			String skuString = "";
			if(orderItemListForCJ.size()>0 && orderItemListForCJ.get(0).get("SKU")!=null){
				skuString = orderItemListForCJ.get(0).get("SKU").toString();
			}
			List<Map<String,Object>> skuList = commonManagerMapper.selectTableListByCol("t_sn_sku", "SKU", skuString, null, null);
			
			Map<String,Object> sku = null;
			if(skuList !=null &&skuList.size()>0){
				sku = skuList.get(0);
			}
			//给川佐发送销售出库单
			Map order = new HashMap();
			order.put("OWNER", data.get("OWNER"));
			order.put("ORDER_CODE", data.get("ORDER_CODE"));
			order.put("ORDER_TYPE", data.get("ORDER_TYPE"));
			order.put("CUSTOMS_MODE", data.get("CUSTOMS_MODE"));
			order.put("TOTAL_WEIGHT", data.get("TOTAL_WEIGHT"));
			order.put("WAY_BILLS", data.get("WAY_BILLS"));
			order.put("PAY_AMOUNT", data.get("PAY_AMOUNT"));
			order.put("DEST_CODE", data.get("DEST_CODE"));
			//出库单下任意一个商品sku在商品表中的t_sn_sku.GRP
			order.put("CMMDTY_GRP", sku!=null?sku.get("GRP"):"");
			order.put("TMS_ORDER_CODE", data.get("TMS_ORDER_CODE"));
			order.put("RECEIVER_ADDRESS", data.get("RECEIVER_ADDRESS"));
			order.put("RECEIVER_NAME", data.get("RECEIVER_NAME"));
			order.put("RECEIVER_MOBILE", data.get("RECEIVER_MOBILE"));
			order.put("RECEIVER_PHONE", data.get("RECEIVER_PHONE"));
			
			order.put("CUST", CommonUtil
					.getSystemConfigProperty("CJ_cust"));
			
			String xmlStringData = XmlUtil.generalCommonXml_CJ(
					"DATA", order, orderItemListForCJ);
			
			String requestXmlData = XmlUtil.generalSoapXml_CJ(xmlStringData,"sendOutStockOrder");
			
			//System.out.println(requestXmlData);
			//向川佐发送销售出库单
			String response = HttpUtil.sendHttpCMD_CJ(requestXmlData,CommonUtil
					.getSystemConfigProperty("CJ_sendOrder_requestUrl").toString());
			
			//获取返回信息
//			String returnXmlData = XmlUtil
//					.getResponseFromXmlString_CJ(response);
			
			String returnXmlData = XmlUtil.getTotalMidValue(StringEscapeUtils.unescapeXml(response),"<ns:return>","</ns:return>");
			
			//正常测试报文
//			String returnXmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><DATA><ORDER><ORDER_CODE>3434222e333</ORDER_CODE><CD>OK</CD><INFO><![CDATA[]]></INFO><ITEMS><ITEM><ORDER_ITEM_ID>420000002xxxxxx</ORDER_ITEM_ID><SKU>P0000KMM</SKU><ACTUAL_QTY>1</ACTUAL_QTY><ACTUAL_QTY_DEFECT>5590</ACTUAL_QTY_DEFECT></ITEM><ITEM><ORDER_ITEM_ID>1234567891</ORDER_ITEM_ID><SKU>P0001KMM</SKU><ACTUAL_QTY>1</ACTUAL_QTY><ACTUAL_QTY_DEFECT>5591</ACTUAL_QTY_DEFECT></ITEM></ITEMS></ORDER></DATA>";
			//异常测试报文
//			String returnXmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><DATA><ORDER><ORDER_CODE>W107xxxxxx</ORDER_CODE><CD>102</CD><INFO>发货人编号在系统中未找到。</INFO></ORDER></DATA>";

			//解析返回报文
			//正常报文
			Map orderResult = XmlUtil.parseXmlFPAPI_SingleNodes(returnXmlData, "//DATA/ORDER/child::*");
			
			//正常返回
			if(orderResult.containsKey("CD") && "OK".equals(orderResult.get("CD").toString())){
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
			
		}else if("02".equals(customsMode)){
			
		}

		return result.toString();
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

/*	private Map handleOrderC005(Map head, String xmlString,
			ResourceBundle bundle) {
		Map result = new HashMap();

		result.put("isSuccess", true);

		SimpleDateFormat sf = CommonUtil
				.getDateFormatter(CommonDefine.COMMON_FORMAT_1);
		// t_new_import_inventory表中查找数据
		List<String> colNames = new ArrayList<String>();
		List<Object> colValues = new ArrayList<Object>();
		colNames.add("ORDER_NO");
		colValues.add(head.get("btcOrderId"));
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
							"//orders/orderImformation/orderItems");

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
					// QTY最小的，RECORD_NO最小的记录减去<orderItems><goodsNumber>。
					Collections.sort(skuList, new Comparator<Map>() {
						public int compare(Map o1, Map o2) {
							double qty1 = Double.valueOf(o1.get("QTY")
									.toString());
							double qty2 = Double.valueOf(o2.get("QTY")
									.toString());
							String recordNo1 = o1.get("RECORD_NO")!=null?o1.get("RECORD_NO").toString():"";
							String recordNo2 = o2.get("RECORD_NO")!=null?o2.get("RECORD_NO").toString():"";
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
								if(recordNo1.compareTo(recordNo2)<0){
									return 1;
								}else{
									return 0;
								}
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
				//
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
	}*/
	
	
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
					Map reponse = postToTJ(xmlStringData);
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
					} else {
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
			newBook.put("ORDER_NO", xxx.getHead().get("btcOrderId"));
			newBook.put("CREAT_DATE", CommonUtil
					.getDateFormatter(CommonDefine.COMMON_FORMAT_1).format(new Date()));
			newBook.put("CREAT_TIME", new Date());

			commonManagerMapper.insertTableByNVList(
					"t_new_import_books", new ArrayList<String>(newBook.keySet()),
					new ArrayList<Object>(newBook.values()), primary);
		}
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
		//查询数据
		List<LinkedHashMap> headList = snCommonManagerMapper.selectInventoryHead(idInt);
		if (headList != null) {
			LinkedHashMap item = headList.get(0);
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
			//如果t_new_import_sku.unit2为空，则<qty2>保持不变，继续填t_new_import_inventory_detail.qty2。
			//如果t_new_import_sku.unit2不为空，则<qty2>改成t_new_import_inventory_detail.qty1
			if(item.get("UNIT2") == null || item.get("UNIT2").toString().isEmpty()){
				//不变
			}else{
				item.put("QTY2", item.get("QTY1"));
			}
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

	// 插入t_new_import_inventory表和t_new_import_inventory_detail表
//	private String insertInventory(String xmlString) {
//		Map head = XmlUtil
//				.parseXmlFPAPI_SingleNodes(xmlString,
//						"//orders/orderImformation/orderHead/child::*");
//		Map orderDeclareHead = XmlUtil
//				.parseXmlFPAPI_SingleNodes(xmlString,
//						"//orders/orderDeclare/orderDeclareHead/child::*");
//		
//		Map orderExpBill = XmlUtil
//				.parseXmlFPAPI_SingleNodes(xmlString,
//						"//orders/orderExpBill/child::*");
//		
//		Map orderDeclareItems = XmlUtil.parseXmlFPAPI_SingleNodes(
//				xmlString,
//				"//orders/orderDeclare/orderDeclareItems/child::*");
//
//		SimpleDateFormat sf = CommonUtil
//				.getDateFormatter(CommonDefine.RETRIEVAL_TIME_FORMAT);
//
//		SimpleDateFormat sf1 = CommonUtil
//				.getDateFormatter(CommonDefine.COMMON_FORMAT_1);
//
//		List<String> colNames = new ArrayList<String>();
//		List<Object> colValues = new ArrayList<Object>();
//		Map primary = new HashMap();
//		primary.put("primaryId", null);
//
//		colNames.add("GUID");
//		colValues.add(CommonUtil.generalGuid(
//				CommonDefine.GUID_FOR_LOGISTICS_SN_1, 10,
//				"t_new_import_inventory"));
//
//		colNames.add("CUSTOM_CODE");
//		colValues.add("0213");
//
//		colNames.add("APP_TYPE");
//		colValues.add("1");
//
//		colNames.add("APP_TIME");
//		colValues.add(sf.format(new Date()));
//
//		colNames.add("APP_STATUS");
//		colValues.add("2");
//
//		colNames.add("COP_NO");
//		colValues.add(head.get("taskOrderid"));
//
//		colNames.add("PRE_NO");
//		colValues.add("");
//
//		colNames.add("EBC_CODE");
//		colValues.add("3201966A69");
//
//		colNames.add("EBC_NAME");
//		colValues.add("江苏苏宁易购电子商务有限公司");
//
//		colNames.add("EBP_CODE");
//		colValues.add("3201966A69");
//
//		colNames.add("EBP_NAME");
//		colValues.add("江苏苏宁易购电子商务有限公司");
//
//		colNames.add("ORDER_NO");
//		colValues.add(head.get("btcOrderId"));
//
//		colNames.add("LOGISTICS_NO");
//		colValues.add(orderExpBill.get("expressCompanyExcode"));
//
//		colNames.add("LOGISTICS_CODE");
//		colValues.add("3201961A28");
//
//		colNames.add("LOGISTICS_NAME");
//		colValues.add("江苏苏宁物流有限公司");
//
//		colNames.add("ASSURE_CODE");
//		colValues.add("3201966A69");
//
//		colNames.add("EMS_NO");
//		colValues.add("T0213W000152");
//
//		colNames.add("INVT_NO");
//		colValues.add("");
//
//		colNames.add("DECL_TIME");
//		colValues.add(sf1.format(new Date()));
//
//		colNames.add("PORT_CODE");
//		colValues.add("0213");
//
//		colNames.add("IE_DATE");
//		colValues.add(null);
//
//		colNames.add("BUYER_NAME");
//		colValues.add(orderDeclareHead.get("payerName"));
//
//		colNames.add("BUYER_IDTYPE");
//		colValues.add("1");
//
//		colNames.add("BUYER_IDNUMBER");
//		colValues.add(orderDeclareHead.get("paperNumber"));
//
//		colNames.add("BUYER_TELEPHONE");
//		colValues.add(orderDeclareHead.get("payerPhoneNumber"));
//
//		colNames.add("CONSIGNEE_ADDRESS");
//		colValues.add(orderDeclareHead.get("consigneeAddress"));
//
//		colNames.add("AGENT_CODE");
//		colValues.add("1207980025");
//
//		colNames.add("AGENT_NAME");
//		colValues.add("天津中外运报关有限公司");
//
//		colNames.add("AERA_CODE");
//		colValues.add("1207610251");
//
//		colNames.add("AERA_NAME");
//		colValues.add("天津中外运国际物流发展有限公司");
//
//		colNames.add("TRADE_MODE");
//		colValues.add("1210");
//
//		colNames.add("TRAF_MODE");
//		colValues.add("Y");
//
//		colNames.add("TRAF_NO");
//		colValues.add("");
//
//		colNames.add("LOCT_NO");
//		colValues.add("");
//
//		colNames.add("LICENSE_NO");
//		colValues.add("");
//
//		colNames.add("COUNTRY");
//		colValues.add("142");
//
//		colNames.add("CURRENCY");
//		colValues.add("142");
//
//		colNames.add("FREIGHT");
//		if (orderDeclareHead.get("freight") != null
//				&& !orderDeclareHead.get("freight").toString().isEmpty()) {
//			colValues.add(orderDeclareHead.get("freight"));
//		} else {
//			colValues.add(null);
//		}
//
//		colNames.add("INSURE_FEE");
//		if (orderDeclareHead.get("insuranceFee") != null
//				&& !orderDeclareHead.get("insuranceFee").toString().isEmpty()) {
//			colValues.add(orderDeclareHead.get("insuranceFee"));
//		} else {
//			colValues.add(0);
//		}
//
//		colNames.add("WRAP_TYPE");
//		colValues.add(orderDeclareHead.get("warpType"));
//
//		colNames.add("PACK_NO");
//		colValues.add("1");
//
//		colNames.add("GROSS_WEIGHT");
//		colNames.add("NET_WEIGHT");
//		if (orderDeclareItems.get("goodsGrossWeight") != null
//				&& !orderDeclareItems.get("goodsGrossWeight").toString()
//						.isEmpty()
//				&& orderDeclareItems.get("declareCount") != null
//				&& !orderDeclareItems.get("declareCount").toString().isEmpty()) {
//			colValues.add(Double.parseDouble(orderDeclareItems.get(
//					"goodsGrossWeight").toString())
//					* Double.parseDouble(orderDeclareItems.get("declareCount")
//							.toString()));
//			colValues.add(Double.parseDouble(orderDeclareItems.get(
//					"goodsGrossWeight").toString())
//					* Double.parseDouble(orderDeclareItems.get("declareCount")
//							.toString()));
//		} else {
//			colValues.add(null);
//			colValues.add(null);
//		}
//
//		colNames.add("PAY_SERIAL_NO");
//		if (orderDeclareHead.get("paySerialNo") != null
//				&& !orderDeclareHead.get("paySerialNo").toString().isEmpty()) {
//			colValues.add(orderDeclareHead.get("paySerialNo"));
//		} else {
//			colValues.add(null);
//		}
//
//		colNames.add("WORTH");
//		if (orderDeclareItems.get("tradeTotal") != null
//				&& !orderDeclareItems.get("tradeTotal").toString().isEmpty()) {
//			colValues.add(orderDeclareItems.get("tradeTotal"));
//		} else {
//			colValues.add(null);
//		}
//
//		colNames.add("NOTE");
//		colValues.add("");
//		
//		colNames.add("LOS_NO");
//		colValues.add(head.get("logisticsOrderId"));
//
//		colNames.add("CREAT_TIME");
//		colValues.add(new Date());
//
//		commonManagerMapper.insertTableByNVList("t_new_import_inventory",
//				colNames, colValues, primary);
//		
//		//插入t_new_import_inventory_detail表
//		colNames.clear();
//		colValues.clear();
//		Map primary_sub = new HashMap();
//		primary_sub.put("primaryId", null);
//
//		colNames.add("INVENTORY_ID");
//		colValues.add(primary.get("primaryId"));
//
//		colNames.add("GNUM");
//		colValues.add(orderDeclareItems.get("goodsOrder"));
//
//		colNames.add("ITEM_NO");
//		colValues.add(orderDeclareItems.get("goodsItemNo"));
//
//		colNames.add("ITEM_NAME");
//		colValues.add(orderDeclareItems.get("goodsName"));
//
//		colNames.add("G_CODE");
//		colValues.add("");
//
//		colNames.add("G_NAME");
//		colValues.add(orderDeclareItems.get("goodsName"));
//
//		colNames.add("G_MODEL");
//		colValues.add(orderDeclareItems.get("goodsModel"));
//
//		colNames.add("BARCODE");
//		colValues.add("");
//
//		colNames.add("COUNTRY");
//		colValues.add(orderDeclareItems.get("originCountry"));
//		
//		colNames.add("TRADE_COUNTRY");
//		colValues.add(orderDeclareHead.get("tradeCountry"));
//
//		colNames.add("CURRENCY");
//		colValues.add(orderDeclareItems.get("tradeCurr"));
//
//		colNames.add("QTY");
//		if (orderDeclareItems.get("declareCount") != null
//				&& !orderDeclareItems.get("declareCount").toString()
//						.isEmpty()) {
//			colValues.add(orderDeclareItems.get("declareCount"));
//		} else {
//			colValues.add(null);
//		}
//
//		colNames.add("QTY1");
//		if (orderDeclareItems.get("goodsGrossWeight") != null
//				&& !orderDeclareItems.get("goodsGrossWeight").toString()
//						.isEmpty()
//				&& orderDeclareItems.get("declareCount") != null
//				&& !orderDeclareItems.get("declareCount").toString().isEmpty()) {
//			colValues.add(Double.parseDouble(orderDeclareItems.get(
//					"goodsGrossWeight").toString())
//					* Double.parseDouble(orderDeclareItems.get("declareCount")
//							.toString()));
//		} else {
//			colValues.add(null);
//		}
//
//		colNames.add("QTY2");
//		if (orderDeclareItems.get("secondCount") != null
//				&& !orderDeclareItems.get("secondCount").toString()
//						.isEmpty()) {
//			colValues.add(orderDeclareItems.get("secondCount"));
//		} else {
//			colValues.add(null);
//		}
//
//		colNames.add("UNIT");
//		colValues.add(orderDeclareItems.get("goodsUnit"));
//
//		colNames.add("UNIT1");
//		colValues.add(orderDeclareItems.get("firstUnit"));
//
//		colNames.add("UNIT2");
//		colValues.add(orderDeclareItems.get("secondUnit"));
//
//		colNames.add("PRICE");
//		if (orderDeclareItems.get("declPrice") != null
//				&& !orderDeclareItems.get("declPrice").toString().isEmpty()) {
//			colValues.add(orderDeclareItems.get("declPrice"));
//		} else {
//			colValues.add(null);
//		}
//
//		colNames.add("TOTAL_PRICE");
//		if (orderDeclareItems.get("declPrice") != null
//				&& !orderDeclareItems.get("declPrice").toString()
//						.isEmpty()
//				&& orderDeclareItems.get("declareCount") != null
//				&& !orderDeclareItems.get("declareCount").toString().isEmpty()) {
//			colValues.add(Double.parseDouble(orderDeclareItems.get(
//					"declPrice").toString())
//					* Double.parseDouble(orderDeclareItems.get("declareCount")
//							.toString()));
//		} else {
//			colValues.add(null);
//		}
////		if (orderDeclareItems.get("declTotalPrice") != null
////				&& !orderDeclareItems.get("declTotalPrice").toString()
////						.isEmpty()) {
////			colValues.add(orderDeclareItems.get("declTotalPrice"));
////		} else {
////			colValues.add(null);
////		}
//
//		colNames.add("CREAT_TIME");
//		colValues.add(new Date());
//
//		commonManagerMapper.insertTableByNVList(
//				"t_new_import_inventory_detail", colNames, colValues,
//				primary_sub);
//
//		return primary.get("primaryId").toString();
//	}
	
	
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
		inventory.put("EBC_CODE", "3201966A69");
		inventory.put("EBC_NAME", "江苏苏宁易购电子商务有限公司");
		inventory.put("EBP_CODE", "3201966A69");
		inventory.put("EBP_NAME", "江苏苏宁易购电子商务有限公司");
		inventory.put("ORDER_NO", head.get("btcOrderId"));
		inventory.put("LOGISTICS_NO", orderExpBill.get("expressCompanyExcode"));
		inventory.put("LOGISTICS_CODE", "3201961A28");
		inventory.put("LOGISTICS_NAME", "江苏苏宁物流有限公司");
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

	public static void main(String arg[]) {

//		String logistics_interface = "<LoadHead><loadContents><loadContent><loadContentId>1</loadContentId><outorderId>6666666666</outorderId></loadContent><loadContent><loadContentId>2</loadContentId><outorderId>7777777777</outorderId></loadContent></loadContents><loadHeadId>12</loadHeadId><loadId>1736474588</loadId><total>2</total><tracyNum>3</tracyNum><TotalWeight>2.5</TotalWeight><CarEcNo>苏A234234</CarEcNo></LoadHead>";
//
//		String data_digest = CommonUtil.makeSign(logistics_interface);
//
//		System.out.println(data_digest);
//		try {
//			System.out
//					.println("logistics_interface="
//							+ URLEncoder.encode(logistics_interface, "utf-8")
//							+ "&data_digest="
//							+ URLEncoder.encode(data_digest, "utf-8"));
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		try {
//			System.out.println(URLEncoder.encode("helloworld", "utf-8"));
//			System.out.println(URLEncoder.encode("voQc3u6+f6pSflMPdw4ySQ==",
//					"utf-8"));
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("RECORD_NO", "335");
		map1.put("CREAT_TIME", "2018-09-14 21:46:17");
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("RECORD_NO", "145");
		map2.put("CREAT_TIME", "2018-09-14 23:46:17");
		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("RECORD_NO", "285");
		map3.put("CREAT_TIME", "2018-09-14 22:46:17");
		Map<String, Object> map4 = new HashMap<String, Object>();
		map4.put("RECORD_NO", "265");
		map4.put("CREAT_TIME", "2018-09-14 22:46:17");
		list.add(map1);
		list.add(map2);
		list.add(map3);
		list.add(map4);
		// 排序前
		for (Map<String, Object> map : list) {
			System.out.println(map);
		}

		Collections.sort(list, new Comparator<Map>() {
			public int compare(Map o1, Map o2) {
//				double qty1 = Double.valueOf(o1.get("QTY")
//						.toString());
//				double qty2 = Double.valueOf(o2.get("QTY")
//						.toString());
				String recordNo1 = o1.get("RECORD_NO")!=null?o1.get("RECORD_NO").toString():"";
				String recordNo2 = o2.get("RECORD_NO")!=null?o2.get("RECORD_NO").toString():"";
//				if (qty1 > qty2) {
					if(recordNo1.compareTo(recordNo2)<0){
						return 0;
					}else{
						return 1;
					}
//				} else {
//					return 0;
//				}
			}
		});

		System.out.println("-------------------");
		for (Map<String, Object> map : list) {
			System.out.println(map);
		}
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

		String response = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soapenv:Envelope xmlns:soapenv=\"http://www.w3.org/2003/05/soap-envelope\"><soapenv:Body><ns:sendOrderResponse xmlns:ns=\"http://ws.com\"><ns:return><?xml version=\"1.0\" encoding=\"UTF-8\"?><DATA><ORDER><ORDER_CODE>W100133410</ORDER_CODE><CD>OK</CD><INFO>11811000073</INFO></ORDER></DATA></ns:return></ns:sendOrderResponse></soapenv:Body></soapenv:Envelope>";

		String xxxx = StringEscapeUtils.escapeXml(response);

//		String returnXmlData = XmlUtil
//				.getResponseFromXmlString_CJ(xxxx);

		String returnXmlData = XmlUtil.getTotalMidValue(response,"<ns:return>","</ns:return>");

		Map orderResult = XmlUtil.parseXmlFPAPI_SingleNodes(returnXmlData, "//DATA/ORDER/child::*");

//		System.out.println(xxxx);

		System.out.println(orderResult);

	}

}
