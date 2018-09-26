/*!
 * Ext JS Library 3.4.0
 * Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */
 
Ext.onReady(function(){
    var win = new Ext.Viewport({
        id:'win',
		layout : 'border',
		items : [{
            id:'btns',
//            title:"导航",
            baseCls:'x-plain',
            region:'center',
            split:true,
            width:150,
            minWidth: 100,
            maxWidth: 250,
            layout:'fit',
            margins: '5 0 5 5',
            items: {
            	layout: {
                    type:'vbox',
                    padding:'5',
                    align:'stretch'
                },
                defaults:{margins:'0 0 5 0'},
                items:[{
                    xtype:'button',
                    text: '商品备案',
                    flex:1,
                    cls:'orange ',
                    handler:function(){
                    	top.addTabPage("../skuManager/main.jsp", "商品备案","",false);
                    }
                },{
                    xtype:'button',
                    text: '电子订单',
                    flex:1,
                    cls:'red ',
                    handler:function(){
                    	top.notSupport();
                    }
                },{
                    xtype:'button',
                    text: '物流运单',
                    flex:1,
                    cls:'blue ',
                },{
                    xtype:'button',
                    text: '出境清单',
                    flex:1,
                    cls:'rosy ',
                    margins:'0'
                }]
            }
       }],
		renderTo : Ext.getBody()
	});
});