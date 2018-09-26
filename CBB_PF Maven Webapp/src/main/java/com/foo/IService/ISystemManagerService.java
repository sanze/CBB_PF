package com.foo.IService;

import java.util.List;
import java.util.Map;

import com.foo.common.CommonException;
import com.foo.manager.commonManager.model.AuthRegion;
import com.foo.manager.commonManager.model.AuthTreeModel;

public interface ISystemManagerService {

	/**
	 * 登录
	 * @param userName
	 * @param password
	 * @throws CommonException
	 */
	public void login(String userName, String password)
			throws CommonException;
	
	/**
	 * 获取用户详细信息
	 * @param sysUserId
	 * @return
	 */
	public Map<String, Object> getUserDetailInfo(Integer sysUserId) throws CommonException;
	
	/**
	 * 修改用户
	 * @param sysUserId
	 * @return
	 */
	public void modifyUser(Map<String, Object> param) throws CommonException;
	
	/**
	 * 删除用户
	 * @param sysUserId
	 * @return
	 */
	public void deleteUser(Map<String, Object> param) throws CommonException;
	
	/**
	 * 修改密码
	 * @param sysUserId
	 * @return
	 */
	public void saveModifyPass(Map<String, Object> param) throws CommonException;
	
	/**
	 * 新增角色
	 * @param sysUserId
	 * @return
	 */
	public void addRole(Map<String, Object> param) throws CommonException;
	
	/**
	 * 修改角色
	 * @param sysUserId
	 * @return
	 */
	public void modifyRole(List<Map> dataList) throws CommonException;
	
	/**
	 * 删除角色
	 * @param sysUserId
	 * @return
	 */
	public void deleteRole(List<Map> dataList) throws CommonException;
	
	
	public List<AuthTreeModel> getAuthTreeNodes(AuthRegion authRegion);
	
	
	/**
	 * 保存角色及角色关联菜单信息
	 * @param authRegion
	 * @return
	 */
	public Map<String, Object> saveAuthRegionData(AuthRegion authRegion)throws CommonException;
	
	/**
	 * 获取指定角色的权限列表
	 * @param authRegion
	 * @return
	 */
	public List<Map> getMenuAuthsByAuthDomainId(AuthRegion authRegion)throws CommonException;

	/**
	 * 删除角色
	 * @param authRegion
	 * @return
	 * @throws CommonException
	 */
	public Map<String, Object> deleteAuthRegions(AuthRegion authRegion)throws CommonException;

}
