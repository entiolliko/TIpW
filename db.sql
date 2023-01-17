-- MySQL dump 10.13  Distrib 8.0.29, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: TIW_Progetto
-- ------------------------------------------------------
-- Server version	8.0.29-0ubuntu0.20.04.3

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
-- Table structure for table `CLIENT`
--

DROP TABLE IF EXISTS `CLIENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CLIENT` (
  `Username` varchar(45) NOT NULL,
  `Password` varchar(45) NOT NULL,
  PRIMARY KEY (`Username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENT`
--

LOCK TABLES `CLIENT` WRITE;
/*!40000 ALTER TABLE `CLIENT` DISABLE KEYS */;
INSERT INTO `CLIENT` VALUES ('clientUsername1','password1'),('clientUsername10','password10'),('clientUsername2','password2'),('clientUsername3','password3'),('clientUsername4','password4'),('clientUsername5','password1'),('clientUsername6','password5'),('clientUsername7','password8'),('clientUsername8','password8'),('clientUsername9','password9'),('username1','passpass22'),('usernameClientTest','password1');
/*!40000 ALTER TABLE `CLIENT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EMPLOYEE`
--

DROP TABLE IF EXISTS `EMPLOYEE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EMPLOYEE` (
  `Username` varchar(45) NOT NULL,
  `Password` varchar(45) NOT NULL,
  PRIMARY KEY (`Username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EMPLOYEE`
--

LOCK TABLES `EMPLOYEE` WRITE;
/*!40000 ALTER TABLE `EMPLOYEE` DISABLE KEYS */;
INSERT INTO `EMPLOYEE` VALUES ('employeeUsername1','password1'),('employeeUsername10','password10'),('employeeUsername2','password2'),('employeeUsername3','password3'),('employeeUsername4','password4'),('employeeUsername5','password5'),('employeeUsername6','password6'),('employeeUsername7','password7'),('employeeUsername8','password8'),('employeeUsername9','password9'),('entiolUsername','ciaociao'),('usernameEmployeeTest','password1');
/*!40000 ALTER TABLE `EMPLOYEE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `OPTION`
--

DROP TABLE IF EXISTS `OPTION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `OPTION` (
  `Code` varchar(45) NOT NULL,
  `ProductCode` varchar(45) NOT NULL,
  `Type` enum('Normale','In_Offerta') NOT NULL,
  `Name` varchar(45) NOT NULL,
  PRIMARY KEY (`Code`),
  KEY `Code_idx` (`ProductCode`),
  CONSTRAINT `ProductForeignKey` FOREIGN KEY (`ProductCode`) REFERENCES `PRODUCT` (`Code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `OPTION`
--

LOCK TABLES `OPTION` WRITE;
/*!40000 ALTER TABLE `OPTION` DISABLE KEYS */;
INSERT INTO `OPTION` VALUES ('optionCode1','productCode1','In_Offerta','optionName1'),('optionCode10','productCode10','Normale','optionName10'),('optionCode11','productCode1','In_Offerta','optionName11'),('optionCode12','productCode2','Normale','optionName12'),('optionCode13','productCode3','In_Offerta','optionName13'),('optionCode14','productCode4','Normale','optionName14'),('optionCode15','productCode5','Normale','optionName15'),('optionCode16','productCode6','In_Offerta','optionName16'),('optionCode17','productCode7','In_Offerta','optionName17'),('optionCode18','productCode8','Normale','optionName18'),('optionCode19','productCode9','In_Offerta','optionName19'),('optionCode2','productCode2','Normale','optionName2'),('optionCode20','productCode10','Normale','optionName20'),('optionCode22','productCode2','Normale','optionName22'),('optionCode23','productCode3','Normale','optionName23'),('optionCode3','productCode3','Normale','optionName3'),('optionCode32','productCode2','In_Offerta','optionName32'),('optionCode4','productCode4','Normale','optionName4'),('optionCode5','productCode5','In_Offerta','optionName5'),('optionCode6','productCode6','In_Offerta','optionName6'),('optionCode7','productCode7','Normale','optionName7'),('optionCode8','productCode8','In_Offerta','optionName8'),('optionCode9','productCode9','Normale','optionName9');
/*!40000 ALTER TABLE `OPTION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `OPTIONSINPREVENTIVE`
--

DROP TABLE IF EXISTS `OPTIONSINPREVENTIVE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `OPTIONSINPREVENTIVE` (
  `IDPreventive` varchar(45) NOT NULL,
  `OptionCode` varchar(45) NOT NULL,
  PRIMARY KEY (`IDPreventive`,`OptionCode`),
  KEY `OptionForeignKey_idx` (`OptionCode`),
  CONSTRAINT `OptionForeignKey` FOREIGN KEY (`OptionCode`) REFERENCES `OPTION` (`Code`),
  CONSTRAINT `PreventiveForeignKey` FOREIGN KEY (`IDPreventive`) REFERENCES `PREVENTIVE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `OPTIONSINPREVENTIVE`
--

LOCK TABLES `OPTIONSINPREVENTIVE` WRITE;
/*!40000 ALTER TABLE `OPTIONSINPREVENTIVE` DISABLE KEYS */;
INSERT INTO `OPTIONSINPREVENTIVE` VALUES ('hpcsmunpoyydylc','optionCode1'),('lhymjmojwbjhcta','optionCode1'),('preventiveID5','optionCode10'),('preventiveID8','optionCode10'),('atoupvqtcpkfnkw','optionCode11'),('focneupuudvlvpn','optionCode11'),('hpcsmunpoyydylc','optionCode11'),('sdijnnjuvziomzg','optionCode11'),('preventiveID2','optionCode12'),('preventiveID3','optionCode12'),('preventiveID4','optionCode12'),('preventiveID7','optionCode12'),('preventiveID11','optionCode13'),('preventiveID20','optionCode13'),('yggpqgyrgumzehc','optionCode13'),('preventiveID10','optionCode14'),('preventiveID15','optionCode14'),('lmfzpgbrbjskbyf','optionCode15'),('mvpekwqeldijnfu','optionCode15'),('preventiveID14','optionCode15'),('hawswuwsbuslgvl','optionCode16'),('sylmcpilddyhqqi','optionCode16'),('preventiveID1','optionCode17'),('preventiveID12','optionCode17'),('preventiveID19','optionCode17'),('preventiveID17','optionCode18'),('preventiveID9','optionCode19'),('preventiveID16','optionCode2'),('preventiveID2','optionCode2'),('preventiveID4','optionCode2'),('evxwwfmhtwkgjkr','optionCode20'),('preventiveID8','optionCode20'),('qzslqiiwvpyyztm','optionCode20'),('jjnimvsffscqtid','optionCode22'),('preventiveID2','optionCode22'),('nnukhylobdfkhqk','optionCode23'),('preventiveID3','optionCode23'),('putptjqogpjcfyp','optionCode23'),('yggpqgyrgumzehc','optionCode23'),('nnukhylobdfkhqk','optionCode3'),('jjnimvsffscqtid','optionCode32'),('preventiveID2','optionCode32'),('jbaxnlvuglcgcnv','optionCode4'),('drdwecgfobhizxq','optionCode5'),('mvpekwqeldijnfu','optionCode5'),('hawswuwsbuslgvl','optionCode6'),('preventiveID18','optionCode6'),('sylmcpilddyhqqi','optionCode6'),('preventiveID1','optionCode7'),('preventiveID13','optionCode7'),('vgkpnvwueufnvjf','optionCode7'),('preventiveID17','optionCode8'),('vzkokddcbsniwcp','optionCode8'),('preventiveID6','optionCode9'),('preventiveID9','optionCode9');
/*!40000 ALTER TABLE `OPTIONSINPREVENTIVE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PREVENTIVE`
--

DROP TABLE IF EXISTS `PREVENTIVE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PREVENTIVE` (
  `ID` varchar(45) NOT NULL,
  `ClientUsername` varchar(45) NOT NULL,
  `ProductCode` varchar(45) NOT NULL,
  `EmployeeUsername` varchar(45) DEFAULT NULL,
  `Price` float DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `fk_PREVENTIVE_1_idx1` (`EmployeeUsername`),
  KEY `ClientForeignKey_idx` (`ClientUsername`),
  KEY `ProductForeignKey_idx` (`ProductCode`),
  CONSTRAINT `fk_PREVENTIVE_1` FOREIGN KEY (`ClientUsername`) REFERENCES `CLIENT` (`Username`),
  CONSTRAINT `fk_PREVENTIVE_2` FOREIGN KEY (`EmployeeUsername`) REFERENCES `EMPLOYEE` (`Username`),
  CONSTRAINT `fk_PREVENTIVE_3` FOREIGN KEY (`ProductCode`) REFERENCES `PRODUCT` (`Code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PREVENTIVE`
--

LOCK TABLES `PREVENTIVE` WRITE;
/*!40000 ALTER TABLE `PREVENTIVE` DISABLE KEYS */;
INSERT INTO `PREVENTIVE` VALUES ('atoupvqtcpkfnkw','clientUsername1','productCode1','employeeUsername1',100000000),('bbwrryzmwoxhqwg','clientUsername1','productCode1','usernameEmployeeTest',123),('drdwecgfobhizxq','clientUsername1','productCode5','employeeUsername1',15632),('evxwwfmhtwkgjkr','clientUsername1','productCode10','employeeUsername1',999),('focneupuudvlvpn','clientUsername1','productCode1',NULL,0),('hawswuwsbuslgvl','clientUsername1','productCode6','employeeUsername1',12365.2),('hpcsmunpoyydylc','clientUsername1','productCode1',NULL,0),('idjjmhqwwlckqog','clientUsername1','productCode9',NULL,0),('jbaxnlvuglcgcnv','usernameClientTest','productCode4','employeeUsername1',10000000),('jjnimvsffscqtid','clientUsername1','productCode2','employeeUsername1',123),('lhymjmojwbjhcta','clientUsername1','productCode1',NULL,0),('lmfzpgbrbjskbyf','clientUsername1','productCode5','employeeUsername1',123.32),('mvpekwqeldijnfu','clientUsername1','productCode5',NULL,0),('nnukhylobdfkhqk','username1','productCode3','employeeUsername1',123),('preventiveID1','clientUsername1','productCode7','employeeUsername1',152.21),('preventiveID10','clientUsername2','productCode4','employeeUsername10',182),('preventiveID11','clientUsername2','productCode3','employeeUsername1',123.54),('preventiveID12','clientUsername8','productCode7','employeeUsername1',123),('preventiveID13','clientUsername8','productCode7','employeeUsername1',521.32),('preventiveID14','clientUsername1','productCode5','employeeUsername8',512.2),('preventiveID15','clientUsername1','productCode4','employeeUsername1',123.32),('preventiveID16','clientUsername1','productCode2','employeeUsername7',896.21),('preventiveID17','clientUsername3','productCode8','employeeUsername9',563.2),('preventiveID18','clientUsername3','productCode6','employeeUsername1',15632.2),('preventiveID19','clientUsername4','productCode7','employeeUsername8',123.45),('preventiveID2','clientUsername4','productCode2','employeeUsername2',632.2),('preventiveID20','clientUsername6','productCode3','employeeUsername1',123),('preventiveID3','clientUsername3','productCode2','employeeUsername3',152.1),('preventiveID4','clientUsername2','productCode2','employeeUsername1',123457000),('preventiveID5','clientUsername8','productCode10',NULL,0),('preventiveID6','clientUsername1','productCode9','employeeUsername1',56.4),('preventiveID7','clientUsername8','productCode2','employeeUsername1',123.123),('preventiveID8','clientUsername8','productCode10','employeeUsername1',89.5),('preventiveID9','clientUsername5','productCode9','entiolUsername',123),('putptjqogpjcfyp','clientUsername1','productCode3',NULL,0),('qzslqiiwvpyyztm','clientUsername1','productCode10',NULL,0),('sdijnnjuvziomzg','clientUsername1','productCode1',NULL,0),('seysjpyigneqlyq','clientUsername1','productCode1',NULL,0),('sylmcpilddyhqqi','clientUsername1','productCode6',NULL,0),('tqndsmtwcwbbtnu','clientUsername1','productCode6','employeeUsername1',123),('vgkpnvwueufnvjf','usernameClientTest','productCode7',NULL,0),('vzkokddcbsniwcp','clientUsername1','productCode8',NULL,0),('yggpqgyrgumzehc','clientUsername1','productCode3','employeeUsername1',1);
/*!40000 ALTER TABLE `PREVENTIVE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PRODUCT`
--

DROP TABLE IF EXISTS `PRODUCT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PRODUCT` (
  `Code` varchar(45) NOT NULL,
  `Name` varchar(45) NOT NULL,
  `ImgPath` varchar(255) NOT NULL,
  PRIMARY KEY (`Code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PRODUCT`
--

LOCK TABLES `PRODUCT` WRITE;
/*!40000 ALTER TABLE `PRODUCT` DISABLE KEYS */;
INSERT INTO `PRODUCT` VALUES ('productCode1','Alfetta','/TIW_-_Progetto_HTML/resources/alfetta.webp'),('productCode10','Audi','/TIW_-_Progetto_HTML/resources/audi.webp'),('productCode2','BMW','/TIW_-_Progetto_HTML/resources/bmw.webp'),('productCode3','Buick','/TIW_-_Progetto_HTML/resources/buick.webp'),('productCode4','Fiat','/TIW_-_Progetto_HTML/resources/fiat.webp'),('productCode5','Ford','/TIW_-_Progetto_HTML/resources/ford.webp'),('productCode6','Honda','/TIW_-_Progetto_HTML/resources/honda.webp'),('productCode7','Hyundai','/TIW_-_Progetto_HTML/resources/hyundai.webp'),('productCode8','Mercedec','/TIW_-_Progetto_HTML/resources/mercedez.webp'),('productCode9','Volkswagen','/TIW_-_Progetto_HTML/resources/volkswagen.webp');
/*!40000 ALTER TABLE `PRODUCT` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-06-19  0:02:37
