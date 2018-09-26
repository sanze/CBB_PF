/*!
 * Ext JS Library 3.4.0
 * Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */
//获取数据列表

var myPageSize = 200;

var store = new Ext.data.Store({
	url : 'common!getAllFilePath.action',
	baseParams : {
		"limit" : myPageSize
	},
	reader : new Ext.data.JsonReader({
		totalProperty : 'total',
		root : "rows"
	}, [ "FILE_LOCATION_CONFIG_ID","CATEGORY", "GENERAL_XML", "RECEIPT_XML","TRANSFER_XML","INPUT_XML" ])
});

var pageTool = new Ext.PagingToolbar({
	id : 'pageTool',
	pageSize : myPageSize,//每页显示的记录值
	store: store,
	displayInfo : true,
	displayMsg : '当前 {0} - {1} ，总数 {2}',
	emptyMsg : "没有记录"
});

var columnModel = new Ext.grid.ColumnModel({
	defaults : {
		sortable : true,
		//forceFit : true,
//		align:'left'
	},
	columns:[ new Ext.grid.RowNumberer({
		width : 26
	}),
		{
			id:'FILE_LOCATION_CONFIG_ID',
			header:'id',
			dataIndex:'FILE_LOCATION_CONFIG_ID',
			hidden: true
		},
		{
			id:'CATEGORY',
			header:'类别',
			dataIndex:'CATEGORY',
			width:120,
			renderer: Renderer.CATEGORY
		},
		{
			id:'GENERAL_XML',
			header:"<span style='font-weight:bold'>生成xml</span>",
			dataIndex:'GENERAL_XML',
			width:200,
			editor : new Ext.form.TextField({
				allowBlank : false,
				regex: /^([\w\/]*)?\w$/,
				regexText:"请输入【xxx/xxx】格式，必须为英文"
			})			
		},         
		{
			id:'RECEIPT_XML',
			header:"<span style='font-weight:bold'>回执地址</span>",
			dataIndex:'RECEIPT_XML',
			width:200,
			editor : new Ext.form.TextField({
				allowBlank : false,
				regex: /^([\w\/]*)?\w$/,
				regexText:"请输入【xxx/xxx】格式，必须为英文"
			})
		}, 
		{
			id:'TRANSFER_XML',
			header : "<span style='font-weight:bold'>转移地址</span>",
			dataIndex:'TRANSFER_XML',
			width:200,
			editor : new Ext.form.TextField({
				allowBlank : false,
				regex: /^([\w\/]*)?\w$/,
				regexText:"请输入【xxx/xxx】格式，必须为英文"
			})
		},{
			id:'INPUT_XML',
			header : "<span style='font-weight:bold'>xml输入地址</span>",
			dataIndex:'INPUT_XML',
			width:200,
			
			editor : new Ext.form.TextField({
				allowBlank : false,
				regex: /^([\w\/]*)?\w$/,
				regexText:"请输入【xxx/xxx】格式，必须为英文"
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
	clicksToEdit : 2,//设置点击几次才可编辑  
	bbar: pageTool,
	tbar : [{
		text : '修改',
		icon : '../../resource/images/btnImages/modify.png',
		handler : function() {
			modifyFilePath();
		}
	}]
});

//修改
function modifyFilePath(){
	var modifyData = store.getModifiedRecords();
	
	var jsonDataList = new Array();
	for(var i = 0; i< modifyData.length;i++){
		var jsonData = {
			"FILE_LOCATION_CONFIG_ID":modifyData[i].get("FILE_LOCATION_CONFIG_ID"),
			"CATEGORY":modifyData[i].get("CATEGORY"),
			"GENERAL_XML":modifyData[i].get("GENERAL_XML"),
			"RECEIPT_XML":modifyData[i].get("RECEIPT_XML"),
			"TRANSFER_XML":modifyData[i].get("TRANSFER_XML"),
			"INPUT_XML":modifyData[i].get("INPUT_XML")
		};
		jsonDataList.push(jsonData);
    }
	var jsonString = Ext.encode(jsonDataList);
	
	Ext.getBody().mask('正在执行，请稍候...');
	
	Ext.Ajax.request({
		url : 'common!modifyFilePath.action',
		type : 'post',
		params : {"jsonString":jsonString},
		success : function(response) {
			Ext.getBody().unmask();
			var obj = Ext.decode(response.responseText);
			if (obj.returnResult == 1) {
				//提交修改
				store.commitChanges();
				
				// 刷新列表
				var pageTool = parent.Ext.getCmp('pageTool');
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


Ext.onReady(function() {
	Ext.BLANK_IMAGE_URL = "../../resource/ext/resources/images/default/s.gif";
	Ext.QuickTips.init();
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
