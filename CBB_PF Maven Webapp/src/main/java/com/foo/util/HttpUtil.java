package com.foo.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

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

public class HttpUtil {

		public static Map doPost(String url,Map<String,Object> head,Map<String,Object> body){
			HttpClient httpClient = null;
			HttpPost httpPost = null;
			String result = null;
			
			System.out.println("【请求url】:"+url);
			System.out.println("【请求head】:"+head);
			System.out.println("【请求body】:"+body);
			
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
			
			System.out.println("【请求url】:"+url);
			System.out.println("【请求head】:"+head);
			System.out.println("【请求body】:"+body);
			
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
		
		public static void main(String args[]){
//			String userName = "ttkj001";
//			String password = "9ol.)P:?";
//			String appkey = "EXEpBMZH6p3lJ6R6rlLdZ1ft5zD1";
//			String host = "223.87.15.132";
//			String path = "/rest/fastlogin/v1.0";
//			String url = "https://"+host+path+"?app_key="+appkey+"&username="+userName;
//			Map<String,String> head = new HashMap<String,String>();
//			head.put("Authorization", password);
//			head.put("Content-Type", "application/json");
//			Map<String,String> body = new HashMap<String,String>();
//			HttpUtil util = new HttpUtil();
//			Map result = util.doPost(url,head,body);
//			System.out.println(result.toString());
			
		}
		
		
		
}
