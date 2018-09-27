/*
SQLyog Ultimate v11.11 (32 bit)
MySQL - 5.6.10 : Database - cbb_pf
*********************************************************************
*/
/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Data for the table `t_sys_menu` */
insert  into `t_sys_menu`(`SYS_MENU_ID`,`MENU_DISPLAY_NAME`,`MENU_HREF`,`MENU_PARENT_ID`,`IS_LEAF`,`ICON_CLASS`) values (1000000,'系统管理',NULL,0,0,'system');
insert  into `t_sys_menu`(`SYS_MENU_ID`,`MENU_DISPLAY_NAME`,`MENU_HREF`,`MENU_PARENT_ID`,`IS_LEAF`,`ICON_CLASS`) values (1010000,'代号管理','../codeManager/codeManagement.jsp',1000000,1,NULL);
insert  into `t_sys_menu`(`SYS_MENU_ID`,`MENU_DISPLAY_NAME`,`MENU_HREF`,`MENU_PARENT_ID`,`IS_LEAF`,`ICON_CLASS`) values (1020000,'-',NULL,1000000,1,NULL);
insert  into `t_sys_menu`(`SYS_MENU_ID`,`MENU_DISPLAY_NAME`,`MENU_HREF`,`MENU_PARENT_ID`,`IS_LEAF`,`ICON_CLASS`) values (1030000,'ftp路径管理','../filePathManager/filePathManagement.jsp',1000000,1,NULL);
insert  into `t_sys_menu`(`SYS_MENU_ID`,`MENU_DISPLAY_NAME`,`MENU_HREF`,`MENU_PARENT_ID`,`IS_LEAF`,`ICON_CLASS`) values (2000000,'跨境电商',NULL,0,0,'goods');
insert  into `t_sys_menu`(`SYS_MENU_ID`,`MENU_DISPLAY_NAME`,`MENU_HREF`,`MENU_PARENT_ID`,`IS_LEAF`,`ICON_CLASS`) values (2010000,'商品备案','../skuManager/SKU.menu',2000000,1,NULL);
insert  into `t_sys_menu`(`SYS_MENU_ID`,`MENU_DISPLAY_NAME`,`MENU_HREF`,`MENU_PARENT_ID`,`IS_LEAF`,`ICON_CLASS`) values (2020000,'-',NULL,2000000,1,NULL);
insert  into `t_sys_menu`(`SYS_MENU_ID`,`MENU_DISPLAY_NAME`,`MENU_HREF`,`MENU_PARENT_ID`,`IS_LEAF`,`ICON_CLASS`) values (2030000,'电子订单','../orderManager/ORDER.menu',2000000,1,NULL);
insert  into `t_sys_menu`(`SYS_MENU_ID`,`MENU_DISPLAY_NAME`,`MENU_HREF`,`MENU_PARENT_ID`,`IS_LEAF`,`ICON_CLASS`) values (2040000,'-',NULL,2000000,1,NULL);
insert  into `t_sys_menu`(`SYS_MENU_ID`,`MENU_DISPLAY_NAME`,`MENU_HREF`,`MENU_PARENT_ID`,`IS_LEAF`,`ICON_CLASS`) values (2050000,'支付凭证','DISABLED',2000000,1,NULL);
insert  into `t_sys_menu`(`SYS_MENU_ID`,`MENU_DISPLAY_NAME`,`MENU_HREF`,`MENU_PARENT_ID`,`IS_LEAF`,`ICON_CLASS`) values (2060000,'-',NULL,2000000,1,NULL);
insert  into `t_sys_menu`(`SYS_MENU_ID`,`MENU_DISPLAY_NAME`,`MENU_HREF`,`MENU_PARENT_ID`,`IS_LEAF`,`ICON_CLASS`) values (2070000,'物流运单','../logisticsManager/LOGISTICS.menu',2000000,1,NULL);
insert  into `t_sys_menu`(`SYS_MENU_ID`,`MENU_DISPLAY_NAME`,`MENU_HREF`,`MENU_PARENT_ID`,`IS_LEAF`,`ICON_CLASS`) values (2080000,'-',NULL,2000000,1,NULL);
insert  into `t_sys_menu`(`SYS_MENU_ID`,`MENU_DISPLAY_NAME`,`MENU_HREF`,`MENU_PARENT_ID`,`IS_LEAF`,`ICON_CLASS`) values (2090000,'出境清单','../inventoryManager/INVENTORY.menu',2000000,1,NULL);
insert  into `t_sys_menu`(`SYS_MENU_ID`,`MENU_DISPLAY_NAME`,`MENU_HREF`,`MENU_PARENT_ID`,`IS_LEAF`,`ICON_CLASS`) values (3000000,'苏宁',NULL,0,0,'suning');
insert  into `t_sys_menu`(`SYS_MENU_ID`,`MENU_DISPLAY_NAME`,`MENU_HREF`,`MENU_PARENT_ID`,`IS_LEAF`,`ICON_CLASS`) values (3010000,'苏宁运单','../logisticsManager/suning/LOGISTICS.menu',3000000,1,NULL);


/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
