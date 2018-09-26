package com.foo;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.foo.dao.mysql.CommonManagerMapper;
import com.foo.util.SpringContextUtil;

/**
 * Maven
 * @author xuxiaojun
 *
 */

public class TestCommonManagerMapper {

//	@Test
//	public void testPaserXml(){
//		File file = new File("D:\\FTP文件目录\\GUID_301.xml");
//		Map data = XmlUtil.parseXml(file);
//		System.out.println(data);
//	}
	
//	@Test
//	public void testSelectDataForMessage201() {
//
//		SpringContextUtil util = new SpringContextUtil();
//
//		CommonManagerMapper commonManagerMapper = (CommonManagerMapper) SpringContextUtil
//				.getBean("commonManagerMapper");
//
//		System.out
//				.println(commonManagerMapper
//						.selectDataForMessage201("SINOTRANS-SKU-20141223110042-0000001"));
//
//	}
	
//	@Test
//	public void testSelectNeedReceiptData() {
//
//		SpringContextUtil util = new SpringContextUtil(true);
//
//		CommonManagerMapper commonManagerMapper = (CommonManagerMapper) SpringContextUtil
//				.getBean("commonManagerMapper");
//		
//		List dataList = commonManagerMapper.selectNeedReceiptData("T_SKU",
//				"RETURN_STATUS", CommonDefine.NEED_RECEIPT_STATUS_SKU, true);
//		
//		for(Object data:dataList){
//			System.out.println(data);
//		}
//
//	}
	
	@Test
	public void testUpdateLogistics() {

		SpringContextUtil util = new SpringContextUtil(true);

		CommonManagerMapper commonManagerMapper = (CommonManagerMapper) SpringContextUtil
				.getBean("commonManagerMapper");
		
		Map data = new HashMap();
		
		data.put("GUID", "SINOTRANS-LOGISTIC-20150106125942-01");
		data.put("APP_TIME", "xxxxxxx");
		
		commonManagerMapper.updateLogistics(data, "t_logistics");

	}
	
}
