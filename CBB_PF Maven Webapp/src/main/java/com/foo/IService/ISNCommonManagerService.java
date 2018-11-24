package com.foo.IService;

import java.util.List;
import java.util.Map;

import com.foo.common.CommonException;

public interface ISNCommonManagerService {
	
	/**
	 * @param params: Integer start, Integer limit, ...
	 * @return Map.key: Integer total, List<Map<String,Object>> rows
	 * @throws CommonException
	 */
	public Map<String,Object> getAllLoads(Map<String,Object> params) throws CommonException;
	
	/**
	 * 修改codeName显示名称
	 * @param dataList
	 */
	public void modify(List<Map> dataList) throws CommonException;
	
	/**
	 * 修改codeName显示名称
	 * @param dataList
	 */
	public void send(List<Map> dataList) throws CommonException;


	public Map<String,Object> getAllSku(Map<String,Object> params) throws CommonException;
	public Map<String,Object> getAllReceipt(Map<String,Object> params) throws CommonException;
	public Map<String,Object> getAllInventory(Map<String,Object> params) throws CommonException;


}
