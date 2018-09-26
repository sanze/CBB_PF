Ext.namespace("Ext.ux");
Ext.ux.SkuGridPanel = Ext.extend(Ext.grid.EditorGridPanel, {
	stripeRows : true, // 交替行效果
	loadMask : true,
	pageSize : myPageSize,
	readOnly: false,
	mode: 'remote', // local/remote
	storeCfg: {},
	showColumns : [  
//	                 "SKU_ID",
	                 "GUID",
				     "CUSTOM_CODE",
//				     "APP_TYPE",
				     "APP_TIME",
				     "APP_STATUS",
//				     "APP_UID",
				     "PRE_NO",
				     "EBP_CODE",
				     "EBC_CODE",
//				     "EBC_NAME",
//				     "AGENT_CODE",
//				     "CLASSIFY_CODE",
				     "ITEM_NO",
				     "ITEM_NAME",
				     "G_NO",
				     "G_CODE",
				     "G_NAME",
//				     "G_MODEL",
//				     "BAR_CODE",
//				     "BRAND",
//				     "TAX_CODE",
//				     "UNIT",
//				     "UNIT1",
//				     "UNIT2",
//				     "PRICE",
//				     "CURRENCY",
//				     "GIFT_FLAG",
//				     "NOTE",
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
	editSku : function (title,param){
		title="商品信息";//+title;
		if(this.readOnly||this.mode=='local'){
			Ext.apply(param,{readOnly:true});
		}
		var infoPanel=new Ext.ux.SkuFormPanel(param);
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
	delSku : function (record){
		
		if(this.mode!="local"){
			var param={
				SKU_ID:record.get("SKU_ID")
			};
			this.getEl().mask("执行中...");
			Ext.Ajax.request({
				scope: this,
				url : 'common!delSku.action',
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
	initComponent : function () {
		this.sm=new Ext.grid.RowSelectionModel({singleSelect:true});
		var store = new Ext.data.Store(Ext.apply({
			url: 'common!getAllSkus.action',
			reader : new Ext.data.JsonReader({
				totalProperty : 'total',
				root : "rows"
			}, [ "SKU_ID",
			     "GUID",
			     "CUSTOM_CODE",
			     "APP_TYPE",
			     "APP_TIME",
			     "APP_STATUS",
			     "APP_UID",
			     "PRE_NO",
			     "EBP_CODE",
			     "EBC_CODE",
			     "AGENT_CODE",
			     "CLASSIFY_CODE",
			     "ITEM_NO",
			     "ITEM_NAME",
			     "G_NO",
			     "G_CODE",
			     "G_NAME",
			     "G_MODEL",
			     "BAR_CODE",
			     "BRAND",
			     "TAX_CODE",
			     "UNIT",
			     "UNIT1",
			     "UNIT2",
			     "PRICE",
			     "CURRENCY",
			     "GIFT_FLAG",
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
			    id : "SKU_ID",
			    header : "",
			    dataIndex : "SKU_ID"
			},{
			    id : "GUID",
			    header : "系统唯一序号",
			    dataIndex : "GUID"
			},new Ext.ux.grid.CodeNameColumn({
				category: relationCategory_custom,
			    id : "CUSTOM_CODE",
			    header : "申报海关代码",
			    dataIndex : "CUSTOM_CODE"
			}),{
			    id : "APP_TYPE",
			    header : "申报类型",
			    dataIndex : "APP_TYPE",
			    renderer: Renderer.APP_TYPE
			},{
			    id : "APP_TIME",
			    header : "申报时间",
			    dataIndex : "APP_TIME",
			    width : 140
			},{
			    id : "APP_STATUS",
			    header : "业务状态",
			    dataIndex : "APP_STATUS",
			    renderer: Renderer.APP_STATUS
			},new Ext.ux.grid.CodeNameColumn({
				category: relationCategory_appUid,
			    id : "APP_UID",
			    header : "用户",
			    dataIndex : "APP_UID"
			}),{
			    id : "PRE_NO",
			    header : "预录入编号",
			    dataIndex : "PRE_NO"
			},new Ext.ux.grid.CodeNameColumn({
				category: relationCategory_ebp,
			    id : "EBP_CODE",
			    header : "电商平台",
			    dataIndex : "EBP_CODE"
			}),new Ext.ux.grid.CodeNameColumn({
				category: relationCategory_ebc,
			    id : "EBC_CODE",
			    header : "电商企业",
			    dataIndex : "EBC_CODE"
			}),new Ext.ux.grid.CodeNameColumn({
				category: relationCategory_agent,
			    id : "AGENT_CODE",
			    header : "申报企业",
			    dataIndex : "AGENT_CODE"
			}),new Ext.ux.grid.CodeNameColumn({
				category: relationCategory_classify,
			    id : "CLASSIFY_CODE",
			    header : "预归类企业",
			    dataIndex : "CLASSIFY_CODE"
			}),{
			    id : "ITEM_NO",
			    header : "企业商品货号",
			    dataIndex : "ITEM_NO"
			},{
			    id : "ITEM_NAME",
			    header : "商品上架品名",
			    dataIndex : "ITEM_NAME"
			},{
			    id : "G_NO",
			    header : "海关商品备案编号",
			    dataIndex : "G_NO"
			},{
			    id : "G_CODE",
			    header : "海关商品编码",
			    dataIndex : "G_CODE"
			},{
			    id : "G_NAME",
			    header : "商品名称",
			    dataIndex : "G_NAME"
			},{
			    id : "G_MODEL",
			    header : "规格型号",
			    dataIndex : "G_MODEL"
			},{
			    id : "BAR_CODE",
			    header : "条形码",
			    dataIndex : "BAR_CODE"
			},{
			    id : "BRAND",
			    header : "品牌",
			    dataIndex : "BRAND"
			},new Ext.ux.grid.CodeNameColumn({
				category: relationCategory_taxCode,
			    id : "TAX_CODE",
			    header : "海关物品税号",
			    dataIndex : "TAX_CODE"
			}),new Ext.ux.grid.CodeNameColumn({
				category: relationCategory_unit,
			    id : "UNIT",
			    header : "计量单位",
			    dataIndex : "UNIT"
			}),new Ext.ux.grid.CodeNameColumn({
				category: relationCategory_unit,
			    id : "UNIT1",
			    header : "法定计量单位",
			    dataIndex : "UNIT1"
			}),new Ext.ux.grid.CodeNameColumn({
				category: relationCategory_unit,
			    id : "UNIT2",
			    header : "第二计量单位",
			    dataIndex : "UNIT2"
			}),{
			    id : "PRICE",
			    header : "备案价格",
			    dataIndex : "PRICE"
			},new Ext.ux.grid.CodeNameColumn({
				category: relationCategory_currency,
			    id : "CURRENCY",
			    header : "币制",
			    dataIndex : "CURRENCY"
			}),{
			    id : "GIFT_FLAG",
			    header : "是否赠品",
			    dataIndex : "GIFT_FLAG",
			    renderer: Renderer.GIFT_FLAG
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
	   					this.editSku('录入',{editType:editType});
	   				}.createDelegate(this)
	   			},{
	   		        text: '编辑',
	   		        scale: 'medium',
	   		        icon:'../../resource/images/btnImages/modify.png',
	   		        handler: function(b,e){
	   		        	var editType='mod';
	   		        	var data=this.checkSelect(true);
	   		        	if(data)
	   		        		this.editSku('编辑',{editType:editType,record:data});
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
	   		        		this.delSku(data);
	   		        }.createDelegate(this)
	   			},{
	   		        text: '复制',
	   		        scale: 'medium',
	   		        icon:'../../resource/images/btnImages/page_copy.png',
	   		        handler: function(b,e){
	   		        	var editType='add';
	   		        	var data=this.checkSelect(true);
	   		        	if(data)
	   		        		this.editSku('录入',{editType:editType,record:data});
	   				}.createDelegate(this)
	   			}]
	   		});
			if(this.readOnly){
				tbar.remove(3);
				tbar.remove(2);
				tbar.get(1).setText("商品信息");
				tbar.remove(0);
			}else if(this.mode=="local"){
				tbar.remove(3);
				tbar.get(1).setText("商品信息");
				tbar.remove(0);
			}
            this.tbar = tbar;
        }

		this.on('rowclick', function(grid, rowIndex, e) {
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
		            delButton.setDisabled(isSkuReadOnly(record));
		        }
			}
	    },this);
		
		Ext.ux.SkuGridPanel.superclass.initComponent.call(this);
	}
});

Ext.ux.SkuFormPanel = Ext.extend(Ext.form.FormPanel, {
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
			return isSkuReadOnly(this.record);
		}
		return false;
	},
	submitForm : function(opType){
		var fp=this;
		var form = fp.getForm();
		if (form.isValid()) {
			var param={
		        editType: this.editType,
		        APP_STATUS: opType
		    };
			//添加displayfield参数或未显示参数
			if(this.editType=="mod"){
				Ext.apply(param,{
			        GUID: this.record.get('GUID'),
	//		        APP_TIME: this.record.get('APP_TIME'),
			        SKU_ID: this.record.get('SKU_ID')
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
			    url: 'common!setSku.action',
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
//		  {xtype:'hidden',name:"SKU_ID"},
		  {
			  xtype:'displayfield',
			  name:"GUID",
			  fieldLabel:'系统唯一序号',
			  hidden: this.editType=="add"
		  },GenerateCodeNameComboGrid({
				allowBlank : false,
				fieldLabel:'申报海关代码',
		        name: 'CUSTOM_CODE'/*,
		        textField: 'CODE',
		        columns : [ {
		            header : '编号',
		            dataIndex : 'CODE'
		        }, {
		            header : '名称',
		            dataIndex : 'NAME',
		            hidden: true
		        } ]*/
	        },relationCategory_custom
	    ),{
			xtype:'displayfield',
	        fieldLabel:'申报时间',
	        name: 'APP_TIME',
	        hidden: !this.submited
	    },GenerateCodeNameComboGrid({
	    		allowBlank : false,
		    	fieldLabel:'用户名称',
		    	name: 'APP_UID'
	        },relationCategory_appUid
	    ),{
			xtype:'displayfield',
	        fieldLabel:'预录入编号',
	        name: 'PRE_NO',
	        hidden: !this.submited
	    },GenerateCodeNameComboGrid({
	    		allowBlank : false,
		    	fieldLabel:'电商平台名称',
		    	name: 'EBP_CODE'
	        },relationCategory_ebp
	    ),GenerateCodeNameComboGrid({
	    		allowBlank : false,
		    	fieldLabel:'电商企业名称',
		    	name: 'EBC_CODE'
	        },relationCategory_ebc
	    ),GenerateCodeNameComboGrid({
	    		allowBlank : false,
		    	fieldLabel:'申报企业名称',
		    	name: 'AGENT_CODE'
	        },relationCategory_agent
	    ),GenerateCodeNameComboGrid({
		    	fieldLabel:'预归类企业名称',
		    	name: 'CLASSIFY_CODE',
		        allowBlank : true
	        },relationCategory_classify
	    ),{
	        fieldLabel:'企业商品货号',
	        name: 'ITEM_NO',
	        maxLength: 20
	    },{
	        fieldLabel:'商品上架品名',
	        name: 'ITEM_NAME'
	    },{
			xtype:'displayfield',
	        fieldLabel:'海关商品备案编号',
	        name: 'PRE_NO',
	        hidden: !this.submited
	    },{
	        fieldLabel:'海关商品编码',
	        name: 'G_CODE',
	        maxLength: 10
	    },{
	        fieldLabel:'商品名称',
	        name: 'G_NAME'
	    },{
	        fieldLabel:'规格型号',
	        name: 'G_MODEL'
	    },{
	        fieldLabel:'条形码',
	        name: 'BAR_CODE',
	        maxLength: 13,
	        allowBlank: true,
	        emptyText: "无"
	    },{
	        fieldLabel:'品牌',
	        name: 'BRAND',
	        maxLength: 100
	    },GenerateCodeNameComboGrid({
	    	fieldLabel:'海关物品税号',
	    	name: 'TAX_CODE',
	        allowBlank : true
        	},relationCategory_taxCode
	    ),GenerateCodeNameComboGrid({
	    	fieldLabel:'计量单位',
	    	name: 'UNIT',
	        allowBlank : false
        	},relationCategory_unit
	    ),GenerateCodeNameComboGrid({
	    	fieldLabel:'法定计量单位',
	    	name: 'UNIT1',
	        allowBlank : false
        	},relationCategory_unit
	    ),GenerateCodeNameComboGrid({
	    	fieldLabel:'第二计量单位',
	    	name: 'UNIT2',
	        allowBlank : true
        	},relationCategory_unit
	    ),{
	    	xtype:'numberfield',
	    	allowNegative: false,
	        fieldLabel:'备案价格',
	        name: 'PRICE'
	    },GenerateCodeNameComboGrid({
	    	fieldLabel:'币制',
	    	name: 'CURRENCY',
	        allowBlank : false
        	},relationCategory_currency
	    ),{
	    	xtype: 'checkbox',
	        fieldLabel:'是否赠品',
	        inputValue: 1,//用于表单提交的值，否则会提交"on"
	        uncheckedValue: 0,
	        name: 'GIFT_FLAG'
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
			    }),new Ext.Button({
			    	disabled: true,
			    	formBind: true,
			    	text : '提交', 
			    	scale: 'large',
			    	icon : '../../resource/images/btnImages/accept.png',
					handler : function(b,e){
						Ext.apply(this.baseParams,{opType:'submit'});
						this.submitForm(2);
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
		Ext.ux.SkuFormPanel.superclass.initComponent.call(this);
		this.renderRecord();
	}
});