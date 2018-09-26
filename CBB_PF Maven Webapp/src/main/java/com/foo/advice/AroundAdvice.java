package com.foo.advice;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.foo.common.CommonDefine;
import com.foo.common.CommonException;
import com.foo.common.IMethodLog;
import com.foo.handler.ExceptionHandler;
import com.foo.util.CommonUtil;


/**
 * @author xuxiaojun
 * 
 */
public class AroundAdvice implements MethodInterceptor {
	
	SimpleDateFormat sf = CommonUtil.getDateFormatter(CommonDefine.COMMON_FORMAT);

	public Object invoke(MethodInvocation methodInterceptor) throws Throwable {
		Method method = methodInterceptor.getMethod();
		IMethodLog log = method.getAnnotation(IMethodLog.class);
		Object obj = null;
		boolean isSuccess = true;
		try{
			//---methodInterceptor.proceed之前可以添加前置操作，相当于MethodBeforeAdvice
			if(log!=null){
				System.out.println(sf.format(new Date())+" 操作日志："+log.desc()+" 开始");
			}
			//目标方法执行
			obj = methodInterceptor.proceed();			
			//---methodInterceptor.proceed之后可以添加后续操作，相当于AfterReturningAdvice
			if(log!=null){
				System.out.println(sf.format(new Date())+" 操作日志："+log.desc()+" 结束");
			}
		}catch(CommonException e){
			isSuccess = false;
			//在执行目标对象方法的过程中，如果发生异常，可以在catch中捕获异常，相当于ThrowsAdvice
			ExceptionHandler.handleException(e);
		} catch(Exception e){
			isSuccess = false;
			ExceptionHandler.handleException(e);
		} finally{
			//记录操作日志
			recordOperate(log,isSuccess);
		}
		return obj;
	}
	
	//记录操作日志
	private void recordOperate(IMethodLog log,boolean isSuccess){
		if(log !=null){
			//删除修改操作入库
			if(IMethodLog.InfoType.DELETE.equals(log.type()) || IMethodLog.InfoType.MOD.equals(log.type())){
				//数据入库
				
			}
//			if(isSuccess){
//				//数据输出控制台
//				System.out.println(sf.format(new Date())+" 操作日志："+log.desc()+" 成功！");
//			}else{
//				//数据输出控制台
//				System.out.println(sf.format(new Date())+" 操作日志："+log.desc()+" 失败！");
//			}
	}
	}
}
