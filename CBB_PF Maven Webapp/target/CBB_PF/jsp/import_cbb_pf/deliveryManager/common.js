function isDeliveryReadOnly(record){
	return false;
}

var DeliveryCompleteParam={
//新建订单选择商品时，不再只显示海关审核通过的商品，全部显示。有些审批通过的商品也没显示。2016/7/29邮件修改点
//	RETURN_STATUS: 2//海关审结
};

var ComboBoxValue_delivery={
	APP_STATUS:[["暂存","1"],["审批中/申报中","2"],["无需申报","4"],["申报完成","99"]],
	BIZ_TYPE:[["一般进口","1"],["一般出口","2"],["保税进口","3"],["保税出口","4"]],
	RETURN_STATUS:[["审批通过","2"],["审批不通过","3"]]
};
