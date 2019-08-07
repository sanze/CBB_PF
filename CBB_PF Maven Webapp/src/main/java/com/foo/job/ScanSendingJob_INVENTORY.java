package com.foo.job;

import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.foo.dao.mysql.CommonManagerMapper;
import com.foo.handler.ExceptionHandler;
import com.foo.manager.commonManager.service.HttpServerManagerService;
import com.foo.manager.commonManager.thread.HttpHandleThread;
import com.foo.manager.commonManager.thread.HttpHandleThread_SNOrder;
import com.foo.util.CommonUtil;
import com.foo.util.SpringContextUtil;

public class ScanSendingJob_INVENTORY extends AbstractJob {

	public ScanSendingJob_INVENTORY() {
		commonManagerMapper = (CommonManagerMapper) SpringContextUtil
				.getBean("commonManagerMapper");
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println(new Date() + "我在搜索等待发送给天津的出库单");
		ResourceBundle bundle = CommonUtil.getMessageMappingResource("CEB_SN");

		Integer id = HttpServerManagerService.TJ_WAITING_TO_SEND.poll();
		
		System.out.println(HttpServerManagerService.TJ_WAITING_TO_SEND.size());
		
		if(id != null){
			String xmlStringData = HttpHandleThread_SNOrder.generalRequestXml4TJ(id.toString(), bundle);
			try{
				// 第五步 向天津外运发送清单数据
				Map reponse = HttpHandleThread.postToTJ(xmlStringData,CommonUtil
							.getSystemConfigProperty("TJ_business_type"));
				System.out.println("【天津返回数据】"+reponse.toString());
			}catch(Exception e){
				System.out.println("请求天津异常!");
				ExceptionHandler.handleException(e);
			}
		}
	}
}
