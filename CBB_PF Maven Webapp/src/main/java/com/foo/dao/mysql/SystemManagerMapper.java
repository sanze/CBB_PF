package com.foo.dao.mysql;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.foo.manager.commonManager.model.AuthRegion;


/**
 * @author xuxiaojun
 *
 */
public interface SystemManagerMapper {
	
	/**
	 * 用户登陆
	 * 
	 * @return
	 */
	public List<Map> getUserByNameAndPass(
			@Param(value = "userName") String userName,
			@Param(value = "password") String password);

	// 获取当前用户的角色列表
	public List<Map<String, Object>> getUserRoleList(
			@Param(value = "sysUserId") Integer sysUserId);
	
	//获取权限节点
	public List<Map> getAuthTreeNodes(@Param(value = "authRegion")AuthRegion authRegion);
	
	public String getIsLeafByMenuId(String menuId);
	
	/**
	 * Method name: insert <BR>
	 * Description: 插入数据<BR>
	 * Remark: 2013-12-07<BR>
	 * @author wuchao
	 * @return Map<String, Object><BR>
	 */
	public void insert(@Param(value = "authRegion")AuthRegion authRegion);
	/**
	 * Method name: update <BR>
	 * Description: 更新数据<BR>
	 * Remark: 2013-12-07<BR>
	 * @author wuchao
	 * @return Map<String, Object><BR>
	 */
	public void update(@Param(value = "authRegion")AuthRegion authRegion);
	
	//插入权限域关联的菜单
	public void insertAuthRegionRefMenu(@Param(value = "authRegion")AuthRegion authRegion);
	
	//删除权限域关联的菜单
	public void deleteAuthRegionRefMenu(@Param(value = "authRegion")AuthRegion authRegion);
	
	//返回权限域对应的权限
	public List<Map> getMenuAuthsByAuthDomainId(@Param(value = "authRegion")AuthRegion authRegion);
	
	//获取父菜单
	public Map getParentMenuByMenuId(String parentMenuId);

}