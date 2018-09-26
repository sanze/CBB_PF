package com.foo;

import org.junit.Test;

import com.foo.util.CommonUtil;
import com.foo.util.SpringContextUtil;

/**
 * Maven
 * @author xuxiaojun
 *
 */

public class TestCommonUtil {

	@Test
	public void testGeneralGuid(){
		
		SpringContextUtil xxxxxx = new SpringContextUtil(true);
		
		String xxx = CommonUtil.generalGuid("SINOTRANS-ORDER",5,"t_orders");
		System.out.println(xxx);
	}
}
