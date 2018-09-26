package com.foo.advice;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.aop.MethodBeforeAdvice;

import com.foo.common.CommonDefine;
import com.foo.common.IMethodLog;
import com.foo.util.CommonUtil;

/**
 * @author xuxiaojun
 * 
 */
public class BeforeAdvice implements MethodBeforeAdvice {
	
	SimpleDateFormat sf = CommonUtil.getDateFormatter(CommonDefine.COMMON_FORMAT);
	// 实现MethodBeforeAdvice的before方法
	public void before(Method method, Object[] args, Object target) {
		IMethodLog log = method.getAnnotation(IMethodLog.class);
		if(log!=null){
			System.out.println(sf.format(new Date())+" 操作日志："+log.desc()+" 开始");
		}
	}
}
