-- MySQL dump 10.13  Distrib 8.0.20, for Win64 (x86_64)
--
-- Host: i9A301.p.ssafy.io    Database: SERVER
-- ------------------------------------------------------
-- Server version	8.0.34-0ubuntu0.20.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Daily_Schedule`
--

DROP TABLE IF EXISTS `Daily_Schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Daily_Schedule` (
  `dailyId` bigint NOT NULL AUTO_INCREMENT,
  `empId` bigint DEFAULT NULL,
  `startTime` datetime(6) DEFAULT NULL,
  `endTime` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`dailyId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Daily_Schedule`
--

LOCK TABLES `Daily_Schedule` WRITE;
/*!40000 ALTER TABLE `Daily_Schedule` DISABLE KEYS */;
/*!40000 ALTER TABLE `Daily_Schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Department`
--

DROP TABLE IF EXISTS `Department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Department` (
  `teamId` bigint NOT NULL,
  `teamName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`teamId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Department`
--

LOCK TABLES `Department` WRITE;
/*!40000 ALTER TABLE `Department` DISABLE KEYS */;
INSERT INTO `Department` VALUES (1,'D6'),(2,'S3'),(3,'E2');
/*!40000 ALTER TABLE `Department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Desk`
--

DROP TABLE IF EXISTS `Desk`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Desk` (
  `seatId` bigint NOT NULL,
  `empId` bigint DEFAULT NULL,
  `deskHeightNow` bigint DEFAULT NULL,
  `seatIp` varchar(255) DEFAULT NULL,
  `floor` bigint DEFAULT NULL,
  PRIMARY KEY (`seatId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Desk`
--

LOCK TABLES `Desk` WRITE;
/*!40000 ALTER TABLE `Desk` DISABLE KEYS */;
INSERT INTO `Desk` VALUES (201,NULL,25,'106.101.3.139',2),(202,NULL,20,'106.101.0.66',2),(203,NULL,20,'106.101.0.66',2),(204,NULL,20,'106.101.0.66',2),(205,NULL,20,'106.101.0.66',2),(206,NULL,20,'106.101.0.66',2),(207,NULL,20,'106.101.0.66',2),(208,NULL,20,'106.101.0.66',2),(209,NULL,20,'106.101.0.66',2),(210,NULL,20,'106.101.0.66',2),(211,NULL,20,'106.101.0.66',2),(212,NULL,20,'106.101.0.66',2),(213,NULL,20,'106.101.0.66',2),(214,NULL,20,'106.101.0.66',2),(215,NULL,20,'106.101.0.66',2),(216,NULL,20,'106.101.0.66',2),(217,NULL,20,'106.101.0.66',2),(218,NULL,20,'106.101.0.66',2),(219,NULL,20,'106.101.0.66',2),(220,NULL,20,'106.101.0.66',2),(221,NULL,20,'106.101.0.66',2),(222,NULL,20,'106.101.0.66',2),(223,NULL,20,'106.101.0.66',2),(224,NULL,20,'106.101.0.66',2),(225,NULL,20,'106.101.0.66',2),(226,NULL,20,'106.101.0.66',2),(227,NULL,20,'106.101.0.66',2),(228,NULL,20,'106.101.0.66',2),(229,NULL,20,'106.101.0.66',2),(230,NULL,20,'106.101.0.66',2),(301,NULL,19,'223.62.22.175',3),(302,NULL,20,'106.101.0.66',3),(303,NULL,20,'106.101.0.66',3),(304,NULL,20,'106.101.0.66',3),(305,NULL,20,'106.101.0.66',3),(306,NULL,20,'106.101.0.66',3),(307,NULL,20,'106.101.0.66',3),(308,NULL,20,'106.101.0.66',3),(309,NULL,20,'106.101.0.66',3),(310,NULL,20,'106.101.0.66',3),(311,NULL,20,'106.101.0.66',3),(312,NULL,20,'106.101.0.66',3),(313,NULL,20,'106.101.0.66',3),(314,NULL,20,'106.101.0.66',3),(315,NULL,20,'106.101.0.66',3),(316,NULL,20,'106.101.0.66',3),(317,NULL,20,'106.101.0.66',3),(318,NULL,20,'106.101.0.66',3),(319,NULL,20,'106.101.0.66',3),(320,NULL,20,'106.101.0.66',3),(321,NULL,20,'106.101.0.66',3),(322,NULL,20,'106.101.0.66',3),(323,NULL,20,'106.101.0.66',3),(324,NULL,20,'106.101.0.66',3),(325,NULL,20,'106.101.0.66',3),(326,NULL,20,'106.101.0.66',3),(327,NULL,20,'106.101.0.66',3),(328,NULL,20,'106.101.0.66',3),(329,NULL,20,'106.101.0.66',3),(330,NULL,20,'106.101.0.66',3),(401,NULL,20,'106.101.0.66',4),(402,NULL,20,'106.101.0.66',4),(403,NULL,20,'106.101.0.66',4),(404,NULL,20,'106.101.0.66',4),(405,NULL,20,'106.101.0.66',4),(406,NULL,20,'106.101.0.66',4),(407,NULL,20,'106.101.0.66',4),(408,NULL,20,'106.101.0.66',4),(409,NULL,20,'106.101.0.66',4),(410,NULL,20,'106.101.0.66',4),(411,NULL,20,'106.101.0.66',4),(412,NULL,20,'106.101.0.66',4),(413,NULL,20,'106.101.0.66',4),(414,NULL,20,'106.101.0.66',4),(415,NULL,20,'106.101.0.66',4),(416,NULL,20,'106.101.0.66',4),(417,NULL,20,'106.101.0.66',4),(418,NULL,20,'106.101.0.66',4),(419,NULL,20,'106.101.0.66',4),(420,NULL,20,'106.101.0.66',4),(421,NULL,20,'106.101.0.66',4),(422,NULL,20,'106.101.0.66',4),(423,NULL,20,'106.101.0.66',4),(424,NULL,20,'106.101.0.66',4),(425,NULL,20,'106.101.0.66',4),(426,NULL,20,'106.101.0.66',4),(427,NULL,20,'106.101.0.66',4),(428,NULL,20,'106.101.0.66',4),(429,NULL,20,'106.101.0.66',4),(430,NULL,20,'106.101.0.66',4);
/*!40000 ALTER TABLE `Desk` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EMP_Attendance`
--

DROP TABLE IF EXISTS `EMP_Attendance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EMP_Attendance` (
  `empId` bigint NOT NULL,
  `workAttTime` time(6) DEFAULT NULL,
  `workEndTime` time(6) DEFAULT NULL,
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '0-Blank, 1-Present, 2-Absence',
  PRIMARY KEY (`empId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EMP_Attendance`
--

LOCK TABLES `EMP_Attendance` WRITE;
/*!40000 ALTER TABLE `EMP_Attendance` DISABLE KEYS */;
INSERT INTO `EMP_Attendance` VALUES (123,NULL,NULL,0),(124,NULL,NULL,0),(125,NULL,NULL,0),(222,NULL,NULL,0),(357,NULL,NULL,0),(722,NULL,NULL,0);
/*!40000 ALTER TABLE `EMP_Attendance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EMP_Seat`
--

DROP TABLE IF EXISTS `EMP_Seat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EMP_Seat` (
  `empId` bigint NOT NULL,
  `prevSeat` bigint DEFAULT NULL,
  `seatId` bigint DEFAULT NULL,
  `personalDeskHeight` bigint DEFAULT NULL,
  `autoBook` tinyint(1) NOT NULL,
  PRIMARY KEY (`empId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EMP_Seat`
--

LOCK TABLES `EMP_Seat` WRITE;
/*!40000 ALTER TABLE `EMP_Seat` DISABLE KEYS */;
INSERT INTO `EMP_Seat` VALUES (123,201,NULL,23,0),(124,201,NULL,23,1),(125,204,NULL,20,0),(222,211,NULL,20,0),(357,201,NULL,25,1),(722,209,NULL,20,0);
/*!40000 ALTER TABLE `EMP_Seat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Employee`
--

DROP TABLE IF EXISTS `Employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Employee` (
  `empId` bigint NOT NULL,
  `name` varchar(255) NOT NULL,
  `nickname` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `teamId` bigint NOT NULL,
  `empIdCard` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`empId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Employee`
--

LOCK TABLES `Employee` WRITE;
/*!40000 ALTER TABLE `Employee` DISABLE KEYS */;
INSERT INTO `Employee` VALUES (123,'hwayeong','Hwa','1234',3,'549813049021'),(124,'daeyeon','Dae','1234',1,'681737985529'),(125,'junsup','Jun','1234',1,'123123213123'),(222,'jaecheon','HJC','1234',2,'681737985527'),(357,'yeri','Yeri','1234',2,'549813049020'),(722,'dongwoo','Woooo','1234',3,'123123131233');
/*!40000 ALTER TABLE `Employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Schedule`
--

DROP TABLE IF EXISTS `Schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Schedule` (
  `schId` bigint NOT NULL AUTO_INCREMENT,
  `empId` bigint NOT NULL,
  `head` varchar(255) DEFAULT NULL,
  `start` datetime(6) DEFAULT NULL,
  `end` datetime(6) DEFAULT NULL,
  `status` tinyint DEFAULT NULL,
  `detail` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`schId`)
) ENGINE=InnoDB AUTO_INCREMENT=89 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Schedule`
--

LOCK TABLES `Schedule` WRITE;
/*!40000 ALTER TABLE `Schedule` DISABLE KEYS */;
INSERT INTO `Schedule` VALUES (1,357,'Meeting','2023-08-11 17:00:00.000000','2023-08-11 17:30:00.000000',2,'Meeting Time'),(2,357,'ABC','2023-08-11 10:00:00.000000','2023-08-11 13:20:00.000000',2,'ABC'),(4,357,'EEE','2023-08-11 12:20:00.000000','2023-08-11 13:20:00.000000',2,'E'),(5,357,'FFF','2023-08-11 11:00:00.000000','2023-08-11 19:20:00.000000',2,'F'),(7,357,'GGG','2023-08-11 10:00:12.000000','2023-08-11 14:00:12.000000',1,'blabla'),(9,357,'Please!!','2023-08-11 14:00:00.000000','2023-08-11 14:30:00.000000',2,''),(15,357,'COFFEE2','2023-08-11 13:30:00.000000','2023-08-11 15:00:00.000000',2,'LATTEE'),(16,123,'HI','2023-08-12 13:30:00.000000','2023-08-12 13:30:00.000000',2,'hhhiii'),(18,357,'COFFEE2','2023-08-11 13:30:00.000000','2023-08-11 15:00:00.000000',2,'LATTEE'),(19,357,'COFFEE2','2023-08-11 13:30:00.000000','2023-08-11 15:00:00.000000',2,'LATTEE'),(20,357,'COFFEE2','2023-08-14 13:30:00.000000','2023-08-14 15:00:00.000000',2,'LATTEE'),(30,123,'4번회의','2023-08-15 16:30:00.000000','2023-08-15 17:30:00.000000',2,'임베 회의'),(49,124,'ㅌㅎㅌ','2023-08-15 12:00:00.000000','2023-08-15 13:00:00.000000',2,'ㅗ'),(51,123,'너누','2023-08-15 20:39:00.000000','2023-08-15 20:00:00.000000',1,'ㅏ'),(71,123,'일정','2023-08-16 17:16:00.000000','2023-08-16 17:17:00.000000',2,'회의ㅎㅊㅎㅊ'),(72,124,'조퇴','2023-08-16 17:15:00.000000','2023-08-16 17:25:00.000000',2,'집'),(81,124,'회의','2023-08-17 15:15:00.000000','2023-08-17 15:30:00.000000',2,'회의감'),(82,124,'퇴근','2023-08-17 08:05:00.000000','2023-08-17 08:09:00.000000',2,'집'),(83,124,'퇴퇴근','2023-08-17 09:06:00.000000','2023-08-17 09:10:00.000000',2,'집집'),(88,357,'야호','2023-08-17 17:02:00.000000','2023-08-17 17:04:00.000000',2,'ㅇㅎ');
/*!40000 ALTER TABLE `Schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequences`
--

DROP TABLE IF EXISTS `hibernate_sequences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hibernate_sequences` (
  `sequence_name` varchar(255) NOT NULL,
  `next_val` bigint DEFAULT NULL,
  PRIMARY KEY (`sequence_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequences`
--

LOCK TABLES `hibernate_sequences` WRITE;
/*!40000 ALTER TABLE `hibernate_sequences` DISABLE KEYS */;
INSERT INTO `hibernate_sequences` VALUES ('default',1200);
/*!40000 ALTER TABLE `hibernate_sequences` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-08-18  9:46:46
