1.停止tomcat进程
2.备份CBB_PF Maven Webapp\src\main\webapp\WEB-INF\classes\resourceConfig\systemConfig.properties文件
3.解压CBB_PF.war覆盖
4.对比升级包中的systemConfig.properties和备份的systemConfig.properties，增加新的配置项
5.重启tomcat

systemConfig.properties中添加如下配置：
requestType_sn_deliverGoodsNotify = deliverGoodsNotify 
