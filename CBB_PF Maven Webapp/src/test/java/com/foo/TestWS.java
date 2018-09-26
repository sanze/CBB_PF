package com.foo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.junit.Test;
import org.xml.sax.SAXException;

public class TestWS {
	
	private String requestUrl = "http://127.0.0.1:8080/CBB_PF/WS/CBB_PF_WS?wsdl";
//	private String requestUrl = "http://221.226.159.219:33789/CBB_PF/WS/CBB_PF_WS?wsdl";
	
	@Test
	public void testGetOrderReceipt() {
		String xml = getDataFromXmlFile(null,"SNT301");
		
//		System.out.println(xml);
		
//		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ParseXml><xml><SNT101Message><OrderHead><EbpCode>1234567890</EbpCode><EbcCode>1234567890</EbcCode><OrderType>1</OrderType><OrderNo>2</OrderNo><GoodsInfo>1231</GoodsInfo><Consignee>陈似的</Consignee><ConsigneeCode>3202191093873487</ConsigneeCode><ConsigneeTelephone>莲花新城</ConsigneeTelephone><ConsigneeCountry>123</ConsigneeCountry><ConsigneeProvince>143</ConsigneeProvince><ConsigneeCity>条达成</ConsigneeCity><ConsigneeDistrict>320219198601221514</ConsigneeDistrict><ConsigneeAddress>高尔夫</ConsigneeAddress><GoodsValue>1</GoodsValue><TaxFee>0.3</TaxFee><Freight>2423</Freight><InsuredFee>123</InsuredFee><Currency>123</Currency><NetWeight>1.1</NetWeight><Note>123</Note></OrderHead><OrderList><Order><GNum>1111</GNum><ItemNo>G111113434</ItemNo><GName>洗衣机</GName><Qty>1.00000</Qty><Price>123.00000</Price><Total>123.00000</Total><BarCode>1</BarCode><Lottable1>234234</Lottable1><Note>123</Note></Order><Order><GNum>2222</GNum><ItemNo>G111113434</ItemNo><GName>洗衣机</GName><Qty>1.00000</Qty><Price>123.00000</Price><Total>123.00000</Total><BarCode>1</BarCode><Lottable1>234234</Lottable1><Note>123</Note></Order></OrderList></SNT101Message></xml><fileType>SNT101</fileType></ParseXml>";
		
		System.out.println(formatXML(sendHttpCMD(xml)));
	}
	
//	@Test
	public void validateXml(String fileType) {

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
//			XmlValidateErrorHandler errorHandler = new XmlValidateErrorHandler();
//	          validator.setErrorHandler(errorHandler);
			// 得到验证的数据源
			Source sourceFile = new StreamSource(Thread
					.currentThread()
					.getContextClassLoader()
					.getResourceAsStream(
							"xmlDataSource/"+fileType+".xml"));
			// 开始验证，成功输出success!!!，失败输出fail
			validator.validate(sourceFile);
			
		} catch (SAXException e) {
			System.out.println("校验错误："+e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	// http请求调用webservice
	private String sendHttpCMD(String xmlString){
		String result = "";
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
		    obj = client.invoke("ParseXml", new Object[]{xmlString});
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(obj!=null && obj.length>0){
			result = (String)obj[0];
		}
		return result;
	}
	
	private String getDataFromXmlFile(String testDataFilePath,String fileType){

		InputStream in = null;
		Document document = null;
		SAXReader saxReader = new SAXReader();
		try {
			//配置了测试数据地址，使用外部数据
			if(testDataFilePath!=null){
				 File file = new File(testDataFilePath);
				 in = new FileInputStream(file);
			}else{
				in = Thread
						.currentThread()
						.getContextClassLoader()
						.getResourceAsStream(
								"xmlDataSource/"+fileType+".xml");
			}
			document = saxReader.read(in);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String xml = document.asXML();
		
		return xml;
	
	}
	
	//格式化xml字符串输出
	private static String formatXML(String inputXML) {
        SAXReader reader = new SAXReader();  
        Document document = null;
		try {
			document = reader.read(new StringReader(inputXML));
		} catch (DocumentException e1) {
			return "result is"+inputXML;
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
	
	public static void main(String args[]){
//		String time = "Tue Jun 28 10:16:06 CTS 2016";
		String time="Thu May 28 18:23:17 CST 2016";
		String xx = null;
		Date date = null;
		SimpleDateFormat sf= new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
		SimpleDateFormat sf2= new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			date = sf.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Calendar   ca   =   new   GregorianCalendar(); 	
		ca.setTime(date);
		ca.add(Calendar.DATE, 1);
		date = ca.getTime();
//		System.out.print(sf2.parse(sf2.format(date)));
		
	}

}
