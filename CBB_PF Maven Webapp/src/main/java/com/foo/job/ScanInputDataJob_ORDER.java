package com.foo.job;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.foo.common.CommonDefine;
import com.foo.dao.mysql.CommonManagerMapper;
import com.foo.util.CommonUtil;
import com.foo.util.ConfigUtil;
import com.foo.util.FtpUtils;
import com.foo.util.SpringContextUtil;
import com.foo.util.XmlUtil;

public class ScanInputDataJob_ORDER extends AbstractJob {

	public ScanInputDataJob_ORDER() {
		commonManagerMapper = (CommonManagerMapper) SpringContextUtil
				.getBean("commonManagerMapper");
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println(new Date() + "我在执行搜索电子订单输入数据");

		Map xmlFilePath = ConfigUtil
				.getFileLocationPath(CommonDefine.FILE_CATEGORY_DZDD);
		// 订单数据源文件夹
		String inputXmlFilePath = xmlFilePath.get("INPUT_XML").toString();
		// 转移文件夹
		String transferXmlFilePath = xmlFilePath.get("TRANSFER_XML").toString();

		FtpUtils ftpUtil = FtpUtils.getDefaultFtp();

		try {
			List<String> fileNameList = ftpUtil.getFileList(inputXmlFilePath);

			for (String fileName : fileNameList) {
				// 获取文件
				File file = ftpUtil.getFile(inputXmlFilePath + "/" + fileName);
				if(file == null){
					continue;
				}
				// 解析文件
				Map data = XmlUtil.parseXmlWithSubData(file, true);
				// 存入订单数据库t_orders
				Map order = generalOrderTableMap(data);
				commonManagerMapper.insertOrderData(order);
				// 存入订单详细数据t_order_detail
				Integer orderId = Integer.valueOf(order.get("ORDERS_ID")
						.toString());
				List<Map> orderList = generalOrderDetailTableMap(
						orderId,
						data.get("ORDER_NO") == null ? null : data.get(
								"ORDER_NO").toString(),
						(List<Map>) (data.get("subDataList")));

				if(orderList.size()>0){
					commonManagerMapper.insertOrderDetailDataBatch(orderList);
				}
				// 解析完成之后转移文件
				ftpUtil.moveFile(inputXmlFilePath, fileName,
						transferXmlFilePath, fileName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 生成订单映射表
	private Map generalOrderTableMap(Map data) {
		Map orderTable = new HashMap();

		orderTable.put("ORDERS_ID", null);
		// 企业系统生成36位唯一序号（英文字母大写和数字和横杠）,
		//格式:SINOTRANS-ORDER-YYYYMMDDhhmmss-00001,即系统当前时间加5位流水号
		String guid = CommonUtil.generalGuid(CommonDefine.GUID_FOR_ORDER,5,"t_orders");
		orderTable.put("GUID",guid);
		orderTable.put("ORDER_NO",
				data.get("ORDER_NO") == null ? null : data.get("ORDER_NO"));
		orderTable.put("ORDER_TYPE",
				data.get("ORDER_TYPE") == null ? null : data.get("ORDER_TYPE"));
		// 申报类型:1-新增 2-变更 3-删除,默认为1
		orderTable.put("APP_TYPE", 1);
		orderTable.put("APP_TIME",
				data.get("APP_TIME") == null ? null : data.get("APP_TIME"));
		// 业务状态:1-暂存,2-申报,默认为1
		orderTable.put("APP_STATUS", 1);
		orderTable.put("APP_UID",
				data.get("APP_UID") == null ? null : data.get("APP_UID"));
		orderTable.put("APP_UNAME",
				data.get("APP_UNAME") == null ? null : data.get("APP_UNAME"));
		orderTable.put("EBP_CODE",
				data.get("EBP_CODE") == null ? null : data.get("EBP_CODE"));
		orderTable.put("EBP_NAME",
				data.get("EBP_NAME") == null ? null : data.get("EBP_NAME"));
		orderTable.put("EBC_CODE",
				data.get("EBC_CODE") == null ? null : data.get("EBC_CODE"));
		orderTable.put("EBC_NAME",
				data.get("EBC_NAME") == null ? null : data.get("EBC_NAME"));
		orderTable.put("AGENT_CODE", data.get("AGENT_CODE") == null ? null
				: data.get("AGENT_CODE"));
		orderTable.put("AGENT_NAME", data.get("AGENT_NAME") == null ? null
				: data.get("AGENT_NAME"));
		orderTable.put("GOODS_VALUE", data.get("GOODS_VALUE") == null ? null
				: data.get("GOODS_VALUE"));
		orderTable.put("FREIGHT",
				data.get("FREIGHT") == null ? null : data.get("FREIGHT"));
		orderTable.put("CURRENCY",
				data.get("CURRENCY") == null ? null : data.get("CURRENCY"));
		orderTable.put("CONSIGNEE",
				data.get("CONSIGNEE") == null ? null : data.get("CONSIGNEE"));
		orderTable.put(
				"CONSIGNEE_ADDRESS",
				data.get("CONSIGNEE_ADDRESS") == null ? null : data
						.get("CONSIGNEE_ADDRESS"));
		orderTable.put(
				"CONSIGNEE_TELEPHONE",
				data.get("CONSIGNEE_TELEPHONE") == null ? null : data
						.get("CONSIGNEE_TELEPHONE"));
		orderTable.put(
				"CONSIGNEE_COUNTRY",
				data.get("CONSIGNEE_COUNTRY") == null ? null : data
						.get("CONSIGNEE_COUNTRY"));
		orderTable.put("NOTE",
				data.get("NOTE") == null ? null : data.get("NOTE"));
		orderTable.put(
				"RETURN_STATUS",
				data.get("RETURN_STATUS") == null ? null : data
						.get("RETURN_STATUS"));
		orderTable.put("RETURN_TIME", data.get("RETURN_TIME") == null ? null
				: data.get("RETURN_TIME"));
		orderTable.put("RETURN_INFO", data.get("RETURN_INFO") == null ? null
				: data.get("RETURN_INFO"));
		orderTable.put("CREAT_TIME", new Date());
		return orderTable;
	}

	// 生成订单映射表
	private List<Map> generalOrderDetailTableMap(int orderId, String orderNo, List<Map> dataList) {
		List<Map> orderDetailList = new ArrayList<Map>();
		for (Map data : dataList) {
			//货号
			String itemNo = data.get("ITEM_NO") == null ? null
					: data.get("ITEM_NO").toString();
			//获取备案信息，itemNo相同，且状态为审结
			Map sku = commonManagerMapper.selectSkuByItemNo(itemNo,CommonDefine.RETURN_STATUS_399);
			//如果没有找到备案信息，放弃
			if(sku == null){
				continue;
			}
			
			Map orderDetail = new HashMap();
			orderDetail.put("ORDER_DETAIL_ID", null);
			orderDetail.put("ORDERS_ID", orderId);
			orderDetail.put("ORDER_NO", orderNo);
			orderDetail.put("ITEM_NO", itemNo);
			orderDetail.put("GNUM",
					data.get("GNUM") == null ? null : data.get("GNUM"));
			orderDetail.put("QTY",
					data.get("QTY") == null ? null : data.get("QTY"));
			orderDetail.put("PRICE",
					data.get("PRICE") == null ? null : data.get("PRICE"));
			orderDetail.put("TOTAL",
					data.get("TOTAL") == null ? null : data.get("TOTAL"));
			orderDetail.put("NOTE",
					data.get("NOTE") == null ? null : data.get("NOTE"));
			orderDetail.put("CREAT_TIME", new Date());
			orderDetailList.add(orderDetail);
		}
		return orderDetailList;
	}

}
