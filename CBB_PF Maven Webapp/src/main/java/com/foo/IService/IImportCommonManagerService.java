package com.foo.IService;

import java.util.Map;

import com.foo.common.CommonException;

public interface IImportCommonManagerService {
	
	/**
	 * @param params: Integer start, Integer limit, ...
	 * @return Map.key: Integer total, List<Map<String,Object>> rows
	 * @throws CommonException
	 */
	public Map<String,Object> getAllDeliveries(Map<String,Object> params) throws CommonException;

	/**
	 * @param params: Integer DELIVERY_ID, ...
	 * @throws CommonException
	 */
	public void delDelivery(Map<String,Object> params) throws CommonException;

	/**
	 * @param delivery
	 * @throws CommonException
	 */
	public void setDelivery(Map<String,Object> delivery,boolean statusOnly) throws CommonException;

	/**
	 * @param delivery
	 * @throws CommonException
	 */
	public void addDelivery(Map<String,Object> delivery) throws CommonException;
	
	
	/**
	 * @param params
	 * @throws CommonException
	 */
	public void batchSubmit_SKU(Map<String,Object> params) throws CommonException;
	
	
	/**
	 * @param order
	 * @throws CommonException
	 */
	public void setOrder(Map<String,Object> order,boolean statusOnly) throws CommonException;
	/**
	 * @param order
	 * @throws CommonException
	 */
	public void addOrder(Map<String,Object> order) throws CommonException;
	/**
	 * @param params: Integer start, Integer limit, ...
	 * @return Map.key: Integer total, List<Map<String,Object>> rows
	 * @throws CommonException
	 */
	public Map<String,Object> getAllOrders(Map<String,Object> params) throws CommonException;
	/**
	 * @param params: Integer SKU_ID, ...
	 * @throws CommonException
	 */
	public void delOrder(Map<String,Object> params) throws CommonException;
	
	/**
	 * @param params
	 * @throws CommonException
	 */
	public void batchSubmit_ORDER(Map<String,Object> params) throws CommonException;
	
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
	public Map<String,Object> getAllInventorys(Map<String,Object> params) throws CommonException;
	/**
	 * @param params: Integer INVENTORY_ID, ...
	 * @throws CommonException
	 */
	public void delInventory(Map<String,Object> params) throws CommonException;
	
	/**
	 * @param params
	 * @throws CommonException
	 */
	public void batchSubmit_INVENTORY(Map<String,Object> params) throws CommonException;

	/**
	 * @param inventory
	 * @throws CommonException
	 */
	public void setInventory(Map<String,Object> inventory,boolean statusOnly) throws CommonException;
	/**
	 * @param inventory
	 * @throws CommonException
	 */
	public void addInventory(Map<String,Object> inventory) throws CommonException;
	
	/**
	 * @param params
	 * @throws CommonException
	 */
	public void batchSubmit_LOGISTICS(Map<String,Object> params) throws CommonException;
	
	/**
	 * @param params
	 * @throws CommonException
	 */
	public void batchSubmit_LOGISTICS_STATUS(Map<String,Object> params) throws CommonException;
	
	/**
	 * @param params
	 * @throws CommonException
	 */
	public void applyExpressNo_LOGISTICS(Map<String,Object> params) throws CommonException;
	
	/**
	 * @param params: Integer start, Integer limit, ...
	 * @return Map.key: Integer total, List<Map<String,Object>> rows
	 * @throws CommonException
	 */
	public Map<String,Object> getAllPayes(Map<String,Object> params) throws CommonException;
	
	/**
	 * @param pay
	 * @throws CommonException
	 */
	public void setPay(Map<String,Object> pay) throws CommonException;
	/**
	 * @param pay
	 * @throws CommonException
	 */
	public void addPay(Map<String,Object> pay) throws CommonException;
	
	/**
	 * @param params: Integer PAY_ID, .
	 * @throws CommonException
	 */
	public void delPay(Map<String,Object> params) throws CommonException;
}
