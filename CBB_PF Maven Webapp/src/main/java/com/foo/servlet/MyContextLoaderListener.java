package com.foo.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.foo.common.Stdout2Log4j;
import com.foo.util.CommonUtil;
import com.foo.util.SpringContextUtil;

public class MyContextLoaderListener extends ContextLoaderListener {
	
	@Override  
    public void contextInitialized(ServletContextEvent event) {  
        ServletContext context = event.getServletContext();   
        super.contextInitialized(event);   
        ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);   
        SpringContextUtil.setApplicationContext(ctx);
        //初始化配置信息
        initConfig(context);
    }
	
	private void initConfig(ServletContext context){
		String debugStr=CommonUtil.getSystemConfigProperty("debug");
		if("true".equals(debugStr)){
			new Stdout2Log4j();//将System.out和System.err信息输出到log4j文件中.
		}
	}
}
