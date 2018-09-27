/*
SQLyog v10.2 
MySQL - 5.6.20-log : Database - cbb_pf
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Data for the table `t_file_location_config` */

insert  into `t_file_location_config`(`FILE_LOCATION_CONFIG_ID`,`CATEGORY`,`GENERAL_XML`,`RECEIPT_XML`,`TRANSFER_XML`,`INPUT_XML`) values (null,1,'sku/general','sku/receipt','sku/transfer','sku/input');
insert  into `t_file_location_config`(`FILE_LOCATION_CONFIG_ID`,`CATEGORY`,`GENERAL_XML`,`RECEIPT_XML`,`TRANSFER_XML`,`INPUT_XML`) values (null,2,'order/general','order/receipt','order/transfer','order/input');
insert  into `t_file_location_config`(`FILE_LOCATION_CONFIG_ID`,`CATEGORY`,`GENERAL_XML`,`RECEIPT_XML`,`TRANSFER_XML`,`INPUT_XML`) values (null,3,'pay/general','pay/receipt','pay/transfer','pay/input');
insert  into `t_file_location_config`(`FILE_LOCATION_CONFIG_ID`,`CATEGORY`,`GENERAL_XML`,`RECEIPT_XML`,`TRANSFER_XML`,`INPUT_XML`) values (null,4,'logistics/general','logistics/receipt','logistics/transfer','logistics/input');
insert  into `t_file_location_config`(`FILE_LOCATION_CONFIG_ID`,`CATEGORY`,`GENERAL_XML`,`RECEIPT_XML`,`TRANSFER_XML`,`INPUT_XML`) values (null,5,'logisticsStatus/general','logisticsStatus/receipt','logisticsStatus/transfer','logisticsStatus/input');
insert  into `t_file_location_config`(`FILE_LOCATION_CONFIG_ID`,`CATEGORY`,`GENERAL_XML`,`RECEIPT_XML`,`TRANSFER_XML`,`INPUT_XML`) values (null,6,'inventory/general','inventory/receipt','inventory/transfer','inventory/input');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
