Ext.namespace("Ext.ux");
Ext.ux.InventoryGridPanel = Ext.extend(Ext.grid.GridPanel, {
	stripeRows : true, // 交替行效果
	loadMask : true,
	pageSize : myPageSize,
	readOnly : false,
	storeCfg: {},
	showColumns : [
//		"INVENTORY_ID",
		"GUID",
//		"CUSTOM_CODE",
//		"APP_TYPE",
		"APP_TIME",
		"APP_STATUS",
//		"APP_UID",
		"COP_NO",
//		"PRE_NO",
		"LOGISTICS_NO",
//		"INVT_NO",
		"PORT_CODE",
		"IE_DATE",
		"OWNER_CODE",
//		"TRADE_MODE",
//		"LOCT_NO",
//		"LICENSE_NO",
		"COUNTRY",
		"DESTINATION_PORT",
//		"FREIGHT",
//		"FREIGHT_CURR",
//		"FREIGHT_MARK",
//		"INSURE_FEE",
//		"INSURE_FEE_CURR",
//		"INSURE_FEE_MARK",
//		"WRAP_TYPE",
//		"PACK_NO",
//		"WEIGHT",
//		"NET_WEIGHT",
		"NOTE",
		"RETURN_STATUS",
		"RETURN_TIME",
		"RETURN_INFO",
//		"CREAT_TIME",
//		"UPDATE_TIME",
//		"GOODSList",
		//order
		"ORDER_NO",
		"EBP_CODE",
//		"EBC_CODE",
//		"AGENT_CODE",
		//logistics
		"LOGISTICS_CODE",
		"IE_FLAG",
		"TRAF_MODE",
//		"SHIP_NAME",
//		"VOYAGE_NO",
//		"BILL_NO"
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
	edit : function (title,param){
		title="清单信息";//+title;
		if(this.readOnly||this.mode=='local'){
			Ext.apply(param,{readOnly:true});
		}
		var infoPanel=new Ext.ux.InventoryPanel(param);
		var tabPage=top.addTabPage(infoPanel,title,"",false);
		infoPanel.on("close",function(){
			top.closeTab(tabPage);
			if(!this.isDestroyed){
				var pageTool = this.pageTool;
				if (pageTool) {
					pageTool.doLoad(pageTool.cursor);
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
	del : function (record){
//		if(isInventoryReadOnly(record)){
			var param={
				INVENTORY_ID:record.get("INVENTORY_ID"),
				COP_NO:record.get("COP_NO")
			};
			this.getEl().mask("执行中...");
			Ext.Ajax.request({
				scope: this,
				url : 'common!delInventory.action',
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
//		}
	},
	initComponent : function () {
		this.sm= new Ext.grid.RowSelectionModel({singleSelect:true});
		var store = new Ext.data.Store(Ext.apply({
			url : 'common!getAllInventorys.action',
			reader : new Ext.data.JsonReader({
				totalProperty : 'total',
				root : "rows"
			}, [ "INVENTORY_ID",
			     "GUID",
			     "CUSTOM_CODE",
			     "APP_TYPE",
			     "APP_TIME",
			     "APP_STATUS",
			     "APP_UID",
			     "COP_NO",
			     "PRE_NO",
			     "LOGISTICS_NO",
			     "INVT_NO",
			     "PORT_CODE",
			     {name:"IE_DATE",convert:Renderer.DATE},
			     "OWNER_CODE",
			     "TRADE_MODE",
			     "LOCT_NO",
			     "LICENSE_NO",
			     "COUNTRY",
			     "DESTINATION_PORT",
			     "FREIGHT",
			     "FREIGHT_CURR",
			     "FREIGHT_MARK",
			     "INSURE_FEE",
			     "INSURE_FEE_CURR",
			     "INSURE_FEE_MARK",
			     "WRAP_TYPE",
			     "PACK_NO",
			     "WEIGHT",
			     "NET_WEIGHT",
			     "NOTE",
			     "RETURN_STATUS",
			     "RETURN_TIME",
			     "RETURN_INFO",
			     "CREAT_TIME",
			     "UPDATE_TIME",
			     "GOODSList",
			     //order
			     "ORDER_NO",
			     "EBP_CODE",
			     "EBC_CODE",
			     "AGENT_CODE",
			     "CONSIGNEE_COUNTRY",
			     //logistics
			     "LOGISTICS_CODE",
			     "IE_FLAG",
			     "TRAF_MODE",
			     "SHIP_NAME",
			     "VOYAGE_NO",
			     "BILL_NO"
		    ]),
			listeners:{
			  	"exception": function(proxy,type,action,options,response,arg){
			  		Ext.Msg.alert("提示","加载出错"+
						"<BR>Status:"+response.statusText||"unknow");
			  	}
			}
		},this.storeCfg));
		if(Ext.isEmpty(this.columns)){
			this.columns= [ new Ext.grid.RowNumberer({
				width : 26
			}), {
			    id : "INVENTORY_ID",
			    header : "",
			    dataIndex : "INVENTORY_ID",
			    hidden : true,
			    hideable : false
			},{
			    id : "GUID",
			    header : "系统唯一序号",
			    dataIndex : "GUID",
			    width : 320
			},{
			    id : "ORDER_NO",
			    header : "订单编号",
			    dataIndex : "ORDER_NO"
			},new Ext.ux.grid.CodeNameColumn({
				category: relationCategory_ebp,
			    id : "EBP_CODE",
			    header : "电商平台",
			    dataIndex : "EBP_CODE"
			}),new Ext.ux.grid.CodeNameColumn({
				category: relationCategory_ebc,
			    id : "EBC_CODE",
			    header : "经营单位",
			    dataIndex : "EBC_CODE"
			}),new Ext.ux.grid.CodeNameColumn({
				category: relationCategory_agent,
			    id : "AGENT_CODE",
			    header : "报关企业",
			    dataIndex : "AGENT_CODE"
			}),{
			    id : "LOGISTICS_NO",
			    header : "物流运单编号",
			    dataIndex : "LOGISTICS_NO"
			},new Ext.ux.grid.CodeNameColumn({
				category: relationCategory_logistics,
			    id : "LOGISTICS_CODE",
			    header : "物流企业",
			    dataIndex : "LOGISTICS_CODE"
			}),new Ext.ux.grid.CodeNameColumn({
				category: relationCategory_ieFlag,
			    id : "IE_FLAG",
			    header : "进出口标记",
			    dataIndex : "IE_FLAG"
			}),new Ext.ux.grid.CodeNameColumn({
				category: relationCategory_trafMode,
			    id : "TRAF_MODE",
			    header : "运输方式",
			    dataIndex : "TRAF_MODE"
			}),{
			    id : "SHIP_NAME",
			    header : "运输工具名称",
			    dataIndex : "SHIP_NAME"
			},{
			    id : "VOYAGE_NO",
			    header : "航班航次号",
			    dataIndex : "VOYAGE_NO"
			},{
			    id : "BILL_NO",
			    header : "提运单号",
			    dataIndex : "BILL_NO"
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
			    id : "COP_NO",
			    header : "企业内部编号",
			    dataIndex : "COP_NO"
			},{
			    id : "PRE_NO",
			    header : "预录入编号",
			    dataIndex : "PRE_NO"
			},{
			    id : "INVT_NO",
			    header : "清单编号",
			    dataIndex : "INVT_NO"
			},new Ext.ux.grid.CodeNameColumn({
				category: relationCategory_custom,
			    id : "PORT_CODE",
			    header : "出口口岸代码",
			    dataIndex : "PORT_CODE"
			}),{
			    id : "IE_DATE",
			    header : "出口日期",
			    dataIndex : "IE_DATE"/*,
			    renderer: Renderer.DATE*/
			},new Ext.ux.grid.CodeNameColumn({
				category: relationCategory_ebc,
			    id : "OWNER_CODE",
			    header : "发货人",
			    dataIndex : "OWNER_CODE"
			}),{
			    id : "TRADE_MODE",
			    header : "贸易方式",
			    dataIndex : "TRADE_MODE"
			},{
			    id : "LOCT_NO",
			    header : "监管场所代码",
			    dataIndex : "LOCT_NO"
			},{
			    id : "LICENSE_NO",
			    header : "许可证号",
			    dataIndex : "LICENSE_NO"
			},new Ext.ux.grid.CodeNameColumn({
				category: relationCategory_country,
				id : "COUNTRY",
			    header : "运抵国（地区）",
			    dataIndex : "COUNTRY"
			}),new Ext.ux.grid.CodeNameColumn({
				category: relationCategory_port,
				id : "DESTINATION_PORT",
			    header : "抵运港代码",
			    dataIndex : "DESTINATION_PORT"
			}),{
			    id : "FREIGHT",
			    header : "运费",
			    dataIndex : "FREIGHT"
			},new Ext.ux.grid.CodeNameColumn({
				category: relationCategory_currency,
			    id : "FREIGHT_CURR",
			    header : "运费币制",
			    dataIndex : "FREIGHT_CURR"
			}),new Ext.ux.grid.CodeNameColumn({
				category: relationCategory_feeMark,
			    id : "FREIGHT_MARK",
			    header : "运费标志",
			    dataIndex : "FREIGHT_MARK"
			}),{
			    id : "INSURE_FEE",
			    header : "保费",
			    dataIndex : "INSURE_FEE"
			},new Ext.ux.grid.CodeNameColumn({
				category: relationCategory_currency,
			    id : "INSURE_FEE_CURR",
			    header : "保费币制",
			    dataIndex : "INSURE_FEE_CURR"
			}),new Ext.ux.grid.CodeNameColumn({
				category: relationCategory_feeMark,
			    id : "INSURE_FEE_MARK",
			    header : "保费标志",
			    dataIndex : "INSURE_FEE_MARK"
			}),new Ext.ux.grid.CodeNameColumn({
				category: relationCategory_wrapType,
			    id : "WRAP_TYPE",
			    header : "包装种类",
			    dataIndex : "WRAP_TYPE"
			}),{
			    id : "PACK_NO",
			    header : "件数",
			    dataIndex : "PACK_NO"
			},{
			    id : "WEIGHT",
			    header : "毛重",
			    dataIndex : "WEIGHT"
			},{
			    id : "NET_WEIGHT",
			    header : "净重",
			    dataIndex : "NET_WEIGHT"
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
			    dataIndex : "CREAT_TIME",
			    hidden : true,
			    hideable : false
			},{
			    id : "UPDATE_TIME",
			    header : "",
			    dataIndex : "UPDATE_TIME",
			    hidden : true,
			    hideable : false
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
				sortable : true
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
						this.edit('录入',{editType:editType});
					}.createDelegate(this)
				},{
			        text: '编辑',
			        scale: 'medium',
			        icon:'../../resource/images/btnImages/modify.png',
			        handler: function(b,e){
			        	var editType='mod';
			        	var data=this.checkSelect(true);
			        	if(data)
			        		this.edit('编辑',{editType:editType,record:data});
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
			        		this.del(data);
			        }.createDelegate(this)
				}/*,{
			        text: '复制',
			        icon:'../../resource/images/btnImages/page_copy.png',
			        handler: function(b,e){
			        	var editType='add';
			        	var data=this.checkSelect(true);
			        	if(data)
			        		this.edit('录入',{editType:editType,record:data});
					}.createDelegate(this)
				}*/]
			});
        	if(this.readOnly){
//				tbar.remove(3);
				tbar.remove(2);
				tbar.get(1).setText("清单信息");
				tbar.remove(0);
			}else if(this.mode=="local"){
//				tbar.remove(3);
				tbar.get(1).setText("清单信息");
				tbar.remove(0);
			}
            this.tbar = tbar;
        }
		this.on('rowclick', function(grid, rowIndex, e) {
			var delButton=this.topToolbar.find("name",'delete')[0];
			if(delButton){
		        var selections = grid.getSelectionModel().getSelections();
		        if (selections.length == 0) return;
		 
		        for ( var i = 0; i < selections.length; i++) {
		            var record = selections[i];
		            delButton.setDisabled(isInventoryReadOnly(record));
		        }
			}
	    },this);
		
		Ext.ux.InventoryGridPanel.superclass.initComponent.call(this);
	}
});

Ext.ux.InventoryFormPanel = Ext.extend(Ext.form.FormPanel, {
	record : undefined,
	editType : 'add',
	readOnly: undefined,
	
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
			var data=this.record;
			this.getForm().loadRecord(data);
			var goodsGrid=findByProperty(this.items.getRange(),"name","itemGrid");
			
			var goodsList=data.get("GOODSList");
			Ext.each(goodsList,function(item,index,allItems){
				item['CONSIGNEE_COUNTRY']=data.get("CONSIGNEE_COUNTRY");
			});
			var store=goodsGrid.getStore();
			store.loadData.defer(1000,store,[{total:goodsList.length,rows:goodsList}]);
		}
	},
	isReadOnly: function(){
		if(this.editType=="add") return false;
		if(this.record){
			return isInventoryReadOnly(this.record);
		}
		return false;
	},
	submitForm : function(opType){
		var fp=this;
		var form = fp.getForm();
		if (form.isValid()) {
			var itemGrid=findByProperty(this.items.getRange(),"name","itemGrid");
			var itemRecords=[];
			itemGrid.getStore().each(function(record){
				itemRecords.push(Ext.encode(record.data));
			});
			var param={
		        editType: this.editType,
		        APP_STATUS: opType,
		        GOODSList: itemRecords
		    };
			//添加displayfield参数或未显示参数
			if(this.editType=="mod"){
				Ext.apply(param,{
			        GUID: this.record.get('GUID'),
	//		        APP_TIME: this.record.get('APP_TIME'),
			        INVENTORY_ID: this.record.get('INVENTORY_ID')
				});
			}
			form.submit({
				scope:fp,
			    clientValidation: true,
			    waitTitle: '正在执行',
			    waitMsg: '请稍后……',
			    url: 'common!setInventory.action',
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
	bindHandler : function(){
		var valid = true;
		var itemGrid=findByProperty(this.items.getRange(),"name","itemGrid");
		if(itemGrid){
			valid=itemGrid.getStore().getCount()>0;
		}
		if(valid){
			Ext.ux.InventoryFormPanel.superclass.bindHandler.call(this);
		}else{
			if(this.fbar){
				var fitems = this.fbar.items.items;
				for(var i = 0, len = fitems.length; i < len; i++){
					var btn = fitems[i];
					if(btn.formBind === true && btn.disabled === valid){
						btn.setDisabled(!valid);
					}
				}
			}
			this.fireEvent('clientvalidation', this, valid);
		}
	},
	initComponent : function () {
		if(this.readOnly==undefined)
			this.readOnly=this.isReadOnly();
		this.submited=!(Ext.isEmpty(this.record)||this.record.get("APP_STATUS")==1);
		this.items=[
//		{xtype:'hidden',name:"INVENTORY_ID"},
		{
			xtype:'displayfield',
			name:"GUID",
			fieldLabel:'系统唯一序号',
			hidden: this.editType=="add"
		},{
	        fieldLabel:'订单编号',
	        name: 'ORDER_NO',
	        submitValue: false,
	        readOnly: true,
	        maxLength: 30
		},GenerateCodeNameComboGrid({
				readOnly: true,
	    		allowBlank : false,
	    		submitValue: false,
		    	fieldLabel:'电商平台名称',
		    	name: 'EBP_CODE'
	        },relationCategory_ebp
	    ),GenerateCodeNameComboGrid({
	    		readOnly: true,
	    		allowBlank : false,
	    		submitValue: false,
		    	fieldLabel:'经营单位',
		    	name: 'EBC_CODE'
	        },relationCategory_ebc
	    ),GenerateCodeNameComboGrid({
	    		readOnly: true,
	    		allowBlank : false,
	    		submitValue: false,
		    	fieldLabel:'报关企业名称',
		    	name: 'AGENT_CODE'
	        },relationCategory_agent
	    ),{
	        fieldLabel:'物流运单编号',
	        name: 'LOGISTICS_NO',
	        readOnly: true,
//	        readOnly: this.editType!=="add",
	        maxLength: 20
		},GenerateCodeNameComboGrid({
				readOnly: true,
	    		allowBlank : false,
	    		submitValue: false,
		    	fieldLabel:'物流企业名称',
		    	name: 'LOGISTICS_CODE'
	        },relationCategory_logistics
	    ),GenerateCodeNameComboGrid({
	    		readOnly: true,
	    		allowBlank : false,
	    		submitValue: false,
		    	fieldLabel:'进出口标记',
		    	name: 'IE_FLAG',
		    	autoLoad: false
	        },relationCategory_ieFlag,CodeNameData.ieFlag
	    ),GenerateCodeNameComboGrid({
	    		readOnly: true,
	    		allowBlank : false,
	    		submitValue: false,
		    	fieldLabel:'运输方式',
		    	name: 'TRAF_MODE'
	        },relationCategory_trafMode
	    ),{
			readOnly: true,
			submitValue: false,
	        fieldLabel:'运输工具名称',
	        name: 'SHIP_NAME',
	        maxLength: 100
		},{
			readOnly: true,
			submitValue: false,
	        fieldLabel:'航班航次号',
	        name: 'VOYAGE_NO',
	        maxLength: 32
		},{
			readOnly: true,
			submitValue: false,
	        fieldLabel:'提运单号',
	        name: 'BILL_NO',
	        maxLength: 37
		},GenerateCodeNameComboGrid({
				readOnly: true,
	    		allowBlank : false,
		    	fieldLabel:'申报海关代码',
		    	name: 'CUSTOM_CODE'
	        },relationCategory_custom
	    ),GenerateCodeNameComboGrid({
	    		readOnly: true,
	    		allowBlank : false,
		    	fieldLabel:'用户名称',
		    	name: 'APP_UID'
	        },relationCategory_appUid
	    ),{
			xtype:'displayfield',
	        fieldLabel:'申报时间',
	        name: 'APP_TIME',
	        submitValue: false,
	        hidden: !this.submited
	    },{
	        fieldLabel:'企业内部编号',
	        name: 'COP_NO',
	        maxLength: 20
		},{
	        /*fieldLabel:'预录入编号',
	        name: 'PRE_NO',
	        maxLength: 18,
	        allowBlank: true*/
			xtype:'displayfield',
	        fieldLabel:'预录入编号',
	        name: 'PRE_NO',
	        submitValue: false,
	        hidden: !this.submited
		},{
			xtype:'displayfield',
			fieldLabel : "清单编号",
			name: "INVT_NO",
//	        maxLength: 30,
			submitValue: false,
	        hidden: !isInventoryComplete(this.record)
		},GenerateCodeNameComboGrid({
	    	fieldLabel:'出口口岸代码',
	    	name: 'PORT_CODE',
	        allowBlank : false
        	},relationCategory_port
	    ),{
			fieldLabel : "出口日期",
			name : "IE_DATE",
			cls : 'Wdate',
			listeners: {
				'focus': function(){
					WdatePicker({
						el : this.id,
						isShowClear : true,
						readOnly : true,
						dateFmt : DATE_FORMAT?DATE_FORMAT:"yyyy-MM-dd",
						autoPickDate : true
					});
					this.blur();
				}
			}
		},GenerateCodeNameComboGrid({
	    	fieldLabel:'发货人',
	    	name: 'OWNER_CODE',
	        allowBlank : false
        	},relationCategory_ebc
	    ),/*GenerateCodeNameComboGrid({
	    	fieldLabel:'贸易方式',
	    	name: 'TRADE_MODE',
	        allowBlank : false
        	},relationCategory_tradeMode
	    ),*/{
		    fieldLabel : "监管场所代码",
		    name : "LOCT_NO",
		    maxLength: 10
		},{
			fieldLabel : "许可证号",
			name : "LICENSE_NO",
		    maxLength: 19
		},GenerateCodeNameComboGrid({
		    	fieldLabel:'运抵国（地区）',
		    	name: 'COUNTRY',
		        allowBlank : false
	    	},relationCategory_country
	    ),GenerateCodeNameComboGrid({
		    	fieldLabel:'抵运港',
		    	name: 'DESTINATION_PORT',
		        allowBlank : false
	    	},relationCategory_port
	    ),{
	    	xtype:'numberfield',
	    	allowNegative: false,
	        fieldLabel:'运费',
	        name: 'FREIGHT'
	    },GenerateCodeNameComboGrid({
	    	fieldLabel:'运费币制',
	    	name: 'FREIGHT_CURR',
	        allowBlank : false
        	},relationCategory_currency
	    ),GenerateCodeNameComboGrid({
	    	fieldLabel:'运费标志',
	    	name: 'FREIGHT_MARK',
	        allowBlank : false,
	        autoLoad: false
        	},relationCategory_feeMark,CodeNameData.feeMark
	    ),{
	    	xtype:'numberfield',
	    	allowNegative: false,
	        fieldLabel:'保费',
	        name: 'INSURE_FEE'
	    },GenerateCodeNameComboGrid({
	    	fieldLabel:'保费币制',
	    	name: 'INSURE_FEE_CURR',
	        allowBlank : false
        	},relationCategory_currency
	    ),GenerateCodeNameComboGrid({
	    	fieldLabel:'保费标志',
	    	name: 'INSURE_FEE_MARK',
	        allowBlank : false,
	        autoLoad: false
        	},relationCategory_feeMark,CodeNameData.feeMark
	    ),GenerateCodeNameComboGrid({
	    	fieldLabel:'包装种类',
	    	name: 'WRAP_TYPE',
	        allowBlank : false
        	},relationCategory_wrapType
	    ),{
	    	xtype:'numberfield',
	    	allowNegative: false,
	    	allowDecimals: false,
	        fieldLabel:'件数',
	        name: 'PACK_NO'
	    },{
	    	xtype:'numberfield',
	    	allowNegative: false,
	        fieldLabel:'毛重',
	        name: 'WEIGHT'
	    },{
	    	xtype:'numberfield',
	    	allowNegative: false,
	        fieldLabel:'净重',
	        name: 'NET_WEIGHT'
	    },{
	        xtype: 'textarea',
	        maxLength: 1000,
	        fieldLabel:'备注',
	        name: 'NOTE'
	    }];
		
		var itemGrid=new Ext.ux.SkuGridPanel({
			name:"itemGrid",
			mode:'local',
			readOnly:true,
			height: 200,
			showColumns : [
			     "ITEM_NO",
			     "ITEM_NAME",
			     "G_NO",
			     "G_CODE",
			     "G_NAME",
			     "G_MODEL",
			     "BAR_CODE",
			     "CURRENCY",
			     
			     "CONSIGNEE_COUNTRY",

			     "UNIT_I",
			     "UNIT1_I",
			     "UNIT2_I",
			     "QTY",
			     "QTY1",
			     "QTY2",
			     "ONE_PRICE",
			     "TOTAL"],
			storeCfg:{
				url: 'common!getAllSkus.action',
				reader : new Ext.data.JsonReader({
					totalProperty : 'total',
					root : "rows"
				}, ["SKU_ID",
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
				     "UPDATE_TIME",
				     
				     "CONSIGNEE_COUNTRY",//订单

				     "INVENTORY_DETAIL_ID",
				     "GNUM",
				     "UNIT_I",//清单
				     "UNIT1_I",
				     "UNIT2_I",
				     "QTY",
				     "QTY1",
				     "QTY2",
				     "ONE_PRICE",
				     "TOTAL"])
			},
			columns:[ new Ext.grid.RowNumberer({
					width : 26
				}), {
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
				},new Ext.ux.grid.CodeNameColumn({
					category: relationCategory_currency,
				    id : "CURRENCY",
				    header : "币制",
				    dataIndex : "CURRENCY"
				}),new Ext.ux.grid.CodeNameColumn({
					category: relationCategory_country,
				    id : "CONSIGNEE_COUNTRY",
				    header : "最终目的国",
				    dataIndex : "CONSIGNEE_COUNTRY"
				}),new Ext.ux.grid.CodeNameColumn({
					category: relationCategory_unit,
				    id : "UNIT",
				    header : "申报计量单位",
				    dataIndex : "UNIT_I",
				    editable : !this.readOnly,
				    editor : GenerateCodeNameComboGrid({
				    		allowBlank : false
				        },relationCategory_unit
				    )
				}),new Ext.ux.grid.CodeNameColumn({
					category: relationCategory_unit,
				    id : "UNIT1",
				    header : "法定计量单位",
				    dataIndex : "UNIT1_I",
				    editable : !this.readOnly,
				    editor : GenerateCodeNameComboGrid({
				    		allowBlank : false
				        },relationCategory_unit
				    )
				}),new Ext.ux.grid.CodeNameColumn({
					category: relationCategory_unit,
				    id : "UNIT2",
				    header : "第二计量单位",
				    allowBlank: true,
				    dataIndex : "UNIT2_I",
				    editable : !this.readOnly,
				    editor : GenerateCodeNameComboGrid({
				    		allowBlank : true
				        },relationCategory_unit
				    )
				}),{
				    header : "申报数量",
				    dataIndex : "QTY",
				    editable : !this.readOnly,
				    editor : new Ext.ux.form.SpinnerField({
				    	allowDecimals:false,
						allowBlank:false,
						allowNegative:false,
						value:1,
						minValue:1,
						accelerate: true,
						alternateIncrementValue: 10
				    })
				},{
				    header : "法定数量",
				    dataIndex : "QTY1",
				    editable : !this.readOnly,
				    editor : new Ext.ux.form.SpinnerField({
				    	allowDecimals:false,
						allowBlank:false,
						allowNegative:false,
						value:1,
						minValue:1,
						accelerate: true,
						alternateIncrementValue: 10
				    })
				},{
				    header : "第二数量",
				    dataIndex : "QTY2",
				    editable : !this.readOnly,
				    editor : new Ext.ux.form.SpinnerField({
				    	allowDecimals:false,
						allowBlank:true,
						allowNegative:false,
						value:1,
						minValue:1,
						accelerate: true,
						alternateIncrementValue: 10
				    })
				},{
				    header : "单价",
				    dataIndex : "ONE_PRICE",
				    editable : !this.readOnly,
				    editor : new Ext.form.NumberField({
						allowBlank:false,
						allowNegative:false
				    })
				},{
				    header : "总价",
				    dataIndex : "TOTAL",
				    editable : !this.readOnly,
				    editor : new Ext.form.NumberField({
						allowBlank:false,
						allowNegative:false
				    })
				}]
			,
			listeners:{
				afteredit : function(e){
					if(e.value!=e.originalValue){
						if(e.field=="ONE_PRICE"||e.field=="QTY"){
							e.record.set("TOTAL",e.record.get("ONE_PRICE")*e.record.get("QTY"));
						}else if(e.field=="TOTAL"){
							var ONE_PRICE=parseFloat(e.record.get("TOTAL")*1.0/e.record.get("QTY")).toFixed(2);
							e.record.set("ONE_PRICE",ONE_PRICE);
						}
					}
				}
			}
		});

		var columns=[];
		var hiddens=[];
		Ext.apply(this.defaults,{readOnly:this.readOnly});
		Ext.each(this.items,function(item,index,allItems){
			if(!item.hidden){
				if(columns.length==0||columns[columns.length-1].items==undefined||columns[columns.length-1].items.length==3){
					columns.push({
						layout:'column',
						border:false,
						defaults:{columnWidth: 1/3},
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
		this.items.push(itemGrid);
		
		var validateButton=function (){
			if(this.buttons==undefined)
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
		    	text : '取消', 
		    	scale: 'medium',
		    	icon : '../../resource/images/btnImages/cancel.png',
				handler : function(b,e){
					this.fireEvent('close',this);
		    	}.createDelegate(this)
		    }));
		}.createDelegate(this);

		validateButton();
		
		this.addEvents('close');
		this.enableBubble('close');

		Ext.ux.InventoryFormPanel.superclass.initComponent.call(this);
		this.renderRecord();
	}
});

Ext.ux.InventoryPanel = Ext.extend(Ext.Panel, {
	editType : 'add',
	record: undefined,
	readOnly: undefined,
	
	layout:'card',
    activeItem: 0, //确保在容器的配置项中设置了当前活动项！
    monitorValid : false,
    monitorPoll : 500,

//    bodyStyle:'padding:10px',
    
    rollPage: function(direction){
    	var layout=this.getLayout();
    	layout.setActiveItem(this.activeItem+direction);
    	this.activeItem=this.items.indexOf(layout.activeItem);
    },
    isReadOnly: function(){
		if(this.editType=="add") return false;
		if(this.record){
			return isInventoryReadOnly(this.record);
		}
		return false;
	},
	addItem: function(){
		var success=false;
		var itemGridSelect=findByProperty(this.items.getRange(),"name","itemGrid");
		if(itemGridSelect){
			var data=itemGridSelect.checkSelect(true);
			if(data){
				var infoForm=findByProperty(this.items.getRange(),"name","infoForm");
				if(infoForm){
					var copyFields=[
		                "ORDER_NO","EBP_CODE","EBC_CODE","AGENT_CODE",
		                "LOGISTICS_NO","LOGISTICS_CODE","CUSTOM_CODE","APP_UID",
		                "IE_FLAG","TRAF_MODE","SHIP_NAME","VOYAGE_NO","BILL_NO",
		                {src:'FREIGHT',applyIf:true},
		                {src:'CURRENCY',tar:'FREIGHT_CURR',applyIf:true},
		                {tar:'FREIGHT_MARK',applyIf:true,value:3},
		                {src:'INSURE_FEE',applyIf:true},
		                {src:'CURRENCY',tar:'INSURE_FEE_CURR',applyIf:true},
		                {tar:'INSURE_FEE_MARK',applyIf:true,value:3},
		                {src:'PACK_NO',applyIf:true},
		                {src:'WEIGHT',applyIf:true},
		                {src:'NET_WEIGHT',applyIf:true}
	                ];
					Ext.each(copyFields,function(item,index,allItems){
						var object={
//							src,tar,value,
							applyIf:false,
						};
						var field;
						if(Ext.isString(item)){
							object.src=item;
							object.tar=item;
						}else if(Ext.isObject(item)){
							object.src=item['src'];
							object.tar=item['tar'];
							if(Ext.isEmpty(object.tar))
								object.tar=object.src;
							object.value=item['value'];
							object.applyIf=item['applyIf']==true;
						}
						if(object.value==undefined&&!Ext.isEmpty(object.src))
							object.value=data.get(object.src);
						field=infoForm.find("name",object.tar)[0];
						if(field){
							if((!object.applyIf)||Ext.isEmpty(field.getValue()))
								field.setValue(object.value);
						}
					});
					var itemGrid=findByProperty(infoForm.items.getRange(),"name","itemGrid");
					var goodsList=data.get("GOODSList");
					var store=itemGrid.getStore();
					Ext.each(goodsList,function(item,index,allItems){
						item['CONSIGNEE_COUNTRY']=data.get("CONSIGNEE_COUNTRY_O");
						item['QTY1']=item['QTY'];
						item['UNIT_I']=item['UNIT'];
						item['UNIT1_I']=item['UNIT1'];
						item['UNIT2_I']=item['UNIT2'];
					});
					store.loadData.defer(500,store,[{total:goodsList.length,rows:goodsList}]);
					success=true;
				}
			}
		}
		if(success){
			this.rollPage.createDelegate(this, [-1])();
		}
	},
	// private
	initItem: function(){
		if(this.readOnly){
			this.items=[new Ext.ux.InventoryFormPanel({
				name:"infoForm",
				readOnly: true,
				editType: this.editType,
				record: this.record
			})];
		}else{
			this.items=[new Ext.ux.InventoryFormPanel({
				name:"infoForm",
				record: this.record,
				editType: this.editType,
				buttons:[new Ext.Button({
		            text: '所属运单',
//		            icon: '../../resource/images/btnImages/package_add.png',
		            scale: 'medium',
		            handler: this.rollPage.createDelegate(this, [1])
		        })]
			}),new Ext.ux.LogisticsGridPanel({
				readOnly:true,
				name:"itemGrid",
				showColumns : [
					"GUID",
					"ORDER_NO",
					"CUSTOM_CODE",
					//"APP_UID",
					"EBP_CODE",
					"LOGISTICS_CODE",
					"LOGISTICS_NO",
//					"LOGISTICS_STATUS",
					"IE_FLAG",
					//"TRAF_MODE",
					//"SHIP_NAME",
					//"VOYAGE_NO",
					//"BILL_NO",
					//"PACK_NO",
					"GOODS_INFO",
					"CONSIGNEE",
					//"CONSIGNEE_COUNTRY",
					"SHIPPER"/*,
					//"SHIPPER_COUNTRY",
					"NOTE"*/],
				storeCfg:{
					baseParams:Ext.applyIf({'IN_USE':false},LogisticsCompleteParam)
				},
				buttons:[new Ext.Button({
					name: "addInto",
		            text: '确认选择',
//		            icon: '../../resource/images/btnImages/cart_put.png',
		            scale: 'large',
		            handler: this.addItem.createDelegate(this)
		        }),new Ext.Button({
					name: "next",
		            text: '清单信息',
//		            icon: '../../resource/images/btnImages/cart_put.png',
		            scale: 'large',
		            handler: this.rollPage.createDelegate(this, [-1])
		        })]
			})];
		}
	},
	// private
	initEvents : function(){
		Ext.ux.InventoryPanel.superclass.initEvents.call(this);
		if(this.monitorValid){
			this.startMonitoring();
		}
	},
	bindHandler : function(){
		var addIntoValid=false;
		var nextValid=false;
		var itemGrid=findByProperty(this.items.getRange(),"name","itemGrid");
		if(itemGrid){
			var itemData=itemGrid.checkSelect(true,true);
			
			var itemValid=itemData!==false;

			addIntoValid=itemValid;
			
			var addInto=findByProperty(itemGrid.buttons,"name","addInto");
			if(addInto){
				addInto.setDisabled(!addIntoValid);
			}
		}
	},
	startMonitoring : function(){
		if(!this.validTask){
			this.validTask = new Ext.util.TaskRunner();
			this.validTask.start({ 
				run : this.bindHandler,
				interval : this.monitorPoll || 200,
				scope: this
			});
		}
	},
	stopMonitoring : function(){
	    if(this.validTask){
			this.validTask.stopAll();
			this.validTask = null;
		}
	},
	// private
	beforeDestroy : function(){
		this.stopMonitoring();
		Ext.ux.InventoryPanel.superclass.beforeDestroy.call(this);
	},
	// private
	initComponent : function () {
		if(this.readOnly==undefined)
			this.readOnly=this.isReadOnly();
		this.initItem();
		if(!this.readOnly){
			this.monitorValid=true;
		}
		Ext.ux.InventoryPanel.superclass.initComponent.call(this);
	}
});