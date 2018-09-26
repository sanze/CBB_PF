package com.foo.abstractService;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.foo.common.CommonDefine;
import com.foo.common.CommonException;
import com.foo.common.MessageCodeDefine;
import com.foo.util.ConfigUtil;
import com.foo.util.FileWriterUtil;
import com.foo.util.FtpUtils;


/**
 * @author xuxiaojun
 *
 */
public abstract class AbstractService {
	
	public static String T_SKU = "t_sku";
	public static String T_ORDERS = "t_orders";
	public static String T_LOGISTICS = "t_logistics";
	public static String T_INVENTORY = "t_inventory";
	public static String T_INVENTORY_DETAIL = "t_inventory_detail";
	
	protected static String V_ORDERS_UNUSE = "v_orders_unuse";
	protected static String V_LOGISTICS = "v_logistics";
	protected static String V_INVENTORY = "v_inventory";
	protected static String V_LOGISTICS_UNUSE = "v_logistics_unuse";
	
	protected static String T_NJ_SKU = "T_NJ_SKU";
	protected static String T_NJ_ORDERS = "T_NJ_ORDERS";
	protected static String T_NJ_ORDER_DETAIL = "T_NJ_ORDER_DETAIL";
	protected static String T_NJ_LOGISTICS = "T_NJ_LOGISTICS";
	protected static String T_NJ_INVENTORY = "T_NJ_INVENTORY";
	protected static String T_NJ_INVENTORY_DETAIL = "T_NJ_INVENTORY_DETAIL";
	protected static String T_NJ_PAY = "T_NJ_PAY";
	protected static String T_CONTACT = "T_CONTACT";

	protected static String V_NJ_INVENTORY = "V_NJ_INVENTORY";
	protected static String V_NJ_INVENTORY_DETAIL = "V_NJ_INVENTORY_DETAIL";
	protected static String V_NJ_LOGISTICS= "V_NJ_LOGISTICS";
	protected static String V_NJ_LOGISTICS_UNUSE = "V_NJ_LOGISTICS_UNUSE";
	protected static String V_NJ_ORDERS_UNUSE = "V_NJ_ORDERS_UNUSE";
	protected static String V_NJ_PAY = "V_NJ_PAY";
	protected static String V_NJ_ORDERS_UNUSE_PAY = "V_NJ_ORDERS_UNUSE_PAY";
	
	//
	protected static String T_IMPORT_SKU = "T_IMPORT_SKU";
	protected static String T_IMPORT_INVENTORY = "T_IMPORT_INVENTORY";
	protected static String T_IMPORT_INVENTORY_DETAIL = "T_IMPORT_INVENTORY_DETAIL";
	protected static String T_IMPORT_PAY = "T_IMPORT_PAY";
	protected static String T_IMPORT_ORDERS = "T_IMPORT_ORDERS";
	protected static String T_IMPORT_ORDER_DETAIL = "T_IMPORT_ORDER_DETAIL";
	protected static String T_IMPORT_LOGISTICS = "T_IMPORT_LOGISTICS";
	protected static String T_IMPORT_DELIVERY = "T_IMPORT_DELIVERY";
	protected static String V_IMPORT_ORDERS_UNUSE = "V_IMPORT_ORDERS_UNUSE";
	protected static String V_IMPORT_LOGISTICS= "V_IMPORT_LOGISTICS";
	protected static String V_IMPORT_LOGISTICS_UNUSE = "V_IMPORT_LOGISTICS_UNUSE";
	protected static String V_IMPORT_INVENTORY = "V_IMPORT_INVENTORY";
	protected static String V_IMPORT_INVENTORY_DETAIL = "V_IMPORT_INVENTORY_DETAIL";
	protected static String V_IMPORT_PAY = "V_IMPORT_PAY";
	protected static String V_IMPORT_ORDERS_UNUSE_PAY = "V_IMPORT_ORDERS_UNUSE_PAY";
	
	//检测ftp时候启动
	protected void checkFtpServerValid() throws CommonException{
		try{
			//上传文件
			FtpUtils ftpUtil = FtpUtils.getDefaultFtp();
			
			ftpUtil.ftpClient.list("/");
		}catch(Exception e){
			//抛出错误信息
			throw new CommonException(new Exception(),
					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR, "FTP服务未启动！");
		}
	}
	
	//上传请求信息及回应
	protected void uploadRequestLog(int messageType, String request,String reponse,String resultString){
			
		String uploadPath = "";
		switch (messageType) {
		case CommonDefine.CEB201:
		case CommonDefine.CEB202:
		case CommonDefine.CEB203:
		case CommonDefine.CEB201_RECEIPT_SINGLE:
		case CommonDefine.CEB201_RECEIPT_LIST:
			uploadPath = ConfigUtil
					.getFileLocationPath(CommonDefine.FILE_CATEGORY_SPBA)
					.get("GENERAL_XML").toString();
			break;
		case CommonDefine.CEB301:
		case CommonDefine.CEB302:
		case CommonDefine.CEB303:
			uploadPath = ConfigUtil
					.getFileLocationPath(CommonDefine.FILE_CATEGORY_DZDD)
					.get("GENERAL_XML").toString();
			break;
		case CommonDefine.CEB501:
		case CommonDefine.CEB502:
		case CommonDefine.CEB503:
			uploadPath = ConfigUtil
					.getFileLocationPath(CommonDefine.FILE_CATEGORY_WLYD)
					.get("GENERAL_XML").toString();
			break;
		case CommonDefine.CEB601:
		case CommonDefine.CEB602:
		case CommonDefine.CEB601_RECEIPT_SINGLE:
		case CommonDefine.CEB601_RECEIPT_LIST:
			uploadPath = ConfigUtil
					.getFileLocationPath(CommonDefine.FILE_CATEGORY_CJQD)
					.get("GENERAL_XML").toString();
			break;
		}
		
		SimpleDateFormat sf = new SimpleDateFormat(CommonDefine.COMMON_SIMPLE_FORMAT);
		//文件名
		String fileName = messageType+"_RequestData_"+sf.format(new Date());
		
		String filePath = System.getProperty("java.io.tmpdir") + "/"
				+ fileName + ".txt";
		
		request = request == null?"":request;
		reponse = reponse == null?"":reponse;
		resultString = resultString == null?"":resultString;

		request = request.replaceAll("&lt;", '<' + "");
		request = request.replaceAll("&gt;", '>'+"");
		
		reponse = reponse.replaceAll("&lt;", '<' + "");
		reponse = reponse.replaceAll("&gt;", '>'+"");

		resultString = resultString.replaceAll("&lt;", '<' + "");
		resultString = resultString.replaceAll("&gt;", '>'+"");
		
		System.out.println("request xml String:"+request);
		System.out.println("reponse xml String:"+reponse);
		System.out.println("reponse result String:"+resultString);
		
		Map resultData = new HashMap();
		resultData.put("请求数据", formatXML(request));
		resultData.put("返回数据", formatXML(reponse));
		resultData.put("结果解析", resultString);
		
		File file = null;
		try {
			file = FileWriterUtil.writeToTxt(filePath, resultData);
			
			//上传文件
			FtpUtils ftpUtil = FtpUtils.getDefaultFtp();

			boolean result = ftpUtil.uploadFile(file.getPath(),
					uploadPath, file.getName());

			if (result) {
				file.delete();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//上传请求信息及回应
	protected void uploadWsLog(int messageType, String message){
		
		SimpleDateFormat sf = new SimpleDateFormat(CommonDefine.COMMON_SIMPLE_FORMAT);
		//文件名
		String fileName = "";
		String uploadPath = "ws";
		switch (messageType) {
		case 1:
			fileName = "InputData_"+sf.format(new Date());
			break;
		case 2:
			fileName = "OutputData_"+sf.format(new Date());
			break;
		}
		
		String filePath = System.getProperty("java.io.tmpdir") + "/"
				+ fileName + ".txt";
		
		message = message == null?"":message;

		message = message.replaceAll("&lt;", '<' + "");
		message = message.replaceAll("&gt;", '>'+"");
		
		Map resultData = new HashMap();
		resultData.put("Data", formatXML(message));
		
		File file = null;
		try {
			file = FileWriterUtil.writeToTxt(filePath, resultData);
			
			//上传文件
			FtpUtils ftpUtil = FtpUtils.getDefaultFtp();

			boolean result = ftpUtil.uploadFile(file.getPath(),
					uploadPath, file.getName());

			if (result) {
				file.delete();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//格式化xml字符串输出
	protected String formatXML(String inputXML) {
		
		if(inputXML == null || inputXML.isEmpty()){
			return "【无内容！】";
		}
		
        SAXReader reader = new SAXReader();  
        Document document = null;
		try {
			document = reader.read(new StringReader(inputXML));
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  
        String requestXML = null;  
        XMLWriter writer = null;  
        if (document != null) {  
          try {  
            StringWriter stringWriter = new StringWriter();  
            OutputFormat format = new OutputFormat(" ", true);  
            writer = new XMLWriter(stringWriter, format);  
            writer.write(document);  
            writer.flush();  
            requestXML = stringWriter.getBuffer().toString();  
          }  catch (Exception e) {  
        	  e.printStackTrace();
          }  finally {  
            if (writer != null) {  
              try {  
                writer.close();  
              } catch (IOException e) {  
              }  
            }  
          }  
        }  
        return requestXML;  
      }
	
}