/*!
 * Ext JS Library 3.4.0
 * Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */
//获取数据列表
var store = new Ext.data.Store({
	url : 's-ncommon!getAllLoads.action',
	baseParams : {
		"limit" : myPageSize
	},
	reader : new Ext.data.JsonReader({
		totalProperty : 'total',
		root : "rows"
	}, [ "LOAD_ID","LOAD_NO", "TOTAL","WEIGHT","CAR_NO","TRACY_NUM","CREAT_DATE","STATUS"])
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
			id:'LOAD_ID',
			header:'id',
			dataIndex:'LOAD_ID',
			hidden: true
		},
		{
			id:'LOAD_NO',
			header:'装载单号',
			dataIndex:'LOAD_NO',
			width:120
		},        
		{
			id:'TOTAL',
			header:'商品件数',
			dataIndex:'TOTAL',
			width:200
		}, {
			id:'WEIGHT',
			header:'商品毛重量',
			dataIndex:'WEIGHT',
			width:200
		},
		{
			id:'CAR_NO',
			header : "车牌号",
			dataIndex:'CAR_NO',
			width:200
		},
		{
			id:'TRACY_NUM',
			header : "托盘数",
			dataIndex:'TRACY_NUM',
			width:200
		},
		{
			id:'CREAT_DATE',
			header : "创建日期",
			dataIndex:'CREAT_DATE',
			width:200
		},
		{
			id:'STATUS',
			header : "<span style='font-weight:bold'>装载单状态</span>",
			dataIndex:'STATUS',
			width:200,
			editor : new Ext.form.NumberField({
				allowBlank : false,
				allowDecimals:false,
				allowNegative:false,
				maxValue:99
			})
		}
		
	]}		
);

var tbar = new Ext.Toolbar({
	items : [{
        xtype: 'tbtext',
        text: '装载单号:',
        width:80
    },{
        xtype: 'textfield',
        fieldLabel: '',
        id:"LOAD_NO_SEARCH",
        emptyText:"",
        width:100,
        anchor:'50%'
    },{
        xtype: 'tbtext',
        text: '车牌号:',
        width:80
    },{
        xtype: 'textfield',
        fieldLabel: '',
        id:"CAR_NO_SEARCH",
        emptyText:"",
        width:100,
        anchor:'50%'
    },{
        xtype: 'tbtext',
        text: '创建日期:',
        width:80
    },{
        xtype: 'textfield',
        fieldLabel: '',
        id:"CREAT_DATE_SEARCH",
        emptyText:"",
        width:100,
        anchor:'50%'
    },{
        xtype: 'tbtext',
        text: '状态:',
        width:80
    },{
        xtype: 'textfield',
        fieldLabel: '',
        id:"STATUS_SEARCH",
        emptyText:"",
        width:100,
        anchor:'50%'
    },{
		text : '查询',
		icon : '../../../resource/images/btnImages/search.png',
		handler : function() {
			search();
		}
	},'-', {
		text : '修改',
		icon : '../../../resource/images/btnImages/modify.png',
		handler : function() {
			modify();
		}
	},'-', {
		text : '状态发送',
		icon : '../../../resource/images/btnImages/sync.png',
		handler : function() {
			send();
		}
	} ]
});


var gridPanel = new Ext.grid.EditorGridPanel({
	region : 'center',
	stripeRows : true,
	autoScroll : true,
	frame : false,
	store : store,
	loadMask : true,
	border:true,
	cm : columnModel,
	sm:new Ext.grid.RowSelectionModel({singleSelect:false}),
	clicksToEdit : 2,// 设置点击几次才可编辑
	bbar: pageTool,
	tbar : tbar
});


//修改
function modify(){
	var modifyData = store.getModifiedRecords();
	
	var jsonDataList = new Array();
	for(var i = 0; i< modifyData.length;i++){
		var jsonData = {
			"LOAD_ID":modifyData[i].get("LOAD_ID"),
			"STATUS":modifyData[i].get("STATUS")
		};
		jsonDataList.push(jsonData);
    }
	var jsonString = Ext.encode(jsonDataList);
	
	Ext.getBody().mask('正在执行，请稍候...');
	
	Ext.Ajax.request({
		url : 's-ncommon!modify.action',
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

//状态发送
function send(){
	var records= gridPanel.getSelectionModel().getSelections();
	
	if(Ext.isEmpty(records)||records.length<1){
		Ext.Msg.alert("提示", '请选择一条记录');
	}else{
		var jsonDataList = new Array();
		for(var i = 0; i< records.length;i++){
			var jsonData = {
				"LOAD_NO":records[i].get("LOAD_NO"),
				"STATUS":records[i].get("STATUS")
			};
			jsonDataList.push(jsonData);
	    }
		var jsonString = Ext.encode(jsonDataList);
		
		Ext.getBody().mask('正在执行，请稍候...');
		
		Ext.Ajax.request({
			url : 's-ncommon!send.action',
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
}

//查询数据
function search(){

	var param = {"limit":myPageSize,"start":0,"fuzzy":true};
	
	if(Ext.getCmp('LOAD_NO_SEARCH').getValue()){
		Ext.apply(param,{"LOAD_NO":Ext.getCmp('LOAD_NO_SEARCH').getValue()});
	}
	if(Ext.getCmp('CAR_NO_SEARCH').getValue()){
		Ext.apply(param,{"CAR_NO":Ext.getCmp('CAR_NO_SEARCH').getValue()});
	}
	if(Ext.getCmp('CREAT_DATE_SEARCH').getValue()){
		Ext.apply(param,{"CREAT_DATE":Ext.getCmp('CREAT_DATE_SEARCH').getValue()});
	}
	if(Ext.getCmp('STATUS_SEARCH').getValue()){
		Ext.apply(param,{"STATUS":Ext.getCmp('STATUS_SEARCH').getValue()});
	}

	store.baseParams = param;

	store.load();
}


Ext.onReady(function() {
	Ext.BLANK_IMAGE_URL = "../../../resource/ext/resources/images/default/s.gif";
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
