package com.foo;

import java.util.List;

import org.junit.Test;

import com.foo.IService.ISuningManagerService;
import com.foo.common.CommonException;
import com.foo.manager.commonManager.serviceImpl.SuningManagerServiceImpl;
import com.foo.model.suningModel.SnReponseContentModel;
import com.foo.model.suningModel.LogisticsCrossbuyTask.QueryLogisticsCrossbuyTask;
import com.foo.util.XmlUtil;
import com.suning.api.DefaultSuningClient;
import com.suning.api.SuningResponse;
import com.suning.api.entity.logistics.LogisticsTaskStatusAddRequest;
import com.suning.api.entity.logistics.LogisticsTaskStatusAddResponse;

/**
 * Maven
 * @author xuxiaojun
 *
 */

public class TestSuningApi {

//	@Test
//	public void testSuningApi() {
//		SpringContextUtil xxxxxx = new SpringContextUtil(true);
//		// 测试数据 20150130
//		String startTime = "2015-01-30 00:00:01";
//		String endTime = "2015-01-30 23:59:59";
//
//		List<QueryLogisticsCrossbuyTask> dataList = null;
////		try {
////			dataList = SuningManagerService
////					.getLogisticsCrossbuyData(startTime, endTime, null, null, false);
////		} catch (CommonException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//
//		for (QueryLogisticsCrossbuyTask data : dataList) {
//			System.out.println(data.isUsed());
//		}
//	}
	
//	@Test
//	public void testFormatSuningXml() {
//		// 测试数据 20150130
//		String startTime = "2015-01-30 00:00:01";
//		String endTime = "2015-01-30 23:59:59";
//
//		QueryLogisticsCrossbuyTaskRequest request = new QueryLogisticsCrossbuyTaskRequest();
//		request.setStartTime(startTime);
//		request.setEndTime(endTime);
//		
//		request.setCheckParam(true);
//		
//		String serverUrl = "http://apipre.cnsuning.com/api/http/sopRequest";
//		String appKey = "7671c52d74ce85fde1d219c7ad9d2c81";
//		String appSecret = "c87d119d63660e4d6372f07a42d2e4e5";
//		DefaultSuningClient client = new DefaultSuningClient(serverUrl, appKey,
//				appSecret, "xml");
//		
//		try {
//			SuningResponse response = client.excute(request);
//			
//			System.out.println(response.getBody());
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
//	@Test
//	public void testLogisticsTaskStatusAddResponse() {
//
//		SnReponseContentModel content = null;
//		LogisticsTaskStatusAddRequest request = new LogisticsTaskStatusAddRequest();
//		SuningResponse response = new LogisticsTaskStatusAddResponse();
//		
//		String serverUrl = "http://apipre.cnsuning.com/api/http/sopRequest";
//		String appKey = "7671c52d74ce85fde1d219c7ad9d2c81";
//		String appSecret = "c87d119d63660e4d6372f07a42d2e4e5";
//		DefaultSuningClient client = new DefaultSuningClient(serverUrl, appKey,
//				appSecret, "xml");
//		
//		//组织请求数据
//		request.setLogisticOrderId("SNCH0100000074792");
//		request.setLogisticExpressId("62000000000000000001");
//		request.setLogisticStation("南京配送中心");
//		request.setState("1020");
//		request.setFinishedDate("2014-07-10");
//		request.setFinishedTime("16:00:00");
//		
//		try {
//			response = client.excute(request);
//			
//			content = XmlUtil.readStringXml_Content(response.getBody());
//			
//			System.out.println(response.getBody());
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
//	@Test
//	public void testGetLogisticsCrossbuyData() {
//
//		SuningManagerServiceImpl service = new SuningManagerServiceImpl();
//		
//		try {
//			List<QueryLogisticsCrossbuyTask> dataList = service.getLogisticsCrossbuyData(null, null, null, null);
//		} catch (CommonException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
//	@Test
//	public void testReadStringXml_Body_Response() {
//
//		String xml = "<sn_body><addLogisticsTaskStatus><msgType>S</msgType><logisticOrderId>SNCY0200000008999</logisticOrderId><logisticExpressId>62000000000000000001</logisticExpressId><msgText>接收SOP第三方物流信息成功!</msgText></addLogisticsTaskStatus></sn_body>";
//		
//		com.foo.model.suningModel.LogisticsCrossbuyTask.LogisticsTaskStatusAddResponse data = XmlUtil.readStringXml_Body_Response(xml);
//	}
}
