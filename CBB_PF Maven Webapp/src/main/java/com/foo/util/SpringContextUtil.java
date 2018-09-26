/**
 * All rights Reserved, Copyright (C) JFTT 2011<BR>
 * 
 * FileName: SpringContextUtil.java <BR>
 * Version: $Id: SpringContextUtil.java, v 1.00 2011-9-22 $<BR>
 * Modify record: <BR>
 * NO. |     Date         |    Name                  |      Content <BR>
 * 1   |    2011-9-22       | JFTT)Cheng Yingqi        | original version <BR>
 */
package com.foo.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * @author xuxiaojun
 * 
 */
public class SpringContextUtil{

	private static ApplicationContext applicationContext;
	
	public SpringContextUtil(boolean isDebug) {
		//如果是debug
		if (isDebug) {
			ApplicationContext applicationContext = null;
			if (applicationContext == null) {
				String[] configFileList = new String[] { 
						"resourceConfig/springConfig/applicationContext.xml",
						"resourceConfig/springConfig/applicationContext-service.xml",
						"resourceConfig/springConfig/applicationContext-persistence.xml",
						"resourceConfig/springConfig/applicationContext-database.xml" ,
						"resourceConfig/springConfig/applicationContext-quartz.xml" };
				
				applicationContext = new ClassPathXmlApplicationContext(configFileList);
			}
			setApplicationContext(applicationContext);
		} else {

		}
	}

	public static void setApplicationContext(ApplicationContext applicationContext){

		SpringContextUtil.applicationContext = applicationContext;

	}

	public ApplicationContext getApplicationContext() {

		return applicationContext;

	}

	public static Object getBean(String name) throws BeansException {

//		String[] names = applicationContext.getBeanDefinitionNames();
//		for(String xxx:names){
//			System.out.println(xxx);
//		}
		return applicationContext.getBean(name);

	}

	public Object getBean(String name, Class requiredType){

		return applicationContext.getBean(name, requiredType);

	}

	public boolean containsBean(String name) {

		return applicationContext.containsBean(name);

	}

	public boolean isSingleton(String name)
			throws NoSuchBeanDefinitionException {

		return applicationContext.isSingleton(name);

	}

	public Class getType(String name)
			throws NoSuchBeanDefinitionException {

		return applicationContext.getType(name);

	}

	public String[] getAliases(String name)
			throws NoSuchBeanDefinitionException {

		return applicationContext.getAliases(name);

	}
	
	/**
	 * 获取配置数据库名
	 * @return
	 */
	public static String getDataBaseName(){
		
		DruidDataSource ds = (DruidDataSource) SpringContextUtil.getBean("dataSource-mysql");
		
		String urlWithoutParam = ds.getUrl().split("[?]")[0];
		
		return urlWithoutParam.split("[/]")[urlWithoutParam.split("[/]").length-1];

	}

	
/*	public static void main(String args[]){
		SpringContextUtil xx = new SpringContextUtil();
		try {
			SpringContextUtil.getDataCollectServiceProxy(1);
		} catch (CommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

}
