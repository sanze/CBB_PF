//统一定义code_name的类别，方便扩充管理
//申报海关代码
var relationCategory_custom = 'CUSTOM_CODE';
//用户名称
var relationCategory_appUid = 'APP_UID';
//电商平台名称
var relationCategory_ebp = 'EBP_CODE';
//电商企业名称
var relationCategory_ebc = 'EBC_CODE';
//申报企业名称
var relationCategory_agent = 'AGENT_CODE';
//预归类企业名称
var relationCategory_classify = 'CLASSIFY_CODE';
//物流企业名称
var relationCategory_logistics = 'LOGISTICS_CODE';
//海关物品税号
var relationCategory_taxCode = 'TAX_CODE';
//计量单位
var relationCategory_unit = 'UNIT';
//币制
var relationCategory_currency = 'CURRENCY';
//国家
var relationCategory_country = 'COUNTRY';
//进出口标记，static
var relationCategory_ieFlag = 'IE_FLAG';
//运输方式
var relationCategory_trafMode = 'TRAF_MODE';
//港口
var relationCategory_port = 'PORT';
//费用标志，static
var relationCategory_feeMark = 'FEE_MARK';
//包装种类
var relationCategory_wrapType = 'WRAP_TYPE';

//业务类型
var relationCategory_bizType = 'BIZ_TYPE';

//支付企业代码
var relationCategory_pay = 'PAY';
//支付状态
var relationCategory_payStatus = 'PAY_STATUS';

var myPageSize = 100;

var CodeNameData={
	ieFlag: [{"CODE":"I","NAME":"进口"},{"CODE":"E","NAME":"出口"}],
	feeMark:[{"CODE":1,"NAME":"率"},{"CODE":2,"NAME":"单价"},{"CODE":3,"NAME":"总价"}],
	bizType:[{"CODE":1,"NAME":"一般进口"},{"CODE":2,"NAME":"一般出口"},{"CODE":3,"NAME":"保税进口"},{"CODE":4,"NAME":"保税出口"}],
	payStatus:[{"CODE":"D","NAME":"代扣"},{"CODE":"S","NAME":"实扣"},{"CODE":"R","NAME":"部分退款"},{"CODE":"C","NAME":"取消(退款)"}]
};


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
        } ],
        autoLoad: true,
        valueField: valueField,
        textField: textField,
        store : ds,
        editable : false,
        allowBlank : true
    },param);
}

function GenerateContactComboGrid(param,data){
	var dsParam_concact={
		reader : new Ext.data.JsonReader({
			totalProperty : 'total',
			root : "rows"
		}, [ "CONTACT_ID","CODE","NAME","TEL","COUNTRY","ADDRESS"]),
		listeners:{
		  	"exception": function(proxy,type,action,options,response,arg){
		  		Ext.Msg.alert("提示","加载出错"+
					"<BR>Status:"+response.statusText||"unknow");
		  	},
		  	"load": function(store, records, options){
		  		for(var i=0;i<records.length;i++){
		  		}
		  	}
		}
	};
	if(Ext.isEmpty(data)){
		Ext.apply(dsParam_concact,{
			url : 'common!getAllContact.action'
		});
	}else{
		Ext.apply(dsParam_concact,{
			data : {'total': data.length, 'rows': data}
		});
	}
	
	var dataStore=new Ext.data.Store(dsParam_concact);
	var valueField="CONTACT_ID";
	var textField="NAME";
	return Ext.apply({
		tbar: [
	       new Ext.ux.form.SearchField({
	    	   store: dataStore,
	    	   paramName: [{property:textField,anyMatch:true},{property:valueField,anyMatch:true}],
	    	   logical: "or"
	       })
        ],
       	xtype: 'combogrid',
    	columns : [ {
            header : 'ID',
            dataIndex : valueField,
            hidden:true
        }, {
            header : '姓名',
            dataIndex : textField
        } , {
            header : '联系方式',
            dataIndex : "TEL"
        } , {
            header : '国家',
            dataIndex : "COUNTRY"
        } , {
            header : '地址',
            dataIndex : "ADDRESS"
        } 
        ],
        autoLoad: true,
        valueField: valueField,
        textField: textField,
        store : dataStore,
        editable : false,
        allowBlank : true
    },param);
}

function GenerateDeliveryNoComboGrid(param,data){
	var dsParam_concact={
		reader : new Ext.data.JsonReader({
			totalProperty : 'total',
			root : "rows"
		}, [ "DELIVERY_NO","NOTE"]),
		listeners:{
		  	"exception": function(proxy,type,action,options,response,arg){
		  		Ext.Msg.alert("提示","加载出错"+
					"<BR>Status:"+response.statusText||"unknow");
		  	},
		  	"load": function(store, records, options){
		  		for(var i=0;i<records.length;i++){
		  		}
		  	}
		}
	};
	if(Ext.isEmpty(data)){
		Ext.apply(dsParam_concact,{
			url : 'import-common!getAllDeliveries.action'
		});
	}else{
		Ext.apply(dsParam_concact,{
			data : {'total': data.length, 'rows': data}
		});
	}
	
	var dataStore=new Ext.data.Store(dsParam_concact);
	var valueField="DELIVERY_NO";
	var textField="DELIVERY_NO";
	return Ext.apply({
		tbar: [
	       new Ext.ux.form.SearchField({
	    	   store: dataStore,
	    	   paramName: [{property:textField,anyMatch:true},{property:valueField,anyMatch:true}],
	    	   logical: "or"
	       })
        ],
       	xtype: 'combogrid',
    	columns : [{
            header : '入库单号',
            dataIndex : textField
        } , {
            header : '备注',
            dataIndex : "NOTE"
        }],
        autoLoad: true,
        valueField: valueField,
        textField: textField,
        store : dataStore,
        editable : false,
        allowBlank : true
    },param);
}

Ext.ns("Ext.ux.grid");
Ext.ux.grid.CodeNameColumn = Ext.extend(Ext.grid.Column, {
	/**
	 * @cfg {String} category
	*/
	category  : undefined,
	valueField: "CODE",
	textField : /*["CODE",*/"NAME"/*]*/,
	store: undefined,
	qtip: true,
	constructor: function(cfg){
		Ext.ux.grid.CodeNameColumn.superclass.constructor.call(this, cfg);
		if(Ext.isEmpty(this.store)){
			this.store = new Ext.data.Store({
				url : 'common!getAllCodeNames.action',
				baseParams:{RELATION_CATEGORY:this.category},
				reader : new Ext.data.JsonReader({
					totalProperty : 'total',
					root : "rows"
				}, [ "CODE_NAME_ID","CODE","SN_CODE","NAME"])
			});
		}
		this.store.load({
			callback:function(records,option,success){
				this.renderer=function(val,metadata,record,row,col,store){
					var idx = this.store.findExact(this.valueField, val);
			        if (idx >= 0) {
			        	if(Ext.isArray(this.textField)){
			        		var texts=[];
			        		Ext.each(this.textField,function(item,index,allItems){
			        			var itemval=this.store.getAt(idx).get(item);
			        			if(itemval)texts.push(itemval);
			        		},this);
			        		val=texts.join('：');
			        	}else{
			        		val=this.store.getAt(idx).get(this.textField);
			        	}
			        }
			        if(this.qtip&&!Ext.isEmpty(val))
						metadata.attr = 'ext:qtip="' +val+'"';   //关键  
			        return val;
				}.createDelegate(this);
			},
			scope: this
		});
	}
});