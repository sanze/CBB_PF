package com.foo.common;



/**
 * @author xuxiaojun
 * 
 */
public class CommonDefine {
	
	public final static int USER_ADMIN_ID = -1;
	
	public final static int FAILED = 0;
	public final static int SUCCESS = 1;
	//申报
	public final static int CMD_TYPE_DECLARE = 1;
	//获取回执
	public final static int CMD_TYPE_RECEIPT = 2;
	
	public final static String RESPONSE_OK = "ok";

	public static final String SYSTEM_CONFIG_FILE = "systemConfig";
	public static final String MESSAGE_CONFIG_FILE = "messageResource";

	// 日期格式
	public static final String COMMON_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public static final String COMMON_FORMAT_1 = "yyyyMMdd";

	public static final String COMMON_FORMAT_2 = "MMddHHmmssyyyy";

	public static final String COMMON_SIMPLE_FORMAT = "yyyy-MM-dd";

	public static final String COMMON_START_FORMAT = "yyyy-MM-dd 00:00:00";

	public static final String COMMON_END_FORMAT = "yyyy-MM-dd 23:59:59";

	public static final String RETRIEVAL_TIME_FORMAT = "yyyyMMddHHmmss";
	
	public static final String RETRIEVAL_TIME_FORMAT_NJ = "yyyyMMddHHmmssSSS";
	
	public static final String DAILY_FORMAT = "yyyy-MM-dd 00:30:00";

	public static final String WEEKLY_FORMAT = "yyyy-MM-dd 00:35:00";

	public static final String MONTHLY_FORMAT = "yyyy-MM-dd 00:40:00";

	public static final String SEASONLY_FORMAT = "yyyy-MM-dd 00:45:00";

	public static final String YEARLY_FORMAT = "yyyy-MM-dd 00:45:00";

	

	// ftp配置参数
	public static final String FTP_IP = "ftpIp";
	public static final String FTP_USER_NAME = "ftpUserName";
	public static final String FTP_PASSWORD = "ftpPassword";
	public static final String FTP_PORT = "ftpPort";
	
	//企业海关10位数编码
	public static final String ENTERPRISE_CODE = "EnterpriseCode";

	//报文类型定义
	//无类型
	public static final int CEB_DEFAULT = 0;
	//CEB201商品备案数据
	public static final int CEB201 = 201;
	//CEB202商品备案回执
	public static final int CEB202 = 202;
	//CEB202商品备案回执
	public static final int CEB203 = 203;
	//CEB301电子订单数据
	public static final int CEB301 = 301;
	//CEB302电子订单回执
	public static final int CEB302 = 302;
	//CEB303电子订单回执
	public static final int CEB303 = 303;
	//CEB401支付凭证数据
	public static final int CEB401 = 401;
	//CEB402支付凭证回执
	public static final int CEB402 = 402;
	//CEB501物流运单数据
	public static final int CEB501 = 501;
	//CEB502物流运单回执
	public static final int CEB502 = 502;
	//CEB503物流运单状态数据
	public static final int CEB503 = 503;
	//CEB504物流运单状态回执
	public static final int CEB504 = 504;
	
	//CEB311电子订单数据
	public static final int CEB311 = 311;
	//CEB511物流运单状态
	public static final int CEB511 = 511;
	public static final int CEB513 = 513;
	
	
	//CEB601出境清单数据
	public static final int CEB601 = 601;
	//CEB602出境清单回执
	public static final int CEB602 = 602;
	
	public static final int CEB603 = 603;
	
	public static final int CEB604 = 604;
	
	public static final int CEB606 = 606;
	
	public static final int CEB607 = 607;
	
	public static final int CEB609 = 609;
	
	public static final int CEB610 = 610;
	
	public static final int CEB612 = 612;
	
	//商品备案回执获取--单条
	public static final int CEB201_RECEIPT_SINGLE = 9201;
	//商品备案回执获取--列表
	public static final int CEB201_RECEIPT_LIST = 9202;
	
	//清单回执获取--单条--一般进口
	public static final int CEB601_RECEIPT_SINGLE = 9601;
	//清单回执获取--单条--一般出口
	public static final int CEB607_RECEIPT_SINGLE = 9607;
	//清单回执获取--单条--保税进口
	public static final int CEB604_RECEIPT_SINGLE = 9604;
	//清单回执获取--单条--一般出口
	public static final int CEB610_RECEIPT_SINGLE = 9610;
	//清单回执获取--列表--一般进口
	public static final int CEB601_RECEIPT_LIST = 9602;
	//清单回执获取--列表--一般出口
	public static final int CEB607_RECEIPT_LIST = 9608;
		
	//申请ems单号
	public static final int APPLY_EMS_NO = 9999;
		
	public static final int SNT101 = 1010;
	public static final int SNT102 = 1020;
	public static final int SNT103 = 1030;
	public static final int SNT201 = 2010;
	
	public static final String MESSAGE_TYPE_CEB201 = "CEB201";
	public static final String MESSAGE_TYPE_CEB202 = "CEB202";
	public static final String MESSAGE_TYPE_CEB203 = "CEB203";
	public static final String MESSAGE_TYPE_CEB301 = "CEB301";
	public static final String MESSAGE_TYPE_CEB303 = "CEB303";
	public static final String MESSAGE_TYPE_CEB401 = "CEB401";
	public static final String MESSAGE_TYPE_CEB501 = "CEB501";
	public static final String MESSAGE_TYPE_CEB502 = "CEB502";
	public static final String MESSAGE_TYPE_CEB503 = "CEB503";
	public static final String MESSAGE_TYPE_CEB511 = "CEB511";
	public static final String MESSAGE_TYPE_CEB513 = "CEB513";
	public static final String MESSAGE_TYPE_CEB601 = "CEB601";
	public static final String MESSAGE_TYPE_CEB602 = "CEB602";
	public static final String MESSAGE_TYPE_CEB603 = "CEB603";
	public static final String MESSAGE_TYPE_CEB604 = "CEB604";
	public static final String MESSAGE_TYPE_CEB606 = "CEB606";
	public static final String MESSAGE_TYPE_CEB607 = "CEB607";
	public static final String MESSAGE_TYPE_CEB609 = "CEB609";
	public static final String MESSAGE_TYPE_CEB610 = "CEB610";
	public static final String MESSAGE_TYPE_CEB612 = "CEB612";
	
	//GUID序号前缀
	public static final String GUID_FOR_SKU = "SINOTRANS-SKU";
	public static final String GUID_FOR_ORDER = "SINOTRANS-ORDER";
	public static final String GUID_FOR_LOGISTICS = "SINOTRANS-LOGISTIC";
	public static final String GUID_FOR_LOGISTICS_SN = "SINOTRANS-SUNING";
	public static final String GUID_FOR_LOGISTICS_SN_1 = "SINOTRANS-SN";
	public static final String GUID_FOR_INVENTORY = "SINOTRANS-EXPORT";
	
	//运单类型 1：普通 2：苏宁
	public static final int LOGISTICS_TYPE_NORMAL = 1;
	public static final int LOGISTICS_TYPE_SUNING = 2;
	public static final int LOGISTICS_TYPE_IMPORT = 3;
	
	//业务状态:1-暂存,2-申报中,默认为1  (自定义)99 申报完成
	public static final int APP_STATUS_STORE = 1;
	public static final int APP_STATUS_UPLOAD = 2;
	
	public static final int APP_STATUS_UNUSE = 4;
	public static final int APP_STATUS_COMPLETE = 99;
	
	//电子口岸已暂存
	public static final int RETURN_STATUS_1 = 1;
	//电子口岸申报中 //审批通过
	public static final int RETURN_STATUS_2 = 2;
	//发送海关成功 //审批不通过
	public static final int RETURN_STATUS_3 = 3;
	//发送海关失败
	public static final int RETURN_STATUS_4 = 4;
	//海关退单
	public static final int RETURN_STATUS_100 = 100;
	//海关入库成功
	public static final int RETURN_STATUS_120 = 120;
	//海关审结
	public static final int RETURN_STATUS_399 = 399;
	//实货放行
	public static final int RETURN_STATUS_800 = 800;
	//结关
	public static final int RETURN_STATUS_899 = 899;
	//扣留移送通关
	public static final int RETURN_STATUS_501 = 501;
	//扣留移送缉私
	public static final int RETURN_STATUS_502 = 502;
	//扣留移送法规
	public static final int RETURN_STATUS_503 = 503;
	//其它扣留
	public static final int RETURN_STATUS_599 = 599;
	
	//需要获取回执的状态集合 201 状态399作为终结
	public static final Integer[] NEED_RECEIPT_STATUS_SKU = new Integer[]{
		RETURN_STATUS_399
	};
	//需要获取回执的状态集合 301 状态120作为终结
	public static final Integer[] NEED_RECEIPT_STATUS_ORDER = new Integer[]{
		RETURN_STATUS_120
	};
	//需要获取回执的状态集合 501 503 状态120作为终结
	public static final Integer[] NEED_RECEIPT_STATUS_LOGISTICS = new Integer[]{
		RETURN_STATUS_120
	};
	//需要获取回执的状态集合 601 状态899作为终结
	public static final Integer[] NEED_RECEIPT_STATUS_INVERNTORY = new Integer[]{
		RETURN_STATUS_899
	};
	
	//1.商品备案数据 2.电子订单 3.支付凭证 4.物流运单 5.物流运单状态 6.出境清单 7.TJ
	public static final int FILE_CATEGORY_SPBA = 1;
	public static final int FILE_CATEGORY_DZDD = 2;
	public static final int FILE_CATEGORY_ZFPZ = 3;
	public static final int FILE_CATEGORY_WLYD = 4;
	public static final int FILE_CATEGORY_YDZT= 5;
	public static final int FILE_CATEGORY_CJQD = 6;
	public static final int FILE_CATEGORY_TJ = 7;
	
	//默认报文地址
	public static final String DEFAULT_GENERAL_XML= "";
	public static final String DEFAULT_RECEIPT_XML= "receipt";
	public static final String DEFAULT_TRANSFER_XML= "transfer";
	public static final String DEFAULT_INPUT_XML= "input";
	
	public static class QUARTZ {
		// job状态值
		public static final int JOB_ACTIVATE = 1;
		public static final int JOB_PAUSE = 2;
		public static final int JOB_DELETE = 3;
		public static final int JOB_RESUME = 4;
		
		//任务类型
		public static final int JOB_TYPE_GET_RECEIPT_DATA_SKU = 100;
		public static final int JOB_TYPE_GET_RECEIPT_DATA_ORDER = 200;
		public static final int JOB_TYPE_GET_RECEIPT_DATA_LOGISTICS = 300;
		public static final int JOB_TYPE_GET_RECEIPT_DATA_INVENTORY = 400;
		
		public static final int JOB_TYPE_GET_KD100_DATA = 500;
		
		public static final int JOB_TYPE_POST_SENDING = 600;
		
		public static final int JOB_TYPE_GET_INPUT_DATA_ORDER = 900;
	}

}
