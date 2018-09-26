function isSkuReadOnly(record){
	return !(record.get("APP_STATUS")==1||//暂存
			record.get("RETURN_STATUS")==4||//发送海关失败
			record.get("RETURN_STATUS")==100);//海关退单
}

var SkuCompleteParam={
	RETURN_STATUS: 399//海关审结
};
