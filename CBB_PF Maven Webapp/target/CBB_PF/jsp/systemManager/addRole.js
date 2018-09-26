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
				id:'NAME',
				xtype:'textfield', 
				fieldLabel:'角色名称', 
				allowBlank : false,
//				emptyText:'默认与代号一致',
				anchor : '95%'
			},{
				id:'NOTE',
				xtype:'textfield', 
				fieldLabel:'备注', 
				allowBlank : false,
				anchor : '95%'
			}] 
		}]
	}],
	buttons : [ {
		text : '确定',
		handler : function() { 
			addRole();
		}
	}, {
		text : '取消',
		handler : function() {
			var win = parent.Ext.getCmp('editWindow');
			if (win) {
				win.close();
			}
		}
	} ]
});   

//新增codename
function addRole(){
	
	if(formPanel.getForm().isValid()){
		var jsonData = {
			"NAME":Ext.getCmp('NAME').getRawValue(),
			"NOTE":Ext.getCmp('NOTE').getValue()
		};
		var jsonString = Ext.encode(jsonData);
		Ext.getBody().mask('正在执行，请稍候...');
	    Ext.Ajax.request({
			url: 'user-management!addRole.action',
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