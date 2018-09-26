package com.foo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.foo.common.CommonDefine;
import com.foo.util.XmlUtil;

/**
 * Maven
 * @author xuxiaojun
 *
 */

public class TestXmlUtil {

//	@Test
//	public void testPaserXml(){
//		File file = new File("D:\\FTP文件目录\\GUID_301.xml");
//		Map data = XmlUtil.parseXml(file);
//		System.out.println(data);
//	}
	
	@Test
	public void testPaserXmlWithSubData(){
		File file = new File("D:\\FTP文件目录\\GUID_301.xml");
		Map data = XmlUtil.parseXmlWithSubData(file,true);
		System.out.println(data);
	}
	
	@Test
	public void testGeneralXml(){
		Map data = new HashMap();
		data.put("GUID", "GUID");
		data.put("CUSTOM_CODE", "CUSTOM_CODE");
		data.put("APP_TYPE", "APP_TYPE");
		data.put("APP_TIME", "APP_TIME");
		
		List<Map> subDataList = new ArrayList<Map>();
		Map subData1 = new HashMap();
		subData1.put("GNUM", "1");
		subData1.put("ORDER_NO", "ORDER_NO111");
		subData1.put("ITEM_NO", "ITEM_NO111");
		subData1.put("QTY", "QTY111");
		subDataList.add(subData1);
		
		Map subData2 = new HashMap();
		subData2.put("GNUM", "2");
		subData2.put("ORDER_NO", "ORDER_NO222");
		subData2.put("ITEM_NO", "ITEM_NO222");
		subData2.put("QTY", "QTY222");
		subDataList.add(subData2);
		
		File file = XmlUtil.generalXml(data, subDataList, CommonDefine.CEB301);
		System.out.println(file.getPath());
	}
	
}
