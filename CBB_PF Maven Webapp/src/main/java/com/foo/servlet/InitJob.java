package com.foo.servlet;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.foo.IService.IQuartzManagerService;
import com.foo.common.CommonDefine;
import com.foo.handler.ExceptionHandler;
import com.foo.job.ScanInputDataJob_ORDER;
import com.foo.job.ScanReceiptJob_INVENTORY;
import com.foo.job.ScanReceiptJob_LOGISTICS;
import com.foo.job.ScanReceiptJob_ORDER;
import com.foo.job.ScanReceiptJob_SKU;
import com.foo.manager.commonManager.serviceImpl.HttpServerManagerServiceImpl;
import com.foo.util.CommonUtil;
import com.foo.util.SpringContextUtil;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;

/*cron 表达式
 * 一个cron表达式有至少6个（也可能是7个）由空格分隔的时间元素。从左至右，这些元素的定义如下：

 1．秒（0–59） 2．分钟（0–59） 3．小时（0–23） 4．月份中的日期（1–31） 5．月份（1–12或JAN–DEC） 6．星期中的日期（1–7或SUN–SAT） 7．年份（1970–2099）

 * 每一个元素都可以显式地规定一个值（如6），一个区间（如9-12），
 一个列表（如9，11，13）或一个通配符（如*）。“月份中的日期”和“星期中的日期”
 这两个元素是互斥的，因此应该通过设置一个问号（？）来表明你不想设置的那个字段。

 *是一个通配符，表示任何值，用在Minutes字段中表示每分钟。
 ?只可以用在day-of-month或者Day-of-Week字段中，用来表示不指定特殊的值。
 -用来表示一个范围，比如10-12用在Month中表示10到12月。
 ,用来表示附加的值，比如MON,WED,FRI在day-of-week字段中表示礼拜一和礼拜三和礼拜五。
 /用来表示增量，比如0/15用在Minutes字段中表示从0分开始0和15和30和45分。
 L只可以用在day-of-month或者Day-of-Week字段中，如果用在Day-of-month中，表示某个月
 的最后一天，1月则是表示31号，2月则表示28号（非闰年），如果用在Day-of-Week中表示礼
 拜六（数字7）；但是如果L与数字组合在一起用在Day-of-month中，比如6L，则表示某个月
 的最后一个礼拜六；
 * 
 */

public class InitJob extends HttpServlet {
	
	
	private IQuartzManagerService quartzManagerService;
	
	public void init() throws ServletException {
		
		//启动http server
		startHttpServer();

		quartzManagerService = (IQuartzManagerService) SpringContextUtil
				.getBean("quartzManagerService");
		
		// 初始化商品备案回执job
		initGetReceiptDataJob("GetReceiptDataJob_SKU",
				"JobCronExpression",
				CommonDefine.QUARTZ.JOB_TYPE_GET_RECEIPT_DATA_SKU,
				ScanReceiptJob_SKU.class);

		// 初始化订单回执job
		initGetReceiptDataJob("GetReceiptDataJob_ORDER",
				"JobCronExpression",
				CommonDefine.QUARTZ.JOB_TYPE_GET_RECEIPT_DATA_ORDER,
				ScanReceiptJob_ORDER.class);
		
		// 初始化订单获取job
		initGetReceiptDataJob("GetInputDataJob_ORDER",
				"JobCronExpression",
				CommonDefine.QUARTZ.JOB_TYPE_GET_INPUT_DATA_ORDER,
				ScanInputDataJob_ORDER.class);
		
		// 初始化物流运单、物流运单状态回执job
		initGetReceiptDataJob("GetReceiptDataJob_LOGISTICS",
				"JobCronExpression",
				CommonDefine.QUARTZ.JOB_TYPE_GET_RECEIPT_DATA_LOGISTICS,
				ScanReceiptJob_LOGISTICS.class);

		// 初始化出境清单回执job
		initGetReceiptDataJob("GetReceiptDataJob_INVENTORY",
				"JobCronExpression",
				CommonDefine.QUARTZ.JOB_TYPE_GET_RECEIPT_DATA_INVENTORY,
				ScanReceiptJob_INVENTORY.class);

	}
	
	private void startHttpServer(){
		
		boolean startHttpServer = false;
		
		// 获取配置
		if (CommonUtil.getSystemConfigProperty("startHttpServer") != null) {
			startHttpServer = Boolean.parseBoolean(CommonUtil
					.getSystemConfigProperty("startHttpServer"));
		}
		if(startHttpServer){
			String httpServerPort = "8888";
			String httpServerUrl = "/CBB_PF/getData";
			if (CommonUtil.getSystemConfigProperty("httpServerPort") != null) {
				httpServerPort = CommonUtil
						.getSystemConfigProperty("httpServerPort");
			}
			if (CommonUtil.getSystemConfigProperty("httpServerUrl") != null) {
				httpServerUrl = CommonUtil
						.getSystemConfigProperty("httpServerUrl");
			}
			HttpServerProvider provider = HttpServerProvider.provider(); 
			HttpServer server = null;
			try {
				server = provider.createHttpServer(new InetSocketAddress(Integer.valueOf(httpServerPort)), 0);
			} catch (NumberFormatException e) {
				ExceptionHandler.handleException(e);
			} catch (IOException e) {
				ExceptionHandler.handleException(e);
			}
	        server.createContext(httpServerUrl, new HttpServerManagerServiceImpl());
	        server.setExecutor(Executors.newFixedThreadPool(Integer.valueOf(CommonUtil.getSystemConfigProperty("threadNumber"))));
	        server.start();
	        System.out.println("server started"); 
		}
	}
	
	//创建任务
	private void initGetReceiptDataJob(String jobNameConfigProperty,
			String cronExpressionConfigProperty, int jobType, Class jobClass ) {

		boolean getReceiptDataJob = false;
		// 获取配置
		if (CommonUtil.getSystemConfigProperty(jobNameConfigProperty) != null) {
			getReceiptDataJob = Boolean.parseBoolean(CommonUtil
					.getSystemConfigProperty(jobNameConfigProperty));
		}
		try {
			if (getReceiptDataJob) {
				// 获取执行周期表达式
				String cronExpression = "0/30 * * * * ?";
				if (CommonUtil
						.getSystemConfigProperty(cronExpressionConfigProperty) != null) {
					cronExpression = CommonUtil.getSystemConfigProperty(
							cronExpressionConfigProperty).toString();
				}
				//
				if (!quartzManagerService.IsJobExist(jobType)) {
					quartzManagerService.addJob(jobType,
							jobClass, cronExpression);
				}
			} else {
				// 删除任务
				if (quartzManagerService.IsJobExist(jobType)) {
					quartzManagerService.ctrlJob(jobType,
							CommonDefine.QUARTZ.JOB_DELETE);
				}
			}
		} catch (Exception e) {
			ExceptionHandler.handleException(e);
		}
	}
}
