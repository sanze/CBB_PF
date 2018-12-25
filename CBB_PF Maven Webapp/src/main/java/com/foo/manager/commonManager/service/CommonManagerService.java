package com.foo.manager.commonManager.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.Resource;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

import com.foo.abstractService.AbstractService;
import com.foo.common.CommonDefine;
import com.foo.common.CommonException;
import com.foo.common.MessageCodeDefine;
import com.foo.dao.mysql.CommonManagerMapper;
import com.foo.dao.mysql.ImportCommonManagerMapper;
import com.foo.dao.mysql.NJCommonManagerMapper;
import com.foo.dao.mysql.SNCommonManagerMapper;
import com.foo.handler.ExceptionHandler;
import com.foo.util.CommonUtil;
import com.foo.util.ConfigUtil;
import com.foo.util.FtpUtils;
import com.foo.util.HttpUtil;
import com.foo.util.XmlUtil;

/**
 * @author xuxiaojun
 *
 */
public abstract class CommonManagerService extends AbstractService {
	@Resource
	protected CommonManagerMapper commonManagerMapper;
	@Resource
	protected NJCommonManagerMapper njCommonManagerMapper;
	@Resource
	protected ImportCommonManagerMapper importCommonManagerMapper;
	@Resource
	protected SNCommonManagerMapper snCommonManagerMapper;
	
	
	public Map<String, Object> getAllCodeNames(Map<String, Object> params)
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
			// 按类别查询
			if (params.get("RELATION_CATEGORY") != null
					&& !params.get("RELATION_CATEGORY").toString().isEmpty()) {
				rows = commonManagerMapper.selectTableListByCol("t_code_name",
						"RELATION_CATEGORY", params.get("RELATION_CATEGORY"),
						start, limit);
				total = commonManagerMapper.selectTableListCountByCol(
						"t_code_name", "RELATION_CATEGORY",
						params.get("RELATION_CATEGORY"));
			} else {
				// 查询所有
				rows = commonManagerMapper.selectTable("t_code_name", start,
						limit);
				total = commonManagerMapper.selectTableCount("t_code_name");
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
	
	public List<Map> getAllCodeCategory()
			throws CommonException {
		
		List<Map> result = new ArrayList<Map>();
		try {
			result = commonManagerMapper.getAllCodeCategory();
			
			return result;
		} catch (Exception e) {
			ExceptionHandler.handleException(e);
			throw new CommonException(e,
					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR);
		}
	}
	
	//获取类别列表
	public List<Map<String, Object>> getCodeCategory(String relationCategory)
			throws CommonException {
		
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		try {
			String tableName = "t_code_name";
			
			result = commonManagerMapper.selectTableListByCol(tableName, "RELATION_CATEGORY", relationCategory, null, null);
			
			return result;
		} catch (Exception e) {
			ExceptionHandler.handleException(e);
			throw new CommonException(e,
					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR);
		}
	}
	
	
	public boolean uniqueCheck(String tableName,String uniqueCol,Object uniqueVal,
			String primaryCol,Object primaryVal,
			boolean preventException) throws CommonException {
		boolean valid=false;
		List<Map<String, Object>> list = commonManagerMapper.selectTableListByCol(tableName, uniqueCol, uniqueVal, null, null);
		switch(list.size()){
		case 0:
			valid=true;
			break;
		case 1:
			if(primaryVal!=null){
				Object val=list.get(0).get(primaryCol);
				valid=(val!=null&&primaryVal.equals(val));
			}else{
				valid=false;
			}
			break;
		default:
			valid=false;
		}
		if(!preventException&&!valid){
			throw new CommonException(null,
					MessageCodeDefine.COM_EXCPT_DUPLICATE_ENTRY);
		}
		return valid;
	}
	
	public boolean uniqueCheck(String tableName,List<String> uniqueCol,List<Object> uniqueVal,
			String primaryCol,Object primaryVal,
			boolean preventException) throws CommonException {
		boolean valid=false;
		List<Map<String, Object>> list = commonManagerMapper.selectTableListByNVList(tableName, uniqueCol, uniqueVal, null, null);
		switch(list.size()){
		case 0:
			valid=true;
			break;
		case 1:
			if(primaryVal!=null){
				Object val=list.get(0).get(primaryCol);
				valid=(val!=null&&primaryVal.equals(val));
			}else{
				valid=false;
			}
			break;
		default:
			valid=false;
		}
		if(!preventException&&!valid){
			String message = "";

			if(T_NJ_SKU.equals(tableName.toUpperCase())){
				message = "商品货号已存在！";
			}else if(T_NJ_ORDERS.equals(tableName.toUpperCase())){
				message = "订单编号已存在！";
			}else if(T_NJ_LOGISTICS.equals(tableName.toUpperCase())){
				message = "运单号已存在！";
			}else if(T_NJ_INVENTORY.equals(tableName.toUpperCase())){
				message = "运单号已存在！";
			}
			
			if(message.isEmpty()){
				throw new CommonException(null,
						MessageCodeDefine.COM_EXCPT_DUPLICATE_ENTRY);
			}else{
				throw new CommonException(null,
						MessageCodeDefine.COM_EXCPT_DUPLICATE_ENTRY,message);
			}
		}
		return valid;
	}

	// 生成xml文件 messageType = CommonDefine.CEB501或CommonDefine.CEB503
	public boolean submitXml_LOGISTICS(Map<String, Object> data,
			Integer logisticsId, int messageType, int logisticsType) {
		
		//修改指定运单表
		String tableName = "T_LOGISTICS";
		
		List<Map> subDataList = null;
		
		switch (logisticsType) {
		case CommonDefine.LOGISTICS_TYPE_NORMAL:
			tableName = "T_LOGISTICS";
			break;
		case CommonDefine.LOGISTICS_TYPE_SUNING:
			tableName = "T_LOGISTICS_SN";
			break;
		case CommonDefine.LOGISTICS_TYPE_IMPORT:
			tableName = "T_IMPORT_LOGISTICS";
			break;
		default:

		}
		// 提交需要生成xml文件,
		if (Integer.valueOf(CommonDefine.APP_STATUS_UPLOAD).equals(
				data.get("APP_STATUS"))) {
			System.out.println("submitXml_LOGISTICS 表："+tableName);

			// 更新数据，添加申报时间
			// 插入申报时间
			String currentTime = new SimpleDateFormat(
					CommonDefine.RETRIEVAL_TIME_FORMAT).format(new Date());
			data.put("APP_TIME", currentTime);
			commonManagerMapper.updateLogistics(data, tableName);

			// 获取guid
			String guid = data.get("GUID").toString();

			String generalXmlFilePath = null;

			switch (messageType) {
			case CommonDefine.CEB501:
			case CommonDefine.CEB511:
				generalXmlFilePath = ConfigUtil
						.getFileLocationPath(CommonDefine.FILE_CATEGORY_WLYD)
						.get("GENERAL_XML").toString();
				
				switch (logisticsType) {
				case CommonDefine.LOGISTICS_TYPE_NORMAL:
					// 获取需要生成报文的数据
					data = commonManagerMapper.selectDataForMessage501(guid);
					break;
				case CommonDefine.LOGISTICS_TYPE_SUNING:
					// 获取需要生成报文的数据
					data = commonManagerMapper.selectDataForMessage501_SN(guid);
					break;
				case CommonDefine.LOGISTICS_TYPE_IMPORT:
					// 获取需要生成报文的数据
					data = commonManagerMapper.selectDataForMessage511_import(guid);
					subDataList = generateBaseTransfer();
					break;
				}
				break;
			case CommonDefine.CEB503:
			case CommonDefine.CEB513:
				// 设置申请状态为申报中
				data.put("APP_STATUS", CommonDefine.APP_STATUS_UPLOAD);
				// 清空回执获取信息
				commonManagerMapper.updateLogisticsReturnInfoToNull(data, tableName);
				generalXmlFilePath = ConfigUtil
						.getFileLocationPath(CommonDefine.FILE_CATEGORY_YDZT)
						.get("GENERAL_XML").toString();
				
				switch (logisticsType) {
				case CommonDefine.LOGISTICS_TYPE_NORMAL:
					// 获取需要生成报文的数据
					data = commonManagerMapper.selectDataForMessage503(guid);
					break;
				case CommonDefine.LOGISTICS_TYPE_SUNING:
					// 获取需要生成报文的数据
					data = commonManagerMapper.selectDataForMessage503_SN(guid);
					break;
				case CommonDefine.LOGISTICS_TYPE_IMPORT:
					// 获取需要生成报文的数据
					data = commonManagerMapper.selectDataForMessage513_import(guid);
					subDataList = generateBaseTransfer();
					break;
				}
				break;
			}

			File file = XmlUtil.generalXml(data, subDataList, messageType);

			FtpUtils ftpUtil = FtpUtils.getDefaultFtp();

			boolean result = ftpUtil.uploadFile(file.getPath(),
					generalXmlFilePath, file.getName());

			if (result) {
				file.delete();
			}
			return result;
		}
		return true;
	}
	
	public boolean submitXml_LOGISTICS4Korea(Map<String, Object> data) {
		
		//修改指定运单表
		String tableName = T_LOGISTICS;

		// 提交需要生成xml文件,
		if (Integer.valueOf(CommonDefine.APP_STATUS_UPLOAD).equals(
				data.get("APP_STATUS"))) {
			System.out.println("submitXml_LOGISTICS 表："+tableName);

			// 更新数据，添加申报时间
			// 插入申报时间
			String currentTime = new SimpleDateFormat(
					CommonDefine.RETRIEVAL_TIME_FORMAT).format(new Date());
			data.put("APP_TIME", currentTime);
//			commonManagerMapper.updateLogistics(data, tableName);

			// 获取guid
			String guid = data.get("GUID").toString();
			
			List<Map<String, Object>> dataList = commonManagerMapper.selectTableListByCol(V_LOGISTICS,"GUID", guid, null, null);
			
			//请求数据
			Map<String,Object> requestData = generateHeader4Korea();
			
			requestData.putAll(generateBody4Korea(dataList));
			
			//发送http请求
			Map<String, Object> head = new HashMap<String, Object>();
			head.put("Content-Type", "application/json");
			//获取url
			String url = CommonUtil.getSystemConfigProperty("pantos_requestUrl");
			
			Map result = HttpUtil.doPost(url, head, requestData);

			System.out.println(result);
		}
		return true;
	}
	
	
	//生成消息体header
	private Map<String,Object> generateHeader4Korea(){
		Map<String,Object> header = new LinkedHashMap<String,Object>();
		
		Map<String,String> body = new LinkedHashMap<String,String>();
		
		ResourceBundle bundle = CommonUtil.getResource("messageMapping/CEB_Pantos_header");
		
		for(String key:bundle.keySet()){
			body.put(key, bundle.getString(key));
		}
		header.put("header", body);
		
		return header;
	}
	
	//生成消息体header
	private Map<String,Object> generateBody4Korea(List<Map<String, Object>> dataList){
		Map<String,Object> header = new HashMap<String,Object>();
		
		List<Map<String,String>> body = new ArrayList<Map<String,String>>();
		
		ResourceBundle bundle = CommonUtil.getResource("messageMapping/CEB_Pantos_body");
		
		for(Map data:dataList){
			Map<String,String> subBody = new LinkedHashMap<String,String>();
			
			for(String key:bundle.keySet()){
				
				String value = bundle.getString(key);
				
				if(data.containsKey(value.toUpperCase())){
					subBody.put(key, data.get(value.toUpperCase()).toString());
				}else{
					subBody.put(key, value);
				}
			}
			body.add(subBody);
		}
		header.put("body", body);
		
		return header;
	}
	
	
	//生成BaseTransfer报文数据
	private List<Map> generateBaseTransfer(){
		List<Map> baseTransfer = new ArrayList<Map>();
		
		Map data = new LinkedHashMap<String,String>();
		data.put("copCode", CommonUtil.getSystemConfigProperty("copCode"));
		data.put("copName", CommonUtil.getSystemConfigProperty("copName"));
		data.put("dxpMode", CommonUtil.getSystemConfigProperty("dxpMode"));
		data.put("dxpId", CommonUtil.getSystemConfigProperty("dxpId"));
		data.put("note", CommonUtil.getSystemConfigProperty("note"));
		
		baseTransfer.add(data);
		return baseTransfer;
	}
	
	//http请求调用webservice
	public String sendHttpCMD(String xmlString,int messageType,int cmdType) throws CommonException {
		String result = "";
		
		String requestUrl = "";

		switch(cmdType){
		case CommonDefine.CMD_TYPE_DECLARE:
			requestUrl = CommonUtil.getSystemConfigProperty("requestUrl");
			break;
		case CommonDefine.CMD_TYPE_RECEIPT:
			requestUrl = CommonUtil.getSystemConfigProperty("receiptUrl");
			break;
		}
		//测试ftp服务器有没有正常启动
		checkFtpServerValid();
		
		try {
			PostMethod postMethod = new PostMethod(requestUrl);

			// 然后把Soap请求数据添加到PostMethod中
			byte[] b = xmlString.getBytes("utf-8");
			InputStream is = new ByteArrayInputStream(b,0,b.length);
			RequestEntity re = new InputStreamRequestEntity(is,b.length,"application/soap+xml; charset=utf-8");
			postMethod.setRequestEntity(re);

			// 最后生成一个HttpClient对象，并发出postMethod请求
			HttpClient httpClient = new HttpClient();
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == 200) {
				String soapResponseData = postMethod.getResponseBodyAsString();
//				System.out.println("request xml String:"+xmlString);
//				System.out.println("reponse xml String:"+soapResponseData);
				result = XmlUtil.getResponseFromXmlString(soapResponseData,messageType);
				//上传请求回应数据
				uploadRequestLog(messageType,xmlString,soapResponseData,result);
				
				if(result == null){
					//抛出错误信息
					throw new CommonException(new Exception(),
							MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR, "无回执信息！");
				}else{
//					System.out.println("reponse result String:"+result);
				}
				
			} else {
				uploadRequestLog(messageType,xmlString,"",result);
				//抛出错误信息
				throw new CommonException(new Exception(),
						MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR, "调用失败！错误码：" + statusCode);
			}
		} catch (CommonException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	// http请求调用webservice
	public String sendHttpCMD(String xmlString,int cmdType) throws CommonException {
		
		String result = "";
		
		String requestUrl = "";

		switch(cmdType){
			
		case CommonDefine.APPLY_EMS_NO:
			requestUrl = CommonUtil.getSystemConfigProperty("applyEmsNo");
			break;
		}
		
		//测试ftp服务器有没有正常启动
		checkFtpServerValid();
		
		Object[] obj = null;
		try {
			JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		    HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();    // 策略
		    httpClientPolicy.setConnectionTimeout( 36000 );    //连接超时 
		    httpClientPolicy.setAllowChunking( false );   
		    httpClientPolicy.setReceiveTimeout( 10000 );       //接收超时
		    Client client = dcf.createClient(requestUrl);
		    HTTPConduit http = (HTTPConduit) client.getConduit();  
		    http.setClient(httpClientPolicy);
		    obj = client.invoke("getBillNoBySys", new Object[]{CommonUtil.encryptionBase64(xmlString)});
		} catch (CommonException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(obj!=null && obj.length>0){
			String soapResponseData = CommonUtil.decryptBase64((String)obj[0]);
			
			System.out.println("request xml String:"+xmlString);
			System.out.println("reponse xml String:"+soapResponseData);
			result = soapResponseData;
			//上传请求回应数据
			uploadRequestLog(cmdType,xmlString,soapResponseData,result);
			
			if(result == null){
				//抛出错误信息
				throw new CommonException(new Exception(),
						MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR, "无回执信息！");
			}else{
				System.out.println("reponse result String:"+result);
			}
		}
		return result;
	}

	public static void main(String args[]){
		
		String soapResponseData = "<NewDataSet>"+"\n"+
		        "<NJKJ_MESSAGE_APPR_RTN>"+"\n"+
	            "<EBC_CODE>3215916102</EBC_CODE>"+"\n"+
	            "<ITEM_NO>G23521506000000261</ITEM_NO>"+"\n"+
				"<G_NO />"+"\n"+
	            "<CHK_STATUS>3</CHK_STATUS>"+"\n"+
	            "<CHK_RESULT>审批意见</CHK_RESULT>"+"\n"+
				"<CHK_TIME>20150723085544 </CHK_TIME>"+"\n"+
	        "</NJKJ_MESSAGE_APPR_RTN>"+"\n"+
	    "</NewDataSet>";
		System.out.println(soapResponseData);
		soapResponseData = soapResponseData.replaceAll( "\\s*|\t|\r|\n", "" );
		System.out.println(soapResponseData);
		if(soapResponseData == null ||soapResponseData.isEmpty()){
			//抛出错误信息
//			throw new CommonException(new Exception(),
//					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR, soapResponseData);
		}
		List<Map<String,String>> response = XmlUtil.parseXmlStringForReceipt(soapResponseData);
		if(response!=null&&response.size()>0){
			//更新商品数据
			//{EBC_CODE=3215916102, G_NO=, CHK_TIME=20150723085544 , ITEM_NO=G23521506000000261, CHK_STATUS=3, CHK_RESULT=审批意见}
			for(Map data:response){
				if(data.get("EBC_CODE")!=null){
					Map sku = new HashMap();
					sku.put("RETURN_STATUS", data.get("CHK_STATUS"));
					sku.put("RETURN_TIME", data.get("CHK_TIME"));
					sku.put("RETURN_INFO", data.get("CHK_RESULT"));
					sku.put("G_NO", data.get("G_NO"));
//					sku.put("GUID", guid);
					//回执状态为3审批通过 更新申报状态 为申报完成
					if (sku.get("RETURN_STATUS") != null
							&& Integer.valueOf(sku.get(
									"RETURN_STATUS").toString()) == CommonDefine.RETURN_STATUS_2) {
						sku.put("APP_STATUS",CommonDefine.APP_STATUS_COMPLETE);
					}
//					njCommonManagerMapper.updateSku_nj(sku);
				}
			}
		}else{
			//抛出错误信息
//			throw new CommonException(new Exception(),
//					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR, soapResponseData);
		}
	}
	

}
