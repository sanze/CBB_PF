package com.foo.job;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.foo.dao.mysql.CommonManagerMapper;
import com.foo.dao.mysql.SNCommonManagerMapper;
import com.foo.manager.commonManager.thread.HttpHandleThread;
import com.foo.util.CommonUtil;
import com.foo.util.HttpUtil;
import com.foo.util.SpringContextUtil;

public class ScanKD100Status extends AbstractJob {

	private static SNCommonManagerMapper snCommonManagerMapper;
	
	private static String kd100_customer = CommonUtil.getSystemConfigProperty("kd100_customer");
	private static String kd100_key = CommonUtil.getSystemConfigProperty("kd100_key");
	private static String kd100_url = CommonUtil.getSystemConfigProperty("kd100_url");
	private static String kd100_com = CommonUtil.getSystemConfigProperty("kd100_com");
	
	public ScanKD100Status() {
		commonManagerMapper = (CommonManagerMapper) SpringContextUtil
				.getBean("commonManagerMapper");
		
		snCommonManagerMapper = (SNCommonManagerMapper) SpringContextUtil
		.getBean("SNCommonManagerMapper");
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
//		System.out.println(new Date() + "我在执行搜索需要查询包裹状态的订单！");

		List<Map> needUpdateEmsStatusOrders = snCommonManagerMapper
				.selectNeedUpdateEmsStatusOrder();

		for (Map order : needUpdateEmsStatusOrders) {

			JSONObject param = new JSONObject();
			param.put("com", kd100_com);
			param.put("num", order.get("TMS_ORDER_CODE"));
			String sign = CommonUtil.encryptionMD5(param.toString() + kd100_key
					+ kd100_customer);
			String requestUrl = null;
			try {
				requestUrl = kd100_url + "?" + "customer=" + kd100_customer
						+ "&sign=" + sign.toUpperCase() + "&param="
						+ URLEncoder.encode(param.toString(), "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String result = HttpUtil.doPost4TJ(requestUrl,
					new HashMap<String, Object>(), "", false);
			
//			String result = "{\"message\":\"ok\",\"nu\":\"W107xxxxxx\",\"ischeck\":\"1\",\"condition\":\"F00\",\"com\":\"emsen\",\"status\":\"200\",\"state\":\"3\",\"data\":[{\"time\":\"2018-10-07 16:25:54\",\"ftime\":\"2018-10-07 16:25:54\",\"context\":\"【JiangSuShengNanJingShi BaiJiaHu】 Delivery\"},{\"time\":\"2018-10-07 16:22:06\",\"ftime\":\"2018-10-07 16:22:06\",\"context\":\"【JiangSuShengNanJingShi BaiJiaHu】 Delivery arranged\"},{\"time\":\"2018-10-04 18:20:07\",\"ftime\":\"2018-10-04 18:20:07\",\"context\":\"【JiangSuShengNanJingShi BaiJiaHu】 Attempted delivery\"},{\"time\":\"2018-10-04 18:08:04\",\"ftime\":\"2018-10-04 18:08:04\",\"context\":\"【JiangSuShengNanJingShi BaiJiaHu】 Delivery arranged\"},{\"time\":\"2018-10-04 10:28:00\",\"ftime\":\"2018-10-04 10:28:00\",\"context\":\"【NANJING】 Held by Customs\"},{\"time\":\"2018-09-15 11:18:00\",\"ftime\":\"2018-09-15 11:18:00\",\"context\":\"【JAPAN JPJPKWSI】 Despatch from Sorting Center\"},{\"time\":\"2018-09-15 11:16:00\",\"ftime\":\"2018-09-15 11:16:00\",\"context\":\"【JAPAN JPJPKWSA】 Arrival at Sorting Center\"},{\"time\":\"2018-09-14 22:09:00\",\"ftime\":\"2018-09-14 22:09:00\",\"context\":\"【JP5500022】 Posting\"}]}";
			synchronized(ScanKD100Status.class){
			handleKD100ReturnMessage(result);
		}
	}
	}
	
	private void handleKD100ReturnMessage(String jsonString){
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		
		List<String> colNames = new ArrayList<String>();
		List<Object> colValues = new ArrayList<Object>();
		//消息正常返回
		if("ok".equals(jsonObject.getString("message").toLowerCase())){
			//state的值回填在t_sn_order.EMS_STATE
			String state = jsonObject.getString("state");
			String tmsOrderCode = jsonObject.getString("nu");
			colNames.add("EMS_STATE");
			colValues.add(state);
			commonManagerMapper.updateTableByNVList("t_sn_order", "TMS_ORDER_CODE", tmsOrderCode, colNames, colValues);
			
			//插入快递信息到 t_sn_return_status
			JSONArray dataList = jsonObject.getJSONArray("data");
			
			//查询已有数据
			//搜索“nu”的值等于ORDER_CODE，“time”的值等于STATUS_TIME的记录行
			List<Map<String,Object>> searchDataList = commonManagerMapper.selectTableListByCol("t_sn_return_status", "ORDER_CODE", tmsOrderCode, null, null);
			
			for(int i = 0;i<dataList.size();i++){
				JSONObject data = dataList.getJSONObject(i);
				String time = data.getString("time");
				String context = data.getString("context");
				//搜索“nu”的值等于ORDER_CODE，“time”的值等于STATUS_TIME的记录行
				boolean isfound = false;
				for(Map searchData:searchDataList){
					if(searchData.get("STATUS_TIME")!=null && time.equals(searchData.get("STATUS_TIME").toString())){
						isfound = true;
						break;
					}else{
						continue;
					}
				}
				//如果不存在插入数据
				if(!isfound){
					
					if(context.contains("】 ")){
						String[] xx = context.split("】 ");
						if(xx.length == 2){
							context = xx[1];
						}
					}
					
					colNames.clear();
					colValues.clear();
					colNames.add("ORDER_CODE");
					colValues.add(tmsOrderCode);
					colNames.add("STATUS_CODE");
					colValues.add("01");
					colNames.add("STATUS_TIME");
					colValues.add(time);
					colNames.add("STATUS_REMARK");
					colValues.add(context);
					colNames.add("CREAT_TIME");
					colValues.add(new Date());
					
					Map primary = new HashMap();
					primary.put("primaryId", null);
					commonManagerMapper.insertTableByNVList("t_sn_return_status", colNames, colValues, primary);
						
					//发送出库单状态给苏宁  7.10
					send2SN(tmsOrderCode,time,context);
				}else{
					//如果存在，什么也不做
				}
			}
		}
	}
	//发送出库单状态给苏宁  7.10
	private void send2SN(String tmsOrderCode,String time,String context){
		
		//查找翻译的address
		List<Map<String,Object>> dataList = commonManagerMapper.selectTableListByCol("t_sn_ems_translate", "TRANSLATE_IN", context, null, null);
		if(dataList.size()>0){
			if(dataList.get(0).get("TRANSLATE_OUT")!=null){
				context = dataList.get(0).get("TRANSLATE_OUT").toString();
			}
		}
		
		JSONObject statusSingleItem = new JSONObject();
		statusSingleItem.put("time", time);
		statusSingleItem.put("address", context);
		statusSingleItem.put("expressNo", tmsOrderCode);
		statusSingleItem.put("mailStatus", "");
		statusSingleItem.put("orderId", "");
		statusSingleItem.put("expressCompanyCode", "Z05");
		statusSingleItem.put("signer", "");
		statusSingleItem.put("remark", "");
		statusSingleItem.put("expressName", "");
		statusSingleItem.put("expressPhone", "");
		statusSingleItem.put("statusType", "TMS");
		
		JSONArray statusListArray = new JSONArray();
		statusListArray.add(statusSingleItem);
		
		JSONObject statusList = new JSONObject();
		statusList.put("statusList", statusListArray);
		
		JSONObject content = new JSONObject();
		content.put("snRequest", statusList);

		Map requestParam = new HashMap();
		requestParam
				.put("logistic_provider_id",
						CommonUtil
								.getSystemConfigProperty("SN_snRequest_logistic_provider_id"));
		requestParam.put("msg_type", CommonUtil
				.getSystemConfigProperty("SN_snRequest_msg_type"));
		requestParam.put("url", CommonUtil
				.getSystemConfigProperty("SN_snRequest_requestUrl"));

		System.out.println(content.toString());
		// 发送请求
		String result = HttpHandleThread.send2SN(requestParam, content.toString());
	}

}
