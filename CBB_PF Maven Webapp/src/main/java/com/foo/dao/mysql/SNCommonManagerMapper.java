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
	
	public List<LinkedHashMap> selectIODeclContainerList(
			@Param(value = "id") int id);
	
	public List<LinkedHashMap> selectIODeclOrderRelationList(
			@Param(value = "id") int id);
	
	public LinkedHashMap selectBaseTransfer();

}