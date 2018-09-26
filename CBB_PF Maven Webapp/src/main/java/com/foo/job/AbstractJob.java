package com.foo.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.quartz.Job;

import com.foo.common.CommonDefine;
import com.foo.dao.mysql.CommonManagerMapper;


/**
 * @author xuxiaojun
 *
 */
public abstract class AbstractJob implements Job{
	
	protected CommonManagerMapper commonManagerMapper;
	
	private void insertGuiRelData(String guids,String guidc){
		
		// 在t_guid_rel表中添加数据
		Map guidRel = new HashMap();
		guidRel.put("GUID_REL_ID", null);
		guidRel.put("GUIDS", guids);
		guidRel.put("GUIDC", guidc);
		guidRel.put("CREAT_TIME", new Date());
		
		Map primary=new HashMap();
		primary.put("primaryId", null);
		commonManagerMapper.insertTableByNVList("t_guid_rel",
				new ArrayList<String>(guidRel.keySet()),
				new ArrayList<Object>(guidRel.values()),
				primary);
	}
	
	// 截取[]中数据
	private String getGuidc(String source) {
		String guid = source;

		if (source.contains("[") && source.contains("]")) {
			Pattern p = Pattern.compile("\\[(.*?)\\]");
			Matcher m = p.matcher(source);
			while (m.find()) {
				guid = m.group(1);
				break;
			}
		}
		return guid;
	}
	
	//物流报文解析专用--弃用
	protected GuidCheckResult operateGuidFor5X(String receiptGuid,Map dataMap,String returnInfo,int type){
		
		GuidCheckResult result = new GuidCheckResult();
		
		String guid = null;
		// 是否第一次收到回执
		boolean isFirstReceipt = true;
		//如果是501的回执报文
		if(CommonDefine.CEB501 == type){
			if(dataMap.get("GUIDC") != null){
				guid = getGuidc(dataMap.get("GUIDC").toString()).split(",")[0];
				isFirstReceipt = false;
			}else{
				guid = dataMap.get("GUID").toString();
			}
		}
		else if(CommonDefine.CEB503 == type){
			if(dataMap.get("GUIDC") != null){
				String[] guidList = getGuidc(dataMap.get("GUIDC").toString()).split(",");
				if(guidList.length == 2){
					guid = getGuidc(dataMap.get("GUIDC").toString()).split(",")[1];
					isFirstReceipt = false;
				}else{
					guid = dataMap.get("GUID").toString();
				}
			}else{
				guid = dataMap.get("GUID").toString();
			}
		}
		// 插入rel数据
		if (isFirstReceipt) {
			if(receiptGuid.equals(guid)){
				// 从数据库中取得的数据
				String guids = dataMap.get("GUID").toString();
				// 从回执报文中获得的数据
				String guidc = getGuidc(returnInfo);
				// 在t_guid_rel表中添加数据
				if(CommonDefine.CEB501 == type){
					insertGuiRelData(guids,guidc);
				}else if(CommonDefine.CEB503 == type){
					//更新原有数据
					commonManagerMapper.updateGuiRelDataFor503(guids,guidc);
				}
			}else{
				//循环下一条标记
				result.setGetNext(true);
			}
		}
		//设置guid
		result.setGuid(guid);
		return result;
	}
	
	
	protected GuidCheckResult operateGuid(String receiptGuid,Map dataMap,String returnInfo){
		
		GuidCheckResult result = new GuidCheckResult();
		
		String guid;
		// 是否第一次收到回执
		boolean isFirstReceipt = true;
		if (dataMap.get("GUIDC") != null) {
			guid = getGuidc(dataMap.get("GUIDC").toString());
			isFirstReceipt = false;
		} else {
			guid = dataMap.get("GUID").toString();
		}
		// 插入rel数据
		if (isFirstReceipt) {
			if(receiptGuid.equals(guid)){
				// 从数据库中取得的数据
				String guids = dataMap.get("GUID").toString();
				// 从回执报文中获得的数据
				String guidc = getGuidc(returnInfo);
				// 在t_guid_rel表中添加数据
				insertGuiRelData(guids,guidc);
//				commonManagerMapper.insertGuidRelData(guidRel);
			}else{
				//循环下一条标记
				result.setGetNext(true);
			}
		}
		//设置guid
		result.setGuid(guid);
		return result;
	}
	
}