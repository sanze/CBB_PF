/*!
 * Ext JS Library 3.4.0
 * Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */
//获取数据列表
var store = new Ext.data.Store({
	url : 's-ncommon!getAllReceipt.action',
	baseParams : {
		"limit" : myPageSize
	},
	reader : new Ext.data.JsonReader({
		totalProperty : 'total',
		root : "rows"
	}, [ "RECEIPT_ID","SKU","DESCRIPTION", "EXPECT_QTY","ACTUAL_QTY","RECEIPT_NO","CREAT_DATE"])
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
			id:'RECEIPT_ID',
			header:'id',
			dataIndex:'RECEIPT_ID',
			hidden: true
		},
		{
			id:'SKU',
			header:'商品编码',
			dataIndex:'SKU',
			width:150
		},        
		{
			id:'DESCRIPTION',
			header:'商品名称',
			dataIndex:'DESCRIPTION',
			width:250
		}, {
			id:'EXPECT_QTY',
			header:'预计数量',
			dataIndex:'EXPECT_QTY',
			width:200
		},
		{
			id:'ACTUAL_QTY',
			header : "实际数量",
			dataIndex:'ACTUAL_QTY',
			width:200
		},
		{
			id:'RECEIPT_NO',
			header : "入库单号",
			dataIndex:'RECEIPT_NO',
			width:200
		},
		{
			id:'CREAT_DATE',
			header : "创建日期",
			dataIndex:'CREAT_DATE',
			width:200
		}
		
	]}		
);

var tbar = new Ext.Toolbar({
	items : [{
        xtype: 'tbtext',
        text: '商品编码:',
        width:80
    },{
        xtype: 'textfield',
        fieldLabel: '',
        id:"SKU_SEARCH",
        emptyText:"",
        width:150,
        anchor:'50%'
    },{
        xtype: 'tbtext',
        text: '入库单号:',
        width:80
    },{
        xtype: 'textfield',
        fieldLabel: '',
        id:"RECEIPT_NO_SEARCH",
        emptyText:"",
        width:200,
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
		text : '查询',
		icon : '../../../resource/images/btnImages/search.png',
		handler : function() {
			search();
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
	
	if(Ext.getCmp('SKU_SEARCH').getValue()){
		Ext.apply(param,{"SKU":Ext.getCmp('SKU_SEARCH').getValue()});
	}
	if(Ext.getCmp('RECEIPT_NO_SEARCH').getValue()){
		Ext.apply(param,{"RECEIPT_NO":Ext.getCmp('RECEIPT_NO_SEARCH').getValue()});
	}
	if(Ext.getCmp('CREAT_DATE_SEARCH').getValue()){
		Ext.apply(param,{"CREAT_DATE":Ext.getCmp('CREAT_DATE_SEARCH').getValue()});
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
