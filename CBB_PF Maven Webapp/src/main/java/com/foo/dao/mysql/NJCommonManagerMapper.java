package com.foo.dao.mysql;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;


/**
 * @author xuxiaojun
 *
 */
public interface NJCommonManagerMapper {
	
	/**
	 * 更新sku数据信息
	 * @param data
	 * @return
	 */
	public void updateSku_nj(Map data);
	
	/**
	 * 获取201报文数据
	 * @param guid
	 * @return
	 */
	public Map selectDataForMessage20X_NJ_head(
			@Param(value = "guid") String guid);
	/**
	 * 获取201报文数据
	 * @param guid
	 * @return
	 */
	public Map selectDataForMessage201_NJ(
			@Param(value = "guid") String guid);
	
	/**
	 * 获取203报文数据
	 * @param guid
	 * @return
	 */
	public Map selectDataForMessage203_NJ(
			@Param(value = "guid") String guid);
	
	
	/**
	 * 更新order数据信息
	 * @param data
	 * @return
	 */
	public void updateOrder_nj(Map data);
	
	/**
	 * 获取301报文数据
	 * @param guid
	 * @return
	 */
	public Map selectDataForMessage30X_NJ_head(
			@Param(value = "guid") String guid);
	
	/**
	 * 获取301报文数据
	 * @param guid
	 * @return
	 */
	public Map selectDataForMessage301_NJ(
			@Param(value = "guid") String guid);
	
	/**
	 * 获取301报文商品数据
	 * @param orderId
	 * @return
	 */
	public List<Map> selectSubDataForMessage301_NJ(
			@Param(value = "orderId") Integer orderId);
	
	/**
	 * 获取303报文数据
	 * @param guid
	 * @return
	 */
	public Map selectDataForMessage303_NJ(
			@Param(value = "guid") String guid);
	
	
	/**
	 * 获取401报文数据
	 * @param guid
	 * @return
	 */
	public Map selectDataForMessage40X_NJ_head(
			@Param(value = "guid") String guid);
	
	/**
	 * 获取401报文数据
	 * @param guid
	 * @return
	 */
	public Map selectDataForMessage401_NJ(
			@Param(value = "guid") String guid);
	
	
	/**
	 * 更新pay数据信息
	 * @param data
	 * @return
	 */
	public void updatePay_nj(Map data);
	
	/**
	 * 获取501报文数据
	 * @param guid
	 * @return
	 */
	public Map selectDataForMessage501_NJ(
			@Param(value = "guid") String guid);
	
	/**
	 * 获取501报文商品数据
	 * @param orderId
	 * @return
	 */
	public List<Map> selectSubDataForMessage501_NJ(
			@Param(value = "orderNo") String orderNo);
	
	/**
	 * 获取502报文数据
	 * @param guid
	 * @return
	 */
	public Map selectDataForMessage502_NJ(
			@Param(value = "guid") String guid);
	
	/**
	 * 获取503报文数据
	 * @param guid
	 * @return
	 */
	public Map selectDataForMessage503_NJ(
			@Param(value = "guid") String guid);
	
	/**
	 * 获取501报文数据
	 * @param guid
	 * @return
	 */
	public Map selectDataForMessage50X_NJ_head(
			@Param(value = "guid") String guid);
	
	/**
	 * 更新order数据信息
	 * @param data
	 * @return
	 */
	public void updateLogistics_nj(Map data);
	
	
	/**
	 * 获取501报文数据
	 * @param guid
	 * @return
	 */
	public Map selectDataForMessage601_NJ(
			@Param(value = "guid") String guid);
	
	/**
	 * 获取501报文数据
	 * @param guid
	 * @return
	 */
	public Map selectDataForMessage60X_NJ_head(
			@Param(value = "guid") String guid);
	
	/**
	 * 获取601报文商品数据
	 * @param orderId
	 * @return
	 */
	public List<Map> selectSubDataForMessage601_NJ(
			@Param(value = "inventoryId") Integer inventoryId);
	
	/**
	 * 获取602报文数据
	 * @param guid
	 * @return
	 */
	public Map selectDataForMessage602_NJ(
			@Param(value = "guid") String guid);
	
	/**
	 * 更新inventory数据信息
	 * @param data
	 * @return
	 */
	public void updateInventory_nj(Map data);
	
	/**
	 * 获取SNT202报文数据
	 * @param LogisticsNo
	 * @param OrderNo
	 * @return
	 */
	public Map selectDataForMessageSNT202(
			@Param(value = "LogisticsNo") String LogisticsNo);
	
	/**
	 * 获取SNT202报文数据
	 * @param LogisticsNo
	 * @param OrderNo
	 * @return
	 */
	public int selectSumPackNoForOrder(
			@Param(value = "orderNo") String orderNo);

}