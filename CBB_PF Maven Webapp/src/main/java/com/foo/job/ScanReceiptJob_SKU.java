package com.foo.job;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.foo.common.CommonDefine;
import com.foo.dao.mysql.CommonManagerMapper;
import com.foo.util.ConfigUtil;
import com.foo.util.FtpUtils;
import com.foo.util.SpringContextUtil;
import com.foo.util.XmlUtil;

public class ScanReceiptJob_SKU extends AbstractJob {

	public ScanReceiptJob_SKU() {
		commonManagerMapper = (CommonManagerMapper) SpringContextUtil
				.getBean("commonManagerMapper");
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println(new Date() + "我在执行搜索商品备案回执！");

		Map xmlFilePath = ConfigUtil
				.getFileLocationPath(CommonDefine.FILE_CATEGORY_SPBA);
		// 回执文件夹
		String receiptXmlFilePath = xmlFilePath.get("RECEIPT_XML").toString();
		// 转移文件夹
		String transferXmlFilePath = xmlFilePath.get("TRANSFER_XML").toString();

		FtpUtils ftpUtil = FtpUtils.getDefaultFtp();

		try {
			// 查询需要回执的数据,包含为null的RETURN_STATUS
			List dataList = commonManagerMapper.selectNeedReceiptData("T_SKU",
					"RETURN_STATUS", CommonDefine.NEED_RECEIPT_STATUS_SKU, true);
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
							//更新数据
							if(receiptGuid.equals(guid)){
								// 更新商品回执信息
								// dataMap.put("GUID", receiptData.get("GUID"));
								dataMap.put("PRE_NO", receiptData.get("PRE_NO"));
								dataMap.put("G_NO", receiptData.get("G_NO"));
//									dataMap.put("APP_TIME", receiptData.get("RETURN_TIME"));
								dataMap.put("RETURN_TIME", receiptData.get("RETURN_TIME"));
								dataMap.put("RETURN_INFO", receiptData.get("RETURN_INFO"));
								dataMap.put("RETURN_STATUS",
										receiptData.get("RETURN_STATUS"));
								//回执状态为399情况 更新申报状态 为申报完成
								if (receiptData.get("RETURN_STATUS") != null
										&& Integer.valueOf(receiptData.get(
												"RETURN_STATUS").toString()) == CommonDefine.RETURN_STATUS_399) {
									dataMap.put("APP_STATUS",CommonDefine.APP_STATUS_COMPLETE);
									//删除t_guid_rel表中数据
									commonManagerMapper.delTableById("t_guid_rel", "GUIDS", dataMap.get("GUID"));
								}
								//更新数据
								commonManagerMapper.updateSku(dataMap);

								// 解析完成之后转移文件
								ftpUtil.moveFile(receiptXmlFilePath, fileName,
										transferXmlFilePath, fileName);
							}else{
								continue;
							}
						}
					}else{
						continue;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
