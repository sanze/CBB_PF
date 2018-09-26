package com.foo.job;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.foo.IService.ISuningManagerService;
import com.foo.common.CommonDefine;
import com.foo.common.CommonException;
import com.foo.dao.mysql.CommonManagerMapper;
import com.foo.model.suningModel.LogisticsCrossbuyTask.AddLogisticsTaskStatusResponse;
import com.foo.util.CommonUtil;
import com.foo.util.ConfigUtil;
import com.foo.util.FtpUtils;
import com.foo.util.SpringContextUtil;
import com.foo.util.XmlUtil;
import com.suning.api.entity.logistics.LogisticsTaskStatusAddRequest;

public class ScanReceiptJob_LOGISTICS extends AbstractJob {

	private ISuningManagerService suningManagerService;
	
	public ScanReceiptJob_LOGISTICS() {
		commonManagerMapper = (CommonManagerMapper) SpringContextUtil
				.getBean("commonManagerMapper");
		suningManagerService = (ISuningManagerService) SpringContextUtil
				.getBean("suningManagerServiceImpl");
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println(new Date() + "我在执行搜索物流运单状态数据/物流运单状态回执");
		
		//处理普通运单
		//handleLogisticsReceipt("T_LOGISTICS");
		//处理苏宁运单
		//handleLogisticsReceipt("T_LOGISTICS_SN");
		//处理苏宁运单
		handleLogisticsReceipt("T_IMPORT_LOGISTICS");
	}
	
	private void handleLogisticsReceipt(String tableName){
		try{
			//处理运单回执
			// 查询需要回执的数据,包含为null的RETURN_STATUS,物流状态为空
			List dataList = commonManagerMapper.selectNeedReceiptData_CEB5X(
					CommonDefine.NEED_RECEIPT_STATUS_LOGISTICS, true, false, tableName);
			handleReceipt(CommonDefine.FILE_CATEGORY_WLYD, dataList,tableName);
			//处理运单状态
			//查询需要回执的数据,包含为null的RETURN_STATUS，物流状态不为空
			dataList = commonManagerMapper.selectNeedReceiptData_CEB5X(
					CommonDefine.NEED_RECEIPT_STATUS_LOGISTICS, true, true, tableName);
			handleReceipt(CommonDefine.FILE_CATEGORY_YDZT, dataList,tableName);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	//处理回执
	private void handleReceipt(int receiptType, List dataList,String tableName){
		//获取路径
		Map xmlFilePath = ConfigUtil
				.getFileLocationPath(receiptType);
		
		// 回执文件夹
		String receiptXmlFilePath = xmlFilePath.get("RECEIPT_XML").toString();
		// 转移文件夹
		String transferXmlFilePath = xmlFilePath.get("TRANSFER_XML").toString();

		FtpUtils ftpUtil = FtpUtils.getDefaultFtp();

		try {
			// 选择正确的guid
			String guid;
			// 获取文件列表
			List<String> fileList = ftpUtil.getFileList(receiptXmlFilePath);
			//循环文件列表
			for (String fileName : fileList) {
				File file = ftpUtil
						.getFile(receiptXmlFilePath + "/" + fileName);
				// 解析回执文件
				if (file != null) {
					// 回执表单字段名未知，需要修改
					Map receiptData = XmlUtil.parseXml(file, true);
					// 回执guid
					String receiptGuid = receiptData.get("GUID") != null ? receiptData
							.get("GUID").toString() : null;

					if (receiptGuid != null) {
						for (Object data : dataList) {
							//需要回执的数据
							Map dataMap = (Map) data;
							//获取检查数据
							GuidCheckResult result = operateGuid(receiptGuid,
									dataMap, receiptData.get("RETURN_INFO")
											.toString());
							//是否找到匹配的回执数据
							if(result.isGetNext()){
								//循环下一个
								continue;
							}else{
								//guid赋值
								guid = result.getGuid();
							}
							// 找到对应数据，更新之
							if (receiptGuid.equals(guid)) {
								// 更新信息
								if(receiptData.get("RETURN_TIME")!=null){
									dataMap.put("RETURN_TIME",
											receiptData.get("RETURN_TIME"));
								}
								if(receiptData.get("RETURN_INFO")!=null){
									dataMap.put("RETURN_INFO",
											receiptData.get("RETURN_INFO"));
								}
								if(receiptData.get("RETURN_STATUS")!=null){
									dataMap.put("RETURN_STATUS",
											receiptData.get("RETURN_STATUS"));
								}
								
								//回执状态为120情况 更新申报状态 为申报完成
								if (receiptData.get("RETURN_STATUS") != null
										&& Integer.valueOf(receiptData.get(
												"RETURN_STATUS").toString()) == CommonDefine.RETURN_STATUS_120) {
									dataMap.put("APP_STATUS",CommonDefine.APP_STATUS_COMPLETE);
									
									//删除t_guid_rel表中数据
									commonManagerMapper.delTableById("t_guid_rel", "GUIDS", dataMap.get("GUID"));
									
									//每收到一次504的120 向苏宁的suning.logistics.taskstatusfeedback.add接口发送物流状态
									if(CommonDefine.FILE_CATEGORY_YDZT == receiptType
											&& "T_LOGISTICS_SN".equals(tableName)){
										sendLogisticsTaskStatusToSuning(dataMap);
									}
								}
								//运单状态，人工更新
//								if(receiptData.get("LOGISTICS_STATUS")!=null){
//									dataMap.put("LOGISTICS_STATUS",
//											receiptData.get("LOGISTICS_STATUS"));
//								}
								// 更新数据
								commonManagerMapper.updateLogistics(dataMap,tableName);

								// 解析完成之后转移文件
								ftpUtil.moveFile(receiptXmlFilePath, fileName,
										transferXmlFilePath, fileName);
							}
						}
					} else {
						continue;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
/*	
 * logisticOrderId 填来自苏宁suning.logistics.crossbuytask.query接口的logisticsOrderId
	logisticExpressId 暂时不填
	logisticStation 填“中外运长江”
	state 503的logStatus
	finishedDate 当前日期
	finishedTime 当前时间
	consignee 暂时不填
	operator 暂时不填
	telNumber 暂时不填
	shipmentCode 503的logNo
	weight 暂时不填
	weightUnit 暂时不填
	note 503的note
	airwayBillNo 暂时不填
	flightDate 暂时不填
	flightNo 503的voyageNo
	*/
	private AddLogisticsTaskStatusResponse sendLogisticsTaskStatusToSuning(Map dataMap){
		
		SimpleDateFormat sf = CommonUtil.getDateFormatter(CommonDefine.COMMON_FORMAT);
		
		String now = sf.format(new Date());
		
		LogisticsTaskStatusAddRequest request = new LogisticsTaskStatusAddRequest();
		// 组织请求数据
		request.setLogisticOrderId(dataMap.get("LOGISTICS_ORDER_ID").toString());
		request.setLogisticExpressId("");
		request.setLogisticStation("中外运长江");
		request.setState(dataMap.get("LOGISTICS_STATUS").toString());
		request.setFinishedDate(now.split(" ")[0]);
		request.setFinishedTime(now.split(" ")[1]);
		request.setConsignee("");
		request.setOperator("");
		request.setTelNumber("");
		request.setShipmentCode(dataMap.get("LOGISTICS_NO").toString());
		request.setWeight("");
		request.setWeightUnit("");
		request.setNote(dataMap.get("NOTE").toString());
		request.setAirwayBillNo("");
		request.setFlightDate("");
		request.setFlightNo(dataMap.get("VOYAGE_NO").toString());
		
		AddLogisticsTaskStatusResponse response = null;
		try {
			response = suningManagerService.getLogisticsTaskStatusAddResponse(request);
		} catch (CommonException e) {
			e.printStackTrace();
		}
		return response;
	}

}
