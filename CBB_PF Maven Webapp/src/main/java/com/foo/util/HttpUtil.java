package com.foo.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import com.foo.common.CommonException;
import com.foo.common.MessageCodeDefine;

public class HttpUtil {

		private static boolean printAble = Boolean.parseBoolean(CommonUtil.getSystemConfigProperty("printRequestInfo"));

		public static Map doPost(String url,Map<String,Object> head,Map<String,Object> body){
			HttpClient httpClient = null;
			HttpPost httpPost = null;
			String result = null;
			
			if(printAble){
			System.out.println("【请求url】:"+url);
			System.out.println("【请求head】:"+head);
			System.out.println("【请求body】:"+body);
			}
			
			try{
				httpClient = new SSLClient();
				httpPost = new HttpPost(url);
				//设置头域
				List<Header> headers = new ArrayList<Header>();
				Iterator it = head.entrySet().iterator();
				while(it.hasNext()){
					Entry<String,String> element = (Entry<String, String>) it.next();
					headers.add(new BasicHeader(element.getKey(),element.getValue()));
				}
				httpClient.getParams().setParameter(ClientPNames.DEFAULT_HEADERS, headers);
				//设置body
				JSONObject param = JSONObject.fromObject(body);

				StringEntity se = new StringEntity(param.toString());
				se.setContentEncoding("UTF-8");
				se.setContentType("application/json");
				httpPost.setEntity(se);
				
				//发送请求
				HttpResponse response = httpClient.execute(httpPost);
				
				if(response!=null){
					HttpEntity resEntity = response.getEntity();
					if(resEntity!=null){
						result = EntityUtils.toString(resEntity,"UTF-8");
					}
				}
				System.out.println("【Result】:"+result);
				System.out.println("***************************分割线*******************************************************");
			}catch(Exception ex){
				ex.printStackTrace();
			}
			return parseResult_json(result);
		}
		
		public static String doPost4TJ(String url,Map<String,Object> head,String body,boolean isSSL){
			HttpClient httpClient = null;
			HttpPost httpPost = null;
			String result = null;
			
			if(printAble){
			System.out.println("【请求url】:"+url);
			System.out.println("【请求head】:"+head);
			System.out.println("【请求body】:"+body);
			}
			
			try{
				if(isSSL){
					httpClient = new SSLClient();
				}else{
				httpClient = new DefaultHttpClient();
				}
				httpPost = new HttpPost(url);
				//设置头域
				List<Header> headers = new ArrayList<Header>();
				Iterator it = head.entrySet().iterator();
				while(it.hasNext()){
					Entry<String,String> element = (Entry<String, String>) it.next();
					headers.add(new BasicHeader(element.getKey(),element.getValue()));
				}
				httpClient.getParams().setParameter(ClientPNames.DEFAULT_HEADERS, headers);

				StringEntity se = new StringEntity(body);
				se.setContentEncoding("UTF-8");
//				se.setContentType("application/json");
				httpPost.setEntity(se);
				
				//发送请求
				HttpResponse response = httpClient.execute(httpPost);
				
				if(response!=null){
					HttpEntity resEntity = response.getEntity();
					if(resEntity!=null){
						result = EntityUtils.toString(resEntity,"UTF-8");
					}
				}
				System.out.println("【Result】:"+result);
				System.out.println("***************************分割线*******************************************************");
			}catch(Exception ex){
				ex.printStackTrace();
			}
			return result;
		}
		
		
		private static Map<String,Object> parseResult_json(String result){
			Map<String,Object> r = new LinkedHashMap<String,Object>();
			
			if(result!=null && !result.isEmpty()){
				JSONObject obj = new JSONObject();
				obj = obj.fromObject(result);
				for(Object key:obj.keySet()){
					r.put(key.toString(),obj.get(key));
				}
			}
			return r;
		}
		
		
		//http请求调用webservice
		public static String sendHttpCMD_CJ(String xmlString,String requestUrl) throws CommonException {
			String result = "";
			
			PostMethod postMethod = new PostMethod(requestUrl);
			try {
				
				// 然后把Soap请求数据添加到PostMethod中
				byte[] b = xmlString.getBytes("utf-8");
				InputStream is = new ByteArrayInputStream(b,0,b.length);
				RequestEntity re = new InputStreamRequestEntity(is,b.length,"application/soap+xml; charset=utf-8");
				postMethod.setRequestEntity(re);

				// 最后生成一个HttpClient对象，并发出postMethod请求
				org.apache.commons.httpclient.HttpClient httpClient = new org.apache.commons.httpclient.HttpClient();
				int statusCode = httpClient.executeMethod(postMethod);
				if (statusCode == 200) {
					String soapResponseData = postMethod.getResponseBodyAsString();
//					System.out.println("request xml String:"+xmlString);
//					System.out.println("reponse xml String:"+soapResponseData);
//					result = XmlUtil.getResponseFromXmlString(soapResponseData,messageType);
					result = soapResponseData;

					if(result == null){
						//抛出错误信息
						throw new CommonException(new Exception(),
								MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR, "无回执信息！");
					}else{
//						System.out.println("reponse result String:"+result);
					}
					
				} else {
					//抛出错误信息
					throw new CommonException(new Exception(),
							MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR, "调用失败！错误码：" + statusCode);
				} 
			} catch (CommonException e) {
				throw e;
			} catch (Exception e) {
				e.printStackTrace();
			}	finally {
				try {
					System.out.println(StringEscapeUtils.unescapeXml(postMethod.getResponseBodyAsString()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return result;
		}
		
		

		
		public static void main(String args[]) throws UnsupportedEncodingException, NoSuchAlgorithmException{

			JSONObject param = new JSONObject();
			param.put("com", "emsen");
			param.put("num", "CI160872127JP");

			String customer = "ADB5D6A4727EDE79EF585B534DA1DEFB";
			String key = "zudoJhZX7023";
			String sign = CommonUtil.encryptionMD5(param.toString()+key+customer);

			String url = "http://poll.kuaidi100.com/poll/query.do";
			
			
			String requestUrl = url + "?" + "customer=" + customer + "&sign="
					+ sign.toUpperCase() + "&param=" + URLEncoder.encode(param.toString(), "utf-8");
			
			Map<String,Object> head = new HashMap<String,Object>();

			String result = HttpUtil.doPost4TJ(requestUrl,head,"",false);
			System.out.println(result);
			
		}
		
		
		
}
