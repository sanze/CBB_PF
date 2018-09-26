package com.foo.IService;

import java.util.Map;

import com.foo.common.CommonException;
import com.foo.model.suningModel.LogisticsCrossbuyTask.AddLogisticsTaskStatusResponse;
import com.suning.api.entity.logistics.LogisticsTaskStatusAddRequest;

public interface ISuningManagerService {
	/**
	 * @param params: Integer start, Integer limit, ...
	 * @return Map.key: Integer total, List<Map<String,Object>> rows
	 * @throws CommonException
	 */
	public Map<String,Object> getAllLogisticses(Map<String,Object> params) throws CommonException;
	/**
	 * @param params: Integer LOGISTICS_ID, ...
	 * @throws CommonException
	 */
	public void delLogistics(Map<String,Object> params) throws CommonException;
	/**
	 * @param logistics
	 * @throws CommonException
	 */
	public void setLogistics(Map<String,Object> logistics,boolean statusOnly) throws CommonException;
	/**
	 * @param logistics
	 * @throws CommonException
	 */
	public void addLogistics(Map<String,Object> logistics) throws CommonException;
	/**
	 * @param params: Integer start, Integer limit, ...
	 * @return Map.key: Integer total, List<Map<String,Object>> rows
	 * @throws CommonException
	 */
	public Map<String,Object> getAllLogisticsTasks(Map<String,Object> params) throws CommonException;
	
	/**
	 * 从苏宁获取数据运单回执信息
	 * 
	 * @return
	 * @throws CommonException
	 */
	public AddLogisticsTaskStatusResponse getLogisticsTaskStatusAddResponse(LogisticsTaskStatusAddRequest request) throws CommonException;
}
