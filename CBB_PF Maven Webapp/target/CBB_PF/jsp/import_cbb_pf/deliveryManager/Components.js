Ext.Ajax.timeout=360000000; 
Ext.namespace("Ext.ux");
Ext.ux.DeliveryGridPanel = Ext.extend(Ext.grid.EditorGridPanel, {
	stripeRows : true, // 交替行效果
	loadMask : true,
	pageSize : myPageSize,
	readOnly: false,
	mode: 'remote', // local/remote
	storeCfg: {},
	showColumns : [  
//	                 "DELIVERY_ID",
	                 "GUID",
				     "DELIVERY_NO",
				     "TRAF_MODE",
				     "TRAF_NO",
				     "VOYAGE_NO",
				     "BILL_NO",
				     "NOTE",
				     "RETURN_STATUS",
				     "RETURN_TIME",
				     "RETURN_INFO"//,
//				     "CREAT_TIME",
//				     "UPDATE_TIME"
	],
	checkSelect : function(single,preventMask){
		var records=this.getSelectionModel().getSelections();
		var data=false;
		if(Ext.isEmpty(records)||records.length<1){
			if(preventMask!=true)Ext.Msg.alert("提示", '请选择一条记录');
		}else if(single){
			if(records.length>1){
				if(preventMask!=true)Ext.Msg.alert("提示", '只能选择一条记录');
			}else{
				data=records[0];
			}
		}else{
			data=records;
		}
		return data;
	},
	editDelivery : function (title,param){
		title="入库单信息";//+title;
		if(this.readOnly||this.mode=='local'){
			Ext.apply(param,{readOnly:true});
		}
		var infoPanel=new Ext.ux.DeliveryFormPanel(param);
		var tabPage=top.addTabPage(infoPanel,title,"",false);
		infoPanel.on("close",function(){
			top.closeTab(tabPage);
			if(!this.isDestroyed){
				if(!this.readOnly&&this.mode!=='local'){
					this.reload();
				}
			}
		},this);
	},
	reload: function(){
		var pageTool = this.pageTool;
		if (pageTool) {
			pageTool.doLoad(pageTool.cursor);
		}
	},
	delConfirm : function (record){
		Ext.MessageBox.confirm('提示', '确定删除？', function(button, text) {
			if (button == 'yes') {
				this.del(record);
			}
		}.createDelegate(this));
	},
	del : function (record){
		if(this.mode!="local"){
			var param={
				GUID:record.get("GUID"),
				DELIVERY_ID:record.get("DELIVERY_ID")
			};
			this.getEl().mask("执行中...");
			Ext.Ajax.request({
				scope: this,
				url : 'import-common!delDelivery.action',
				method : "POST",
				params : param,
				success : function(response) {
					this.getEl().unmask();
					var obj = Ext.decode(response.responseText);
					if (obj.returnResult == 0) {
						Ext.Msg.alert("信息", obj.returnMessage, this.reload, this);
					}else{
						this.reload();
					}
				},
				error : function(response) {
					this.getEl().unmask();
					Ext.Msg.alert("异常", response.responseText);
				},
				failure : function(response) {
					this.getEl().unmask();
					Ext.Msg.alert("异常", response.responseText);
				}
			});
		}else{
			this.getStore().remove(record);
			this.getView().refresh();
		}
	},
	
	batchSubmit : function (record){
		var jsonDataList = new Array();
		for(var i = 0; i< record.length;i++){
			jsonDataList.push(record[i].get('GUID'));
	    }
		var param={
			guidList:jsonDataList
		};
		this.getEl().mask("执行中...");
		Ext.Ajax.request({
			scope: this,
			url : 'import-common!batchSubmit_delivery.action',
			method : "POST",
			params : param,
			success : function(response) {
				this.getEl().unmask();
				var obj = Ext.decode(response.responseText);
				if (obj.returnResult == 0) {
					Ext.Msg.alert("信息", obj.returnMessage, this.reload, this);
				}else{
					this.reload();
				}
			},
			error : function(response) {
				this.getEl().unmask();
				Ext.Msg.alert("异常", response.responseText);
			},
			failure : function(response) {
				this.getEl().unmask();
				Ext.Msg.alert("异常", response.responseText);
			}
		});
	},
	initComponent : function () {
		this.sm=new Ext.grid.RowSelectionModel({singleSelect:false});
		var store = new Ext.data.Store(Ext.apply({
			url: 'import-common!getAllDeliveries.action',
			reader : new Ext.data.JsonReader({
				totalProperty : 'total',
				root : "rows"
			}, [ 
			     "DELIVERY_ID",
                 "GUID",
			     "DELIVERY_NO",
			     "TRAF_MODE",
			     "TRAF_NO",
			     "VOYAGE_NO",
			     "BILL_NO",
			     "NOTE",
			     "RETURN_STATUS",
			     "RETURN_TIME",
			     "RETURN_INFO",
			     "CREAT_TIME",
			     "UPDATE_TIME"]),
			listeners:{
			  	"exception": function(proxy,type,action,options,response,arg){
			  		Ext.Msg.alert("提示","加载出错"+
						"<BR>Status:"+response.statusText||"unknow");
			  	}
			}
		},this.storeCfg));
//		var selModel = new Ext.grid.CheckboxSelectionModel({hidden:false,singleSelect:true,header:""});
		if(Ext.isEmpty(this.columns)){
			this.columns=[ new Ext.grid.RowNumberer({
				width : 26
			}), /*selModel, */{
			    id : "DELIVERY_ID",
			    header : "",
			    dataIndex : "DELIVERY_ID"
			},{
			    id : "GUID",
			    header : "系统唯一序号",
			    dataIndex : "GUID"
			},{
			    id : "DELIVERY_NO",
			    header : "入库单号",
			    dataIndex : "DELIVERY_NO"
			},new Ext.ux.grid.CodeNameColumn({
				category: relationCategory_trafMode,
			    id : "TRAF_MODE",
			    header : "运输方式",
			    dataIndex : "TRAF_MODE"
			}),{
			    id : "TRAF_NO",
			    header : "运输号",
			    dataIndex : "TRAF_NO"
			},{
			    id : "VOYAGE_NO",
			    header : "航班航次号",
			    dataIndex : "VOYAGE_NO"
			},{
			    id : "BILL_NO",
			    header : "提运单号",
			    dataIndex : "BILL_NO"
			},{
			    id : "NOTE",
			    header : "备注",
			    dataIndex : "NOTE"
			},{
			    id : "RETURN_STATUS",
			    header : "回执状态",
			    dataIndex : "RETURN_STATUS",
			    renderer: Renderer.RETURN_STATUS
			},{
			    id : "RETURN_TIME",
			    header : "回执时间",
			    dataIndex : "RETURN_TIME",
			    width : 140
			},{
			    id : "RETURN_INFO",
			    header : "回执信息",
			    dataIndex : "RETURN_INFO"
			},{
			    id : "CREAT_TIME",
			    header : "",
			    dataIndex : "CREAT_TIME"
			},{
			    id : "UPDATE_TIME",
			    header : "",
			    dataIndex : "UPDATE_TIME"
			}];
		}
		
		Ext.each(this.columns,function(item,index,allItems){
			if(item['dataIndex']){
				var idx=findIndexByProperty(this.showColumns,null,item['dataIndex']);
				item['hidden']=idx<0;
				item['hideable']=!item['hidden'];
			}
			if(Ext.isEmpty(item['width'])&&!Ext.isEmpty(item['header'])){
				item['width']=item['header'].length*20;
			}
			function extendRenderer(val,metadata,record,row,col,store,renderer){
				var data=val;
				if(!Ext.isEmpty(renderer)&&Ext.isFunction(renderer)){
					data=renderer(val,metadata,record,row,col,store);
				}
				if(!Ext.isEmpty(data))
					metadata.attr = 'ext:qtip="' +data+'"';   //关键  
				return data;
			}
			item['renderer']=extendRenderer.createDelegate(undefined,[item['renderer']],6);
		},this);
		var cm = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
				editable : !this.readOnly
			},
			columns : this.columns
		});
		
        if(Ext.isEmpty(this.store)&&Ext.isEmpty(this.ds)){
            this.store = store;
        }
        if(Ext.isEmpty(this.cm)&&Ext.isEmpty(this.colModel)){
            this.colModel = cm;
        }
        /*if(Ext.isEmpty(this.selModel)&&Ext.isEmpty(this.sm)){
            this.selModel = selModel;
        }*/
		
		if(Ext.isEmpty(this.bbar)&&this.mode!=="local"){
			var bbar = new Ext.PagingToolbar({
				pageSize : this.pageSize,// 每页显示的记录值
				store : this.store,
				displayInfo : true,
				displayMsg : '当前 {0} - {1} ，总数 {2}',
				emptyMsg : "没有记录"
			});
			bbar.doRefresh();
			this.pageTool=bbar;
            this.bbar = bbar;
        }
		
		if(Ext.isEmpty(this.tbar)&&this.tbar!=false){
			
			var tbar = new Ext.Toolbar({items:[/*{
	   		        text: '查询',
	   		        icon:'../../resource/images/btnImages/search.png',
	   		        handler: function(){
	   	
	   				}.createDelegate(this)
	   			},'-',*/{
	   				text:'新增',
	   				scale: 'medium',
	   				icon:'../../resource/images/btnImages/add.png',
	   				handler: function(b,e){
	   					var editType='add';
	   					this.editDelivery('录入',{editType:editType});
	   				}.createDelegate(this)
	   			},{
	   		        text: '编辑',
	   		        scale: 'medium',
	   		        icon:'../../resource/images/btnImages/modify.png',
	   		        handler: function(b,e){
	   		        	var editType='mod';
	   		        	var data=this.checkSelect(true);
	   		        	if(data)
	   		        		this.editDelivery('编辑',{editType:editType,record:data});
	   				}.createDelegate(this)
	   			},{
	   		        text: '删除',
	   		        scale: 'medium',
	   		        name: 'delete',
	   		        disabled: true,
	   		        icon:'../../resource/images/btnImages/delete.png',
	   		        handler: function(){
	   		        	var data=this.checkSelect(true);
	   		        	if(data)
	   		        		this.delConfirm(data);
	   		        }.createDelegate(this)
	   			}]
	   		});
			if(this.readOnly){
				//移除按钮
				tbar.remove(2);
				tbar.get(1).setText("入库单信息");
				tbar.remove(0);
			}else if(this.mode=="local"){
				//移除按钮
				tbar.remove(2);
				tbar.get(1).setText("入库单信息");
				tbar.remove(0);
			}
            this.tbar = tbar;
        }
//		this.on('render', function() {
//			//添加第二列查询控件
//			//搜索字段包括：进出口业务类型，电商企业，商品货号，业务状态，回执状态，备注
//
//			var onebar_delivery = new Ext.Toolbar({
//				id : 'onebar_delivery',
////				enableOverflow:true,
//				items : [{
//			        xtype: 'tbtext',
//			        text: '进出口业务类型:',
//			        width:120
//			    },{
//			        xtype: 'combo',
//			        fieldLabel: '',
//			        id:"BIZ_TYPE_SKU_SEARCH",
//			        store: new Ext.data.ArrayStore({
//			            fields: ['text','value'],
//			            data: ComboBoxValue_sku.BIZ_TYPE
//			        }),
//			        displayField:'text',
//			        valueField: 'value',
//			        mode: 'local',
////			        forceSelection: true,
//			        triggerAction: 'all',
////			        selectOnFocus:true,
//			        width:100,
//			        anchor:'50%'
//			    },{
//			        xtype: 'tbtext',
//			        text: '电商企业:',
//			        width:95
//			    },{
//			        xtype: 'combo',
//			        fieldLabel: '',
//			        id:"EBC_CODE_SKU_SEARCH",
//			        store: new Ext.data.Store({
//			        	url : 'common!getCodeCategory.action',
//			        	baseParams : {
//			        		"relationCategory" : "EBC_CODE"
//			        	},
//			        	reader : new Ext.data.JsonReader({
//			        		totalProperty : 'total',
//			        		root : "rows"
//			        	}, [ "NAME", "CODE" ])
//			        }),
//			        displayField:'NAME',
//			        valueField: 'CODE',
////			        mode: 'local',
////			        forceSelection: true,
//			        triggerAction: 'all',
////			        selectOnFocus:true,
//			        width:120,
//			        anchor:'50%'
//			    },{
//			        xtype: 'tbtext',
//			        text: '商品货号:',
//			        width:95
//			    },{
//			        xtype: 'textfield',
//			        fieldLabel: '',
//			        id:"ITEM_NO_SKU_SEARCH",
//			        emptyText:"",
//			        width:100,
//			        anchor:'50%'
//			    },{
//			        xtype: 'tbtext',
//			        text: '业务状态:',
//			        width:95
//			    },{
//			        xtype: 'combo',
//			        fieldLabel: '',
//			        id:"APP_STATUS_SKU_SEARCH",
//			        store: new Ext.data.ArrayStore({
//			            fields: ['text','value'],
//			            data: ComboBoxValue_sku.APP_STATUS
//			        }),
//			        displayField:'text',
//			        valueField: 'value',
//			        mode: 'local',
////			        forceSelection: true,
//			        triggerAction: 'all',
////			        selectOnFocus:true,
//			        width:100,
//			        anchor:'50%'
//			    },{
//			        xtype: 'tbtext',
//			        text: '回执状态:',
//			        width:95
//			    },{
//			        xtype: 'combo',
//			        fieldLabel: '',
//			        id:"RETURN_STATUS_SKU_SEARCH",
//			        store: new Ext.data.ArrayStore({
//			            fields: ['text','value'],
//			            data: ComboBoxValue_sku.RETURN_STATUS
//			        }),
//			        displayField:'text',
//			        valueField: 'value',
//			        mode: 'local',
////			        forceSelection: true,
//			        triggerAction: 'all',
////			        selectOnFocus:true,
//			        width:100,
//			        anchor:'50%'
//			    },{
//			        xtype: 'tbtext',
//			        text: '备注:',
//			        width:95
//			    },{
//			        xtype: 'textfield',
//			        fieldLabel: '',
//			        id:"NOTE_SKU_SEARCH",
//			        emptyText:"",
//			        width:100,
//			        anchor:'50%'
//			    }]
//			});
//			if(this.readOnly){
//				
//			}else if(this.mode=="local"){
//				
//			}else{
//				onebar_delivery.render(this.tbar); // add one tbar
//			}
//	    },this);
//		this.on('destroy', function() {
//			if(Ext.getCmp('onebar_delivery')){
//				Ext.destroy(Ext.getCmp('onebar_delivery'));// 这一句不加可能会有麻烦滴
//			}
//	    },this);
		this.on('rowclick', function(grid, rowIndex, e) {
//			var getReceiptButton=this.topToolbar.find("name",'getReceipt')[0];
			var delButton=this.topToolbar.find("name",'delete')[0];
			if(delButton){
		        var selections = grid.getSelectionModel().getSelections();
		        if (selections.length == 0) return;
		 
		        if(this.mode=='local'){
		        	delButton.setDisabled(false);
		        	return;
		        }
		        for ( var i = 0; i < selections.length; i++) {
		            var record = selections[i];
		            delButton.setDisabled(isDeliveryReadOnly(record));
		        }
			}
			
//			button=this.topToolbar.find("name",'batchSubmit')[0];
//			if(button){
//				var enableFlag = true;
//				for ( var i = 0; i < selections.length; i++) {
//		            var record = selections[i];
//		            if(record.get("APP_STATUS") != 1){
//		            	enableFlag = false;
//		            	break;
//		            }
//		        }
//				button.setDisabled(!enableFlag);
//			}
	    },this);
		
		Ext.ux.DeliveryGridPanel.superclass.initComponent.call(this);
	}
});

Ext.ux.DeliveryFormPanel = Ext.extend(Ext.form.FormPanel, {
	record : undefined,
	editType : 'add',
	readOnly : undefined,
	submited : false,
	autoScroll : true,
	defaults : {
		xtype: 'textfield',
        allowBlank : false,
        anchor : "100%",
        maxLength: 250
	},
	bodyStyle:'padding:10px 10px 10px 10px',
	labelWidth: 130,
	monitorValid: true,
	setRecord : function(record){
		this.record = record;
		this.renderRecord();
	},
	renderRecord : function(){
		if(this.record){
			this.getForm().loadRecord(this.record);
		}
	},
	isReadOnly: function(){
		if(this.editType=="add") return false;
		if(this.record){
			return isDeliveryReadOnly(this.record);
		}
		return false;
	},
	submitForm : function(opType){
		var fp=this;
		var form = fp.getForm();
		if (form.isValid()) {
			var param={
		        editType: this.editType
		        //APP_STATUS: opType
		    };
			//添加displayfield参数或未显示参数
			if(this.editType=="mod"){
				Ext.apply(param,{
			        GUID: this.record.get('GUID'),
	//		        APP_TIME: this.record.get('APP_TIME'),
			        DELIVERY_ID: this.record.get('DELIVERY_ID')
				});
			}
			form.items.each(function(f){
				if(f.submitValue&&
						f.el.dom.type=="checkbox"&&
						f.getName()&&
						!f.getValue()&&
						f.uncheckedValue!=undefined){
					param[f.getName()]=f.uncheckedValue;
				}
			});
			form.submit({
				scope:fp,
			    clientValidation: true,
			    waitTitle: '正在执行',
			    waitMsg: '请稍后……',
			    url: 'import-common!setDelivery.action',
			    params: param,
			    success: function(form, action) {
			    	if(!Ext.isEmpty(action.result.msg)){
			    		Ext.Msg.alert('成功', action.result.msg, 
			    		function(){
			    			this.fireEvent('close',this);
			    		}, this);
			    	}else{
			    		this.fireEvent('close',this);
			    	}
			    },
			    failure: function(form, action) {
			        switch (action.failureType) {
			            case Ext.form.Action.CLIENT_INVALID:
			                Ext.Msg.alert('失败', '表单项存在非法值');
			                break;
			            case Ext.form.Action.CONNECT_FAILURE:
			                Ext.Msg.alert('失败', '与服务器通信异常');
			                break;
			            case Ext.form.Action.SERVER_INVALID:
			               Ext.Msg.alert('失败', action.result.msg);
			       }
			    }
			});
		}
	},
	initComponent : function () {
		if(this.readOnly==undefined)
			this.readOnly=this.isReadOnly();
		this.submited=!(Ext.isEmpty(this.record)||this.record.get("APP_STATUS")==1);
		this.items=[
//		  {xtype:'hidden',name:"DELIVERY_ID"},
		  {
			  xtype:'displayfield',
			  name:"GUID",
			  fieldLabel:'系统唯一序号',
			  hidden: this.editType=="add"
		  },{
		        fieldLabel:'入库单号',
		        name: 'DELIVERY_NO',
		        readOnly:this.editType!="add"
		  },GenerateCodeNameComboGrid({
				allowBlank : false,
				fieldLabel:'运输方式',
		        name: 'TRAF_MODE'
	        },relationCategory_trafMode
	    ),{
	        fieldLabel:'运输工具名称',
	        name: 'TRAF_NO',
	        maxLength: 100
	    },{
	        fieldLabel:'航班航次号',
	        allowBlank : false,
	        name: 'VOYAGE_NO'
	    },{
	        fieldLabel:'提运单号',
	        name: 'BILL_NO',
	        allowBlank : false,
	        maxLength: 37
	    },{
	        xtype: 'textarea',
	        maxLength: 1000,
	        fieldLabel:'备注',
	        name: 'NOTE'
	    }];
		
		var columns=[];
		var hiddens=[];
		Ext.apply(this.defaults,{readOnly:this.readOnly});
		Ext.each(this.items,function(item,index,allItems){
			if(!item.hidden){
				if(columns.length==0||columns[columns.length-1].items.length==2){
					columns.push({
						layout:'column',
						border:false,
						defaults:{columnWidth: .4},
						items:[]
					});
				}
				var bodyStyle;
				if(columns[columns.length-1].items.length==0)
					bodyStyle='padding:0px 10px 0px 0px';
				else
					bodyStyle='padding:0px 0px 0px 10px';
				columns[columns.length-1].items.push({
					layout:'form',
					border:false,
					bodyStyle:bodyStyle,
					defaults: this.defaults,
					items:[item]
				});
			}
		},this);
		Ext.each(this.items,function(item,index,allItems){
			if(item.hidden){
				columns.push(item);
			}
		},this);
		this.defaults=undefined;
		this.items=columns;
		
		var validateButton=function (){
			this.buttons=[];
			if(!this.readOnly){
				this.buttons.push(new Ext.Button({
					disabled: true,
					formBind: true,
			    	text : '保存', 
			    	scale: 'large',
			    	icon : '../../resource/images/btnImages/save.png',
					handler : function(b,e){
						Ext.apply(this.baseParams,{opType:'save'});
						this.submitForm(1);
			    	}.createDelegate(this)
			    }));
			}
			this.buttons.push(new Ext.Button({
		    	text : '返回', 
		    	scale: 'medium',
		    	icon : '../../resource/images/btnImages/cancel.png',
				handler : function(b,e){
					this.fireEvent('close',this);
		    	}.createDelegate(this)
		    }));
		}.createDelegate(this);

		validateButton();
		
		this.addEvents('close');
		Ext.ux.DeliveryFormPanel.superclass.initComponent.call(this);
		this.renderRecord();
	}
});

//设置税号，行邮税率
function setTaxCode_Rate(bizType,needClearValue){
	
	//商品编码字段，”10位海关商品编码，出口商品备案必填，保税进口业务必填“。一般进口不需要必填，去掉限制就行
	if(bizType == 1){
		Ext.getCmp("G_CODE").allowBlank = false;
		Ext.getCmp("G_CODE").clearInvalid();
	}else{
		Ext.getCmp("G_CODE").allowBlank = false;
	}
	
/*	//进口 海关物品税号和行邮税率都直接填 必填
	if(bizType == 1 || bizType == 3){
		Ext.getCmp("TAX_CODE").setDisabled(false);
		Ext.getCmp("TAX_CODE").allowBlank = false;
		
		Ext.getCmp("TAX_RATE").setDisabled(false);
		Ext.getCmp("TAX_RATE").allowBlank = false;
	}
	//出口 海关物品税号和行邮税率为空
	else if(bizType == 2 || bizType == 4){
		if(needClearValue){
			Ext.getCmp("TAX_CODE").setValue("");
		}
		Ext.getCmp("TAX_CODE").setDisabled(true);
		Ext.getCmp("TAX_CODE").allowBlank = true;
		Ext.getCmp("TAX_CODE").clearInvalid();
		
		if(needClearValue){
			Ext.getCmp("TAX_RATE").setValue("");
		}
		Ext.getCmp("TAX_RATE").setDisabled(true);
		Ext.getCmp("TAX_RATE").allowBlank = true;
		Ext.getCmp("TAX_RATE").clearInvalid();
	}*/
}

//查询数据
function delivery_search(store){

	var param = {"limit":myPageSize,"start":0,"fuzzy":true};
	
	if(Ext.getCmp('BIZ_TYPE_SKU_SEARCH').getValue()){
		Ext.apply(param,{"BIZ_TYPE":Ext.getCmp('BIZ_TYPE_SKU_SEARCH').getValue()});
	}
	if(Ext.getCmp('EBC_CODE_SKU_SEARCH').getValue()){
		Ext.apply(param,{"EBC_CODE":Ext.getCmp('EBC_CODE_SKU_SEARCH').getValue()});
	}
	if(Ext.getCmp('ITEM_NO_SKU_SEARCH').getValue()){
		Ext.apply(param,{"ITEM_NO":Ext.getCmp('ITEM_NO_SKU_SEARCH').getValue()});
	}
	if(Ext.getCmp('APP_STATUS_SKU_SEARCH').getValue()){
		Ext.apply(param,{"APP_STATUS":Ext.getCmp('APP_STATUS_SKU_SEARCH').getValue()});
	}
	if(Ext.getCmp('RETURN_STATUS_SKU_SEARCH').getValue()){
		Ext.apply(param,{"RETURN_STATUS":Ext.getCmp('RETURN_STATUS_SKU_SEARCH').getValue()});
	}
	if(Ext.getCmp('NOTE_SKU_SEARCH').getValue()){
		Ext.apply(param,{"NOTE":Ext.getCmp('NOTE_SKU_SEARCH').getValue()});
	}
	
//	for(var data in param){
//	if(param[data]){
//		alert(data+"_"+param[data]);
//	}
//}
	
	store.baseParams = param;

	store.load();
}