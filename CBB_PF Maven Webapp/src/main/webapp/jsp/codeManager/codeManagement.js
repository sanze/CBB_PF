/*!
 * Ext JS Library 3.4.0
 * Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */
//获取数据列表
var store = new Ext.data.Store({
	url : 'common!getAllCodeNames.action',
	baseParams : {
		"limit" : myPageSize
	},
	reader : new Ext.data.JsonReader({
		totalProperty : 'total',
		root : "rows"
	}, [ "CODE_NAME_ID","RELATION_CATEGORY", "RELATION_NAME","CODE","NAME","SN_CODE"])
});

var relationStore = new Ext.data.Store({
	url : 'common!getAllCodeCategory.action',
	reader : new Ext.data.JsonReader({
		totalProperty : 'total',
		root : "rows"
	}, [ "RELATION_CATEGORY","RELATION_NAME"]),
	listeners:{
	  	"load": function(store, records, options){
	  		var all = new Ext.data.Record({RELATION_CATEGORY:-1,RELATION_NAME:"全部"});
	  		relationStore.insert(0,all); 
	  	}
	}
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
			id:'CODE_NAME_ID',
			header:'id',
			dataIndex:'CODE_NAME_ID',
			hidden: true
		},
		{
			id:'RELATION_CATEGORY',
			header:'类别代号',
			dataIndex:'RELATION_CATEGORY',
			width:120
		},
		{
			id:'RELATION_NAME',
			header:'类别说明',
			dataIndex:'RELATION_NAME',
			width:200			
		},         
		{
			id:'CODE',
			header:'代号',
			dataIndex:'CODE',
			width:200
		}, {
			id:'SN_CODE',
			header:'苏宁代号',
			dataIndex:'SN_CODE',
			width:200
		},
		{
			id:'NAME',
			header : "<span style='font-weight:bold'>名称</span>",
			dataIndex:'NAME',
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
	clicksToEdit : 2,//设置点击几次才可编辑  
	bbar: pageTool,
	tbar : ['类别：', {
		xtype : 'combo',
		id : 'codeNameCombo',
		store : relationStore,// 数据源
		valueField : 'RELATION_CATEGORY',// 下拉框实际值
		displayField : 'RELATION_NAME',// 下拉框显示值
		editable : false,
		triggerAction : 'all',// 每次加载所有值，否则下拉框选择一个值后，再点击就只有一个值
		width :150,
		listeners : {
			select : function(combo, record, index) {
				var RELATION_CATEGORY = Ext.getCmp('codeNameCombo').getValue();
				if(RELATION_CATEGORY != -1){
					store.baseParams = {
						"RELATION_CATEGORY":RELATION_CATEGORY
					};
				}else{
					store.baseParams = {
						"RELATION_CATEGORY":""
					};
				}
				//刷新当前页
				pageTool.doLoad(pageTool.cursor);
			}
		}
	},'-', {
		text : '新增',
		icon : '../../resource/images/btnImages/add.png',
		handler : function() {
			addCodeName();
		}
	},'-', {
		text : '修改',
		icon : '../../resource/images/btnImages/modify.png',
		handler : function() {
			modifyCodeName();
		}
	}]
});

//新增
function addCodeName(){
	
	var url = "addCodeName.jsp"; 

	var editWindow=new Ext.Window({
        id:'editWindow',
        title:"新增",
        width : 400,      
        height : 220, 
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
function modifyCodeName(){
	var modifyData = store.getModifiedRecords();
	
	var jsonDataList = new Array();
	for(var i = 0; i< modifyData.length;i++){
		var jsonData = {
			"CODE_NAME_ID":modifyData[i].get("CODE_NAME_ID"),
			"NAME":modifyData[i].get("NAME")
		};
		jsonDataList.push(jsonData);
    }
	var jsonString = Ext.encode(jsonDataList);
	
	Ext.getBody().mask('正在执行，请稍候...');
	
	Ext.Ajax.request({
		url : 'common!modifyCodeName.action',
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
