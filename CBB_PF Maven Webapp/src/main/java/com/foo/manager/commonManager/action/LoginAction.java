package com.foo.manager.commonManager.action;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.foo.IService.ISystemManagerService;
import com.foo.abstractAction.AbstractAction;
import com.foo.common.CommonDefine;
import com.foo.common.CommonException;
import com.foo.common.CommonResult;
import com.foo.common.IMethodLog;


public class LoginAction extends AbstractAction{

	@Resource
	public ISystemManagerService systemManagerService;
	public String userName;
	public String password;

	/**
	 * 登入
	 * @throws CommonException 
	 */
	@IMethodLog(desc = "登录")
	public String login(){

		try {
			systemManagerService.login(userName,password);
			
			result.setReturnResult(CommonDefine.SUCCESS);
			
			resultObj = JSONObject.fromObject(result);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}

	/**
	 * 用户注销
	 * @throws CommonException 
	 */
	@IMethodLog(desc = "用户注销")
	public String logout(){

//			systemManagerService.logout(sysUserId, new Date());
			
		ServletActionContext.getRequest().getSession().removeAttribute("SYS_USER_ID");
		ServletActionContext.getRequest().getSession().invalidate();
		
		result.setReturnResult(CommonDefine.SUCCESS);
		
		resultObj = JSONObject.fromObject(result);

		return RESULT_OBJ;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
