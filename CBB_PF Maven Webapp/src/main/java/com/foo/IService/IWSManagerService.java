package com.foo.IService;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;


/**
 * @author xuxiaojun
 *
 */
@WebService
public interface IWSManagerService {
	
	/**
	 * 解析报文
	 * @param xmlString xml字符串
	 * @return 结果字符串
	 */
	/*	<ParseXml>
		<xml>
		</xml>
		<fileType>SNT101</fileType>
	</ParseXml>*/
	@WebMethod 
	@WebResult
	public String ParseXml(String xmlString);
	
}
