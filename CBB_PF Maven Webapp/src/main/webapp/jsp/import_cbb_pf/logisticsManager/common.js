function isLogisticsReadOnly(record){
	return !(record.get("APP_STATUS")==1||	//暂存
			(Ext.isEmpty(record.get("LOGISTICS_STATUS"))&&
			(record.get("RETURN_STATUS")==4||	//发送海关失败
			record.get("RETURN_STATUS")==100)));	//海关退单
}
function isLogisticsStatusReadOnly(record){
	return !((record.get("RETURN_STATUS")==120&&	//海关入库、签收
			  record.get("LOGISTICS_STATUS")!="S")||
//			 record.get("LOGISTICS_STATUS")=="W"||
			 (!Ext.isEmpty(record.get("LOGISTICS_STATUS"))&&
			  (record.get("RETURN_STATUS")==4||	//发送海关失败
			   record.get("RETURN_STATUS")==100)));	//海关退单
}

var LogisticsCompleteParam={
	LOGISTICS_STATUS:'("R","C","L","S")',
	RETURN_STATUS:2
};

var ComboBoxValue_logistics={
	APP_STATUS:[["暂存","1"],["审批中/申报中","2"],["无需申报","4"],["申报完成","99"]],
	LOGISTICS_STATUS:[["承运","A"],["运抵","R"], ["退货","C"],["离境","L"],["签收","S"]],
	RETURN_STATUS:[["审批通过","2"],["审批不通过","3"]]
};
