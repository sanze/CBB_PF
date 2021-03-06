package com.foo.dao.mysql;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;


/**
 * @author xuxiaojun
 *
 */
public interface SNCommonManagerMapper {
	
	/**
	 * 获取SNT202报文数据
	 * @param LogisticsNo
	 * @param OrderNo
	 * @return
	 */
	public List<Map> selectBookNumber(
			@Param(value = "sku") String sku,
			@Param(value = "qty") double qty);

	public List<LinkedHashMap> selectInventoryHead(
			@Param(value = "id") int id);
	
	public List<LinkedHashMap> selectInventoryList(
			@Param(value = "id") int id);
	
	public List<LinkedHashMap> selectInventoryListRelateBookInfo(
			@Param(value = "orderNo") String orderNo,
			@Param(value = "itemNo") String itemNo);
	
	public List<LinkedHashMap> selectIODeclContainerList(
			@Param(value = "id") int id);
	
	public List<LinkedHashMap> selectIODeclOrderRelationList(
			@Param(value = "id") int id);
	
	public LinkedHashMap selectBaseTransfer();
	
	public List<Map> selectNeedUpdateEmsStatusOrder();
	
	public List<Map> selectRecordNoSum(
			@Param(value = "orderNo") String orderNo);
	
	public List<Map> selectFlightInfo(
			@Param(value = "bol") String bol);
	public List<Map> selectOrderConfirmInfo(
			@Param(value = "bol") String bol);

}