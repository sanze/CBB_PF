package com.foo.IService;

import java.util.List;
import java.util.Map;

import com.foo.common.CommonException;

/**
 * @author 晓军
 *
 */
public interface ICommonManagerService {
	
	/**
	 * @param params
	 * @throws CommonException
	 */
	public Map<String, Object> getDataList(Map<String, Object> params) throws CommonException;
	
	/**
	 * 获取菜单集合
	 * @param menuIds
	 * @return
	 * @throws CommonException
	 */
	public List<Map> getMenuList(List<Integer> menuIds) throws CommonException;
	
	/**
	 * 获取子菜单集合
	 * @param menuId
	 * @return
	 * @throws CommonException
	 */
	public List<Map> getSubMenuList (Integer userId, int menuId, boolean needAuthCheck) throws CommonException;

	/**
	 * @param params: Integer start, Integer limit, ...
	 * @return Map.key: Integer total, List<Map<String,Object>> rows
	 * @throws CommonException
	 */
	public Map<String,Object> getAllSkus(Map<String,Object> params) throws CommonException;

	/**
	 * @param params: Integer SKU_ID, ...
	 * @throws CommonException
	 */
	public void delSku(Map<String,Object> params) throws CommonException;

	/**
	 * @param sku
	 * @throws CommonException
	 */
	public void setSku(Map<String,Object> sku,boolean statusOnly) throws CommonException;

	/**
	 * @param sku
	 * @throws CommonException
	 */
	public void addSku(Map<String,Object> sku) throws CommonException;
	
	/**
	 * @param params: Integer start, Integer limit, String RELATION_CATEGORY ...
	 * @return Map.key: Integer total, List<Map<String,Object>> rows
	 * @throws CommonException
	 */
	public Map<String,Object> getAllCodeNames(Map<String,Object> params) throws CommonException;

	/**
	 * @return Map.key RELATION_CATEGORY,RELATION_NAME
	 * @throws CommonException
	 */
	public List<Map>  getAllCodeCategory() throws CommonException;
	
	
	/**
	 * @return Map.key RELATION_CATEGORY,RELATION_NAME
	 * @throws CommonException
	 */
	public List<Map<String, Object>>  getCodeCategory(String relationCategory) throws CommonException;
	
	/**
	 * 新增codeName数据
	 * @param dataList
	 */
	public void addCodeName(Map data) throws CommonException;
	
	/**
	 * 修改codeName显示名称
	 * @param dataList
	 */
	public void modifyCodeName(List<Map> dataList) throws CommonException;
	
	/**
	 * 唯一性校验
	 * @param dataList
	 */
	public boolean checkCodeName(Map data) throws CommonException;
	
	
	/**
	 * @param params: Integer start, Integer limit
	 * @return Map.key: Integer total, List<Map<String,Object>> rows
	 * @throws CommonException
	 */
	public Map<String,Object> getAllFilePath(Map<String,Object> params) throws CommonException;
	
	/**
	 * @param params: Integer start, Integer limit
	 * @return Map.key: Integer total, List<Map<String,Object>> rows
	 * @throws CommonException
	 */
	public Map<String,Object> getAllContact(Map<String,Object> params) throws CommonException;
	
	/**
	 * 新增contact数据
	 * @param dataList
	 */
	public void addContact(Map data) throws CommonException;
	/**
	 * 唯一性校验
	 * @param dataList
	 */
	public boolean checkContact(Map data) throws CommonException;
	/**
	 * 修改contact数据
	 * @param dataList
	 */
	public void modifyContact(List<Map> dataList) throws CommonException;
	
	/**
	 * 删除contact数据
	 * @param dataList
	 */
	public void deleteContact(List<Map> dataList) throws CommonException;
	
	/**
	 * 修改filePath数据
	 * @param dataList
	 */
	public void modifyFilePath(List<Map> dataList) throws CommonException;
	
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
	 * @param param
	 * @throws CommonException
	 */
	public void importFile(Map<String,Object> param) throws CommonException;

}
