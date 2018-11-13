package com.foo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.map.LinkedMap;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.foo.common.CommonDefine;
import com.foo.common.CommonException;
import com.foo.model.suningModel.SnErrorModel;
import com.foo.model.suningModel.SnHeadModel;
import com.foo.model.suningModel.SnReponseContentModel;
import com.foo.model.suningModel.LogisticsCrossbuyTask.AddLogisticsTaskStatusResponse;
import com.foo.model.suningModel.LogisticsCrossbuyTask.QueryLogisticsCrossbuyTask;

/**
 * @author xuxiaojun
 *
 */
public class XmlUtil {
	
	private static String nameSpace = "ceb";
	private static String nameSpace4NJ = "soap";
	private static String nameSpace_soap = "http://www.w3.org/2003/05/soap-envelope";
	private static String nameSpace_xsi = "http://www.w3.org/2001/XMLSchema-instance";
	private static String nameSpace_xsd = "http://www.w3.org/2001/XMLSchema";
	private static String nameSpace_ = "http://tempuri.org/";
	private static String nameSpace_1 = "http://ws.com";
	private static String nameSpace4TJ = "http://www.chinaport.gov.cn/ENT";

	/**
	 * 生成xml文件
	 * @param data
	 * @param type 生成xml报文类型
	 * @return
	 * @throws CommonException
	 */
	public static File generalXml(Map data, List<Map> subDataList, int type){
		//生成文件
		File file = null;
		//消息类型例CEB201
		String messageType = "";
		//文件名
		String fileName = "";
		
		String rootPrifix = nameSpace + ":";
		String rootElementName = "";
		String subRootElementName = "";

		switch(type){
		//CEB201商品备案数据
		case CommonDefine.CEB201:
			messageType = CommonDefine.MESSAGE_TYPE_CEB201;
			fileName = data.get("GUID").toString();
			rootElementName = rootPrifix +"PgrHead";
			//APP_STATUS写死为1，暂存
			if(data.containsKey("APP_STATUS")){
				data.put("APP_STATUS", CommonDefine.APP_STATUS_STORE);
			}
			break;
		//CEB202商品备案回执
		case CommonDefine.CEB202:
			break;
		//CEB301电子订单数据
		case CommonDefine.CEB301:
			messageType = CommonDefine.MESSAGE_TYPE_CEB301;
			fileName = data.get("GUID").toString();
			rootElementName = rootPrifix +"OrderHead";
			subRootElementName = rootPrifix+"OrderList";
			//APP_STATUS写死为2，暂存
			if(data.containsKey("APP_STATUS")){
				data.put("APP_STATUS", CommonDefine.APP_STATUS_UPLOAD);
			}
			break;
		//CEB302电子订单回执
		case CommonDefine.CEB302:
			break;
		//CEB401支付凭证数据
		case CommonDefine.CEB401:
			break;
		//CEB402支付凭证回执
		case CommonDefine.CEB402:
			break;
		//CEB501物流运单数据
		case CommonDefine.CEB501:
			messageType = CommonDefine.MESSAGE_TYPE_CEB501;
			fileName = data.get("GUID").toString();
			rootElementName = rootPrifix +"LogisticsHead";
			//APP_STATUS写死为2，暂存
			if(data.containsKey("APP_STATUS")){
				data.put("APP_STATUS", CommonDefine.APP_STATUS_UPLOAD);
			}
			break;
		//CEB511物流运单数据
		case CommonDefine.CEB511:
			messageType = CommonDefine.MESSAGE_TYPE_CEB511;
			fileName = data.get("GUID").toString();
			rootElementName = rootPrifix +"Logistics"+"_"+rootPrifix +"LogisticsHead";
			subRootElementName = rootPrifix +"BaseTransfer";
			//APP_STATUS写死为2，暂存
			if(data.containsKey("APP_STATUS")){
				data.put("APP_STATUS", CommonDefine.APP_STATUS_UPLOAD);
			}
			break;
		//CEB502物流运单回执
		case CommonDefine.CEB502:
			break;
		//CEB503物流运单状态数据
		case CommonDefine.CEB503:
			messageType = CommonDefine.MESSAGE_TYPE_CEB503;
			fileName = data.get("GUID").toString();
			rootElementName = rootPrifix +"LogisticsStatus";
			//APP_STATUS写死为2，暂存
			if(data.containsKey("APP_STATUS")){
				data.put("APP_STATUS", CommonDefine.APP_STATUS_UPLOAD);
			}
			break;
		//CEB513物流运单状态数据
		case CommonDefine.CEB513:
			messageType = CommonDefine.MESSAGE_TYPE_CEB513;
			fileName = data.get("GUID").toString();
			rootElementName = rootPrifix +"LogisticsStatus";
			subRootElementName = rootPrifix +"BaseTransfer";
			//APP_STATUS写死为2，暂存
			if(data.containsKey("APP_STATUS")){
				data.put("APP_STATUS", CommonDefine.APP_STATUS_UPLOAD);
			}
			break;
		//CEB504物流运单状态回执
		case CommonDefine.CEB504:
			break;
		//CEB601出境清单数据
		case CommonDefine.CEB601:
			messageType = CommonDefine.MESSAGE_TYPE_CEB601;
			fileName = data.get("GUID").toString();
			rootElementName = rootPrifix +"InventoryHead";
			subRootElementName = rootPrifix+"InventoryList";
			//APP_STATUS写死为1，暂存
			if(data.containsKey("APP_STATUS")){
				data.put("APP_STATUS", CommonDefine.APP_STATUS_STORE);
			}
			break;
		//CEB602出境清单回执
		case CommonDefine.CEB602:
			break;
			
		//SNT101
		case CommonDefine.SNT101:
			break;
		//SNT102
		case CommonDefine.SNT102:
			break;
		//SNT103
		case CommonDefine.SNT103:
			break;
		//SNT201
		case CommonDefine.SNT201:
			break;
			
		}
		//生成文件
		file = generalXmlImpl(data, subDataList, messageType,fileName, rootElementName, subRootElementName);
		
		return file;
	}
	
	
	/**
	 * 生成报文
	 * @param data
	 * @param messageType 报文类型 例：CEB201
	 * @param rootElementName 根元素名称
	 * @param fileName 文件名
	 * @param excludeList data中不需要的元素
	 * @return
	 * @throws CommonException
	 */
	private static File generalXmlImpl(Map data, List<Map> subDataList,
			String messageType, String fileName, String rootElementName,
			String subRootElementName) {
		FileOutputStream fos = null;
		File xmlfile = null;

		// 获取资源文件
		ResourceBundle bundle = CommonUtil
				.getMessageMappingResource("CEB");
		
		if(messageType.equals(CommonDefine.MESSAGE_TYPE_CEB511)
				||messageType.equals(CommonDefine.MESSAGE_TYPE_CEB513)){
			bundle = CommonUtil
					.getMessageMappingResource("CEB_IMPORT");
		}
		try {
			String filePath = System.getProperty("java.io.tmpdir") + "/"
					+ fileName + ".xml";
			xmlfile = new File(filePath);
			if (!xmlfile.exists()) {
				xmlfile.createNewFile();
			}
			fos = new FileOutputStream(xmlfile);
			OutputFormat format = OutputFormat.createPrettyPrint();
			// 设置编码格式
			format.setEncoding("UTF-8");
			XMLWriter writer = new XMLWriter(fos, format);
			Document doc = DocumentHelper.createDocument();
			// 添加根元素
			Element rootElement = DocumentHelper.createElement(nameSpace + ":"
					+ messageType + "Message");
			//添加属性
			if(messageType.equals(CommonDefine.MESSAGE_TYPE_CEB511)){
				rootElement.addAttribute("guid",
						data.get("GUID").toString());
				rootElement.addAttribute("version",
						"1.0");
			}else{
				rootElement.addNamespace("xsi",
						nameSpace_xsi);
				rootElement.addAttribute("xsi:schemaLocation",
						"http://www.chinaport.gov.cn/ceb");
			}			
			rootElement.addNamespace("ceb", "http://www.chinaport.gov.cn/ceb");
			
			
			doc.setRootElement(rootElement);
			
			String[] rootElementArray = rootElementName.split("_");
			
			// 设置第一级元素
			Element temp = rootElement;
			for(String name:rootElementArray){
				temp = temp.addElement(name);
			}
			Element firstElement = temp;
			// 第二级元素
			Element secondElement;

			String keyString;
			for (Object key : data.keySet()) {
				keyString = key.toString();
				if (!bundle.containsKey(keyString)) {
					continue;
				}
				// 获取映射字段
				secondElement = firstElement.addElement(nameSpace + ":"
						+ bundle.getString(keyString));
				secondElement.addText(data.get(key) == null ? "" : data
						.get(key).toString());
			}
			// 加入子元素
			if (subDataList != null) {

				for (Map subData : subDataList) {

					Element subRootElement = rootElement
							.addElement(subRootElementName);

					for (Object key : subData.keySet()) {
						keyString = key.toString();
						if (!bundle.containsKey(keyString)) {
							continue;
						}
						// 获取映射字段
						Element subSecondElement = subRootElement
								.addElement(nameSpace + ":"
										+ bundle.getString(keyString));
						subSecondElement.addText(subData.get(key) == null ? ""
								: subData.get(key).toString());
					}
				}
			}
			writer.write(doc);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return xmlfile;
	}
	
	/**
	 * 生成xml 字符创
	 * @param data
	 * @param type 生成xml报文类型
	 * @return
	 * @throws CommonException
	 */
	public static String generalRequestXml4NJ(Map head, Map data, List<Map> subDataList, int type){
		//消息类型例CEB201
		String messageType = "";
		String nodeName = generalXmlNodeName4NJ(type);
		//文件名
		String fileName = "";
		
		String rootPrefix = "";
		String headElementName = rootPrefix +"MessageHead";
		String rootElementName = "";
		String subRootElementName = "";
		String subSubRootElementName = "";

		switch(type){
		//CEB201商品备案数据
		case CommonDefine.CEB201:
			messageType = CommonDefine.MESSAGE_TYPE_CEB201;

			rootElementName = rootPrefix +"NjkjPgrHeadEntity";
			//APP_STATUS写死为1，暂存
			if(data.containsKey("APP_STATUS")){
				data.put("APP_STATUS", CommonDefine.APP_STATUS_STORE);
			}
			break;
		//CEB202商品备案回执
		case CommonDefine.CEB202:
			messageType = CommonDefine.MESSAGE_TYPE_CEB202;
			
			rootElementName = rootPrefix +"NjkjPgrHeadEntity";
			//APP_STATUS写死为1，暂存
			if(data.containsKey("APP_STATUS")){
				data.put("APP_STATUS", CommonDefine.APP_STATUS_STORE);
			}
			break;
		//CEB202商品备案回执
		case CommonDefine.CEB203:
			messageType = CommonDefine.MESSAGE_TYPE_CEB203;
			
			rootElementName = rootPrefix +"NjkjPgrHeadEntity";
			//APP_STATUS写死为1，暂存
			if(data.containsKey("APP_STATUS")){
				data.put("APP_STATUS", CommonDefine.APP_STATUS_STORE);
			}
			break;
		//CEB301电子订单数据
		case CommonDefine.CEB301:
			messageType = CommonDefine.MESSAGE_TYPE_CEB301;
			
			rootElementName = rootPrefix +"NjkjOrderHeadEntity";
			subRootElementName = rootPrefix+"NjkjOrderListEntityList";
			subSubRootElementName = rootPrefix+"NjkjOrderListEntity";
			//APP_STATUS写死为1，暂存
			if(data.containsKey("APP_STATUS")){
				data.put("APP_STATUS", CommonDefine.APP_STATUS_UPLOAD);
			}
			break;
		//CEB302电子订单回执
		case CommonDefine.CEB302:
			break;
			//CEB301电子订单数据
		case CommonDefine.CEB303:
			messageType = CommonDefine.MESSAGE_TYPE_CEB303;
			
			rootElementName = rootPrefix +"NjkjOrderHeadEntity";
//			subRootElementName = rootPrefix+"NjkjOrderListEntityList";
			//APP_STATUS写死为1，暂存
			if(data.containsKey("APP_STATUS")){
				data.put("APP_STATUS", CommonDefine.APP_STATUS_UPLOAD);
			}
			break;
		//CEB401支付凭证数据
		case CommonDefine.CEB401:
			messageType = CommonDefine.MESSAGE_TYPE_CEB401;
			
			rootElementName = rootPrefix +"NjkjPaymentHeadEntity";
//			subRootElementName = rootPrefix+"NjkjOrderListEntityList";
			//APP_STATUS写死为1，暂存
			if(data.containsKey("APP_STATUS")){
				data.put("APP_STATUS", CommonDefine.APP_STATUS_UPLOAD);
			}
			break;
		//CEB402支付凭证回执
		case CommonDefine.CEB402:
			break;
		//CEB501物流运单数据
		case CommonDefine.CEB501:
			messageType = CommonDefine.MESSAGE_TYPE_CEB501;
			rootElementName = rootPrefix +"NjkjLogisticsHeadEntity";
			subRootElementName = rootPrefix+"NjkjLogisticsLineEntityList";
			subSubRootElementName = rootPrefix+"NjkjLogisticsLineEntity";
			//APP_STATUS写死为2，暂存
			if(data.containsKey("APP_STATUS")){
				data.put("APP_STATUS", CommonDefine.APP_STATUS_UPLOAD);
			}
			break;
		//CEB502物流运单回执
		case CommonDefine.CEB502:
			messageType = CommonDefine.MESSAGE_TYPE_CEB502;
			rootElementName = rootPrefix +"NjkjLogisticsHeadEntity";
			break;
		//CEB503物流运单状态数据
		case CommonDefine.CEB503:
			messageType = CommonDefine.MESSAGE_TYPE_CEB503;
			rootElementName = rootPrefix +"NjkjLogisticsStatusEntity";
			break;
		//CEB504物流运单状态回执
		case CommonDefine.CEB504:
			break;
		//CEB601出境清单数据
		case CommonDefine.CEB601:
			messageType = "CEB"+head.get("MESSAGE_TYPE").toString();
//			messageType = CommonDefine.MESSAGE_TYPE_CEB601;
			//一般进口
			if(CommonDefine.MESSAGE_TYPE_CEB601.equals(messageType)){
				rootElementName = rootPrefix +"NjkjNiInventoryHeadEntity";
				subRootElementName = rootPrefix+"NjkjNiInventoryListEntityList";
				subSubRootElementName = rootPrefix+"NjkjNiInventoryListEntity";
			}
			//一般出口
			if(CommonDefine.MESSAGE_TYPE_CEB607.equals(messageType)){
				rootElementName = rootPrefix +"NjkjNeInventoryHeadEntity";
				subRootElementName = rootPrefix+"NjkjNeInventoryListEntityList";
				subSubRootElementName = rootPrefix+"NjkjNeInventoryListEntity";
			}
			//保税进口
			if(CommonDefine.MESSAGE_TYPE_CEB604.equals(messageType)){
				rootElementName = rootPrefix +"NjkjBiInventoryHeadEntity";
				subRootElementName = rootPrefix+"NjkjBiInventoryListEntityList";
				subSubRootElementName = rootPrefix+"NjkjBiInventoryListEntity";
			}
			//保税出口
			if(CommonDefine.MESSAGE_TYPE_CEB610.equals(messageType)){
				rootElementName = rootPrefix +"NjkjBeInventoryHeadEntity";
				subRootElementName = rootPrefix+"NjkjBeInventoryListEntityList";
				subSubRootElementName = rootPrefix+"NjkjBeInventoryListEntity";
			}
			
			//APP_STATUS写死为1，暂存
			if(data.containsKey("APP_STATUS")){
				data.put("APP_STATUS", CommonDefine.APP_STATUS_STORE);
			}
			break;
		//CEB602出境清单回执
		case CommonDefine.CEB602:
			messageType = CommonDefine.MESSAGE_TYPE_CEB602;
			rootElementName = rootPrefix +"NjkjNiInventoryHeadEntity";
			//APP_STATUS写死为1，暂存
			if(data.containsKey("APP_STATUS")){
				data.put("APP_STATUS", CommonDefine.APP_STATUS_STORE);
			}
			break;
		case CommonDefine.CEB603:
			messageType = "CEB"+head.get("MESSAGE_TYPE").toString();
//			messageType = CommonDefine.MESSAGE_TYPE_CEB603;
			//一般进口
			if(CommonDefine.MESSAGE_TYPE_CEB603.equals(messageType)){
				rootElementName = rootPrefix +"NjkjNiInventoryHeadEntity";
			}
			//一般出口
			if(CommonDefine.MESSAGE_TYPE_CEB609.equals(messageType)){
				rootElementName = rootPrefix +"NjkjNeInventoryHeadEntity";
			}
			//保税进口
			if(CommonDefine.MESSAGE_TYPE_CEB606.equals(messageType)){
				rootElementName = rootPrefix +"NjkjBiInventoryHeadEntity";
			}
			//保税出口
			if(CommonDefine.MESSAGE_TYPE_CEB612.equals(messageType)){
				rootElementName = rootPrefix +"NjkjBeInventoryHeadEntity";
			}
			//APP_STATUS写死为1，暂存
			if(data.containsKey("APP_STATUS")){
				data.put("APP_STATUS", CommonDefine.APP_STATUS_STORE);
			}
			break;			
			
		//SNT101
		case CommonDefine.SNT101:
			break;
		//SNT102
		case CommonDefine.SNT102:
			break;
		//SNT103
		case CommonDefine.SNT103:
			break;
		//SNT201
		case CommonDefine.SNT201:
			break;
			
		}
		fileName = head.get("MESSAGE_ID").toString();
		//生成文件
		String resultXmlString = generalRequestXml4NJImpl(
				head, data, subDataList, messageType,
				nodeName, fileName, headElementName, rootElementName,
				subRootElementName,subSubRootElementName);
		
		return resultXmlString;
	}
	
	private static String generalRequestXml4NJImpl(Map head,Map data, List<Map> subDataList,
			String messageType, String nodeName, String fileName, String headElementName, String rootElementName,
			String subRootElementName,String subSubRootElementName){

		String resultXml = "";
		
		FileOutputStream fos = null;
		File xmlfile = null;

		try {
			String filePath = System.getProperty("java.io.tmpdir") + "/"
					+ fileName + ".xml";
			xmlfile = new File(filePath);
			if (!xmlfile.exists()) {
				xmlfile.createNewFile();
			}
			fos = new FileOutputStream(xmlfile);
			OutputFormat format = OutputFormat.createPrettyPrint();
			// 设置编码格式
			format.setEncoding("UTF-8");
			XMLWriter writer = new XMLWriter(fos, format);
			Document doc = DocumentHelper.createDocument();
			// 添加根元素
			Element rootElement = DocumentHelper.createElement(nameSpace4NJ+":"+"Envelope");
			rootElement.addNamespace("xsi",
					nameSpace_xsi);
			rootElement.addNamespace("xsd",
					nameSpace_xsd);
			rootElement.addNamespace("soap",
					nameSpace_soap);
			doc.setRootElement(rootElement);
			// 设置第一级元素
			Element firstElement = rootElement.addElement(nameSpace4NJ+":"+"Body");
			// 第二级元素
			Element secondElement = firstElement.addElement(nodeName);
			secondElement.addNamespace("", nameSpace_);
			Element xmlElement = secondElement.addElement("xml");
			
			//添加报文字符串
			xmlElement.addText(generalXmlString4NJ_data(head,data, subDataList,
					messageType,  headElementName, rootElementName,
					subRootElementName,subSubRootElementName));
			Element fileTypeElement = secondElement.addElement("fileType");
			fileTypeElement.addText(messageType.substring(3));

			writer.write(doc);
			
//			//上传文件
//			FtpUtils ftpUtil = FtpUtils.getDefaultFtp();
//
//			String generalXmlFilePath = ConfigUtil
//					.getFileLocationPath(ftpFilePathFlag)
//					.get("GENERAL_XML").toString();
//
//			boolean result = ftpUtil.uploadFile(xmlfile.getPath(),
//					generalXmlFilePath, prefix+xmlfile.getName());
//
//			if (result) {
//				xmlfile.delete();
//			}
			//返回xml字符串
			resultXml = doc.asXML();
			
			resultXml = resultXml.replaceAll(" xmlns=\"\"", "");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultXml;
	}
	
	//生成回执xml
	public static String generalReceiptXml4NJ(int type, Map<String,String> leafNodes){

		String nodeName = generalXmlNodeName4NJ(type);
		
		String resultXml = "";

		try {
			Document doc = DocumentHelper.createDocument();
			// 添加根元素
			Element rootElement = DocumentHelper.createElement(nameSpace4NJ+":"+"Envelope");
			rootElement.addNamespace("xsi",
					nameSpace_xsi);
			rootElement.addNamespace("xsd",
					nameSpace_xsd);
			rootElement.addNamespace("soap",
					nameSpace_soap);
			doc.setRootElement(rootElement);
			// 设置第一级元素
			Element firstElement = rootElement.addElement(nameSpace4NJ+":"+"Body");
			// 第二级元素
			Element secondElement = firstElement.addElement(nodeName);
			secondElement.addNamespace("", nameSpace_);
			
			for(String elementName:leafNodes.keySet()){
				Element leaf = secondElement.addElement(elementName);
				leaf.addText(leafNodes.get(elementName));
			}
			//返回xml字符串
			resultXml = doc.asXML();
			
			resultXml = resultXml.replaceAll(" xmlns=\"\"", "");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return resultXml;
	}
	
	
	/**
	 * 生成报文
	 * @param data
	 * @param messageType 报文类型 例：CEB201
	 * @param rootElementName 根元素名称
	 * @param fileName 文件名
	 * @param excludeList data中不需要的元素
	 * @return
	 * @throws CommonException
	 */
	private static String generalXmlString4NJ_data(Map head,Map data, List<Map> subDataList,
			String messageType, String headElementName, String rootElementName,
			String subRootElementName,String subSubRootElementName) {
		String xmlString = "";
		// 获取资源文件
		ResourceBundle bundle = CommonUtil
				.getMessageMappingResource("CEB_NJ");
		Document doc = DocumentHelper.createDocument();
		// 添加根元素CEB501Message
		Element rootElement = DocumentHelper.createElement(messageType+"Message");
		doc.setRootElement(rootElement);
		
		//设置消息头MessageHead
		Element headElement = rootElement.addElement(headElementName);
		// 设置第一级元素NjkjLogisticsHeadEntity
		Element firstElement = rootElement.addElement(rootElementName);
		// 第二级元素
		Element secondElement;

		String keyString;
		//设置消息头
		for (Object key : head.keySet()) {
			keyString = key.toString();
			if (!bundle.containsKey(keyString)) {
				continue;
			}
			// 获取映射字段
			secondElement = headElement.addElement(bundle.getString(keyString));
			secondElement.addText(head.get(key) == null ? "" : head
					.get(key).toString());
		}

		//设置一级消息体
		for (Object key : data.keySet()) {
			keyString = key.toString();
			if (!bundle.containsKey(keyString)) {
				continue;
			}
			// 获取映射字段
			secondElement = firstElement.addElement(bundle.getString(keyString));
			secondElement.addText(data.get(key) == null ? "" : data
					.get(key).toString());
		}
		// 加入子元素
		if (subDataList != null) {
			// 设置第一级元素NjkjLogisticsLineEntityList
			Element subRootElement = rootElement.addElement(subRootElementName);
			
			for (Map subData : subDataList) {

				Element subSubRootElement = subRootElement
						.addElement(subSubRootElementName);

				for (Object key : subData.keySet()) {
					keyString = key.toString();
					if (!bundle.containsKey(keyString)) {
						continue;
					}
					// 获取映射字段
					Element subSecondElement = subSubRootElement
							.addElement(bundle.getString(keyString));
					subSecondElement.addText(subData.get(key) == null ? ""
							: subData.get(key).toString());
				}
			}
		}
		xmlString = doc.asXML();
		//去除xml头文件
		xmlString = xmlString.split("\n")[1];
		
		System.out.println("request xml body:"+xmlString);
		
		return xmlString;
	}
	
	
	
	
	/**
	 * 生成xml 字符创
	 * @param data
	 * @param type 生成xml报文类型
	 * @return
	 * @throws CommonException
	 */
	public static String generalRequestXml4IMPORT(Map head, Map data, List<Map> subDataList, int type){
		//消息类型例CEB201
		String messageType = "";
		String nodeName = generalXmlNodeName4IMPORT(type);
		//文件名
		String fileName = "";
		
		String rootPrefix = "";
		String headElementName = rootPrefix +"MessageHead";
		String rootElementName = "";
		String subRootElementName = "";
		String subSubRootElementName = "";

		switch(type){
		//CEB301电子订单数据
		case CommonDefine.CEB301:
			messageType = CommonDefine.MESSAGE_TYPE_CEB301;
			
			rootElementName = rootPrefix +"NjkjOrderHeadEntity";
			subRootElementName = rootPrefix+"NjkjOrderListEntityList";
			subSubRootElementName = rootPrefix+"NjkjOrderListEntity";
			//APP_STATUS写死为1，暂存
			if(data.containsKey("APP_STATUS")){
				data.put("APP_STATUS", CommonDefine.APP_STATUS_UPLOAD);
			}
			break;
		//CEB302电子订单回执
		case CommonDefine.CEB302:
			break;
			//CEB301电子订单数据
		case CommonDefine.CEB303:
			messageType = CommonDefine.MESSAGE_TYPE_CEB303;
			
			rootElementName = rootPrefix +"NjkjOrderHeadEntity";
//			subRootElementName = rootPrefix+"NjkjOrderListEntityList";
			//APP_STATUS写死为1，暂存
			if(data.containsKey("APP_STATUS")){
				data.put("APP_STATUS", CommonDefine.APP_STATUS_UPLOAD);
			}
			break;
		//CEB401支付凭证数据
		case CommonDefine.CEB401:
			messageType = CommonDefine.MESSAGE_TYPE_CEB401;
			
			rootElementName = rootPrefix +"NjkjPaymentHeadEntity";
//			subRootElementName = rootPrefix+"NjkjOrderListEntityList";
			//APP_STATUS写死为1，暂存
			if(data.containsKey("APP_STATUS")){
				data.put("APP_STATUS", CommonDefine.APP_STATUS_UPLOAD);
			}
			break;
		//CEB402支付凭证回执
		case CommonDefine.CEB402:
			break;
		//CEB501物流运单数据
		case CommonDefine.CEB501:
			messageType = CommonDefine.MESSAGE_TYPE_CEB501;
			rootElementName = rootPrefix +"NjkjLogisticsHeadEntity";
			subRootElementName = rootPrefix+"NjkjLogisticsLineEntityList";
			subSubRootElementName = rootPrefix+"NjkjLogisticsLineEntity";
			//APP_STATUS写死为2，暂存
			if(data.containsKey("APP_STATUS")){
				data.put("APP_STATUS", CommonDefine.APP_STATUS_UPLOAD);
			}
			break;
		//CEB502物流运单回执
		case CommonDefine.CEB502:
			messageType = CommonDefine.MESSAGE_TYPE_CEB502;
			rootElementName = rootPrefix +"NjkjLogisticsHeadEntity";
			break;
		//CEB503物流运单状态数据
		case CommonDefine.CEB503:
			messageType = CommonDefine.MESSAGE_TYPE_CEB503;
			rootElementName = rootPrefix +"NjkjLogisticsStatusEntity";
			break;
		//CEB504物流运单状态回执
		case CommonDefine.CEB504:
			break;
			
			
		//CEB511物流运单数据
		case CommonDefine.CEB511:
			headElementName = null;
			messageType = CommonDefine.MESSAGE_TYPE_CEB511;
			rootElementName = rootPrefix +"Logistics";
			subRootElementName = rootPrefix+"BaseTransfer";
			subSubRootElementName = null;
			//APP_STATUS写死为2，暂存
			if(data.containsKey("APP_STATUS")){
				data.put("APP_STATUS", CommonDefine.APP_STATUS_UPLOAD);
			}
			break;
		//CEB513物流运单状态数据
		case CommonDefine.CEB513:
			headElementName = null;
			messageType = CommonDefine.MESSAGE_TYPE_CEB513;
			rootElementName = rootPrefix +"LogisticsStatus";
			subRootElementName = rootPrefix+"BaseTransfer";
			subSubRootElementName = null;
			break;
			
		//CEB601出境清单数据
		case CommonDefine.CEB601:
			messageType = "CEB"+head.get("MESSAGE_TYPE").toString();
//			messageType = CommonDefine.MESSAGE_TYPE_CEB601;
			//一般进口
			if(CommonDefine.MESSAGE_TYPE_CEB601.equals(messageType)){
				rootElementName = rootPrefix +"NjkjNiInventoryHeadEntity";
				subRootElementName = rootPrefix+"NjkjNiInventoryListEntityList";
				subSubRootElementName = rootPrefix+"NjkjNiInventoryListEntity";
			}
			//一般出口
			if(CommonDefine.MESSAGE_TYPE_CEB607.equals(messageType)){
				rootElementName = rootPrefix +"NjkjNeInventoryHeadEntity";
				subRootElementName = rootPrefix+"NjkjNeInventoryListEntityList";
				subSubRootElementName = rootPrefix+"NjkjNeInventoryListEntity";
			}
			//保税进口
			if(CommonDefine.MESSAGE_TYPE_CEB604.equals(messageType)){
				rootElementName = rootPrefix +"NjkjBiInventoryHeadEntity";
				subRootElementName = rootPrefix+"NjkjBiInventoryListEntityList";
				subSubRootElementName = rootPrefix+"NjkjBiInventoryListEntity";
			}
			//保税出口
			if(CommonDefine.MESSAGE_TYPE_CEB610.equals(messageType)){
				rootElementName = rootPrefix +"NjkjBeInventoryHeadEntity";
				subRootElementName = rootPrefix+"NjkjBeInventoryListEntityList";
				subSubRootElementName = rootPrefix+"NjkjBeInventoryListEntity";
			}
			
			//APP_STATUS写死为1，暂存
			if(data.containsKey("APP_STATUS")){
				data.put("APP_STATUS", CommonDefine.APP_STATUS_STORE);
			}
			break;
		//CEB602出境清单回执
		case CommonDefine.CEB602:
			messageType = CommonDefine.MESSAGE_TYPE_CEB602;
			rootElementName = rootPrefix +"NjkjNiInventoryHeadEntity";
			//APP_STATUS写死为1，暂存
			if(data.containsKey("APP_STATUS")){
				data.put("APP_STATUS", CommonDefine.APP_STATUS_STORE);
			}
			break;
		case CommonDefine.CEB603:
			messageType = "CEB"+head.get("MESSAGE_TYPE").toString();
//			messageType = CommonDefine.MESSAGE_TYPE_CEB603;
			//一般进口
			if(CommonDefine.MESSAGE_TYPE_CEB603.equals(messageType)){
				rootElementName = rootPrefix +"NjkjNiInventoryHeadEntity";
			}
			//一般出口
			if(CommonDefine.MESSAGE_TYPE_CEB609.equals(messageType)){
				rootElementName = rootPrefix +"NjkjNeInventoryHeadEntity";
			}
			//保税进口
			if(CommonDefine.MESSAGE_TYPE_CEB606.equals(messageType)){
				rootElementName = rootPrefix +"NjkjBiInventoryHeadEntity";
			}
			//保税出口
			if(CommonDefine.MESSAGE_TYPE_CEB612.equals(messageType)){
				rootElementName = rootPrefix +"NjkjBeInventoryHeadEntity";
			}
			//APP_STATUS写死为1，暂存
			if(data.containsKey("APP_STATUS")){
				data.put("APP_STATUS", CommonDefine.APP_STATUS_STORE);
			}
			break;			
			
		//SNT101
		case CommonDefine.SNT101:
			break;
		//SNT102
		case CommonDefine.SNT102:
			break;
		//SNT103
		case CommonDefine.SNT103:
			break;
		//SNT201
		case CommonDefine.SNT201:
			break;
			
		}
		fileName = head.get("MESSAGE_ID").toString();
		//生成文件
		String resultXmlString = generalRequestXml4IMPORTImpl(
				head, data, subDataList, messageType,
				nodeName, fileName, headElementName, rootElementName,
				subRootElementName,subSubRootElementName);
		
		return resultXmlString;
	}
	
	private static String generalRequestXml4IMPORTImpl(Map head,Map data, List<Map> subDataList,
			String messageType, String nodeName, String fileName, String headElementName, String rootElementName,
			String subRootElementName,String subSubRootElementName){

		String resultXml = "";
		
		FileOutputStream fos = null;
		File xmlfile = null;

		try {
			String filePath = System.getProperty("java.io.tmpdir") + "/"
					+ fileName + ".xml";
			xmlfile = new File(filePath);
			if (!xmlfile.exists()) {
				xmlfile.createNewFile();
			}
			fos = new FileOutputStream(xmlfile);
			OutputFormat format = OutputFormat.createPrettyPrint();
			// 设置编码格式
			format.setEncoding("UTF-8");
			XMLWriter writer = new XMLWriter(fos, format);
			Document doc = DocumentHelper.createDocument();
			// 添加根元素
			Element rootElement = DocumentHelper.createElement(nameSpace4NJ+":"+"Envelope");
			rootElement.addNamespace("xsi",
					nameSpace_xsi);
			rootElement.addNamespace("xsd",
					nameSpace_xsd);
			rootElement.addNamespace("soap",
					nameSpace_soap);
			doc.setRootElement(rootElement);
			// 设置第一级元素
			Element firstElement = rootElement.addElement(nameSpace4NJ+":"+"Body");
			// 第二级元素
			Element secondElement = firstElement.addElement(nodeName);
			secondElement.addNamespace("", nameSpace_);
			Element xmlElement = secondElement.addElement("xml");
			
			//添加报文字符串
			xmlElement.addText(generalXmlString4IMPORT_data(head,data, subDataList,
					messageType,  headElementName, rootElementName,
					subRootElementName,subSubRootElementName));
			Element fileTypeElement = secondElement.addElement("fileType");
			fileTypeElement.addText(messageType.substring(3));

			writer.write(doc);
			
//			//上传文件
//			FtpUtils ftpUtil = FtpUtils.getDefaultFtp();
//
//			String generalXmlFilePath = ConfigUtil
//					.getFileLocationPath(ftpFilePathFlag)
//					.get("GENERAL_XML").toString();
//
//			boolean result = ftpUtil.uploadFile(xmlfile.getPath(),
//					generalXmlFilePath, prefix+xmlfile.getName());
//
//			if (result) {
//				xmlfile.delete();
//			}
			//返回xml字符串
			resultXml = doc.asXML();
			
			resultXml = resultXml.replaceAll(" xmlns=\"\"", "");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultXml;
	}
	
	
	/**
	 * 生成报文
	 * @param data
	 * @param messageType 报文类型 例：CEB201
	 * @param rootElementName 根元素名称
	 * @param fileName 文件名
	 * @param excludeList data中不需要的元素
	 * @return
	 * @throws CommonException
	 */
	private static String generalXmlString4IMPORT_data(Map head,Map data, List<Map> subDataList,
			String messageType, String headElementName, String rootElementName,
			String subRootElementName,String subSubRootElementName) {
		String xmlString = "";
		// 获取资源文件
		ResourceBundle bundle = CommonUtil
				.getMessageMappingResource("CEB_IMPORT");
		Document doc = DocumentHelper.createDocument();
		// 添加根元素CEB501Message
		Element rootElement = DocumentHelper.createElement(messageType+"Message");
		doc.setRootElement(rootElement);
		
		Element headElement = null;
		if(headElementName!=null){
			headElement = rootElement.addElement(headElementName);
		} 
		// 设置第一级元素NjkjLogisticsHeadEntity
		Element firstElement = rootElement.addElement(rootElementName);
		// 第二级元素
		Element secondElement;

		String keyString;
		if(headElementName!=null){
			//设置消息头
			for (Object key : head.keySet()) {
				keyString = key.toString();
				if (!bundle.containsKey(keyString)) {
					continue;
				}
				// 获取映射字段
				secondElement = headElement.addElement(bundle.getString(keyString));
				secondElement.addText(head.get(key) == null ? "" : head
						.get(key).toString());
			}
		}

		//设置一级消息体
		for (Object key : data.keySet()) {
			keyString = key.toString();
			if (!bundle.containsKey(keyString)) {
				continue;
			}
			// 获取映射字段
			secondElement = firstElement.addElement(bundle.getString(keyString));
			secondElement.addText(data.get(key) == null ? "" : data
					.get(key).toString());
		}
		// 加入子元素
		if (subDataList != null) {
			// 设置第一级元素NjkjLogisticsLineEntityList
			Element subRootElement = rootElement.addElement(subRootElementName);
			
			for (Map subData : subDataList) {
				
				if(subSubRootElementName!=null){

					Element subSubRootElement = subRootElement
							.addElement(subSubRootElementName);
	
					for (Object key : subData.keySet()) {
						keyString = key.toString();
						if (!bundle.containsKey(keyString)) {
							continue;
						}
						// 获取映射字段
						Element subSecondElement = subSubRootElement
								.addElement(bundle.getString(keyString));
						subSecondElement.addText(subData.get(key) == null ? ""
								: subData.get(key).toString());
					}
				}else{
					for (Object key : subData.keySet()) {
						keyString = key.toString();
						if (!bundle.containsKey(keyString)) {
							continue;
						}
						// 获取映射字段
						Element subSecondElement = subRootElement
								.addElement(bundle.getString(keyString));
						subSecondElement.addText(subData.get(key) == null ? ""
								: subData.get(key).toString());
					}
				}
			}
		}
		xmlString = doc.asXML();
		//去除xml头文件
		xmlString = xmlString.split("\n")[1];
		
		System.out.println("request xml body:"+xmlString);
		
		return xmlString;
	}
	
	// 生成回执xml
	public static String generalRequestXml4TJ_621(Map InventoryHead,
			Map InventoryList, Map IODeclContainerList,
			Map IODeclOrderRelationList, Map BaseTransfer,ResourceBundle bundle) {

		String resultXml = "";
		
		FileOutputStream fos = null;
		File xmlfile = null;

		try {
			String filePath = System.getProperty("java.io.tmpdir") + "/"
					+ InventoryHead.get("guid") + ".xml";
			xmlfile = new File(filePath);
			if (!xmlfile.exists()) {
				xmlfile.createNewFile();
			}
			fos = new FileOutputStream(xmlfile);
			OutputFormat format = OutputFormat.createPrettyPrint();
			// 设置编码格式
			format.setEncoding("UTF-8");
			XMLWriter writer = new XMLWriter(fos, format);
			Document doc = DocumentHelper.createDocument();
			// 添加根元素
			Element rootElement = doc.addElement(
					bundle.getString("elementName") != null ? bundle
							.getString("elementName") : "ENT621Message");
//			rootElement.addAttribute(
//					"reciptCode",
//					bundle.getString("reciptCode") != null ? bundle
//							.getString("reciptCode") : "");
//			rootElement.addAttribute(
//					"sendCode",
//					bundle.getString("sendCode") != null ? bundle
//							.getString("sendCode") : "");
//			rootElement.addAttribute(
//					"version",
//					bundle.getString("version") != null ? bundle
//							.getString("version") : "");
			rootElement.addAttribute("guid", InventoryHead.get("guid").toString());
			// 设置第一级元素
			Element firstElement_1 = rootElement.addElement("Inventory");
			// 第二级元素
			//InventoryHead
			Element secondElement_1 = firstElement_1.addElement("InventoryHead");
			
			for(Object obj:InventoryHead.keySet()){
				String elementName = (String)obj;
				Element leaf = secondElement_1.addElement(elementName);
				leaf.addText(InventoryHead.get(elementName) != null?InventoryHead.get(elementName).toString():"");
			}
			//InventoryList
			Element secondElement_2 = firstElement_1.addElement("InventoryList");
			for(Object obj:InventoryList.keySet()){
				String elementName = (String)obj;
				Element leaf = secondElement_2.addElement(elementName);
				leaf.addText(InventoryList.get(elementName) != null?InventoryList.get(elementName).toString():"");
			}
			//IODeclContainerList
			Element secondElement_3 = firstElement_1.addElement("IODeclContainerList");
			for(Object obj:IODeclContainerList.keySet()){
				String elementName = (String)obj;
				Element leaf = secondElement_3.addElement(elementName);
				leaf.addText(IODeclContainerList.get(elementName) != null?IODeclContainerList.get(elementName).toString():"");
			}
			//IODeclOrderRelationList
			Element secondElement_4 = firstElement_1.addElement("IODeclOrderRelationList");
			for(Object obj:IODeclOrderRelationList.keySet()){
				String elementName = (String)obj;
				Element leaf = secondElement_4.addElement(elementName);
				leaf.addText(IODeclOrderRelationList.get(elementName) != null?IODeclOrderRelationList.get(elementName).toString():"");
			}
			//设置第一级元素
			//BaseTransfer
			Element firstElement_2 = rootElement.addElement("BaseTransfer");
			for(Object obj:BaseTransfer.keySet()){
				String elementName = (String)obj;
				Element leaf = firstElement_2.addElement(elementName);
				leaf.addText(BaseTransfer.get(elementName) != null?BaseTransfer.get(elementName).toString():"");
			}
			writer.write(doc);
			
			//上传文件
			FtpUtils ftpUtil = FtpUtils.getDefaultFtp();

			String generalXmlFilePath = ConfigUtil
					.getFileLocationPath(CommonDefine.FILE_CATEGORY_TJ)
					.get("GENERAL_XML").toString();

			boolean result = ftpUtil.uploadFile(xmlfile.getPath(),
					generalXmlFilePath, "TJ_"+xmlfile.getName());

			if (result) {
				xmlfile.delete();
			}
			//返回xml字符串
			resultXml = doc.asXML();
			
			resultXml = resultXml.replaceAll(" xmlns=\"\"", "");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultXml;
	}
	
	
	// 生成回执xml
	public static String generalReceiptXml_LoadRec_FP(String rootName,
			String firstElementName, List<Map> dataList) {

		String resultXml = "";

		try {
			Document doc = DocumentHelper.createDocument();
			// 添加根元素
			Element rootElement = DocumentHelper.createElement(rootName);
			doc.setRootElement(rootElement);

			if(firstElementName!=null){
				for (Map content : dataList) {
					// 设置第一级元素xml
					Element firstElementXml = rootElement
							.addElement(firstElementName);

					for (Object obj : content.keySet()) {
						String elementName = (String) obj;
						Element leaf = firstElementXml.addElement(elementName);
						leaf.addText(content.get(elementName) != null ? content
								.get(elementName).toString() : "");
					}
				}
			}else{
				//无二级元素
				for (Map content : dataList) {
					for (Object obj : content.keySet()) {
						String elementName = (String) obj;
						Element leaf = rootElement.addElement(elementName);
						leaf.addText(content.get(elementName) != null ? content
								.get(elementName).toString() : "");
					}
				}
			}

			// 返回xml字符串
			resultXml = doc.getRootElement().asXML();

			resultXml = resultXml.replaceAll(" xmlns=\"\"", "");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return resultXml;
	}
	
	//生成回执xml
	public static String generalCommonXml(String root,Map content){

		String resultXml = "";

		try {
			Document doc = DocumentHelper.createDocument();
			// 添加根元素
			Element rootElement = DocumentHelper.createElement(root);
			doc.setRootElement(rootElement);
			
			for(Object obj:content.keySet()){
				String elementName = (String)obj;
				Element leaf = rootElement.addElement(elementName);
				leaf.addText(content.get(elementName) != null?content.get(elementName).toString():"");
			}
			//返回xml字符串
			resultXml = doc.asXML();

			resultXml = resultXml.replaceAll(" xmlns=\"\"", "");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return resultXml;
	}

	
	//生成回执xml
	public static String generalSoapXml_CJ(String xmlContent,String secondElementText){

		String resultXml = "";

		try {
			Document doc = DocumentHelper.createDocument();
			// 添加根元素
			Element rootElement = DocumentHelper.createElement(nameSpace4NJ+":"+"Envelope");
			rootElement.addNamespace("xsi",
					nameSpace_xsi);
			rootElement.addNamespace("xsd",
					nameSpace_xsd);
			rootElement.addNamespace("soap",
					nameSpace_soap);
			doc.setRootElement(rootElement);
			// 设置第一级元素
			Element firstElement = rootElement.addElement(nameSpace4NJ+":"+"Body");
			// 第二级元素
			Element secondElement = firstElement.addElement(secondElementText);
			secondElement.addNamespace("", nameSpace_1);
			Element xmlElement = secondElement.addElement("handler");
			xmlElement.addText(xmlContent);

			//返回xml字符串
			resultXml = doc.asXML();

			resultXml = resultXml.replaceAll(" xmlns=\"\"", "");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return resultXml;
	}
	
	public static String generalCommonXml_CJ(String root,Map order,List<Map> orderItemList){

		String resultXml = "";

		try {
			Document doc = DocumentHelper.createDocument();
			// 添加根元素
			Element rootElement = DocumentHelper.createElement(root);
			doc.setRootElement(rootElement);
			
			Element id = rootElement.addElement("ID");
			id.addText(CommonUtil.getSystemConfigProperty("CJ_id"));
			
			Element key = rootElement.addElement("KEY");
			key.addText(CommonUtil.getSystemConfigProperty("CJ_key"));
			
			Element orderElement = rootElement.addElement("ORDER");
			
			for(Object obj:order.keySet()){
				String elementName = (String)obj;
				Element leaf = orderElement.addElement(elementName);
				leaf.addText(order.get(elementName) != null?order.get(elementName).toString():"");
			}
			
			Element itemsElement = orderElement.addElement("ITEMS");
			for(Map item:orderItemList){
				Element itemElement = itemsElement.addElement("ITEM");
				for(Object obj:item.keySet()){
					String elementName = (String)obj;
					Element leaf = itemElement.addElement(elementName);
					leaf.addText(item.get(elementName) != null?item.get(elementName).toString():"");
				}
			}
			//返回xml字符串
			resultXml = doc.asXML();

			resultXml = resultXml.replaceAll(" xmlns=\"\"", "");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return resultXml;
	}
	

	/**
	 * 解析xml文件
	 * @param file
	 * @return
	 * @throws CommonException
	 */
	public static Map parseXml(File file, boolean needTransKey){
		//转换key value值
		Map<String,String> keyValueMap = reverseKeyValue();
		
		InputStream in = null;

		SAXReader saxReader = new SAXReader();

		//数据对象
		Map<String,String> data = new HashMap<String,String>();
		
		try {
			in = new FileInputStream(file);
			Document document = saxReader.read(in);
			Element rootElement = document.getRootElement();
			for (Iterator i = rootElement.elementIterator(); i.hasNext();) {
				Element firstElement = (Element) i.next();
				// 每个对象
				for (Iterator j = firstElement.elementIterator(); j.hasNext();) {
					Element node = (Element) j.next();
					//容错处理
					if(node.getName().contains(":")){
						if(needTransKey){
							data.put(keyValueMap.get(node.getName().split(":")[1]), node.getText().trim());
						}else{
							data.put(node.getName().split(":")[1], node.getText().trim());
						}
					}else{
						if(needTransKey){
							data.put(keyValueMap.get(node.getName()), node.getText().trim());
						}else{
							data.put(node.getName(), node.getText().trim());
						}
					}
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally{
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return data;
	}
	
	/**
	 * 解析xml文件，含数据层级
	 * @param file
	 * @return 子数据key:subDataList
	 * @throws CommonException
	 */
	public static Map parseXmlWithSubData(File file,boolean needTransKey){
		
		//转换key value值
		Map<String,String> keyValueMap = reverseKeyValue();
		
		InputStream in = null;

		SAXReader saxReader = new SAXReader();

		//数据对象
		Map<String,Object> data = new HashMap<String,Object>();
		
		//表体数据
		List<Map> subDataList = new ArrayList<Map>();
		
		try {
			in = new FileInputStream(file);
			Document document = saxReader.read(in);
			Element rootElement = document.getRootElement();
			for (Iterator i = rootElement.elementIterator(); i.hasNext();) {
				Element firstElement = (Element) i.next();
				//表头数据
				if(firstElement.getName().toLowerCase().contains("head")){
					// 每个对象
					for (Iterator j = firstElement.elementIterator(); j.hasNext();) {
						Element node = (Element) j.next();
						//容错处理
						if(node.getName().contains(":")){
							if(needTransKey){
								data.put(keyValueMap.get(node.getName().split(":")[1]), node.getText().trim());
							}else{
								data.put(node.getName().split(":")[1], node.getText().trim());
							}
						}else{
							if(needTransKey){
								data.put(keyValueMap.get(node.getName()), node.getText().trim());
							}else{
								data.put(node.getName(), node.getText().trim());
							}
						}
					}
				}
				//表体数据
				else if(firstElement.getName().toLowerCase().contains("list")){
					Map subData = new HashMap();
					// 每个对象
					for (Iterator j = firstElement.elementIterator(); j.hasNext();) {
						Element node = (Element) j.next();
						//容错处理
						if(node.getName().contains(":")){
							if(needTransKey){
								subData.put(keyValueMap.get(node.getName().split(":")[1]), node.getText().trim());
							}else{
								subData.put(node.getName().split(":")[1], node.getText().trim());
							}
						}else{
							if(needTransKey){
								subData.put(keyValueMap.get(node.getName()), node.getText().trim());
							}else{
								subData.put(node.getName(), node.getText().trim());
							}
						}
					}
					subDataList.add(subData);
				}
			}
			data.put("subDataList", subDataList);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally{
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return data;
	}
	
	//解析xml字符串
	public static SnReponseContentModel readStringXml_Content(String xml){
		
		SnReponseContentModel snReponseContentModel = new SnReponseContentModel();
		SnHeadModel sn_head = new SnHeadModel();
		String sn_body = null;
		SnErrorModel sn_error = new SnErrorModel();
		
		Document doc = null;
		try{
			doc = DocumentHelper.parseText(xml); // 将字符串转为XML
			
			 Element rootElt = doc.getRootElement(); // 获取根节点
			 //sn_head 迭代器
			 Iterator iter = rootElt.elementIterator("sn_head"); // 获取根节点下的子节点head
			// 遍历sn_head节点
			while (iter.hasNext()) {
				Element head = (Element) iter.next();
				//存储head节点下数据
				for(Object o:head.elements()){
					Element recordEle = (Element)o;
					if (recordEle.getName().equals("pageTotal")) {
						sn_head.setPageTotal(recordEle.getTextTrim());
						continue;
					}
					if (recordEle.getName().equals("pageNo")) {
						sn_head.setPageNo(recordEle.getTextTrim());
						continue;
					}
					if (recordEle.getName().equals("returnMessage")) {
						sn_head.setReturnMessage(recordEle.getTextTrim());
						continue;
					}
				}
			}
			 //sn_error 迭代器
			 iter = rootElt.elementIterator("sn_error"); // 获取根节点下的子节点head
			// 遍历sn_error节点
			while (iter.hasNext()) {
				Element error = (Element) iter.next();
				//存储error节点下数据
				for(Object o:error.elements()){
					Element recordEle = (Element)o;
					if (recordEle.getName().equals("error_code")) {
						sn_error.setError_code(recordEle.getTextTrim());
						continue;
					}
				}
			}
			 //sn_error 迭代器
			 iter = rootElt.elementIterator("sn_body"); // 获取根节点下的子节点head
			// 遍历sn_error节点
			while (iter.hasNext()) {
				Element body = (Element) iter.next();
				//sn_body内容
				sn_body = body.asXML();
			}
			//组装数据
			snReponseContentModel.setSn_head(sn_head);
			snReponseContentModel.setSn_body(sn_body);
			snReponseContentModel.setSn_error(sn_error);
			
		} catch (DocumentException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }
		return snReponseContentModel;
	}
	
	
	//解析xml字符串
	public static List<QueryLogisticsCrossbuyTask> readStringXml_Body(String xml){
		
		List<QueryLogisticsCrossbuyTask> dataList = new ArrayList<QueryLogisticsCrossbuyTask>();
		
		Document doc = null;
		try{
			doc = DocumentHelper.parseText(xml); // 将字符串转为XML
			
			 Element rootElt = doc.getRootElement(); // 获取根节点

			 //queryLogisticsCrossbuyTask 迭代器
			 Iterator iter = rootElt.elementIterator("queryLogisticsCrossbuyTask"); // 获取根节点下的子节点head
			// 遍历queryLogisticsCrossbuyTask节点
			while (iter.hasNext()) {
				//任务对象
				QueryLogisticsCrossbuyTask task = new QueryLogisticsCrossbuyTask();
				
				Element queryLogisticsCrossbuyTask = (Element) iter.next();
				//存储feedBackImformation节点下数据
				Iterator iters = queryLogisticsCrossbuyTask.elementIterator("feedBackImformation"); // 获取子节点head下的子节点script
                // 遍历queryLogisticsCrossbuyTask节点下的feedBackImformation节点
                while (iters.hasNext()) {
                	//信息对象
                    Element element = (Element) iters.next();
                    
                    HashMap data = readStringXml_EndElement(element.asXML());
                    
    				task.setFeedBackImformation(data);
                }
              //存储feedBackOrderItems节点下数据
				iters = queryLogisticsCrossbuyTask.elementIterator("feedBackOrderItems"); // 获取子节点head下的子节点script
                // 遍历queryLogisticsCrossbuyTask节点下的feedBackOrderItems节点
				List<HashMap<String,String>> feedBackOrderItems = new ArrayList<HashMap<String,String>>();
                while (iters.hasNext()) {
                	//信息对象
                    Element element = (Element) iters.next();
                    
                    HashMap data = readStringXml_EndElement(element.asXML());
                    
                    feedBackOrderItems.add(data);
                    
    				task.setFeedBackOrderItems(feedBackOrderItems);
                }
                //存储feedBackOrderItems节点下数据
  				iters = queryLogisticsCrossbuyTask.elementIterator("feedBackOrderCustomers"); // 获取子节点head下的子节点script
                  // 遍历queryLogisticsCrossbuyTask节点下的feedBackOrderItems节点
  				List<HashMap<String,String>> feedBackOrderCustomers = new ArrayList<HashMap<String,String>>();
                  while (iters.hasNext()) {
                  	//信息对象
                      Element element = (Element) iters.next();
                      
                      HashMap data = readStringXml_EndElement(element.asXML());

                      feedBackOrderCustomers.add(data);
                      
      				task.setFeedBackOrderCustomers(feedBackOrderCustomers);
                  }
                //添加数据
                dataList.add(task);
			}
			
		} catch (DocumentException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }
		return dataList;
	}
	
	//解析xml字符串
	public static AddLogisticsTaskStatusResponse readStringXml_Body_Response(
			String xml) {

		AddLogisticsTaskStatusResponse response = new AddLogisticsTaskStatusResponse();

		Document doc = null;
		try {
			doc = DocumentHelper.parseText(xml); // 将字符串转为XML

			Element rootElt = doc.getRootElement(); // 获取根节点

			// queryLogisticsCrossbuyTask 迭代器
			Iterator iter = rootElt.elementIterator("addLogisticsTaskStatus"); // 获取根节点下的子节点head
			// 遍历queryLogisticsCrossbuyTask节点
			while (iter.hasNext()) {

				Element addLogisticsTaskStatus = (Element) iter.next();

				HashMap data = readStringXml_EndElement(addLogisticsTaskStatus
						.asXML());

				response.setAddLogisticsTaskStatus(data);
			}

		} catch (DocumentException e) {
			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	//解析xml字符串
	private static HashMap<String,String> readStringXml_EndElement(String xml){
		
		HashMap data = new HashMap();
		
		Document doc = null;
		try{
			doc = DocumentHelper.parseText(xml); // 将字符串转为XML
			
			 Element rootElt = doc.getRootElement(); // 获取根节点
			 
			for(Object o:rootElt.elements()){
				Element recordEle = (Element)o;
				data.put(recordEle.getName(), recordEle.getTextTrim());
			}
		} catch (DocumentException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }
		return data;
	}
	
	/**
	 * 格式化xml字符串并生成文件，并生成文件，文件名为当前时刻
	 * @param xmlString
	 * @return
	 */
	public static File formatXml(String xmlString) {
		FileOutputStream fos = null;
		File xmlfile = null;
		Document document = null;
		try {
			document = DocumentHelper.parseText(xmlString);

			// 格式化输出格式
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("UTF-8");
			
			SimpleDateFormat sf = new SimpleDateFormat(
					CommonDefine.RETRIEVAL_TIME_FORMAT);

			String currentTime = sf.format(new Date());

			String filePath = System.getProperty("java.io.tmpdir") + "/"
					+ currentTime + ".xml";
			xmlfile = new File(filePath);
			if (!xmlfile.exists()) {
				xmlfile.createNewFile();
			}
			fos = new FileOutputStream(xmlfile);

			XMLWriter xmlWriter = new XMLWriter(fos, format);
			// 将document写入到输出流
			xmlWriter.write(document);
			xmlWriter.close();
			fos.close();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return xmlfile;
	}
	
	
	/**
	 * 格式化xml字符串并生成文件，并生成文件，文件名为当前时刻
	 * @param xmlString
	 * @return
	 */
	public static String getResponseFromXmlString(String xmlString,int messageType) {
		String result = "no data";
		Document document = null;
		String methodName = generalXmlNodeName4NJ(messageType);
		try {
			document = DocumentHelper.parseText(xmlString);
			Map uris = new HashMap();  
			uris.put("soap", nameSpace_soap);
			uris.put("aa", nameSpace_);
			
			XPath xpath = document.createXPath("//soap:Envelope/soap:Body/aa:"+methodName+"Response"); 
			xpath.setNamespaceURIs(uris);
			
			Node responseNode = xpath.selectSingleNode(document);
			
			xpath = document.createXPath("//soap:Envelope/soap:Body/aa:"+methodName+"Response/aa:"+methodName+"Result"); 
			xpath.setNamespaceURIs(uris);
			
			Node node = xpath.selectSingleNode(document);
			
			if(node == null && responseNode == null){
				result = null;
			}else{
				if(node == null){
					result = "";
				}else{
					result = node.getText();
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * 格式化xml字符串并生成文件，并生成文件，文件名为当前时刻
	 * @param xmlString
	 * @return
	 */
	public static String getResponseFromXmlString_CJ(String xmlString) {
		String result = "no data";
		Document document = null;
		try {
			document = DocumentHelper.parseText(xmlString);
			Map uris = new HashMap();  
			uris.put("soapenv", nameSpace_soap);
			uris.put("ns", nameSpace_1);
			
			XPath xpath = document.createXPath("//soapenv:Envelope/soapenv:Body/ns:sendInStockOrderResponse/ns:return"); 
			xpath.setNamespaceURIs(uris);

			Node node = xpath.selectSingleNode(document);
			
			if(node == null){
				result = "";
			}else{
				result = node.getText();
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String getTotalMidValue(String source, String priStr, String suxStr) {
		if (source == null)
			return null;
		int iFirst = source.indexOf(priStr);
		int iLast = source.lastIndexOf(suxStr);
		if (iFirst < 0 || iLast < 0)
			return null;
		int beginIndex = iFirst + priStr.length();

		return source.substring(beginIndex, iLast);
	}
	
	
	/**
	 * 格式化xml字符串并生成文件，并生成文件，文件名为当前时刻
	 * @param xmlString
	 * @return
	 */
	public static List<Map<String,String>> parseXmlStringForReceipt(String xmlString) {
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		Document document = null;
		try {
			document = DocumentHelper.parseText(xmlString);
//			Map uris = new HashMap();
//			uris.put("aa", nameSpace_);
			
			XPath xpath = document.createXPath("//NewDataSet/NJKJ_MESSAGE_APPR_RTN"); 
//			xpath.setNamespaceURIs(uris);
			//查找到所有的NJKJ_MESSAGE_APPR_RTN节点
			List<Node> nodes = xpath.selectNodes(document, xpath);
			
			for(Node node:nodes){
				Map<String,String> data = new HashMap<String,String>();
				//查询节点值
				List<Node> subNodes = node.selectNodes("*");
				for(Node subNode:subNodes){
					data.put(subNode.getName(), subNode.getText());
				}
				result.add(data);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 格式化xml字符串并生成文件，并生成文件，文件名为当前时刻
	 * @param xmlString
	 * @return
	 */
	public static Map<String,Object> parseXmlStringForReceipt_EMS(String xmlString) {
		Map<String,Object> result = new HashMap<String,Object>();
		
		List<String> assignIds = new ArrayList<String>();
		
		Document document = null;
		try {
			document = DocumentHelper.parseText(xmlString);
//			Map uris = new HashMap();
//			uris.put("aa", nameSpace_);
			
			XPath xpath = document.createXPath("//response"); 
//			xpath.setNamespaceURIs(uris);
			//查找response节点
			Node root = xpath.selectSingleNode(document);
			
			Map<String,String> data = new HashMap<String,String>();
			//查询节点值
			List<Node> subNodes = root.selectNodes("*");
			for(Node subNode:subNodes){
				result.put(subNode.getName(), subNode.getText());
			}
			//搜索订单号
			List<Node> billnoNodes = document.selectNodes("//billno");
			for(Node billno:billnoNodes){
				assignIds.add(billno.getText());
			}
			result.put("assignIds", assignIds);

		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 解析xml字符串，根文件
	 * @param xmlString
	 * @return
	 */
	public static Map<String,String> parseXmlRoot_WS(String xmlString) {
		Map<String,String> result = new HashMap<String,String>();
		Document document = null;
		try {
			document = DocumentHelper.parseText(xmlString);
			
			//获取文件类型节点
			XPath xpath = document.createXPath("//ParseXml/fileType"); 
			
			Node fileTypenode = xpath.selectSingleNode(document);
			
			result.put(fileTypenode.getName(), fileTypenode.getText());
			//获取报文正文节点
			xpath = document.createXPath("//ParseXml/xml/child::*"); 
			
			Node xmlnode = xpath.selectSingleNode(document);

			result.put("xml", xmlnode.asXML());
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 解析xml字符串,SNT101报文
	 * @param xmlString
	 * @return
	 */
	public static Map<String,Object> parseXmlSNT101_WS(String xmlString) {
		Map<String,Object> result = new HashMap<String,Object>();
		
		String rootNode = "SNT101Message";
		
		Document document = null;
		try {
			document = DocumentHelper.parseText(xmlString);
			
			//获取文件类型节点
			XPath xpath = document.createXPath("//"+rootNode+"/OrderHead/child::*"); 
			
			List<Node> OrderHeadChildNodes = xpath.selectNodes(document);
			
			Map<String,String> OrderHead = new HashMap<String,String>();
			//添加OrderHead数据
			for(Node node:OrderHeadChildNodes){
				OrderHead.put(node.getName(), node.getText());
			}
			result.put("OrderHead", OrderHead);
			//选取orderList节点的子节点，即order节点
			xpath = document.createXPath("//"+rootNode+"/OrderList/child::*"); 
			
			List<Node> OrderListChildNodes = xpath.selectNodes(document);
			
			List<Map<String,String>> orderList = new ArrayList<Map<String,String>>();
			//添加OrderHead数据
			for(Node order:OrderListChildNodes){
				List<Node> OrderChildNodes = order.selectNodes("child::*");
						
				Map<String,String> orderData = new HashMap<String,String>();
				//添加Order数据
				for(Node node:OrderChildNodes){
					orderData.put(node.getName(), node.getText());
				}
				orderList.add(orderData);
			}
			result.put("OrderList", orderList);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * 解析xml字符串,SNT201报文
	 * @param xmlString
	 * @return
	 */
	public static Map<String,Object> parseXmlSNT201_WS(String xmlString) {
		Map<String,Object> result = new HashMap<String,Object>();
		
		String rootNode = "SNT201Message";
		
		Document document = null;
		try {
			document = DocumentHelper.parseText(xmlString);
			
			//获取文件类型节点
			XPath xpath = document.createXPath("//"+rootNode+"/Logistics/child::*"); 
			
			List<Node> HeadChildNodes = xpath.selectNodes(document);
			
			Map<String,String> Head = new HashMap<String,String>();
			//添加OrderHead数据
			for(Node node:HeadChildNodes){
				Head.put(node.getName(), node.getText());
			}
			result.put("Head", Head);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * 解析xml字符串,SNT101报文
	 * @param xmlString
	 * @return
	 */
	public static Map<String,Object> parseXmlSNT301_WS(String xmlString) {
		Map<String,Object> result = new HashMap<String,Object>();
		
		String rootNode = "SNT301Message";
		
		Document document = null;
		try {
			document = DocumentHelper.parseText(xmlString);
			
			//获取文件类型节点
			XPath xpath = document.createXPath("//"+rootNode+"/OrderHead/child::*"); 
			
			List<Node> OrderHeadChildNodes = xpath.selectNodes(document);
			
			Map<String,String> OrderHead = new HashMap<String,String>();
			//添加OrderHead数据
			for(Node node:OrderHeadChildNodes){
				OrderHead.put(node.getName(), node.getText());
			}
			result.put("OrderHead", OrderHead);
			//选取orderList节点的子节点，即order节点
			xpath = document.createXPath("//"+rootNode+"/OrderList/child::*"); 
			
			List<Node> OrderListChildNodes = xpath.selectNodes(document);
			
			List<Map<String,String>> orderList = new ArrayList<Map<String,String>>();
			//添加OrderHead数据
			for(Node order:OrderListChildNodes){
				List<Node> OrderChildNodes = order.selectNodes("child::*");
						
				Map<String,String> orderData = new HashMap<String,String>();
				//添加Order数据
				for(Node node:OrderChildNodes){
					orderData.put(node.getName(), node.getText());
				}
				orderList.add(orderData);
			}
			result.put("OrderList", orderList);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * 解析xml字符串,SNT201报文
	 * @param xmlString
	 * @return
	 */
	public static Map<String,Object> parseXmlSNT401_WS(String xmlString) {
		Map<String,Object> result = new HashMap<String,Object>();
		
		String rootNode = "SNT401Message";
		
		Document document = null;
		try {
			document = DocumentHelper.parseText(xmlString);
			
			//获取文件类型节点
			XPath xpath = document.createXPath("//"+rootNode+"/Logistics/child::*"); 
			
			List<Node> HeadChildNodes = xpath.selectNodes(document);
			
			Map<String,String> Head = new HashMap<String,String>();
			//添加OrderHead数据
			for(Node node:HeadChildNodes){
				Head.put(node.getName(), node.getText());
			}
			result.put("Head", Head);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	//截取指定节点报文集合
	public static List<String> getNodesXmlData(String xmlString,String xPath){
		List<String> nodesXmlData = new ArrayList<String>();
		Document document = null;
		try {
			document = DocumentHelper.parseText(xmlString);
			
			XPath xpath = document.createXPath(xPath);
			
			List<Node> childrenNodes = xpath.selectNodes(document);
			
			for(Node node:childrenNodes){
				nodesXmlData.add(node.asXML());
			}
		}catch (DocumentException e) {
			e.printStackTrace();
		}
		return nodesXmlData;
		
	}
	
	/**
	 * 解析xml字符串,苏宁推动订单数据
	 * @param xmlString
	 * @return
	 */
	public static List<Map> parseXmlFPAPI_MulitpleNodes(String xmlString,String xPath) {
		List<Map> result = null;
		
//		String rootNode = "inputData/ordersList/orders";
		
		Document document = null;
		try {
			document = DocumentHelper.parseText(xmlString);
			
			result = getMultipleNodes(document,xPath);
			
//			//orderImformation-head节点解析
//			result.put("orderHead", getNodes(document,"//"+rootNode+"/orderImformation/orderHead/child::*"));
//			
//			//orderImformation-orderItems节点解析
//			result.put("orderItems", getNodes(document,"//"+rootNode+"/orderImformation/orderItems/child::*"));
//			
//			//orderImformation-feedBackOrderCustomers节点解析
//			result.put("feedBackOrderCustomers", getMultipleNodes(document,"//"+rootNode+"/orderImformation/feedBackOrderCustomers"));
//			
//			//orderExpBill节点解析
//			result.put("orderExpBill", getNodes(document,"//"+rootNode+"/orderExpBill/child::*"));
//			
//			//orderDeclare-orderDeclareHead节点解析
//			result.put("orderDeclareHead", getNodes(document,"//"+rootNode+"/orderDeclare/orderDeclareHead/child::*"));
//			
//			//orderDeclare-orderDeclareItems节点解析
//			result.put("orderDeclareItems", getNodes(document,"//"+rootNode+"/orderDeclare/orderDeclareItems/child::*"));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 解析xml字符串,苏宁推动订单数据
	 * @param xmlString
	 * @return
	 */
	public static Map parseXmlFPAPI_SingleNodes(String xmlString,String xPath) {
		Map result = null;
		
//		String rootNode = "inputData/ordersList/orders";
		
		Document document = null;
		try {
			document = DocumentHelper.parseText(xmlString);
			
			result = getNodes(document,xPath);
			
//			//orderImformation-head节点解析
//			result.put("orderHead", getNodes(document,"//"+rootNode+"/orderImformation/orderHead/child::*"));
//			
//			//orderImformation-orderItems节点解析
//			result.put("orderItems", getNodes(document,"//"+rootNode+"/orderImformation/orderItems/child::*"));
//			
//			//orderImformation-feedBackOrderCustomers节点解析
//			result.put("feedBackOrderCustomers", getMultipleNodes(document,"//"+rootNode+"/orderImformation/feedBackOrderCustomers"));
//			
//			//orderExpBill节点解析
//			result.put("orderExpBill", getNodes(document,"//"+rootNode+"/orderExpBill/child::*"));
//			
//			//orderDeclare-orderDeclareHead节点解析
//			result.put("orderDeclareHead", getNodes(document,"//"+rootNode+"/orderDeclare/orderDeclareHead/child::*"));
//			
//			//orderDeclare-orderDeclareItems节点解析
//			result.put("orderDeclareItems", getNodes(document,"//"+rootNode+"/orderDeclare/orderDeclareItems/child::*"));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	//xml字符串转化为map
	public static Map<String, Object> Dom2Map(String xmlString){
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(xmlString);
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (doc == null)
			return map;
		Element root = doc.getRootElement();
		for (Iterator iterator = root.elementIterator(); iterator.hasNext();) {
			Element e = (Element) iterator.next();
			List list = e.elements();
			if (list.size() > 0) {
				map.put(e.getName(), Dom2Map(e));
			} else
				map.put(e.getName(), e.getText());
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	public static Map Dom2Map(Element e) {
		Map map = new HashMap();
		List list = e.elements();
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Element iter = (Element) list.get(i);
				List mapList = new ArrayList();

				if (iter.elements().size() > 0) {
					Map m = Dom2Map(iter);
					if (map.get(iter.getName()) != null) {
						Object obj = map.get(iter.getName());
						if (!obj.getClass().getName()
								.equals("java.util.ArrayList")) {
							mapList = new ArrayList();
							mapList.add(obj);
							mapList.add(m);
						}
						if (obj.getClass().getName()
								.equals("java.util.ArrayList")) {
							mapList = (List) obj;
							mapList.add(m);
						}
						map.put(iter.getName(), mapList);
					} else
						map.put(iter.getName(), m);
				} else {
					if (map.get(iter.getName()) != null) {
						Object obj = map.get(iter.getName());
						if (!obj.getClass().getName()
								.equals("java.util.ArrayList")) {
							mapList = new ArrayList();
							mapList.add(obj);
							mapList.add(iter.getText());
						}
						if (obj.getClass().getName()
								.equals("java.util.ArrayList")) {
							mapList = (List) obj;
							mapList.add(iter.getText());
						}
						map.put(iter.getName(), mapList);
					} else
						map.put(iter.getName(), iter.getText());
				}
			}
		} else
			map.put(e.getName(), e.getText());
		return map;
	}


	
	
	//获取节点内容
	private static Map getNodes(Document document,String xPath){
		//orderImformation-head节点解析
		XPath xpath = document.createXPath(xPath); 
		
		List<Node> childrenNodes = xpath.selectNodes(document);
		
		Map<String,String> result = new HashMap<String,String>();
		//添加OrderHead数据
		for(Node node:childrenNodes){
			result.put(node.getName(), getNodeText(node.getText()));
		}
		return result;
	}
	

	//获取节点内容
	private static List<Map> getMultipleNodes(Document document,String xPath){
		//orderImformation-head节点解析
		XPath xpath = document.createXPath(xPath); 
		
		List<Node> childrenNodes = xpath.selectNodes(document);
		
		List<Map> ListResult = new ArrayList<Map>();
		//添加OrderHead数据
		for(Node node:childrenNodes){
			Map<String,String> data = new HashMap<String,String>();
			List<Node> subChildrenNodes = node.selectNodes("child::*");
			for(Node subNode:subChildrenNodes){
				data.put(subNode.getName(), getNodeText(subNode.getText()));
			}
			ListResult.add(data);
		}
		
		return ListResult;
	}
	
	private static String getNodeText(String text){
		Pattern p = Pattern.compile(".*<!\\[CDATA\\[(.*)\\]\\]>.*");
		Matcher m = p.matcher(text);

		if (m.matches()) {
			return m.group(1);
		}else{
			return text;
		}
	}
	
	//生成回执xml
	public static String generalReceiptXml_WS(String fileType,Map content){

		String resultXml = "";

		try {
			Document doc = DocumentHelper.createDocument();
			// 添加根元素
			Element rootElement = DocumentHelper.createElement("Response");
			doc.setRootElement(rootElement);
			// 设置第一级元素xml
			Element firstElementXml = rootElement.addElement("xml");
			// 设置第一级元素
			Element firstElementFileType = rootElement.addElement("fileType");
			
			firstElementFileType.setText(fileType);
			// 第二级元素
			Element secondElement = firstElementXml.addElement(fileType+"Message");
			
			for(Object obj:content.keySet()){
				String elementName = (String)obj;
				Element leaf = secondElement.addElement(elementName);
				leaf.addText(content.get(elementName) != null?content.get(elementName).toString():"");
			}
			//返回xml字符串
			resultXml = doc.asXML();
			
			resultXml = resultXml.replaceAll(" xmlns=\"\"", "");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return resultXml;
	}
	
	// 生成回执xml
	public static String generalReceiptXml_FP(String rootName,
			String firstElementName, List<LinkedMap> dataList) {

		String resultXml = "";

		try {
			Document doc = DocumentHelper.createDocument();
			// 添加根元素
			Element rootElement = DocumentHelper.createElement(rootName);
			doc.setRootElement(rootElement);

			if(firstElementName!=null){
				for (Map content : dataList) {
					// 设置第一级元素xml
					Element firstElementXml = rootElement
							.addElement(firstElementName);

					for (Object obj : content.keySet()) {
						String elementName = (String) obj;
						Element leaf = firstElementXml.addElement(elementName);
						leaf.addText(content.get(elementName) != null ? content
								.get(elementName).toString() : "");
					}
				}
			}else{
				//无二级元素
				for (Map content : dataList) {
					for (Object obj : content.keySet()) {
						String elementName = (String) obj;
						Element leaf = rootElement.addElement(elementName);
						leaf.addText(content.get(elementName) != null ? content
								.get(elementName).toString() : "");
					}
				}
			}

			// 返回xml字符串
			resultXml = doc.getRootElement().asXML();

			resultXml = resultXml.replaceAll(" xmlns=\"\"", "");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return resultXml;
	}
	
	//获取对应节点名
	private static String generalXmlNodeName4NJ(int messageType){
		String nodeName = "";
		switch(messageType){
		case CommonDefine.CEB201:
		case CommonDefine.CEB301:
		case CommonDefine.CEB401:
		case CommonDefine.CEB501:
		case CommonDefine.CEB503:
		case CommonDefine.CEB601:
			nodeName = "ParseXml";
			break;
		case CommonDefine.CEB202:
		case CommonDefine.CEB302:
			nodeName = "ChangeXml";
			break;
		case CommonDefine.CEB203:
		case CommonDefine.CEB303:
		case CommonDefine.CEB502:
		case CommonDefine.CEB602:
			nodeName = "DeleteXml";
			break;	
			
			
		case CommonDefine.CEB201_RECEIPT_SINGLE:
			nodeName = "GetReceiptPgrByCode";
			break;
		case CommonDefine.CEB201_RECEIPT_LIST:
			nodeName = "GetReceiptPgrListByCode";
			break;
			
		case CommonDefine.CEB601_RECEIPT_SINGLE:
			nodeName = "GetReceiptNiInvtByCode";
			break;
		case CommonDefine.CEB607_RECEIPT_SINGLE:
			nodeName = "GetReceiptNeInvtByCode";
			break;
/*		case CommonDefine.CEB604_RECEIPT_SINGLE:
			nodeName = "GetReceiptNiInvtByCode";
			break;
		case CommonDefine.CEB610_RECEIPT_SINGLE:
			nodeName = "GetReceiptNiInvtByCode";
			break;*/
			
		case CommonDefine.CEB601_RECEIPT_LIST:
			nodeName = "GetReceiptNiInvtListByCode";
			break;
		case CommonDefine.CEB607_RECEIPT_LIST:
			nodeName = "GetReceiptNeInvtListByCode";
			break;
			
		}
		
		return nodeName;
	}
	
	
	//获取对应节点名
	private static String generalXmlNodeName4IMPORT(int messageType){
		String nodeName = "";
		switch(messageType){
		case CommonDefine.CEB201:
		case CommonDefine.CEB301:
		case CommonDefine.CEB401:
		case CommonDefine.CEB501:
		case CommonDefine.CEB503:
		case CommonDefine.CEB511:
		case CommonDefine.CEB601:
			nodeName = "ParseXml";
			break;
		case CommonDefine.CEB202:
		case CommonDefine.CEB302:
			nodeName = "ChangeXml";
			break;
		case CommonDefine.CEB203:
		case CommonDefine.CEB303:
		case CommonDefine.CEB502:
		case CommonDefine.CEB602:
			nodeName = "DeleteXml";
			break;	
			
			
		case CommonDefine.CEB201_RECEIPT_SINGLE:
			nodeName = "GetReceiptPgrByCode";
			break;
		case CommonDefine.CEB201_RECEIPT_LIST:
			nodeName = "GetReceiptPgrListByCode";
			break;
			
		case CommonDefine.CEB601_RECEIPT_SINGLE:
			nodeName = "GetReceiptNiInvtByCode";
			break;
		case CommonDefine.CEB607_RECEIPT_SINGLE:
			nodeName = "GetReceiptNeInvtByCode";
			break;
/*		case CommonDefine.CEB604_RECEIPT_SINGLE:
			nodeName = "GetReceiptNiInvtByCode";
			break;
		case CommonDefine.CEB610_RECEIPT_SINGLE:
			nodeName = "GetReceiptNiInvtByCode";
			break;*/
			
		case CommonDefine.CEB601_RECEIPT_LIST:
			nodeName = "GetReceiptNiInvtListByCode";
			break;
		case CommonDefine.CEB607_RECEIPT_LIST:
			nodeName = "GetReceiptNeInvtListByCode";
			break;
			
		}
		
		return nodeName;
	}

	/**
	 * 转换消息的key_value值
	 * @param messageType
	 * @return
	 */
 	private static Map<String,String> reverseKeyValue(){
		Map<String,String> result = new HashMap<String,String>();
		ResourceBundle bundle = CommonUtil.getMessageMappingResource("CEB");
		for(String key : bundle.keySet()){
			result.put(bundle.getString(key), key);
		}
		return result;
	}
 	
 	public static void main(String args[]){

// 		parseXmlFPAPI_ORDER(xxxxxx);
 		
//		String soapResponseData =
//			    "<NewDataSet>"+
//			        "<NJKJ_MESSAGE_APPR_RTN>"+
//			            "<EBC_CODE>3215916102</EBC_CODE>"+
//			            "<ITEM_NO>G23521506000000261</ITEM_NO>"+
//						"<G_NO />"+
//			            "<CHK_STATUS>3</CHK_STATUS>"+
//			            "<CHK_RESULT>审批意见</CHK_RESULT>"+
//						"<CHK_TIME>20150723085544 </CHK_TIME>"+
//			        "</NJKJ_MESSAGE_APPR_RTN>"+
//			        "<NJKJ_MESSAGE_APPR_RTN>"+
//			            "<EBC_CODE>3215916102</EBC_CODE>"+
//			           "<ITEM_NO>G23521506000000262</ITEM_NO>"+
//			            "<G_NO>32159161020000000012</G_NO>"+
//			            "<CHK_STATUS>2</CHK_STATUS>"+
//						"<CHK_RESULT />"+
//						"<CHK_TIME>20150723085544 </CHK_TIME>"+
//			        "</NJKJ_MESSAGE_APPR_RTN>"+
//			    "</NewDataSet>";
//		List<Map<String,String>> xxx = parseXmlStringForReceipt(soapResponseData);
//		
//		for(Map data:xxx){
//			System.out.println(data);
//		}
 	}

}
