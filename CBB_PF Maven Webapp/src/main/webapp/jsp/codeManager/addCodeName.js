var relationStore = new Ext.data.Store({
	url : 'common!getAllCodeCategory.action',
	reader : new Ext.data.JsonReader({
		totalProperty : 'total',
		root : "rows"
	}, [ "RELATION_CATEGORY","RELATION_NAME"]),
	listeners:{
//	  	"load": function(store, records, options){
//	  		var all = new Ext.data.Record({RELATION_CATEGORY:-1,RELATION_NAME:"全部"});
//	  		relationStore.insert(0,all); 
//	  	}
	}
});

var formPanel = new Ext.FormPanel({
	id : 'formPanel', 
	region:'center',
	bodyStyle : 'padding:30px 30px 0 30px',
	autoScroll : true,  
	items : [{
		border:false, 
		items:[{
			layout : 'form', 
			border : false,
			labelWidth:60,
			items:[{
				fieldLabel:'类别', 
				xtype : 'combo',
				id : 'codeNameCombo',
				store : relationStore,// 数据源
				valueField : 'RELATION_CATEGORY',// 下拉框实际值
				displayField : 'RELATION_NAME',// 下拉框显示值
				editable : false,
				triggerAction : 'all',// 每次加载所有值，否则下拉框选择一个值后，再点击就只有一个值
				allowBlank : false,
				anchor : '95%',
				listeners:{
				  	"select": function(combo, record, index){
				  		if(combo.getValue() == relationCategory_country){
				  			Ext.getCmp('SN_CODE').show();
				  		}else{
				  			Ext.getCmp('SN_CODE').hide();
				  		}
				  	}
				}
			},{
				id:'CODE',
				xtype:'textfield', 
				fieldLabel:'代号', 
				allowBlank : false,
				anchor : '95%'
//					,
//				listeners:{
//				  	"blur": function(obj){
//				  		checkCode();
//				  	}
//				}
			},{
				id:'SN_CODE',
				xtype:'textfield', 
				fieldLabel:'苏宁代号', 
				allowBlank : true,
				emptyText:'默认与代号一致',
				anchor : '95%',
				hidden: true
//					,
//				listeners:{
//				  	"blur": function(obj){
//				  		checkCode();
//				  	}
//				}
			},{
				id:'NAME',
				xtype:'textfield', 
				fieldLabel:'名称', 
				allowBlank : false,
				anchor : '95%'
			}] 
		}]
	}],
	buttons : [ {
		text : '确定',
		handler : function() { 
			modifyCodeName();
		}
	}, {
		text : '取消 ',
		handler : function() {
			var win = parent.Ext.getCmp('editWindow');
			if (win) {
				win.close();
			}
		}
	} ]
});   

//新增codename
function modifyCodeName(){
	
	if(formPanel.getForm().isValid()){
		var code = Ext.getCmp('CODE').getValue();
		var sn_code = Ext.getCmp('SN_CODE').getValue();
		if(sn_code == null || sn_code == ''){
			sn_code = code;
		}
		var jsonData = {
			"RELATION_CATEGORY":Ext.getCmp('codeNameCombo').getValue(),
			"RELATION_NAME":Ext.getCmp('codeNameCombo').getRawValue(),
			"CODE":code,
			"SN_CODE":sn_code,
			"NAME ":Ext.getCmp('NAME').getValue()
		};
		var jsonString = Ext.encode(jsonData);
		Ext.getBody().mask('正在执行，请稍候...');
	    Ext.Ajax.request({
			url: 'common!addCodeName.action',
			method : 'POST',
			params: {"jsonString":jsonString},
			success: function(response) {
				Ext.getBody().unmask();
			    var obj = Ext.decode(response.responseText);
			    if(obj.returnResult == 1){
					Ext.Msg.alert("信息", "新增成功！", function(r) {
						// 刷新列表
						var pageTool = parent.Ext.getCmp('pageTool');
						if (pageTool) {
							pageTool.doLoad(pageTool.cursor);
						}
						Ext.Msg.confirm('信息', '继续添加？', function(btn) {
							if (btn == 'yes') {
								//重置窗口
								formPanel.getForm().reset();
								Ext.getCmp('SN_CODE').hide();
							} else {
								// 关闭修改任务信息窗口
								var win = parent.Ext
										.getCmp('editWindow');
								if (win) {
									win.close();
								}
							}
						});
					});
	            }
	            if(obj.returnResult == 0){
	            	Ext.Msg.alert("提示",obj.returnMessage);
	            }
			},
			error:function(response) {
			    Ext.getBody().unmask();
	            Ext.Msg.alert("异常",response.responseText);
			},
			failure:function(response) {
			    Ext.getBody().unmask();
	            Ext.Msg.alert("异常",response.responseText);
			}
		});
	}
}

////唯一性校验
//function checkCode(){
//	
//	if(Ext.getCmp('codeNameCombo').getValue() ==''
//		|| Ext.getCmp('CODE').getValue() ==''){
//		return;
//	}
//	var jsonData = {
//		"RELATION_CATEGORY":Ext.getCmp('codeNameCombo').getValue(),
//		"CODE":Ext.getCmp('CODE').getValue()
//	};
//	var jsonString = Ext.encode(jsonData);
//	
//    Ext.Ajax.request({
//		url: 'common!checkCodeName.action',
//		method : 'POST',
//		params: {"jsonString":jsonString},
//		success: function(response) {
//			Ext.getBody().unmask();
//		    var obj = Ext.decode(response.responseText);
//		    if(obj.returnResult == 1){
//		    	return true;
//            }
//            if(obj.returnResult == 0){
//            	return false;
//            }
//		},
//		error:function(response) {
//		    Ext.getBody().unmask();
//            Ext.Msg.alert("异常",response.responseText);
//            return false;
//		},
//		failure:function(response) {
//		    Ext.getBody().unmask();
//            Ext.Msg.alert("异常",response.responseText);
//            return false;
//		}
//	});
//}

Ext.onReady(function() {
	Ext.BLANK_IMAGE_URL = "../../resource/ext/resources/images/default/s.gif";
	document.onmousedown = function() {
		top.Ext.menu.MenuMgr.hideAll();
	};
	Ext.Ajax.timeout = 900000;
	var win = new Ext.Viewport({
		id : 'win',
		layout : 'border',
		items : [ formPanel ]
	});
	win.show();   
});