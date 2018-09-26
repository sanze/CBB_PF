/*!
 * Ext JS Library 3.4.0
 * Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */
//获取数据列表
var fp = new Ext.FormPanel({
	region:'center',
    fileUpload: true,
    width: 500,
    frame: false,
    autoHeight: true,
    bodyStyle: 'padding: 10px 10px 10px 10px;',
    labelWidth: 50,
    defaults: {
        anchor: '95%',
        allowBlank: false,
        msgTarget: 'side'
    },
    items: [{
        xtype: 'fileuploadfield',
        id: 'uploadFile',
        emptyText: '上传导入数据excel',
        buttonText: '',
        buttonCfg: {
            iconCls: 'upload-icon'
        },
        listeners: {
            'fileselected': function(fb, v){
            	//检测是否pdf格式
            	if (v!=''&&
            			(!/^.*?\.(xlsx)$/.test(v)&&
            			!/^.*?\.(xls)$/.test(v))) {
            		fp.getForm().reset();
            		Ext.Msg.alert("提示","请上传xlsx,xls格式文档！");
            		return;
            	}
            }
        }
    }],
    buttons: [{
        text: '上传',
        handler: function(){
            if(fp.getForm().isValid()){
                fp.getForm().submit({
                    url: 'common!importFile.action',
                    waitMsg: '处理中，请稍后！',
                    success : function(form, action) {
    					var r = /\{(.+?)\}/g;
    					var obj = Ext.decode(action.response.responseText.match(r)[0]);
    					if (obj.returnResult == 1) {
    						Ext.Msg.alert("信息", "导入成功！");
    					}else{
    						Ext.Msg.alert("信息", obj.returnMessage);
    					}
    				},
    				error : function(form, action) {
    					var r = /\{(.+?)\}/g;
    					var obj = Ext.decode(action.response.responseText.match(r)[0]);
    					if (obj.returnResult == 1) {
    						Ext.Msg.alert("信息", "导入成功！");
    					}else{
    						Ext.Msg.alert("信息", obj.returnMessage);
    					}
    				},
    				failure : function(form, action) {
    					var r = /\{(.+?)\}/g;
    					var obj = Ext.decode(action.response.responseText.match(r)[0]);
    					if (obj.returnResult == 1) {
    						Ext.Msg.alert("信息", "导入成功！");
    					}else{
    						Ext.Msg.alert("信息", obj.returnMessage);
    					}
    				}
                });
            }
        }
    },{
        text: '重置',
        handler: function(){
            fp.getForm().reset();
        }
    }]
});

Ext.onReady(function() {
	Ext.BLANK_IMAGE_URL = "../../resource/ext/resources/images/default/s.gif";
	Ext.QuickTips.init();
	Ext.Ajax.timeout = 900000;
	//Ext.Msg = top.Ext.Msg;
	document.onmousedown=function(){top.Ext.menu.MenuMgr.hideAll();};
	
	var win = new Ext.Viewport({
		id : 'win',
		layout : 'border',
		items : [fp]
	});

	win.show();  
});
