/*!
 * Ext JS Library 3.4.0
 * Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */
//获取数据列表
var store = new Ext.data.Store({
	url : 's-ncommon!getAllInventory.action',
	baseParams : {
		"limit" : myPageSize
	},
	reader : new Ext.data.JsonReader({
		totalProperty : 'total',
		root : "rows"
	}, [ "INVENTORY_ID","ORDER_NO","ITEM_NUMBER", "INVT_NO","STATUS","CREAT_TIME","UPDATE_TIME"])
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
			id:'INVENTORY_ID',
			header:'id',
			dataIndex:'INVENTORY_ID',
			hidden: true
		},
		{
			id:'ORDER_NO',
			header:'出库单号',
			dataIndex:'ORDER_NO',
			width:250
		},        
		{
			id:'ITEM_NUMBER',
			header:'商品种类数',
			dataIndex:'ITEM_NUMBER',
			width:200
		}, {
			id:'INVT_NO',
			header:'清单号',
			dataIndex:'INVT_NO',
			width:200
		},
		{
			id:'STATUS',
			header : "状态",
			dataIndex:'STATUS',
			width:200,
			renderer:function(value) {
				if(value == 0){
					return value+":未生成清单";
				}
				else if(value == 1){
					return value+":已生成清单";
				}
				else if(value == 2){
					return value+":异常";
				}
			}
		},
		{
			id:'CREAT_TIME',
			header : "创建时间",
			dataIndex:'CREAT_TIME',
			width:200,
			renderer:function(value) {
				return new Date(value.time).dateFormat('Y-m-d H:m:s');
			}

		},
		{
			id:'UPDATE_TIME',
			header : "修改时间",
			dataIndex:'UPDATE_TIME',
			width:200,
			renderer:function(value) {
				return new Date(value.time).dateFormat('Y-m-d H:m:s');
			}
		}
		
	]}		
);

var tbar = new Ext.Toolbar({
	items : [{
        xtype: 'tbtext',
        text: '出库单号:',
        width:80
    },{
        xtype: 'textfield',
        fieldLabel: '',
        id:"ORDER_NO_SEARCH",
        emptyText:"",
        width:250,
        anchor:'50%'
    },{
        xtype: 'tbtext',
        text: '商品种类数:',
        width:80
    },{
        xtype: 'textfield',
        fieldLabel: '',
        id:"ITEM_NUMBER_SEARCH",
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
	},{
		text : '重新发送',
		icon : '../../../resource/images/btnImages/sync.png',
		handler : function() {
			send();
		}
	}]
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


//查询数据
function search(){
	var param = {"limit":myPageSize,"start":0,"fuzzy":true};
	
	if(Ext.getCmp('ORDER_NO_SEARCH').getValue()){
		Ext.apply(param,{"ORDER_NO":Ext.getCmp('ORDER_NO_SEARCH').getValue()});
	}
	if(Ext.getCmp('STATUS_SEARCH').getValue()){
		Ext.apply(param,{"STATUS":Ext.getCmp('STATUS_SEARCH').getValue()});
	}
	if(Ext.getCmp('ITEM_NUMBER_SEARCH').getValue()){
		Ext.apply(param,{"ITEM_NUMBER":Ext.getCmp('ITEM_NUMBER_SEARCH').getValue()});
	}

	store.baseParams = param;

	store.load();
}

//向天津重新发送报文
function send(){
	var records= gridPanel.getSelectionModel().getSelections();
	
	flag = true;
	
	if(Ext.isEmpty(records)||records.length<1){
		flag = false;
	}
	
	for(var i = 0; i< records.length;i++){
		if(records[i].get("STATUS") != 1){
			flag = false;
			break;
		}
	}
	//判断数据正确性
	if(flag){
		var jsonDataList = new Array();
		for(var i = 0; i< records.length;i++){
			var jsonData = {
				"INVENTORY_ID":records[i].get("INVENTORY_ID"),
				"ORDER_NO":records[i].get("ORDER_NO"),
				"STATUS":records[i].get("STATUS")
			};
			jsonDataList.push(jsonData);
	    }
		var jsonString = Ext.encode(jsonDataList);
		
		Ext.getBody().mask('正在执行，请稍候...');
		
		Ext.Ajax.request({
			url : 's-ncommon!sendToTJ.action',
			type : 'post',
			params : {"jsonString":jsonString},
			success : function(response) {
				Ext.getBody().unmask();
				Ext.Msg.minWidth = 500;
				var obj = Ext.decode(response.responseText);
				Ext.Msg.alert("信息", obj.returnMessage);
				// 刷新列表
				var pageTool = parent.Ext.getCmp('pageTool');
				if (pageTool) {
					pageTool.doLoad(pageTool.cursor);
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
	}else{
		Ext.Msg.alert("信息", '请至少选择一条数据且状态为1！');
	}

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
