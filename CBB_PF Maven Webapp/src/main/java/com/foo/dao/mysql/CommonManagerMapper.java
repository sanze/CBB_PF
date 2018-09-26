package com.foo.dao.mysql;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;


/**
 * @author xuxiaojun
 *
 */
public interface CommonManagerMapper {
	
	/**
	 * 	获取整表数据
	 * @param tableName 表名
	 * @return
	 */
	public List selectTable(
			@Param(value = "tableName") String tableName,
			@Param(value = "startNumber") Integer startNumber,
			@Param(value = "pageSize") Integer pageSize);
	
	/**
	 * 	获取整表数据count值
	 * @param tableName 表名
	 * @return
	 */
	public int selectTableCount(
			@Param(value = "tableName") String tableName);
	
	/**
	 * 	按id获取一条数据
	 * @param tableName 表名
	 * @param idName id字段名
	 * @param id id值
	 * @return
	 */
	public Map selectTableById(
			@Param(value = "tableName") String tableName,
			@Param(value = "idName") String idName,
			@Param(value = "id") int id);
	
	/**
	 * 	按id获取数据列表
	 * @param tableName 表名
	 * @param idName id字段名
	 * @param id id值
	 * @return
	 */
	public List selectTableListById(
			@Param(value = "tableName") String tableName,
			@Param(value = "idName") String idName,
			@Param(value = "id") int id,
			@Param(value = "startNumber") Integer startNumber,
			@Param(value = "pageSize") Integer pageSize);
	
	/**
	 * 	按id获取数据列表count值
	 * @param tableName 表名
	 * @param idName id字段名
	 * @param id id值
	 * @return
	 */
	public int selectTableListCountById(
			@Param(value = "tableName") String tableName,
			@Param(value = "idName") String idName,
			@Param(value = "id") int id);
	
	/**
	 * 	按id获取数据列表
	 * @param tableName 表名
	 * @param idName id字段名
	 * @param id id值
	 * @return
	 */
	public List<Map<String, Object>> selectTableListByCol(
			@Param(value = "tableName") String tableName,
			@Param(value = "colName") String colName,
			@Param(value = "colValue") Object colValue,
			@Param(value = "startNumber") Integer startNumber,
			@Param(value = "pageSize") Integer pageSize);
	
	/**
	 * 查询数据
	 * @param tableName 表名
	 * @param colNames 字段名
	 * @param colValues 字段值
	 */
	public List<Map<String,Object>> selectTableListByNVList(
			@Param(value = "tableName") String tableName,
			@Param(value = "colNames") List<String> colNames,
			@Param(value = "colValues") List<Object> colValues,
			@Param(value = "startNumber") Integer startNumber,
			@Param(value = "pageSize") Integer pageSize);
	
	/**
	 * 查询数据
	 * @param tableName 表名
	 * @param colNames 字段名
	 * @param colValues 字段值
	 */
	public List<Map<String,Object>> selectTableListByNVList_Fuzzy(
			@Param(value = "tableName") String tableName,
			@Param(value = "colNames") List<String> colNames,
			@Param(value = "colValues") List<Object> colValues,
			@Param(value = "startNumber") Integer startNumber,
			@Param(value = "pageSize") Integer pageSize);
	
	/**
	 * 查询数据
	 * @param tableName 表名
	 * @param colNames 字段名
	 * @param colValues 字段值
	 */
	public int selectTableListCountByNVList(
			@Param(value = "tableName") String tableName,
			@Param(value = "colNames") List<String> colNames,
			@Param(value = "colValues") List<Object> colValues);
	
	/**
	 * 查询数据
	 * @param tableName 表名
	 * @param colNames 字段名
	 * @param colValues 字段值
	 */
	public int selectTableListCountByNVList_Fuzzy(
			@Param(value = "tableName") String tableName,
			@Param(value = "colNames") List<String> colNames,
			@Param(value = "colValues") List<Object> colValues);
	
	/**
	 * 	按id获取数据列表count值
	 * @param tableName 表名
	 * @param idName id字段名
	 * @param id id值
	 * @return
	 */
	public int selectTableListCountByCol(
			@Param(value = "tableName") String tableName,
			@Param(value = "colName") String colName,
			@Param(value = "colValue") Object colValue);
	
	/**
	 * 获取当前最大Id值
	 * @param tableName
	 * @param colName
	 * @return
	 */
	public Integer selectMaxIdFromTable(
			@Param(value = "dbName") String dbName,
			@Param(value = "tableName") String tableName);
	
	/**
	 * 获取当前最大Id值
	 * @param tableName
	 * @param colName
	 * @return
	 */
	public Integer selectMaxNoFromTable(
			@Param(value = "columnName") String columnName,
			@Param(value = "tableName") String tableName);
	
	/**
	 * 获取子菜单集合--含权限
	 * @param menuParentId
	 * @param userId
	 * @return
	 */
	public List<Map> getAuthSubMenuList(
			@Param(value = "menuParentId") int menuParentId,
			@Param(value = "userId") int userId);
	
	
	/**
	 * 获取菜单集合--首页显示用
	 * @param userId
	 * @param menuIds
	 * @return
	 */
	public List<Map> getMenuList(
			@Param(value = "menuIds") List<Integer> menuIds);
	
	/**
	 * 获取子菜单集合--无权限
	 * @param menuParentId
	 * @return
	 */
	public List<Map> getAllSubMenuList(
			@Param(value = "menuParentId") int menuParentId);
	
	/**
	 * 插入数据
	 * @param tableName 表名
	 * @param colNames 字段名
	 * @param colValues 字段值
	 */
	public void insertTableByNVList(
			@Param(value = "tableName") String tableName,
			@Param(value = "colNames") List<String> colNames,
			@Param(value = "colValues") List<Object> colValues,
			@Param(value = "primaryMap") Map primaryMap);
	
	/**
	 * 更新数据
	 * @param tableName 表名
	 * @param colNames 字段名
	 * @param colValues 字段值
	 */
	public void updateTableByNVList(
			@Param(value = "tableName") String tableName,
			@Param(value = "idName") String idName,
			@Param(value = "id") Object id,
			@Param(value = "colNames") List<String> colNames,
			@Param(value = "colValues") List<Object> colValues);
	
	public void delTableById(
			@Param(value = "tableName") String tableName,
			@Param(value = "idName") String idName,
			@Param(value = "id") Object id);
	/**
	 * 查询需要回执的数据
	 * @param tableName
	 * @param columnName
	 * @param returnStatus，不在指定returnStatus中
	 * @return
	 */
	public List selectNeedReceiptData(
			@Param(value = "tableName") String tableName,
			@Param(value = "columnName") String columnName,
			@Param(value = "returnStatus") Integer[] returnStatus,
			@Param(value = "needRelGuidc") boolean needRelGuidc);

	/**
	 * 查询需要回执的数据，5系报文专用
	 * @param returnStatus，不在指定returnStatus中
	 * @param alreadyHasLogStatus
	 * @return
	 */
	public List selectNeedReceiptData_CEB5X(
			@Param(value = "returnStatus") Integer[] returnStatus,
			@Param(value = "needRelGuidc") boolean needRelGuidc,
			@Param(value = "alreadyHasLogStatus") boolean alreadyHasLogStatus,
			@Param(value = "tableName") String tableName);
	
	/**
	 * 更新sku数据信息
	 * @param data
	 * @return
	 */
	public void updateSku(Map data);
	
	/**
	 * 更新order数据信息
	 * @param data
	 * @return
	 */
	public void updateOrder(Map data);
	
	/**
	 * 更新logistics数据信息
	 * @param data
	 * @return
	 */
	public void updateLogistics(
			@Param(value = "data") Map data,
			@Param(value = "tableName") String tableName);
	
	/**
	 * 更新logistics数据信息,清空回执数据,并且将申报状态置为2（申报中）
	 * @param data
	 * @return
	 */
	public void updateLogisticsReturnInfoToNull(
			@Param(value = "data") Map data,
			@Param(value = "tableName") String tableName);
	
	/**
	 * 更新inventory数据信息
	 * @param data
	 * @return
	 */
	public void updateInventory(Map data);


	/**
	 * 获取所有code列表distinct
	 * @return
	 */
	public List<Map> getAllCodeCategory();
	
//	修改为使用通用方法
//	/**
//	 * 插入guid关联表
//	 * @param data
//	 * @return
//	 */
//	public void insertGuidRelData(Map data);
	
	/**
	 * 插入订单数据
	 * @param data
	 */
	public int insertOrderData(Map data);
	
	/**
	 * 插入订单详细数据
	 * @param data
	 */
	public int insertOrderDetailDataBatch(List<Map> data);
	
	
	/**
	 * 获取审结的商品信息，通过itemNo
	 * @param itemNo
	 * @return
	 */
	public Map selectSkuByItemNo(
			@Param(value = "itemNo") String itemNo,
			@Param(value = "returnStatus") int returnStatus);
	
	/**
	 * 获取201报文数据
	 * @param guid
	 * @return
	 */
	public Map selectDataForMessage201(
			@Param(value = "guid") String guid);
	
	/**
	 * 获取301报文数据
	 * @param guid
	 * @return
	 */
	public Map selectDataForMessage301(
			@Param(value = "guid") String guid);
	
	/**
	 * 获取301报文商品数据
	 * @param orderId
	 * @return
	 */
	public List<Map> selectSubDataForMessage301(
			@Param(value = "orderId") Integer orderId);
	
	/**
	 * 获取501报文数据
	 * @param guid
	 * @return
	 */
	public Map selectDataForMessage501(
			@Param(value = "guid") String guid);
	
	/**
	 * 获取511报文数据
	 * @param guid
	 * @return
	 */
	public Map selectDataForMessage511_import(
			@Param(value = "guid") String guid);
	
	/**
	 * 获取501报文数据
	 * @param guid
	 * @return
	 */
	public Map selectDataForMessage501_SN(
			@Param(value = "guid") String guid);
	

	/**
	 * 获取503报文数据
	 * @param guid
	 * @return
	 */
	public Map selectDataForMessage503(
			@Param(value = "guid") String guid);
	
	/**
	 * 获取513报文数据
	 * @param guid
	 * @return
	 */
	public Map selectDataForMessage513_import(
			@Param(value = "guid") String guid);
	
	/**
	 * 获取503报文数据
	 * @param guid
	 * @return
	 */
	public Map selectDataForMessage503_SN(
			@Param(value = "guid") String guid);
	
	/**
	 * 获取601报文数据
	 * @param guid
	 * @return
	 */
	public Map selectDataForMessage601(
			@Param(value = "guid") String guid);
	
	/**
	 * 获取601报文商品数据
	 * @param orderId
	 * @return
	 */
	public List<Map> selectSubDataForMessage601(
			@Param(value = "inventoryId") Integer inventoryId);
	
	/**
	 * 获取所有苏宁已经生成运单的id
	 * @param orderId
	 * @return
	 */
	public List<String> selectSnLogistics();
	
	/**
	 * 更新503回执报文的guid关联数据，在原有501的关联数据后添加以“,”分隔
	 * @param guids
	 * @param guidc
	 */
	public void updateGuiRelDataFor503(
			@Param(value = "guids") String guids,
			@Param(value = "guidc") String guidc);
}