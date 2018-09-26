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
				id:'CODE',
				xtype:'textfield', 
				fieldLabel:'ID', 
				allowBlank : false,
				anchor : '95%'
			}
			,{
				id:'NAME',
				xtype:'textfield', 
				fieldLabel:'姓名', 
				allowBlank : false,
//				emptyText:'默认与代号一致',
				anchor : '95%'
			},{
				id:'TEL',
				xtype:'textfield', 
				fieldLabel:'联系方式', 
				allowBlank : false,
				anchor : '95%'
			},{
				id:'PROVINCE',
				xtype:'textfield', 
				fieldLabel:'省', 
				allowBlank : false,
				anchor : '95%'
			},{
				id:'CITY',
				xtype:'textfield', 
				fieldLabel:'市', 
				allowBlank : false,
				anchor : '95%'
			},{
				id:'DISTRICT',
				xtype:'textfield', 
				fieldLabel:'区', 
				allowBlank : false,
				anchor : '95%'
			},{
				id:'SPECIFIC_ADDRESS',
				xtype:'textfield', 
				fieldLabel:'具体地址', 
				allowBlank : false,
				anchor : '95%'
			}
			,GenerateCodeNameComboGrid({
				id: 'COUNTRY',
		    	fieldLabel:'国家',
		    	name: 'COUNTRY',
		        allowBlank : false,
		        anchor : '95%'
	        	},relationCategory_country
		    )
		    ] 
		}]
	}],
	buttons : [ {
		text : '确定',
		handler : function() { 
			addContact();
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
function addContact(){
	
	if(formPanel.getForm().isValid()){
		var jsonData = {
			"CODE":Ext.getCmp('CODE').getValue(),
			"NAME":Ext.getCmp('NAME').getRawValue(),
			"TEL":Ext.getCmp('TEL').getValue(),
			"PROVINCE":Ext.getCmp('PROVINCE').getValue(),
			"CITY":Ext.getCmp('CITY').getValue(),
			"DISTRICT":Ext.getCmp('DISTRICT').getValue(),
			"SPECIFIC_ADDRESS":Ext.getCmp('SPECIFIC_ADDRESS').getValue(),
			"COUNTRY":Ext.getCmp('COUNTRY').getValue()
		};
		var jsonString = Ext.encode(jsonData);
		Ext.getBody().mask('正在执行，请稍候...');
	    Ext.Ajax.request({
			url: 'common!addContact.action',
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
/*var relationCategory_country = 'COUNTRY';
function GenerateCodeNameComboGrid(param,RELATION_CATEGORY,data){
	var dsParam={
		reader : new Ext.data.JsonReader({
			totalProperty : 'total',
			root : "rows"
		}, [ "CODE_NAME_ID","CODE","NAME","SN_CODE"]),
		listeners:{
		  	"exception": function(proxy,type,action,options,response,arg){
		  		Ext.Msg.alert("提示","加载出错"+
					"<BR>Status:"+response.statusText||"unknow");
		  	}
		}
	};
	if(Ext.isEmpty(data)){
		Ext.apply(dsParam,{
			url : 'common!getAllCodeNames.action',
			baseParams:{RELATION_CATEGORY:RELATION_CATEGORY}
		});
	}else{
		Ext.apply(dsParam,{
			data : {'total': data.length, 'rows': data}
		});
	}
	var ds=new Ext.data.Store(dsParam);
	var valueField=param.valueField?param.valueField:"CODE";
	var textField=param.textField?param.textField:"NAME";

	return Ext.apply({
		tbar: [
	       new Ext.ux.form.SearchField({
	    	   store: ds,
	    	   paramName: [{property:textField,anyMatch:true},{property:valueField,anyMatch:true}],
	    	   logical: "or"
	       })
        ],
    	xtype: 'combogrid',
    	columns : [ {
            header : '编号',
            dataIndex : valueField
        }, {
            header : '名称',
            dataIndex : textField
        } ]
	,
        autoLoad: true,
        valueField: valueField,
        textField: textField,
        store : ds,
        editable : false,
        allowBlank : true
    },param);
}*/

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