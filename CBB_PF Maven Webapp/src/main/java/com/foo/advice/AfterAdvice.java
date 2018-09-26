package com.foo.advice;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.aop.AfterReturningAdvice;

import com.foo.common.CommonDefine;
import com.foo.common.IMethodLog;
import com.foo.util.CommonUtil;

/**
 * @author xuxiaojun
 * 
 */

public class AfterAdvice implements AfterReturningAdvice {
	
	SimpleDateFormat sf = CommonUtil.getDateFormatter(CommonDefine.COMMON_FORMAT);
	
	public void afterReturning(Object returnValue, Method method,
			Object[] args, Object target) throws Throwable {
		IMethodLog log = method.getAnnotation(IMethodLog.class);
		if(log!=null){
			System.out.println(sf.format(new Date())+" 操作日志："+log.desc()+" 结束");
		}
	}
}
