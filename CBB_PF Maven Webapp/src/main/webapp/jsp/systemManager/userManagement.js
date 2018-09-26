﻿/*!
 * Ext JS Library 3.4.0
 * Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */

var store = new Ext.data.Store({
	//1代表查
	url : 'user-management!getUserList.action',
	baseParams : {
		"limit":200
	},
	reader : new Ext.data.JsonReader({
		totalProperty : 'total',
		root : "rows"
	}, ["SYS_USER_ID","USER_NAME","LOGIN_NAME","JOB_NUMBER","DEPARTMENT","POSITION",
	    "CREATE_TIME","EMAIL","TELEPHONE","NOTE","ROLE_NAME"])
});

store.load();

var pageTool = new Ext.PagingToolbar({
	id : 'pageTool',
	pageSize : 200,//每页显示的记录值
	store : store,
	displayInfo : true,
	displayMsg : '当前 {0} - {1} ，总数 {2}',
	emptyMsg : "没有记录"
});

var checkboxSelectionModel = new Ext.grid.CheckboxSelectionModel({
	singleSelect : false
});

var columnModel = new Ext.grid.ColumnModel({
	defaults : {
		 sortable : true,
		 forceFit : true,
		 align:'left',
		 renderer: function (data, metadata, record, rowIndex, columnIndex, store) {       
	       	    metadata.attr = 'ext:qtip="' +data+'"';   //关键  
	       	    return data ;     
       	 } 
	},
	columns:[ new Ext.grid.RowNumberer({
		width : 26
	}),
	          checkboxSelectionModel,
				{
					id : 'SYS_USER_ID',
					header : 'id',
					dataIndex : 'SYS_USER_ID',
					width : 80,
					hidden : true
				},
				{
					id:'USER_NAME',
					header:'姓名',
					dataIndex:'USER_NAME',
					width:80					
				},
				{
					id:'LOGIN_NAME',
					header:'登录名',
					dataIndex:'LOGIN_NAME',
					width:80					
				},         
				{
					id:'JOB_NUMBER',
					header:'工号',
					dataIndex:'JOB_NUMBER',
					width:80,
					hidden : true					
				}, 
				{
					id:'DEPARTMENT',
					header:'部门',
					dataIndex:'DEPARTMENT',
					width:80	,
					hidden : true				
				}, 
				{
					id:'POSITION',
					header:'职务',
					dataIndex:'POSITION',
					width:80	,
					hidden : true				
				}, 
				{
					id:'ROLE_NAME',
					header:'角色',
					dataIndex:'GROUP_NAME',
					width:100					
				}, {
					id:'EMAIL',
					header:'邮箱',
					dataIndex:'EMAIL',
					width:120					
				}, 
				{
					id:'TELEPHONE',
					header:'手机号码',
					dataIndex:'TELEPHONE',
					width:120					
				}, 
				{
					id:'NOTE',
					header:'备注',
					dataIndex:'NOTE',
					width:150					
				},{
					id:'CREATE_TIME',
					header:'创建时间',
					dataIndex:'CREATE_TIME',
					width:100,
					hidden : true
				}
]}		
);

var userListPanel = new Ext.grid.GridPanel({
	id : "userListPanel",
	region : "center",
	stripeRows : true,
	autoScroll : true, 
	frame : false,
	cm : columnModel,
	store : store,
	loadMask : true,
	columnLines:true,
	multiSelect:true,
	//collapsible: true,
	forceFit : true,
	clicksToEdit : 2,//设置点击几次才可编辑  
	selModel : checkboxSelectionModel, //必须加不然不能选checkbox 
	bbar : pageTool,
	tbar : [
          	'-',
        	{
        		text:'新增',
        		icon : '../../resource/images/btnImages/user_add.png',
        		handler : createUser
        	},	{
        		text:'修改',
        		icon : '../../resource/images/btnImages/user_edit.png',
        		handler : modifyUser		
        	},
	{
		text:'删除',
		icon : '../../resource/images/btnImages/user_delete.png',
		handler :  function(){
            var selectRecord =userListPanel.getSelectionModel().getSelections(); 
			   if(selectRecord.length == 0){
			      Ext.Msg.alert("提示","请选择需要删除的用户");
			   }else{
			      var sysUserIdList = new Array();
			      for(var i = 0; i< selectRecord.length;i++){
			    	  if(selectRecord[i].get("SYS_USER_ID")=='-1'){
			    		  Ext.Msg.alert("提示","管理员账户不能删除");
			    		  return;
			    	  }
			    	  sysUserIdList.push(selectRecord[i].get("SYS_USER_ID"));
			      }
			      deleteUser(sysUserIdList);
			   }
				
			}		
	},
	'-',
	{
		text:'重置密码',
		icon : '../../resource/images/btnImages/arrow_undo.png',
		hidden:true,
		handler :  function(){
            var selectRecord1 =userListPanel.getSelectionModel().getSelections(); 
			   if(selectRecord1.length == 0){
			      Ext.Msg.alert("提示","请选择用户");
			   }else{
			      var sysUserIdList = new Array();
			      for(var i = 0; i< selectRecord1.length;i++){
			    	  sysUserIdList.push(selectRecord1[i].get("SYS_USER_ID"));
			      }
			      reSetPassword(sysUserIdList);
			   }
				
			}		
	}
]
}		
);

function getCurrentDetail(){
	var cell = userListPanel.getSelectionModel().getSelections();
	if (cell.length > 0 && cell.length < 2) {
		var sysUserId = cell[0].get("SYS_USER_ID");
		var url = 'viewUser.jsp?sysUserId=' + sysUserId+ '&saveType=2';
		var createUserWindow = new Ext.Window({
			id : 'createUserWindow',
			title : '用户详情',
			width:Ext.getBody().getWidth()*0.75,  
			height:Ext.getBody().getHeight()-80,
			isTopContainer : true,
			modal : true,
			autoScroll : true,
			plain:true,  //是否为透明背景   
			html : '<iframe  id="modifyUser_panel" name = "modifyUser_panel"  src = ' + url
					+ ' height="100%" width="100%" frameBorder=0 border=0/>'
		});
		createUserWindow.show();

	} else
		Ext.Msg.alert("提示", "请选择需要查看的用户，每次选择一条！");
}



function createUser(){
	var createUserWindow=new Ext.Window({
		id:'createUserWindow',
		title:'新增用户',
		width:Ext.getBody().getWidth()*0.5,  
		height:Ext.getBody().getHeight()-80,
		isTopContainer : true,
		modal : true,
		plain:true,  //是否为透明背景 
		html : '<iframe src = "addUser.jsp?saveType=0&sysUserId=0" height="100%" width="100%"  frameBorder=0 border=0/>'
	});
	createUserWindow.show();
}

function modifyUser() {
	var cell = userListPanel.getSelectionModel().getSelections();
	if (cell.length !=1) {
		Ext.Msg.alert("提示", "请选择需要修改的用户,每次选择一条！");
		return;
	}
	var sysUserId = cell[0].get("SYS_USER_ID");
	var url = 'addUser.jsp?sysUserId='+ sysUserId+'&saveType=1';
	var createUserWindow = new Ext.Window({
		id : 'createUserWindow',
		title : '修改用户信息',
		width:Ext.getBody().getWidth()*0.5,  
		height:Ext.getBody().getHeight()-80,
		isTopContainer : true,
		modal : true,
		autoScroll : false,
		style:"margin-top:0",
		html : '<iframe  id="modifyUser_panel" name = "modifyUser_panel"  src = ' + url
				+ ' height="100%" width="100%" frameBorder=0 border=0/>'
	});
	createUserWindow.show();
}

function deleteUser(sysUserIdList){
	Ext.Msg.confirm('提示','删除用户。</br></br>确认删除?',
		function(btn){
			if(btn=='yes'){
				var jsonData = {
            			"sysUserIdList" : sysUserIdList
        		};
            	var jsonString = Ext.encode(jsonData);
			  Ext.Ajax.request({
				      url:'user-management!deleteUser.action',
				      method:'Post',
				      params : {"jsonString":jsonString},
				      success: function(response) {
				    	  
				    	  var obj = Ext.decode(response.responseText);
							if (obj.returnResult == 1) {
								// 刷新列表
								var pageTool = Ext.getCmp('pageTool');
								if (pageTool) {
									pageTool.doLoad(pageTool.cursor);
								}
							}
							if (obj.returnResult == 0) {
								Ext.Msg.alert("信息", obj.returnMessage);
							}
				      },
				    error:function(response) {
				    	Ext.Msg.alert("错误",response.responseText);
				    },
				    failure:function(response) {
				    	Ext.Msg.alert("错误",response.responseText);
				    }		   
			   }); 			
				}
			});
		}


function reSetPassword(sysUserIdList){
	Ext.Msg.confirm('提示','重置用户密码。</br></br>确认重置?',
		function(btn){
			if(btn=='yes'){
			
			  var jsonData = {
		       "sysUserIdList":sysUserIdList
		      };			
			  Ext.Ajax.request({
				      url:'user-management!reSetPassword.action',
				      method:'Post',
				      params:jsonData,
				      success: function(response) {
				    	  var obj = Ext.decode(response.responseText);
							if (obj.returnResult == 1) {
								// 刷新列表
								var pageTool = Ext.getCmp('pageTool');
								if (pageTool) {
									pageTool.doLoad(pageTool.cursor);
								}
							}
							if (obj.returnResult == 0) {
								Ext.Msg.alert("信息", obj.returnMessage);
							}
						    },
				    error:function(response) {
				    	Ext.Msg.alert("错误",response.responseText);
				    },
				    failure:function(response) {
				    	Ext.Msg.alert("错误",response.responseText);
				    }
			   
			   }); 
				}
			});
		}



Ext.onReady(function() {
	Ext.Msg = top.Ext.Msg;
	Ext.BLANK_IMAGE_URL = "../../resource/ext/resources/images/default/s.gif";
	Ext.QuickTips.init(); // 开启悬停提示
	Ext.form.Field.prototype.msgTarget = 'side'; // 提示显示风格
	document.onmousedown=function(){top.Ext.menu.MenuMgr.hideAll();}
	var win = new Ext.Viewport({
		id : 'win',
		layout : 'border',
		items : [ userListPanel ]
	});

	//放最后才能显示遮罩效果
	store.load({
		callback : function(r, options, success) {
			if (success) {

			} else {
				Ext.Msg.alert('错误', '加载失败！');
			}
		}
	});
});
