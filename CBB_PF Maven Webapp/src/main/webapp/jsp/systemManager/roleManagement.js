/*!
 * Ext JS Library 3.4.0
 * Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */
//获取数据列表
var store = new Ext.data.Store({
	url : 'user-management!getRoleList.action',
	baseParams : {
		"limit" : myPageSize
	},
	reader : new Ext.data.JsonReader({
		totalProperty : 'total',
		root : "rows"
	}, [ "NAME", "NOTE","SYS_ROLE_ID"])
});

var pageTool = new Ext.PagingToolbar({
	id : 'pageTool',
	pageSize : myPageSize,//每页显示的记录值
	store: store,
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
		//forceFit : true,
//		align:'left'
	},
	columns:[ new Ext.grid.RowNumberer({
		width : 26
	}),checkboxSelectionModel,
		{
			id:'SYS_ROLE_ID',
			header:'SYS_ROLE_ID',
			dataIndex:'SYS_ROLE_ID',
			hidden:true
		},
		{
			id:'NAME',
			header:'角色名',
			dataIndex:'NAME',
			width:200,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		},
		{
			id:'NOTE',
			header:'备注',
			dataIndex:'NOTE',
			width:200,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}
	]}		
);

var gridPanel = new Ext.grid.EditorGridPanel({
	region : 'center',
	stripeRows : true,
	autoScroll : true,
	frame : false,
	store : store,
	loadMask : true,
	border:true,
	cm : columnModel,
	selModel : checkboxSelectionModel, //必须加不然不能选checkbox 
	clicksToEdit : 2,//设置点击几次才可编辑  
	bbar: pageTool,
	tbar : [{
		text : '新增',
		icon : '../../resource/images/btnImages/add.png',
		handler : function() {
			addRole();
		}
	},'-', {
		text : '修改',
		icon : '../../resource/images/btnImages/modify.png',
		handler : function() {
			modifyRole();
		}
	},'-', {
		text : '删除',
		icon : '../../resource/images/btnImages/delete.png',
		handler : function() {
			deleteRole();
		}
	}]
});

//新增
function addRole(){
	
	var url = "addRole.jsp"; 

	var editWindow=new Ext.Window({
        id:'editWindow',
        title:"新增",
        width : 600,      
        height : 400, 
        isTopContainer : true,
        modal : true,
        autoScroll:false,
		maximized:false,
		html : '<iframe src='
			+ url
			+ ' height="100%" width="100%" frameborder=0 border=0/>'
     });
    editWindow.show();  
}

//修改
function modifyRole(){
	var modifyData = store.getModifiedRecords();
	
	var jsonDataList = new Array();
	for(var i = 0; i< modifyData.length;i++){
		var jsonData = {
			"SYS_ROLE_ID":modifyData[i].get("SYS_ROLE_ID"),
			"NAME":modifyData[i].get("NAME"),
			"NOTE":modifyData[i].get("NOTE")
		};
		jsonDataList.push(jsonData);
    }
	var jsonString = Ext.encode(jsonDataList);
	
	Ext.getBody().mask('正在执行，请稍候...');
	
	Ext.Ajax.request({
		url : 'user-management!modifyRole.action',
		type : 'post',
		params : {"jsonString":jsonString},
		success : function(response) {
			Ext.getBody().unmask();
			var obj = Ext.decode(response.responseText);
			if (obj.returnResult == 1) {
				//提交修改
				store.commitChanges();
				
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
		error : function(response) {
			Ext.getBody().unmask();
			Ext.Msg.alert("错误", response.responseText);
		},
		failure : function(response) {
			Ext.getBody().unmask();
			Ext.Msg.alert("错误", response.responseText);
		}
	});
}

//修改
function deleteRole(){
	var modifyData = gridPanel.getSelectionModel().getSelections();
	var jsonDataList = new Array();
	for(var i = 0; i< modifyData.length;i++){
		var jsonData = {
			"SYS_ROLE_ID":modifyData[i].get("SYS_ROLE_ID"),
		};
		jsonDataList.push(jsonData);
    }
	
	if(jsonDataList.length<1){
		Ext.Msg.alert("提示", "请选择至少一条数据！");
		return;
	}
	
	var jsonString = Ext.encode(jsonDataList);

	Ext.Msg.confirm('提示','是否确认删除？',
			function(btn) {
				if (btn == 'yes') {
					Ext.getBody().mask('正在执行，请稍候...');
					
					Ext.Ajax.request({
						url : 'user-management!deleteRole.action',
						type : 'post',
						params : {"jsonString":jsonString},
						success : function(response) {
							Ext.getBody().unmask();
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
						error : function(response) {
							Ext.getBody().unmask();
							Ext.Msg.alert("错误", response.responseText);
						},
						failure : function(response) {
							Ext.getBody().unmask();
							Ext.Msg.alert("错误", response.responseText);
						}
					});
				} else {

				}
			});
}


Ext.onReady(function() {
	Ext.BLANK_IMAGE_URL = "../../resource/ext/resources/images/default/s.gif";
	Ext.Ajax.timeout = 900000;
	//Ext.Msg = top.Ext.Msg;
	document.onmousedown=function(){top.Ext.menu.MenuMgr.hideAll();};
	
	var win = new Ext.Viewport({
		id : 'win',
		layout : 'border',
		items : [gridPanel]
	});
	//刷新当前页
	pageTool.doLoad(pageTool.cursor);
});
