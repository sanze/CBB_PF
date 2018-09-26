package com.foo.manager.commonManager.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.foo.IService.ICommonManagerService;
import com.foo.IService.ISystemManagerService;
import com.foo.abstractAction.AbstractAction;
import com.foo.common.CommonDefine;
import com.foo.common.CommonException;
import com.foo.common.IMethodLog;
import com.foo.common.MessageCodeDefine;
import com.foo.manager.commonManager.model.AuthRegion;
import com.foo.manager.commonManager.model.AuthTreeModel;
import com.foo.util.JsonUtil;
import com.opensymphony.xwork2.ModelDriven;

public class UserManagementAction extends AbstractAction  implements ModelDriven<AuthRegion>{
	
	private static String param_key_table = "tableName";
	
	@Resource
	public ISystemManagerService systemManagerService;
	@Resource
	public ICommonManagerService commonManagerService;
	
	public AuthRegion authRegion = new AuthRegion();
	@Override
	public AuthRegion getModel(){
		  return authRegion;
	}
	
	/**
	 * @return
	 */
	@IMethodLog(desc = "用户信息查询")
	public String getUserList(){
		try {
			params.put(param_key_table, "T_SYS_USER");
			Map<String,Object> userList = commonManagerService.getDataList(params);
			resultObj = JSONObject.fromObject(userList);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}
	
	/**
	 * 详情
	 * @return
	 */
	@IMethodLog(desc = "获取用户信息")
	public String getUserDetailInfo(){
		try {
			
			JSONObject jsonObject = JSONObject.fromObject(jsonString);
			
			Map data = (Map) jsonObject.toBean(jsonObject, Map.class);
			
			Integer sysUserId = Integer.valueOf(data.get("sysUserId").toString());
			
			Map<String,Object> userDetailInfo = systemManagerService.getUserDetailInfo(sysUserId);
			resultObj = JSONObject.fromObject(userDetailInfo);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}
	
	/**
	 * 添加用户
	 * @return
	 */
	@IMethodLog(desc = "修改用户")
	public String modifyUser(){
		try {
			
			JSONObject jsonObject = JSONObject.fromObject(jsonString);
			
			Map data = (Map) jsonObject.toBean(jsonObject, Map.class);
			
			systemManagerService.modifyUser(data);
			
			result.setReturnResult(CommonDefine.SUCCESS);
			
			resultObj = JSONObject.fromObject(result);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}

	@IMethodLog(desc = "删除用户")
	public String deleteUser(){
		try {
			
			JSONObject jsonObject = JSONObject.fromObject(jsonString);
			
			Map data = (Map) jsonObject.toBean(jsonObject, Map.class);
			
			//获取角色列表
			List<Integer> userList = (List<Integer>) data.get("sysUserIdList");
			
			//检查是否包含自己
			for(Integer userId:userList){
				if(sysUserId.intValue() == userId.intValue()){
					throw new CommonException(new NullPointerException(),
							MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR,"不能删除自己！");
				}
			}
			
			systemManagerService.deleteUser(data);
			
			result.setReturnResult(CommonDefine.SUCCESS);
			
			resultObj = JSONObject.fromObject(result);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}
	
	@IMethodLog(desc = "修改密码")
	public String saveModifyPass(){
		try {
			
			JSONObject jsonObject = JSONObject.fromObject(jsonString);
			
			Map data = (Map) jsonObject.toBean(jsonObject, Map.class);
			
			systemManagerService.saveModifyPass(data);
			
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
	 * @return
	 */
	@IMethodLog(desc = "角色信息查询")
	public String getRoleList(){
		try {
			params.put(param_key_table, "T_SYS_ROLE");
			Map<String,Object> userList = commonManagerService.getDataList(params);
			resultObj = JSONObject.fromObject(userList);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}
	
	/**
	 * @return
	 */
	public String addRole(){
		try {
			JSONObject jsonObject = JSONObject.fromObject(jsonString);
			
			Map data = (Map) jsonObject.toBean(jsonObject, Map.class);
			//新增数据
			systemManagerService.addRole(data);
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
	 * @return
	 */
	@IMethodLog(desc = "修改角色")
	public String modifyRole(){
		try {
			
			JSONArray jsonObject = JSONArray.fromObject(jsonString);
			
			List dataList = jsonObject.toList(jsonObject, Map.class);
			
			systemManagerService.modifyRole(dataList);
			
			result.setReturnResult(CommonDefine.SUCCESS);
			
			resultObj = JSONObject.fromObject(result);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}
	
	
	@IMethodLog(desc = "删除角色")
	public String deleteRole(){
		try {
			
			JSONArray jsonObject = JSONArray.fromObject(jsonString);
			
			List dataList = jsonObject.toList(jsonObject, Map.class);
			
			systemManagerService.deleteRole(dataList);
			
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
	 * Method name: getAuthTreeNodes <BR>
	 * Description: 获取树状数据<BR>
	 * Remark: 2013-12-07<BR>
	 * @author wuchao
	 * @return String<BR>
	 */
	public String getAuthTreeNodes(){
		
		List<AuthTreeModel> treeList= systemManagerService.getAuthTreeNodes(authRegion);
		resultArray =JsonUtil.getJson4JavaList(treeList);
//		for(Object o:resultArray){
//			JSONObject json=(JSONObject)o;
//			if(!(Boolean)json.get("leaf")){
//				json.remove("checked");
//			}
//		}
		return RESULT_ARRAY;
	}
	
	
	/**
	 * Method name: saveDeviceRegionData <BR>
	 * Description: 设备域保存<BR>
	 * Remark: 2013-12-07<BR>
	 * @author wuchao
	 * @return String<BR>
	 */
	@IMethodLog(desc = "角色保存", type = IMethodLog.InfoType.MOD)
	public String saveAuthRegionData(){
		Map<String, Object> map=new HashMap();
		if(authRegion.getId().equals("0")){
//			//验证权限域名称是否存在
//			Map validateResult=validateUserAuthDomainName();
//			if(!(Boolean)validateResult.get("success")){
//				resultObj = JSONObject.fromObject(validateResult);
//				return RESULT_OBJ;
//			}
		}
		try {
			map = systemManagerService.saveAuthRegionData(authRegion);
		} catch (CommonException e) {
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		// 将返回的结果转成JSON对象，返回前台
		resultObj = JSONObject.fromObject(map);
		return RESULT_OBJ;
	}
	
	/**
	 * Method name: getMenuAuthsByAuthDomainId <BR>
	 * Description: 获取权限域对应的菜单权限<BR>
	 * Remark: 2013-12-10<BR>
	 * @author wuchao
	 * @return String<BR>
	 */
	public String getMenuAuthsByAuthDomainId(){
		Map<String,Object> menus;
		try {
			List<Map> menuAuths= systemManagerService.getMenuAuthsByAuthDomainId(authRegion);
			menus = new HashMap<String,Object>();
			menus.put("menus",menuAuths);
			resultObj = JSONObject.fromObject(menus);
		} catch (CommonException e) {
			e.printStackTrace();
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
		}
		return RESULT_OBJ;
	}
	
	/**
	 * Method name: deleteAuthRegions <BR>
	 * Description: 删除权限域记录<BR>
	 * Remark: 2013-12-07<BR>
	 * @author wuchao
	 * @return String<BR>
	 */
	@IMethodLog(desc = "删除角色", type = IMethodLog.InfoType.DELETE)
	public String deleteAuthRegions(){
		Map<String, Object> map=new HashMap();
		try {
			map = systemManagerService.deleteAuthRegions(authRegion);
		} catch (CommonException e) {
			e.printStackTrace();
			result.setReturnResult(CommonDefine.FAILED);
			result.setReturnMessage(e.getErrorMessage());
			resultObj = JSONObject.fromObject(result);
			e.printStackTrace();
		}
		// 将返回的结果转成JSON对象，返回前台
		resultObj = JSONObject.fromObject(map);
		return RESULT_OBJ;
	}
}
