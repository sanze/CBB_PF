package com.foo.manager.commonManager.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foo.IService.ISystemManagerService;
import com.foo.common.CommonDefine;
import com.foo.common.CommonException;
import com.foo.common.CommonResult;
import com.foo.common.MessageCodeDefine;
import com.foo.dao.mysql.SystemManagerMapper;
import com.foo.handler.MessageHandler;
import com.foo.manager.commonManager.model.AuthRegion;
import com.foo.manager.commonManager.model.AuthTreeModel;
import com.foo.manager.commonManager.service.SystemManagerService;

@Service
@Transactional(rollbackFor = Exception.class)
public class SystemManagerServiceImpl extends SystemManagerService implements ISystemManagerService{

	
	@Resource
	private SystemManagerMapper systemManagerMapper;
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.fujitsu.IService.ISystemManagerService#login(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void login(String userName, String password)
			throws CommonException {
		Map result = new HashMap<String, Object>();

		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();

		//
		Map user = null;
		List<Map> rows = systemManagerMapper.getUserByNameAndPass(userName,
				password);
		if (rows.size() > 0) {
			user = rows.get(0);
		}
		if (user != null) {
			session.setAttribute("SYS_USER_ID", user.get("SYS_USER_ID"));
			session.setAttribute("USER_NAME", user.get("USER_NAME"));
			session.setAttribute("LOGIN_NAME", user.get("LOGIN_NAME"));
			session.setAttribute("TELEPHONE", user.get("TELEPHONE"));
			// session.setAttribute("privilege", privilegeString);
			session.setAttribute("clientIP", request.getRemoteAddr());
		} else {
			throw new CommonException(new NullPointerException(),
					MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR,"用户名或密码错误！");
		}
	}
	
	@Override
	public Map<String, Object> getUserDetailInfo(Integer sysUserId){
		
		String tableName = "T_SYS_USER";
		
		Map<String, Object> userDetailInfoMap = new HashMap<String, Object>();
		List<Map<String, Object>> userInfo = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> roleInfo = new ArrayList<Map<String, Object>>();
		//用户信息
		userInfo = commonManagerMapper.selectTableListById(tableName, "SYS_USER_ID", sysUserId, null, null);
		userDetailInfoMap.put("userInfo", userInfo);
		//角色信息
		roleInfo = systemManagerMapper.getUserRoleList(sysUserId);
		userDetailInfoMap.put("roleInfo", roleInfo);
		
		return userDetailInfoMap;

	}
	
	@Override
	public void modifyUser(Map<String, Object> param) throws CommonException {
		String sysUserId = null;
		
		Map primary=new HashMap();
		primary.put("primaryId", null);
		//获取角色列表
		List<Integer> roleList = (List<Integer>) param.get("roleArray");
		//新增用户
		if(param.get("SYS_USER_ID")  == null){
			//登录名唯一性验证
			List userList = commonManagerMapper.selectTableListByCol("T_SYS_USER",
					"LOGIN_NAME", param.get("LOGIN_NAME"), null, null);
			
			if(userList.size()>0){
				throw new CommonException(new NullPointerException(),
						MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR,"登录名已存在！");
			}
			//保存用户信息
			param.remove("roleArray");
			param.put("PASSWORD", "888888");
			param.put("CREATE_TIME", new Date());
			
			
			commonManagerMapper.insertTableByNVList("T_SYS_USER",
					new ArrayList<String>(param.keySet()), 
					new ArrayList<Object>(param.values()),
					primary);
			sysUserId = primary.get("primaryId").toString();

		}else{
			//更新用户信息
			sysUserId = param.get("SYS_USER_ID").toString();
			param.remove("roleArray");
			commonManagerMapper.updateTableByNVList("T_SYS_USER",
					"SYS_USER_ID", sysUserId,
					new ArrayList<String>(param.keySet()),
					new ArrayList<Object>(param.values()));
			//删除用户关联角色信息
			commonManagerMapper.delTableById("T_SYS_USER_REF_ROLE", "SYS_USER_ID", sysUserId);
		}
		//保存用户关联角色信息
		for(Integer role:roleList){
			Map data = new HashMap();
			data.put("SYS_USER_ID", sysUserId);
			data.put("SYS_ROLE_ID", role);
			commonManagerMapper.insertTableByNVList("T_SYS_USER_REF_ROLE",
					new ArrayList<String>(data.keySet()), 
					new ArrayList<Object>(data.values()),
					primary);
		}
	}
	
	@Override
	public void deleteUser(Map<String, Object> param) throws CommonException {
		//获取角色列表
		List<Integer> userList = (List<Integer>) param.get("sysUserIdList");

		for(Integer sysUserId:userList){
			//删除用户关联角色信息
			commonManagerMapper.delTableById("T_SYS_USER", "SYS_USER_ID", sysUserId);
			//删除用户关联角色信息
			commonManagerMapper.delTableById("T_SYS_USER", "SYS_USER_ID", sysUserId);
		}
	}
	
	
	@Override
	public void saveModifyPass(Map<String, Object> param) throws CommonException {
		
		Integer sysUserId = Integer.valueOf(param.get("sysUserId").toString());
		
		//获取用户信息
		List userList = commonManagerMapper.selectTableListById("T_SYS_USER", "SYS_USER_ID", sysUserId, null, null);
		//获取用户
		Map user = (Map) userList.get(0);
		 //获取数据库中密码
		 String oldPassDB = user.get("PASSWORD").toString();
		 //获取页面旧密码
		 String oldPassPg = param.get("oldPass").toString();
		//获取页面新密码
		 String newPassPg = param.get("newPass").toString();
		 
		 if(oldPassDB.equals(oldPassPg)){
			 Map data=new HashMap();
			//保存用户信息
			 data.put("PASSWORD", newPassPg);
			commonManagerMapper.updateTableByNVList("T_SYS_USER",
					"SYS_USER_ID", sysUserId,
					new ArrayList<String>(data.keySet()),
					new ArrayList<Object>(data.values()));
		 }else{
			 throw new CommonException(new NullPointerException(),
						MessageCodeDefine.COM_EXCPT_INTERNAL_ERROR,"原始密码不正确！");
		 } 
	}
	
	
	@Override
	public void addRole(Map data) {
		Map primary=new HashMap();
		primary.put("primaryId", null);
		
		data.put("CREATE_TIME", new Date());
		
		commonManagerMapper.insertTableByNVList("T_SYS_ROLE",
				new ArrayList<String>(data.keySet()), 
				new ArrayList<Object>(data.values()),
				primary);
	}
	
	
	@Override
	public void modifyRole(List<Map> dataList) throws CommonException {

		// 更新数据
		for (Map data : dataList) {
			commonManagerMapper.updateTableByNVList("T_SYS_ROLE",
					"SYS_ROLE_ID", data.get("SYS_ROLE_ID"),
					new ArrayList<String>(data.keySet()),
					new ArrayList<Object>(data.values()));
		}
	}
	
	@Override
	public void deleteRole(List<Map> dataList) throws CommonException {
		// 更新数据
		for (Map data : dataList) {
			commonManagerMapper.delTableById("T_SYS_ROLE",
					"SYS_ROLE_ID", data.get("SYS_ROLE_ID"));
		}
	}
	
	
	//获取树状数据
	@Override
	public List<AuthTreeModel> getAuthTreeNodes(AuthRegion authRegion) {
		List<AuthTreeModel> authNodes=new ArrayList<AuthTreeModel>();
		if(authRegion.getMenuId()==null){
			return null;
		}
		String isLeaf;//判定传过来来的菜单是否为叶子节点
		if(authRegion.getMenuId()!=null && "0".equals(authRegion.getMenuId())){
			isLeaf="0";
		}else{
			isLeaf=systemManagerMapper.getIsLeafByMenuId(authRegion.getMenuId());
		}
		if(isLeaf!=null && "0".equals(isLeaf)){//不是叶子节点
			List<Map> authList=systemManagerMapper.getAuthTreeNodes(authRegion);
			if(authList!=null && authList.size()>0){
				for(int i=0;i<authList.size();i++){
					Map m=authList.get(i);
					AuthTreeModel d=new AuthTreeModel();
					d.setId(m.get("sys_menu_id").toString());
					d.setText(m.get("menu_display_name").toString());
					d.setNode(m.get("sys_menu_id").toString());
					if(m.get("is_leaf") !=null){
						d.setLeaf(m.get("is_leaf").toString().equals("1"));
					}
					authNodes.add(d);
				}
			}
			return authNodes;
		}else if(isLeaf!=null && "1".equals(isLeaf)){//是叶子节点
//			return getOperatorAuths(authRegion);
		}
		return authNodes;
	}
	
	//保存角色
	@Override
	public Map<String, Object> saveAuthRegionData(AuthRegion authRegion)throws CommonException {
		Map m = new HashMap();

		try{
			if(authRegion.getId().equals("0")){
				//生成时间
				authRegion.setCreateTime(new Date());
				
				systemManagerMapper.insert(authRegion);
				Integer key=Integer.parseInt(authRegion.getId());
				if(authRegion.getAuthLists()!=null){
					for(int i=0;i<authRegion.getAuthLists().size();i++){
						String id=authRegion.getAuthLists().get(i);
						if(id!=null || !"".equals(id)){
							String[] ids=id.split("_");
							authRegion.setMenuId(ids[0]);
//							authRegion.setAuthId(ids[1]);
							systemManagerMapper.insertAuthRegionRefMenu(authRegion);
						}
					}
				}
			}else{
				systemManagerMapper.update(authRegion);
				//删除权限与关联的菜单
				systemManagerMapper.deleteAuthRegionRefMenu(authRegion);
				//插入权限与关联的菜单
				if(authRegion.getAuthLists()!=null){
					for(int i=0;i<authRegion.getAuthLists().size();i++){
						String id=authRegion.getAuthLists().get(i);
						String[] ids=id.split("_");
						authRegion.setMenuId(ids[0]);
//						authRegion.setAuthId(ids[1]);
						systemManagerMapper.insertAuthRegionRefMenu(authRegion);
					}
				}
			}
			m.put("success", true);
			m.put("msg", "保存成功！");
		}catch(Exception e){
			e.printStackTrace();
			m.put("success", false);
			m.put("msg", "保存失败！");
		}
		return m;
	}
	
	@Override
	public List<Map> getMenuAuthsByAuthDomainId(AuthRegion authRegion) throws CommonException {
		List<Map> menus=systemManagerMapper.getMenuAuthsByAuthDomainId(authRegion);
		List<Map> datas=new ArrayList<Map>();
		if(menus!=null && menus.size()>0){
			for(int i=0;i<menus.size();i++){
				Map menu=menus.get(i);
				Map data=new HashMap();
				data.put("id",menu.get("sys_menu_id").toString()+"_");
				data.put("node",menu.get("sys_menu_id").toString());
				String menuPath=getMenuPathByMenu(menu);//获取菜单父子关系路径
				if(menuPath!=null){
					data.put("text",menuPath);
				}
				datas.add(data);
			}
		}
		return datas;
	}
	
	private String getMenuPathByMenu(Map menu) {
		String parentMenuId=menu.get("menu_parent_id").toString();
		String menuPath=menu.get("menu_display_name").toString();
		while(parentMenuId!=null && !"0".equals(parentMenuId)){
			//获取父菜单
			Map parentMenu=systemManagerMapper.getParentMenuByMenuId(parentMenuId);
			if(parentMenu!=null){
				menuPath=parentMenu.get("menu_display_name").toString()+"-->"+menuPath;
				parentMenuId=parentMenu.get("menu_parent_id").toString();
			}else{
				return menuPath;
			}
		}
		return menuPath;
	}
	
	@Override
	public Map<String, Object> deleteAuthRegions(AuthRegion authRegion)throws CommonException {
		Map m = new HashMap();
		try{
			if(authRegion.getIds()!=null){
				for(int i=0;i<authRegion.getIds().size();i++){
					//先删除权限域关联的菜单
					authRegion.setId(authRegion.getIds().get(i));
//					authRegionManageMapper.deleteAuthRegionRefMenu(authRegion);
//					//删除用户引用的权限域
//					authRegionManageMapper.deleUserRefAuthRegion(authRegion);
					//删除权限域
					commonManagerMapper.delTableById("T_SYS_ROLE", "SYS_ROLE_ID", authRegion.getId());
				}
			}
			m.put("success", true);
			m.put("msg", "保存成功！");
		}catch(Exception e){
			e.printStackTrace();
			m.put("success", false);
			m.put("msg", "保存失败！");
		}
		return m;
	}

}
