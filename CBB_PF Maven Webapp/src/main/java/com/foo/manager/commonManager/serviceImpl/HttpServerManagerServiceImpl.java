package com.foo.manager.commonManager.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.foo.common.CommonDefine;
import com.foo.common.CommonException;
import com.foo.common.MessageCodeDefine;
import com.foo.handler.ExceptionHandler;
import com.foo.manager.commonManager.model.RequestParamModel;
import com.foo.manager.commonManager.service.HttpServerManagerService;
import com.foo.manager.commonManager.thread.HttpHandleThread;
import com.foo.util.CommonUtil;
import com.sun.net.httpserver.HttpExchange;

/**
 * @author xuxiaojun
 *
 */
@Service
public class HttpServerManagerServiceImpl extends HttpServerManagerService{


	@Override
	public void handle(HttpExchange exchange) throws IOException {
		
		RequestParamModel result = new RequestParamModel();
		// 获得查询字符串
		String queryString = exchange.getRequestURI().getRawQuery();

		InputStream in = exchange.getRequestBody();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));   
        String queryBody = "";
        String temp = null;
        while((temp = reader.readLine()) != null) {   
        	queryBody = queryBody+temp;
        }
		
		OutputStream os = exchange.getResponseBody();
		try {
			// 解析参数
			result = validateParam(queryString,queryBody);
			//
			if (result.getErrorCode() == CommonDefine.SUCCESS) {
				// 添加采集进程
				FutureTask<Object> future = new FutureTask<Object>(
						new HttpHandleThread(result.getRequestType(),result.getLogistics_interface(),
								result.getData_digest()));
				//多线程报文处理
				executorService.submit(future);
				
				Object data = null;
				
				data = future.get(10, TimeUnit.MINUTES);
				
				if(data != null){
					exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, data.toString().getBytes("utf-8").length);
					os.write(data.toString().getBytes("utf-8"));
					
				}
			} else {
				exchange.sendResponseHeaders(200, 0);
				os.write(result.getErrorMessage().getBytes("utf-8"));
			}
			
		} catch (Exception e) {
			ExceptionHandler.handleException(e);
		} finally {
			System.out.println("【URI】:"+queryString);
			System.out.println("【queryBody】:"+queryBody);
			try {
				os.flush();  
				exchange.close(); 
			} catch (Exception e) {
				ExceptionHandler.handleException(e);
			}
			
		}
	}

	//参数校验
	private RequestParamModel validateParam(String queryString,String queryBody)
			throws CommonException {

		RequestParamModel result = new RequestParamModel();

		String requestType = "";
		
		try {
			//获取requestType
			if(!queryString.isEmpty() && queryString.contains("=")){
				String[] params = queryString.split("&");
				
				for(String param :params){
					String[] keyValue = param.split("=");
					if(keyValue.length>1 && keyValue[0].equals("requestType")){
						requestType = keyValue[1];
						break;
					}
				}
			}else{
				result.setErrorCode(CommonDefine.FAILED);
				result.setErrorMessage("请求URL错误！");
				return result;
			}

			//校验requestType
			if(requestType.isEmpty()){
				result.setErrorCode(CommonDefine.FAILED);
				result.setErrorMessage("请求URL错误！");
				return result;
			}else{
				if(requestType.equals(requestType_Order)){
					if(!queryBody.contains("ordersList")){
						result.setErrorCode(CommonDefine.FAILED);
						result.setErrorMessage("请求URL与内容不符！");
						return result;
					}
				}
				else if(requestType.equals(requestType_inventory)){
					if(!queryBody.contains("inventoryOrder")){
						result.setErrorCode(CommonDefine.FAILED);
						result.setErrorMessage("请求URL与内容不符！");
						return result;
					}
				}else if(requestType.equals(requestType_load)){
					if(!queryBody.contains("LoadHead")){
						result.setErrorCode(CommonDefine.FAILED);
						result.setErrorMessage("请求URL与内容不符！");
						return result;
					}
				}else if(requestType.equals(requestType_listRelease)){
					if(!queryBody.contains("InventoryReturn")){
						result.setErrorCode(CommonDefine.FAILED);
						result.setErrorMessage("请求URL与内容不符！");
						return result;
					}
				}else if(requestType.equals(requestType_sn_sku)){
					if(!queryBody.contains("cmmdtyInfo")){
						result.setErrorCode(CommonDefine.FAILED);
						result.setErrorMessage("请求URL与内容不符！");
						return result;
					}
				}else if(requestType.equals(requestType_sn_receipt)){
					if(!queryBody.contains("orderInfo")){
						result.setErrorCode(CommonDefine.FAILED);
						result.setErrorMessage("请求URL与内容不符！");
						return result;
					}
				}else if(requestType.equals(requestType_sn_warehousing)){
					if(!queryBody.contains("orderInfo")){
						result.setErrorCode(CommonDefine.FAILED);
						result.setErrorMessage("请求URL与内容不符！");
						return result;
					}
				}else{
					result.setErrorCode(CommonDefine.FAILED);
					result.setErrorMessage("请求URL错误！");
					return result;
				}
				
				result.setRequestType(requestType);
			}
			
			//天津发送的清单状态回执暂不校验参数
			if(requestType.equals(requestType_listRelease)){
				String[] params = queryBody.split("&");
				Map<String, String> pairs = new HashMap<String, String>();
				if (params.length > 1) {
					for (String param : params) {
						String[] pair = param.split("=");
						if (pair.length == 2) {
							pairs.put(pair[0],pair[1]);
						} else {
							continue;
						}
					}
				}
				result.setLogistics_interface(pairs.get("data"));
//				result.setData_digest(pairs.get(""));
				result.setErrorCode(CommonDefine.SUCCESS);
			}
			// 获取参数字符串
			else if (queryBody != null && queryBody.length() > 0
					&& queryBody.contains("&") && queryBody.contains("=")
					&& queryBody.contains(requestParame_1)
					&& queryBody.contains(requestParame_2)) {

				String[] params = queryBody.split("&");
				Map<String, String> pairs = new HashMap<String, String>();
				if (params.length > 1) {
					for (String param : params) {
						String[] pair = param.split("=");
						if (pair.length == 2) {
							pairs.put(pair[0],
									URLDecoder.decode(pair[1], "utf-8"));
						} else {
							continue;
						}
					}
				} else {
					result.setErrorCode(CommonDefine.FAILED);
					result.setErrorMessage("参数错误！");
				}

				String logistics_interface = pairs.get(requestParame_1);
				String data_digest = pairs.get(requestParame_2);

				if (data_digest
						.equals(CommonUtil.makeSign(logistics_interface))) {
					result.setLogistics_interface(pairs.get(requestParame_1));
					result.setData_digest(pairs.get(requestParame_2));
					result.setErrorCode(CommonDefine.SUCCESS);
				} else {
					result.setErrorCode(CommonDefine.FAILED);
					result.setErrorMessage("签名与内容不符！");
				}

			} else {
				result.setErrorCode(CommonDefine.FAILED);
				result.setErrorMessage("参数错误！");
			}
		} catch (Exception e) {
			throw new CommonException(e,
					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR);
		}

		return result;
	}
}
