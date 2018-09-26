/*!
 * Ext JS Library 3.4.0
 * Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */
//获取数据列表
var store = new Ext.data.Store({
	url : 'common!getAllContact.action',
	baseParams : {
		"limit" : myPageSize
	},
	reader : new Ext.data.JsonReader({
		totalProperty : 'total',
		root : "rows"
	}, [ "CONTACT_ID","CODE","NAME", "TEL","COUNTRY","ADDRESS","PROVINCE","CITY","DISTRICT","SPECIFIC_ADDRESS"])
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
			id:'CONTACT_ID',
			header:'CONTACT_ID',
			dataIndex:'CONTACT_ID',
			hidden:true
		},	
		{
			id:'CODE',
			header:'ID',
			dataIndex:'CODE',
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		},
		{
			id:'NAME',
			header:'姓名',
			dataIndex:'NAME',
			width:200,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		},
		{
			id:'TEL',
			header:'联系方式',
			dataIndex:'TEL',
			width:200,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		},         
//		{
//			id:'COUNTRY',
//			header:'国家',
//			dataIndex:'COUNTRY',
//			width:200,
//			editor : new Ext.form.TextField({
//				allowBlank : false
//			})
//		}, 
		new Ext.ux.grid.CodeNameColumn({
			category: relationCategory_country,
		    id : "COUNTRY",
		    header : "国家",
		    width:200,
		    dataIndex : "COUNTRY"
		}),{
			id:'ADDRESS',
			header:'地址',
			dataIndex:'ADDRESS',
			width:300
		},{
			id:'PROVINCE',
			header:'省',
			dataIndex:'PROVINCE',
			width:100,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		},{
			id:'CITY',
			header:'市',
			dataIndex:'CITY',
			width:100,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		},{
			id:'DISTRICT',
			header:'区',
			dataIndex:'DISTRICT',
			width:100,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		},{
			id:'SPECIFIC_ADDRESS',
			header:'具体地址',
			dataIndex:'SPECIFIC_ADDRESS',
			width:300,
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
			addContact();
		}
	},'-', {
		text : '修改',
		icon : '../../resource/images/btnImages/modify.png',
		handler : function() {
			modifyContact();
		}
	},'-', {
		text : '删除',
		icon : '../../resource/images/btnImages/delete.png',
		handler : function() {
			deleteContact();
		}
	}]
});

//新增
function addContact(){
	
	var url = "addContact.jsp"; 

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
function modifyContact(){
	var modifyData = store.getModifiedRecords();
	
	var jsonDataList = new Array();
	for(var i = 0; i< modifyData.length;i++){
		var jsonData = {
			"CONTACT_ID":modifyData[i].get("CONTACT_ID"),
			"CODE":modifyData[i].get("CODE"),
			"NAME":modifyData[i].get("NAME"),
			"TEL":modifyData[i].get("TEL"),
			"COUNTRY":modifyData[i].get("COUNTRY"),
			"ADDRESS":modifyData[i].get("ADDRESS"),
			"PROVINCE":modifyData[i].get("PROVINCE"),
			"CITY":modifyData[i].get("CITY"),
			"DISTRICT":modifyData[i].get("DISTRICT"),
			"SPECIFIC_ADDRESS":modifyData[i].get("SPECIFIC_ADDRESS")
		};
		jsonDataList.push(jsonData);
    }
	var jsonString = Ext.encode(jsonDataList);
	
	Ext.getBody().mask('正在执行，请稍候...');
	
	Ext.Ajax.request({
		url : 'common!modifyContact.action',
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
function deleteContact(){
	var modifyData = gridPanel.getSelectionModel().getSelections();
	var jsonDataList = new Array();
	for(var i = 0; i< modifyData.length;i++){
		var jsonData = {
			"CONTACT_ID":modifyData[i].get("CONTACT_ID"),
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
						url : 'common!deleteContact.action',
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
