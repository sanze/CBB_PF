package com.foo.manager.wsManager.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.Resource;

import com.foo.IService.IWSManagerService;
import com.foo.abstractService.AbstractService;
import com.foo.dao.mysql.CommonManagerMapper;
import com.foo.dao.mysql.ImportCommonManagerMapper;
import com.foo.dao.mysql.NJCommonManagerMapper;
import com.foo.util.CommonUtil;

public abstract class WSManagerService extends AbstractService implements IWSManagerService {
	
	@Resource
	protected CommonManagerMapper commonManagerMapper;
	
	@Resource
	protected NJCommonManagerMapper njCommonManagerMapper;
	
	@Resource
	protected ImportCommonManagerMapper importCommonManagerMapper;
	
	//获取资源
	protected Map<String,String> getBundleData(){
		Map<String,String> bundleData = new HashMap<String,String>();
		
		// 获取资源文件
		ResourceBundle bundle = CommonUtil
				.getMessageMappingResource("CEB_NJ");
		
		for(String key:bundle.keySet()){
			bundleData.put(bundle.getString(key), key);
		}
			
		return bundleData;
	}
	
	//获取资源
	protected Map<String,String> getBundleData_import(){
		Map<String,String> bundleData = new HashMap<String,String>();
		
		// 获取资源文件
		ResourceBundle bundle = CommonUtil
				.getMessageMappingResource("CEB_IMPORT");
		
		for(String key:bundle.keySet()){
			bundleData.put(bundle.getString(key), key);
		}
			
		return bundleData;
	}
	
	//去配置文件查找对应的数据库列名
	protected Map changeDbColumn(Map source){
		//获取列名字典
		Map<String,String> columnDictionary = getBundleData();
		
		Map result = new HashMap();

		for(Object obj:source.keySet()){
			String key = (String)obj;
			result.put(columnDictionary.get(key), source.get(key));
		}
		return result;
	}
	
	//去配置文件查找对应的数据库列名
	protected List<Map> changeDbColumn(List<Map> source){
		//获取列名字典
		Map<String,String> columnDictionary = getBundleData();
		
		List<Map> result = new ArrayList<Map>();
		
		for(Map map:source){
			Map data = new HashMap();
			data = changeDbColumn(map);
			result.add(data);
		}
		return result;
	}
	
	
	//去配置文件查找对应的数据库列名
	protected Map changeDbColumn_import(Map source){
		//获取列名字典
		Map<String,String> columnDictionary = getBundleData_import();
		
		Map result = new HashMap();

		for(Object obj:source.keySet()){
			String key = (String)obj;
			result.put(columnDictionary.get(key), source.get(key));
		}
		return result;
	}
	
	//去配置文件查找对应的数据库列名
	protected List<Map> changeDbColumn_import(List<Map> source){
		//获取列名字典
		Map<String,String> columnDictionary = getBundleData_import();
		
		List<Map> result = new ArrayList<Map>();
		
		for(Map map:source){
			Map data = new HashMap();
			
			if(map.containsKey("itemName")){
				map.put("gname", map.get("itemName"));
			}
			
			if(map.containsKey("itemNameF")){
				map.put("itemName", map.get("itemNameF"));
			}
			
			data = changeDbColumn_import(map);
			result.add(data);
		}
		return result;
	}

}
