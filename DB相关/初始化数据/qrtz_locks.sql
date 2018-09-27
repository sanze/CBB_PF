/*
SQLyog v10.2 
MySQL - 5.1.62-community : Database - ftsp3
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Data for the table `qrtz_locks` */

insert  into `qrtz_locks`(`LOCK_NAME`) values ('CALENDAR_ACCESS');
insert  into `qrtz_locks`(`LOCK_NAME`) values ('JOB_ACCESS');
insert  into `qrtz_locks`(`LOCK_NAME`) values ('MISFIRE_ACCESS');
insert  into `qrtz_locks`(`LOCK_NAME`) values ('STATE_ACCESS');
insert  into `qrtz_locks`(`LOCK_NAME`) values ('TRIGGER_ACCESS');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
