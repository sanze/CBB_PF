function isOrderReadOnly(record){
	return !(record.get("APP_STATUS")==1||//暂存
			record.get("RETURN_STATUS")==3);//审批不通过
}
var OrderCompleteParam={
	RETURN_STATUS:2	//海关入库
};

var ComboBoxValue_order={
	APP_STATUS:[["暂存","1"],["审批中/申报中","2"],["无需申报","4"],["申报完成","99"]],
	ORDER_TYPE:[["一般进口","1"],["一般出口","2"],["保税进口","3"],["保税出口","4"]],
	RETURN_STATUS:[["审批通过","2"],["审批不通过","3"]]
};