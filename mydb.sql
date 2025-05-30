-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: mydb
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `componenttype`
--

DROP TABLE IF EXISTS `componenttype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `componenttype` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Type` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `componenttype`
--

LOCK TABLES `componenttype` WRITE;
/*!40000 ALTER TABLE `componenttype` DISABLE KEYS */;
INSERT INTO `componenttype` VALUES (1,'Motherboard'),(2,'CPU'),(3,'GPU'),(4,'RAM'),(5,'Storage'),(6,'Power Supply'),(7,'Cooling System'),(8,'Case'),(9,'Optical Drive'),(10,'Sound Card'),(11,'Network Card'),(12,'Monitor'),(13,'Keyboard'),(14,'Mouse'),(15,'Speakers'),(16,'Webcam'),(17,'Microphone'),(18,'External Storage'),(19,'Audio Interface'),(20,'UPS (Uninterruptible Power Supply)'),(21,'Fan'),(22,'Heat Sink'),(23,'Wireless Card'),(24,'TV Tuner Card');
/*!40000 ALTER TABLE `componenttype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `Customer ID` int NOT NULL AUTO_INCREMENT,
  `Full Name` varchar(255) DEFAULT NULL,
  `PhoneNo` varchar(30) DEFAULT NULL,
  `Email` varchar(255) DEFAULT NULL,
  `Address` varchar(255) DEFAULT NULL,
  `City` varchar(255) DEFAULT NULL,
  `Province` varchar(255) DEFAULT NULL,
  `Note` mediumtext,
  PRIMARY KEY (`Customer ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (1,'Ralph Geoffrey S. Marcos','09479557505','sanjuanrj40@gmail.com','312 Villaluz Subd.','Marilao','Bulacan','');
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employees`
--

DROP TABLE IF EXISTS `employees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employees` (
  `employee_id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `phone_number` varchar(15) DEFAULT NULL,
  `hourly_pay` decimal(5,2) DEFAULT NULL,
  `hire_date` date DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`employee_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employees`
--

LOCK TABLES `employees` WRITE;
/*!40000 ALTER TABLE `employees` DISABLE KEYS */;
INSERT INTO `employees` VALUES (1,'James','Yap','094356784568',25.40,'2023-01-02','12345678'),(2,'Jorick','Abundo','09443257646',9.11,'2022-03-04','Stelle'),(3,'Ralph Geoffrey','Marcos','09479557505',4.20,'2020-05-01','Cryptic'),(4,'Hannel Felix','Culdora','092341643743',6.90,'2025-02-04','Catmosphere');
/*!40000 ALTER TABLE `employees` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `low_stock_alert`
--

DROP TABLE IF EXISTS `low_stock_alert`;
/*!50001 DROP VIEW IF EXISTS `low_stock_alert`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `low_stock_alert` AS SELECT 
 1 AS `Part ID`,
 1 AS `Part Name`,
 1 AS `Model`,
 1 AS `Stock`,
 1 AS `SafetyStock`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `Order ID` int NOT NULL AUTO_INCREMENT,
  `Customer ID` int DEFAULT NULL,
  `Date of Order` datetime DEFAULT NULL,
  `Revenue` double DEFAULT NULL,
  `Item Purchased` mediumtext,
  `Queue Order` bit(1) DEFAULT NULL,
  PRIMARY KEY (`Order ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,1,'2025-05-12 00:00:00',3510,'102,68,37,4,5,6,7,8,14,12,21,13,20,17,15,16',_binary '');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `partlist`
--

DROP TABLE IF EXISTS `partlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `partlist` (
  `Part ID` int NOT NULL AUTO_INCREMENT,
  `Part Name` varchar(255) DEFAULT NULL,
  `Model` varchar(255) DEFAULT NULL,
  `Type` int DEFAULT NULL,
  `Stock` varchar(255) DEFAULT NULL,
  `Price` double DEFAULT NULL,
  `Restock?` bit(1) DEFAULT NULL,
  `SafetyStock` int DEFAULT '10',
  PRIMARY KEY (`Part ID`),
  KEY `fk_type` (`Type`),
  CONSTRAINT `fk_type` FOREIGN KEY (`Type`) REFERENCES `componenttype` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=115 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `partlist`
--

LOCK TABLES `partlist` WRITE;
/*!40000 ALTER TABLE `partlist` DISABLE KEYS */;
INSERT INTO `partlist` VALUES (1,'ASUS Z390','Z390-A PRO',1,'49',200,_binary '\0',10),(2,'Intel i7-9700K','i7-9700K',2,'29',350,_binary '\0',10),(3,'NVIDIA RTX 3080','RTX 3080',3,'0',700,_binary '',10),(4,'Corsair Vengeance 16GB','Vengeance LPX',4,'96',80,_binary '\0',10),(5,'Samsung 970 EVO 1TB SSD','970 EVO',5,'196',120,_binary '\0',10),(6,'Corsair RM850x','RM850x',6,'46',130,_binary '\0',10),(7,'Cooler Master Hyper 212','Hyper 212',7,'116',40,_binary '\0',10),(8,'NZXT H510','H510',8,'71',70,_binary '\0',10),(9,'LG 24X DVD Writer','GH24NSD1',9,'20',20,_binary '\0',10),(10,'Creative Sound Blaster Z','Sound Blaster Z',10,'10',100,_binary '\0',10),(11,'TP-Link Archer T9E','Archer T9E',11,'60',70,_binary '\0',10),(12,'Dell U2720Q','U2720Q',12,'14',600,_binary '\0',10),(13,'Logitech G Pro','G Pro',13,'149',100,_binary '\0',10),(14,'Logitech G502','G502',14,'199',50,_binary '\0',10),(15,'Bose Companion 2','Companion 2',15,'29',80,_binary '\0',10),(16,'Logitech C920','C920',16,'199',50,_binary '\0',10),(17,'Blue Yeti','Yeti',17,'49',130,_binary '\0',10),(18,'Seagate Backup Plus 2TB','Backup Plus',18,'150',100,_binary '\0',10),(19,'Focusrite Scarlett 2i2','Scarlett 2i2',19,'80',170,_binary '\0',10),(20,'APC Back-UPS','Back-UPS',20,'24',150,_binary '\0',10),(21,'Noctua NF-A14','NF-A14',21,'89',30,_binary '\0',10),(22,'Thermalright Silver Arrow','Silver Arrow',22,'40',90,_binary '\0',10),(23,'Intel AC 9560','AC 9560',23,'120',20,_binary '\0',10),(24,'Hauppauge WinTV','WinTV',24,'10',80,_binary '\0',10),(25,'NVIDIA RTX 3060','RTX 3060',3,'150',450,_binary '\0',10),(26,'NVIDIA RTX 3070','RTX 3070',3,'120',700,_binary '\0',10),(27,'NVIDIA RTX 3080','RTX 3080',3,'100',900,_binary '\0',10),(28,'AMD Radeon RX 6600 XT','RX 6600 XT',3,'180',400,_binary '\0',10),(29,'NVIDIA RTX 3090','RTX 3090',3,'50',1500,_binary '\0',10),(30,'NVIDIA GTX 1660 Ti','GTX 1660 Ti',3,'249',250,_binary '\0',10),(31,'AMD Radeon RX 6800','RX 6800',3,'80',700,_binary '\0',10),(32,'NVIDIA RTX 3050','RTX 3050',3,'200',350,_binary '\0',10),(33,'AMD Radeon RX 6700 XT','RX 6700 XT',3,'130',600,_binary '\0',10),(34,'NVIDIA GTX 1650 Super','GTX 1650 Super',3,'200',200,_binary '\0',10),(35,'NVIDIA RTX 4070','RTX 4070',3,'50',1200,_binary '\0',10),(36,'AMD Radeon RX 6900 XT','RX 6900 XT',3,'40',1100,_binary '\0',10),(37,'NVIDIA RTX 4080','RTX 4080',3,'27',1500,_binary '\0',10),(38,'NVIDIA GTX 1050 Ti','GTX 1050 Ti',3,'220',150,_binary '\0',10),(39,'NVIDIA RTX 4070 Ti','RTX 4070 Ti',3,'25',1300,_binary '\0',10),(40,'AMD Radeon RX 5700 XT','RX 5700 XT',3,'180',400,_binary '\0',10),(41,'NVIDIA RTX 2060','RTX 2060',3,'200',350,_binary '\0',10),(42,'NVIDIA GTX 1650','GTX 1650',3,'250',180,_binary '\0',10),(43,'AMD Radeon RX 5600 XT','RX 5600 XT',3,'160',300,_binary '\0',10),(44,'NVIDIA RTX 2080 Ti','RTX 2080 Ti',3,'30',1200,_binary '\0',10),(45,'AMD Radeon RX 6500 XT','RX 6500 XT',3,'220',250,_binary '\0',10),(46,'NVIDIA RTX 3060 Ti','RTX 3060 Ti',3,'100',600,_binary '\0',10),(47,'NVIDIA RTX 2070 Super','RTX 2070 Super',3,'50',800,_binary '\0',10),(48,'AMD Radeon RX 570','RX 570',3,'180',180,_binary '\0',10),(49,'NVIDIA RTX 3090 Ti','RTX 3090 Ti',3,'20',2000,_binary '\0',10),(50,'AMD Radeon RX Vega 64','RX Vega 64',3,'40',600,_binary '\0',10),(51,'NVIDIA GTX 1070 Ti','GTX 1070 Ti',3,'60',500,_binary '\0',10),(52,'NVIDIA RTX 2080','RTX 2080',3,'80',900,_binary '\0',10),(53,'NVIDIA RTX 3060 12GB','RTX 3060 12GB',3,'150',500,_binary '\0',10),(54,'NVIDIA GTX 1080 Ti','GTX 1080 Ti',3,'70',750,_binary '\0',10),(55,'MSI B450 TOMAHAWK','B450 TOMAHAWK',1,'154',120,_binary '',10),(56,'Gigabyte Z490 AORUS','Z490 AORUS',1,'103',220,_binary '\0',10),(57,'ASRock X570 Steel Legend','X570 Steel Legend',1,'120',250,_binary '',10),(58,'ASUS ROG Strix Z590-E','Z590-E',1,'80',300,_binary '\0',10),(59,'MSI MAG B550 TOMAHAWK','B550 TOMAHAWK',1,'90',180,_binary '',10),(60,'Gigabyte Z590 AORUS','Z590 AORUS',1,'110',260,_binary '\0',10),(61,'ASRock Z490 Taichi','Z490 Taichi',1,'95',280,_binary '',10),(62,'MSI MEG Z490 GODLIKE','Z490 GODLIKE',1,'30',450,_binary '',10),(63,'ASUS TUF Gaming B550-PLUS','B550-PLUS',1,'100',160,_binary '\0',10),(64,'Gigabyte X570 AORUS ELITE','X570 AORUS ELITE',1,'75',230,_binary '',10),(65,'ASRock B550M Steel Legend','B550M Steel Legend',1,'110',140,_binary '\0',10),(66,'MSI MPG Z490 GAMING EDGE','Z490 GAMING EDGE',1,'50',250,_binary '',10),(67,'ASUS Prime Z490-A','Z490-A',1,'120',230,_binary '\0',10),(68,'Gigabyte B550M DS3H','B550M DS3H',1,'147',110,_binary '',10),(69,'ASRock Z390 Phantom Gaming','Z390 Phantom Gaming',1,'80',200,_binary '\0',10),(70,'MSI B450 GAMING PRO','B450 GAMING PRO',1,'130',120,_binary '',10),(71,'ASUS Z490-PRO','Z490-PRO',1,'70',220,_binary '\0',10),(72,'Gigabyte Z370 AORUS','Z370 AORUS',1,'95',180,_binary '',10),(73,'ASRock B365M PRO4','B365M PRO4',1,'160',100,_binary '\0',10),(74,'MSI H310M PRO','H310M PRO',1,'200',85,_binary '',10),(75,'ASUS TUF Z590-PLUS','Z590-PLUS',1,'65',250,_binary '\0',10),(76,'Gigabyte Z590 AORUS MASTER','Z590 AORUS MASTER',1,'40',380,_binary '',10),(77,'ASRock Z590 Steel Legend','Z590 Steel Legend',1,'90',300,_binary '\0',10),(78,'MSI MPG Z490 GAMING PLUS','Z490 GAMING PLUS',1,'100',190,_binary '',10),(79,'ASUS ROG Strix B550-F','B550-F',1,'75',220,_binary '\0',10),(80,'Gigabyte X470 AORUS','X470 AORUS',1,'85',180,_binary '',10),(81,'ASRock B550M Pro4','B550M Pro4',1,'130',160,_binary '\0',10),(82,'MSI Z490-A PRO','Z490-A PRO',1,'120',210,_binary '',10),(83,'Intel Core i5-11600K','i5-11600K',2,'180',250,_binary '\0',10),(84,'Intel Core i7-11700K','i7-11700K',2,'160',350,_binary '',10),(85,'Intel Core i9-11900K','i9-11900K',2,'90',450,_binary '\0',10),(86,'AMD Ryzen 5 5600X','5600X',2,'200',300,_binary '',10),(87,'AMD Ryzen 7 5800X','5800X',2,'150',450,_binary '\0',10),(88,'AMD Ryzen 9 5900X','5900X',2,'80',750,_binary '',10),(89,'Intel Core i3-10100F','i3-10100F',2,'300',100,_binary '\0',10),(90,'Intel Core i5-10400F','i5-10400F',2,'250',150,_binary '',10),(91,'AMD Ryzen 3 3100','3100',2,'300',120,_binary '\0',10),(92,'Intel Core i7-10700K','i7-10700K',2,'130',400,_binary '',10),(93,'Intel Core i5-10600K','i5-10600K',2,'180',260,_binary '\0',10),(94,'AMD Ryzen 5 3600','3600',2,'220',200,_binary '',10),(95,'AMD Ryzen 7 3700X','3700X',2,'170',320,_binary '\0',10),(96,'Intel Core i9-10900K','i9-10900K',2,'60',550,_binary '',10),(97,'Intel Core i5-9600K','i5-9600K',2,'150',250,_binary '\0',10),(98,'AMD Ryzen 9 3950X','3950X',2,'50',750,_binary '',10),(99,'Intel Core i7-9700K','i7-9700K',2,'140',350,_binary '\0',10),(100,'AMD Ryzen 5 3400G','3400G',2,'200',190,_binary '',10),(101,'Intel Core i9-10980XE','i9-10980XE',2,'40',1000,_binary '',10),(102,'AMD Ryzen 5 5600G','5600G',2,'87',270,_binary '\0',10),(103,'Intel Core i5-12400F','i5-12400F',2,'220',300,_binary '',10),(104,'AMD Ryzen 9 7950X','7950X',2,'30',950,_binary '\0',10),(105,'Intel Core i7-12700K','i7-12700K',2,'100',500,_binary '',10),(106,'AMD Ryzen 5 5800G','5800G',2,'49',400,_binary '\0',10),(107,'Intel Core i5-13600K','i5-13600K',2,'150',450,_binary '',10),(108,'AMD Ryzen 7 7700X','7700X',2,'90',600,_binary '\0',10),(109,'Intel Core i9-13900K','i9-13900K',2,'20',750,_binary '',10),(110,'AMD Ryzen 5 7600X','7600X',2,'120',350,_binary '\0',10),(111,'Intel Core i7-13700K','i7-13700K',2,'80',600,_binary '',10),(114,'Maono PD300X','PD300X',17,'0',54,_binary '\0',10);
/*!40000 ALTER TABLE `partlist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Final view structure for view `low_stock_alert`
--

/*!50001 DROP VIEW IF EXISTS `low_stock_alert`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_unicode_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `low_stock_alert` AS select `p`.`Part ID` AS `Part ID`,`p`.`Part Name` AS `Part Name`,`p`.`Model` AS `Model`,`p`.`Stock` AS `Stock`,`p`.`SafetyStock` AS `SafetyStock` from `partlist` `p` where (`p`.`Stock` < `p`.`SafetyStock`) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-12 19:37:25
