package com.foo.manager.commonManager.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.foo.abstractService.AbstractService;
import com.foo.util.CommonUtil;
import com.sun.net.httpserver.HttpHandler;

/**
 * @author xuxiaojun
 *
 */

public abstract class HttpServerManagerService extends AbstractService implements HttpHandler{
	protected static ExecutorService executorService = Executors.newCachedThreadPool();
	
	protected static String requestUri =  CommonUtil.getSystemConfigProperty("httpServerUrl");
	public static String requestType_Order = CommonUtil.getSystemConfigProperty("requestType_Order");
	public static String requestType_inventory = CommonUtil.getSystemConfigProperty("requestType_inventory");
	public static String requestType_load = CommonUtil.getSystemConfigProperty("requestType_load");
	public static String requestType_listRelease = CommonUtil.getSystemConfigProperty("requestType_listRelease");
	public static String requestType_sn_sku = CommonUtil.getSystemConfigProperty("requestType_sn_sku");
	public static String requestType_sn_receipt = CommonUtil.getSystemConfigProperty("requestType_sn_receipt");
	public static String requestType_cj_entryOrderConfirm = CommonUtil.getSystemConfigProperty("requestType_cj_entryOrderConfirm");
	public static String requestType_sn_deliverGoodsNotify = CommonUtil.getSystemConfigProperty("requestType_sn_deliverGoodsNotify");
	public static String requestType_cj_deliveryOrderConfirm = CommonUtil.getSystemConfigProperty("requestType_cj_deliveryOrderConfirm");
	public static String requestType_cj_deliveryOrderStatus = CommonUtil.getSystemConfigProperty("requestType_cj_deliveryOrderStatus");
	
	protected static String requestParame_1 =  CommonUtil.getSystemConfigProperty("requestParame_1");
	protected static String requestParame_2 =  CommonUtil.getSystemConfigProperty("requestParame_2");
	
}
