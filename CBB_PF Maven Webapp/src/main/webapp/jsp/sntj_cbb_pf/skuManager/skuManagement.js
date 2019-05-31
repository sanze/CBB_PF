/*!
 * Ext JS Library 3.4.0
 * Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */
//获取数据列表
var store = new Ext.data.Store({
	url : 's-ncommon!getAllSku.action',
	baseParams : {
		"limit" : myPageSize
	},
	reader : new Ext.data.JsonReader({
		totalProperty : 'total',
		root : "rows"
	}, [ "SKU_ID","ITEM_NO","G_CODE", "SJ_RECORD_NO","SJ_COUNTRY","UNIT","UNIT1","UNIT2","G_NAME","G_MODEL"])
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
			id:'SKU_ID',
			header:'id',
			dataIndex:'SKU_ID',
			hidden: true
		},
		{
			id:'G_NAME',
			header : "商品名称",
			dataIndex:'G_NAME',
			width:200
		},
		{
			id:'ITEM_NO',
			header:'商品编码',
			dataIndex:'ITEM_NO',
			width:120
		},        
		{
			id:'G_CODE',
			header:'海关hscode',
			dataIndex:'G_CODE',
			width:200
		},{
			id:'G_MODEL',
			header:'规格型号',
			dataIndex:'G_MODEL',
			width:150
		}, {
			id:'SJ_RECORD_NO',
			header:'商检备案料号',
			dataIndex:'SJ_RECORD_NO',
			width:200
		},
		{
			id:'SJ_COUNTRY',
			header : "商检国家码",
			dataIndex:'SJ_COUNTRY',
			width:200
		},
		{
			id:'UNIT',
			header : "申报单位",
			dataIndex:'UNIT',
			width:200
		},
		{
			id:'UNIT1',
			header : "第一法定单位",
			dataIndex:'UNIT1',
			width:200
		},
		{
			id:'UNIT2',
			header : "第二法定单位",
			dataIndex:'UNIT2',
			width:200
		}
		
	]}		
);

var tbar = new Ext.Toolbar({
	items : [{
        xtype: 'tbtext',
        text: '商品名称:',
        width:60
    },{
        xtype: 'textfield',
        fieldLabel: '',
        id:"G_NAME_SEARCH",
        emptyText:"",
        width:150,
        anchor:'50%'
    },{
        xtype: 'tbtext',
        text: '商品编码:',
        width:60
    },{
        xtype: 'textfield',
        fieldLabel: '',
        id:"ITEM_NO_SEARCH",
        emptyText:"",
        width:150,
        anchor:'50%'
    },{
        xtype: 'tbtext',
        text: '海关hscode:',
        width:80
    },{
        xtype: 'textfield',
        fieldLabel: '',
        id:"G_CODE_SEARCH",
        emptyText:"",
        width:100,
        anchor:'50%'
    },{
        xtype: 'tbtext',
        text: '商检国家码:',
        width:80
    },{
        xtype: 'textfield',
        fieldLabel: '',
        id:"SJ_COUNTRY_SEARCH",
        emptyText:"",
        width:100,
        anchor:'50%'
    },{
        xtype: 'tbtext',
        text: '申报单位:',
        width:60
    },{
        xtype: 'textfield',
        fieldLabel: '',
        id:"UNIT_SEARCH",
        emptyText:"",
        width:100,
        anchor:'50%'
    },{
        xtype: 'tbtext',
        text: '第一法定单位:',
        width:80
    },{
        xtype: 'textfield',
        fieldLabel: '',
        id:"UNIT1_SEARCH",
        emptyText:"",
        width:100,
        anchor:'50%'
    },{
        xtype: 'tbtext',
        text: '第二法定单位:',
        width:80
    },{
        xtype: 'textfield',
        fieldLabel: '',
        id:"UNIT2_SEARCH",
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
	if(Ext.getCmp('G_NAME_SEARCH').getValue()){
		Ext.apply(param,{"G_NAME":Ext.getCmp('G_NAME_SEARCH').getValue()});
	}
	if(Ext.getCmp('ITEM_NO_SEARCH').getValue()){
		Ext.apply(param,{"ITEM_NO":Ext.getCmp('ITEM_NO_SEARCH').getValue()});
	}
	if(Ext.getCmp('G_CODE_SEARCH').getValue()){
		Ext.apply(param,{"G_CODE":Ext.getCmp('G_CODE_SEARCH').getValue()});
	}
	if(Ext.getCmp('SJ_COUNTRY_SEARCH').getValue()){
		Ext.apply(param,{"SJ_COUNTRY":Ext.getCmp('SJ_COUNTRY_SEARCH').getValue()});
	}
	if(Ext.getCmp('UNIT_SEARCH').getValue()){
		Ext.apply(param,{"UNIT":Ext.getCmp('UNIT_SEARCH').getValue()});
	}
	if(Ext.getCmp('UNIT1_SEARCH').getValue()){
		Ext.apply(param,{"UNIT1":Ext.getCmp('UNIT1_SEARCH').getValue()});
	}
	if(Ext.getCmp('UNIT2_SEARCH').getValue()){
		Ext.apply(param,{"UNIT2":Ext.getCmp('UNIT2_SEARCH').getValue()});
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
