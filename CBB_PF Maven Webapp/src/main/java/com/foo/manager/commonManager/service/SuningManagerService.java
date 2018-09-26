package com.foo.manager.commonManager.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.foo.common.CommonException;
import com.foo.common.MessageCodeDefine;
import com.foo.dao.mysql.CommonManagerMapper;
import com.foo.model.suningModel.ModelToMap;
import com.foo.model.suningModel.SnReponseContentModel;
import com.foo.model.suningModel.LogisticsCrossbuyTask.AddLogisticsTaskStatusResponse;
import com.foo.model.suningModel.LogisticsCrossbuyTask.QueryLogisticsCrossbuyTask;
import com.foo.util.CommonUtil;
import com.foo.util.FtpUtils;
import com.foo.util.XmlUtil;
import com.suning.api.DefaultSuningClient;
import com.suning.api.SuningRequest;
import com.suning.api.SuningResponse;
import com.suning.api.entity.logistics.LogisticsTaskStatusAddRequest;
import com.suning.api.entity.logistics.QueryLogisticsCrossbuyTaskRequest;
import com.suning.api.exception.SuningApiException;

/**
 * @author xuxiaojun
 *
 */
public abstract class SuningManagerService extends CommonManagerService {
	@Resource
	protected CommonManagerMapper commonManagerMapper;
	
	/**
	 * 从苏宁获取数据
	 * 
	 * @param startTime
	 *            时间格式2014-12-01 10:24:00
	 * @param endTime
	 *            时间格式2014-12-01 10:24:00
	 * @param pageNo
	 *            为null获取所有
	 * @param pageSize
	 *            为null获取所有
	 * @return
	 * @throws CommonException
	 */
	public List<QueryLogisticsCrossbuyTask> getLogisticsCrossbuyData(
			String startTime, String endTime, Integer pageNo, Integer pageSize) throws CommonException {

		List<QueryLogisticsCrossbuyTask> dataList = new ArrayList<QueryLogisticsCrossbuyTask>();

		boolean getTestData = CommonUtil.getSystemConfigProperty("useTestData") != null ? Boolean
				.valueOf(CommonUtil.getSystemConfigProperty("useTestData")
						.toString()) : false;
		//测试数据地址
		String testDataFilePath = CommonUtil
				.getSystemConfigProperty("testDataFilePath") != null ? CommonUtil
				.getSystemConfigProperty("testDataFilePath").toString() : null;
		
		// 人造数据
		if (getTestData) {
			InputStream in = null;
			Document document = null;
			SAXReader saxReader = new SAXReader();
			try {
				//配置了测试数据地址，使用外部数据
				if(testDataFilePath!=null){
					 File file = new File(testDataFilePath);
					 in = new FileInputStream(file);
				}else{
					in = Thread
							.currentThread()
							.getContextClassLoader()
							.getResourceAsStream(
									"suning.logistics.crossbuytask.query.xml");
				}
				document = saxReader.read(in);
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			String xml = document.asXML();

			SnReponseContentModel content = XmlUtil.readStringXml_Content(xml);

			dataList = XmlUtil.readStringXml_Body(content.getSn_body());
		} else {
			// 苏宁服务器数据
			QueryLogisticsCrossbuyTaskRequest request = new QueryLogisticsCrossbuyTaskRequest();
			request.setStartTime(startTime);
			request.setEndTime(endTime);

			if (pageNo != null && pageSize != null) {
				request.setPageNo(pageNo);
				request.setPageSize(pageSize);
			}
			// 获取原始数据
			SnReponseContentModel content = getDataFormSnService(request);
			if (content.getSn_body() == null) {
				if (content.getSn_error() != null) {
					throw new CommonException(new NullPointerException(),
							MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR, content
									.getSn_error().getError_code());
				}
			} else {
				dataList = XmlUtil.readStringXml_Body(content.getSn_body());
			}
		}
		return dataList;
	}
	
	/**
	 * 从苏宁获取数据运单回执信息
	 * 
	 * @return
	 * @throws CommonException
	 */
	public AddLogisticsTaskStatusResponse getLogisticsTaskStatusAddResponse(LogisticsTaskStatusAddRequest request)
			throws CommonException {

		SnReponseContentModel content = getDataFormSnService(request);

		AddLogisticsTaskStatusResponse data = XmlUtil
				.readStringXml_Body_Response(content.getSn_body());
		
		return data;
	}
	
	/**
	 * 从苏宁获取数据
	 * @param request
	 * @return
	 * @throws CommonException 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private  SnReponseContentModel getDataFormSnService(SuningRequest request) throws CommonException {
		//获取是否要校验参数配置，现开发阶段配置为ture
//		if (needCheckParam) {
//			// api入参校验逻辑开关，当测试稳定之后建议设置为 false 或者删除该行
//			request.setCheckParam(true);
//		}
		request.setCheckParam(true);

		SnReponseContentModel content = null;

		String snServerUrl = CommonUtil.getSystemConfigProperty("snServerUrl");
		String appKey = CommonUtil.getSystemConfigProperty("appKey");
		String appSecret = CommonUtil.getSystemConfigProperty("appSecret");
		DefaultSuningClient client = new DefaultSuningClient(snServerUrl, appKey,
				appSecret, "xml");
		try {
			SuningResponse response = client.excute(request);
			//上传文件
			uploadRespondContent(response.getBody());
			content = XmlUtil.readStringXml_Content(response.getBody());
		} catch (SuningApiException e) {
			throw new CommonException(e,
					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR, e.getErrMsg());
		}	catch (Exception e) {
			throw new CommonException(new NullPointerException(),
					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR);
		}
		return content;
	}

	public Map<String, Object> getAllLogisticsTasks(Map<String, Object> params)
			throws CommonException {
		String startTime=""+params.get("startTime");
		String endTime=""+params.get("endTime");
		Integer start=(Integer)params.get("start");
		Integer limit=(Integer)params.get("limit");
		List<QueryLogisticsCrossbuyTask> datas=getLogisticsCrossbuyData(startTime,endTime,start,limit);
		//获取已经存在的苏宁运单
		List<String> snLogistics = commonManagerMapper
				.selectSnLogistics();
		
		List<Map<String,Object>> rows=new ArrayList<Map<String,Object>>();
		for(QueryLogisticsCrossbuyTask data:datas){
			//数据加工，标志获取的数据是否已经生成过运单
			String btcItemOrderId = data.getFeedBackImformation()
					.get("btcItemOrderId");
			if (snLogistics.contains(btcItemOrderId)) {
				data.setUsed(true);
			} else {
				data.setUsed(false);
			}
			for(Map feedBackOrderCustomer:data.getFeedBackOrderCustomers()){
                //临时版本 如果没有国家 默认添加142
                if(feedBackOrderCustomer.get("country") == null){
                	feedBackOrderCustomer.put("country", "CN");
                }
			}
			rows.add(ModelToMap.toLogisticsTask(data));
		}
		int page = rows.size();
		int total = 0;
		if(page==0){
			if(start!=null){
				total=start;
			}
		}else{
			total=(start==null?0:start)+page;
			total+=limit!=null&&page<limit?0:1;
		}
		Map<String, Object> resultMap=new HashMap<String, Object>();
		resultMap.put("rows", rows);
		resultMap.put("total", total);
		return resultMap;
	}
	
	private void uploadRespondContent(String xmlString){
		//生成xml文件并上传ftp
		File file = XmlUtil.formatXml(xmlString);
		
		FtpUtils ftpUtil = FtpUtils.getDefaultFtp();
		ftpUtil.uploadFile(file.getPath(), "/snRespondContent", file.getName());
		if(file.exists()){
			file.delete();
		}
	}
	
}
