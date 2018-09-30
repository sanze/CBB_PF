package com.foo.manager.commonManager.serviceImpl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.springframework.stereotype.Service;

import com.foo.IService.ISNCommonManagerService;
import com.foo.common.CommonDefine;
import com.foo.common.CommonException;
import com.foo.common.MessageCodeDefine;
import com.foo.manager.commonManager.service.CommonManagerService;
import com.foo.util.CommonUtil;
import com.foo.util.HttpUtil;
import com.foo.util.XmlUtil;

@Service
public class SNCommonManagerServiceImpl extends CommonManagerService implements ISNCommonManagerService{

	@Override
	public Map<String, Object> getAllLoads(Map<String, Object> params)
			throws CommonException {
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

		int total = 0;

		// 开始
		Integer start = params.get("start") == null ? null : Integer
				.valueOf(params.get("start").toString());
		// 结束
		Integer limit = params.get("limit") == null ? null : Integer
				.valueOf(params.get("limit").toString());

		params.remove("start");
		params.remove("limit");
		try {
			List<String> keys=new ArrayList<String>(params.keySet());
			List<Object> values=new ArrayList<Object>(params.values());
			
			if(!params.containsKey("Fuzzy")){
				rows = commonManagerMapper.selectTableListByNVList(T_SN_LOADS, 
						keys,values,start, limit);
				total = commonManagerMapper.selectTableListCountByNVList(T_SN_LOADS,
						keys,values);
			}else{
				//模糊查询
				params.remove("Fuzzy");
				keys=new ArrayList<String>(params.keySet());
				values=new ArrayList<Object>(params.values());
				rows = commonManagerMapper.selectTableListByNVList_Fuzzy(T_SN_LOADS, 
						keys,values,start, limit);
				total = commonManagerMapper.selectTableListCountByNVList_Fuzzy(T_SN_LOADS,
						keys,values);
			}

//			rows = commonManagerMapper.selectTableListByNVList(T_NJ_SKU, 
//					keys,values,start, limit);
//
//			total = commonManagerMapper.selectTableListCountByNVList(T_NJ_SKU,
//					keys,values);

			Map<String, Object> result = new HashMap<String, Object>();
			result.put("total", total);
			result.put("rows", rows);
			return result;
		} catch (Exception e) {
			throw new CommonException(e,
					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR);
		}
	}
	
	@Override
	public void modify(List<Map> dataList) {
		// 更新数据
		for (Map data : dataList) {
			commonManagerMapper.updateTableByNVList(T_SN_LOADS,
					"LOAD_ID", data.get("LOAD_ID"),
					new ArrayList<String>(data.keySet()),
					new ArrayList<Object>(data.values()));
		}
	}
	
	@Override
	public void send(List<Map> items) {
		SimpleDateFormat sf = CommonUtil.getDateFormatter(CommonDefine.COMMON_FORMAT);
		
		ResourceBundle bundle = CommonUtil.getMessageMappingResource("CEB_SN");
		// 更新数据
		List<Map> dataList = new ArrayList<Map>();
		for (Map item : items) {
			Map data = new HashMap();
			//转换
			for(Object key:item.keySet()){
				if(bundle.containsKey("LOAD_REC_"+key.toString())){
					data.put(bundle.getObject("LOAD_REC_"+key.toString()), item.get(key));
				}else{
					data.put(key, item.get(key));
				}
			}
			data.put("finishedDate", sf.format(new Date()));
			dataList.add(data);
		}
		
		String rootElement = CommonUtil.getSystemConfigProperty("loadStatusRecRoot");
		String firstElement = CommonUtil.getSystemConfigProperty("loadStatusRecFirstElement");
		
		String resultXmlString = XmlUtil.generalReceiptXml_LoadRec_FP(rootElement, firstElement, dataList);
		
		//向苏宁发送报文
		//请求数据
		String data_digest = CommonUtil.makeSign(resultXmlString);
		String logistic_provider_id = CommonUtil.getSystemConfigProperty("SN_LoadRecStatus_logistic_provider_id");
		String msg_type = CommonUtil.getSystemConfigProperty("SN_LoadRecStatus_msg_type");
		String url = CommonUtil.getSystemConfigProperty("SN_LoadRecStatus_requestUrl");
		
		String requestUrl = "";
		try {
			requestUrl = url + "?" + 
				"logistics_interface="+ URLEncoder.encode(resultXmlString, "utf-8") + 
				"&data_digest="+ URLEncoder.encode(data_digest, "utf-8")+ 
				"&logistic_provider_id=" + logistic_provider_id+ 
				"&msg_type=" + msg_type;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 发送http请求
		Map<String, Object> head = new HashMap<String, Object>();
		head.put("Content-Type",
				CommonUtil.getSystemConfigProperty("Content_Type"));

		String result = HttpUtil.doPost4TJ(requestUrl, head, "",false);
		
		System.out.println(result);
	}


}
