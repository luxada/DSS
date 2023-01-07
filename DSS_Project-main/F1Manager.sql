-- MariaDB dump 10.19  Distrib 10.6.11-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: F1Manager
-- ------------------------------------------------------
-- Server version	10.6.11-MariaDB-0ubuntu0.22.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Campeonato`
--

DROP TABLE IF EXISTS `Campeonato`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Campeonato` (
  `nomeCamp` varchar(100) NOT NULL,
  PRIMARY KEY (`nomeCamp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Campeonato`
--

LOCK TABLES `Campeonato` WRITE;
/*!40000 ALTER TABLE `Campeonato` DISABLE KEYS */;
/*!40000 ALTER TABLE `Campeonato` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Carro`
--

DROP TABLE IF EXISTS `Carro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Carro` (
  `marca` varchar(50) NOT NULL,
  `modelo` varchar(50) NOT NULL,
  `PAC` decimal(2,1) NOT NULL,
  `potMotorCombs` int(11) NOT NULL,
  `cilindrada` int(11) NOT NULL,
  `potMotorEle` int(11) DEFAULT NULL,
  `tipoCarro` varchar(5) NOT NULL,
  CONSTRAINT `MarcaModelo` PRIMARY KEY (`marca`, `modelo`),
  CONSTRAINT `CheckTipo` CHECK(`tipoCarro` IN ('C1', 'C1H', 'C2', 'C2H', 'GT', 'GTH', 'SC')),
  CONSTRAINT `CheckHibrido` CHECK((`potMotorEle` IS NOT NULL AND `tipoCarro` LIKE '%H%') OR (`potMotorEle` IS NULL AND NOT(`tipoCarro` LIKE '%H%')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Carro`
--

LOCK TABLES `Carro` WRITE;
/*!40000 ALTER TABLE `Carro` DISABLE KEYS */;
/*!40000 ALTER TABLE `Carro` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Circuito`
--

DROP TABLE IF EXISTS `Circuito`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Circuito` (
  `nomeC` varchar(50) NOT NULL,
  `nrVoltas` int(11) NOT NULL,
  `distancia` decimal(5,2) NOT NULL,
  PRIMARY KEY (`nomeC`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Circuito`
--

LOCK TABLES `Circuito` WRITE;
/*!40000 ALTER TABLE `Circuito` DISABLE KEYS */;
/*!40000 ALTER TABLE `Circuito` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Corrida`
--

DROP TABLE IF EXISTS `Corrida`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Corrida` (
  `idCorrida` int(11) NOT NULL,
  `nomeC_fk` varchar(60) NOT NULL,
  `nomeCamp_fk` varchar(100) NOT NULL,
  PRIMARY KEY (`idCorrida`),
  KEY `nomeC_fk` (`nomeC_fk`),
  KEY `nomeCamp_fk` (`nomeCamp_fk`),
  CONSTRAINT `Corrida_ibfk_1` FOREIGN KEY (`nomeC_fk`) REFERENCES `Circuito` (`nomeC`),
  CONSTRAINT `Corrida_ibfk_2` FOREIGN KEY (`nomeCamp_fk`) REFERENCES `Campeonato` (`nomeCamp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Corrida`
--

LOCK TABLES `Corrida` WRITE;
/*!40000 ALTER TABLE `Corrida` DISABLE KEYS */;
/*!40000 ALTER TABLE `Corrida` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ParteCircuito`
--

DROP TABLE IF EXISTS `ParteCircuito`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ParteCircuito` (
  `idParte` int(11) NOT NULL,
  `tipo` varchar(10) NOT NULL,
  `GDU` int(11) NOT NULL,
  `nomeC_fk` varchar(60) NOT NULL,
  PRIMARY KEY (`idParte`),
  KEY `nomeC_fk` (`nomeC_fk`),
  CONSTRAINT `ParteCircuito_ibfk_1` FOREIGN KEY (`nomeC_fk`) REFERENCES `Circuito` (`nomeC`),
  CONSTRAINT `TipoParteCircuito` CHECK(`tipo` IN ('chicane', 'curva', 'reta'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ParteCircuito`
--

LOCK TABLES `ParteCircuito` WRITE;
/*!40000 ALTER TABLE `ParteCircuito` DISABLE KEYS */;
/*!40000 ALTER TABLE `ParteCircuito` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Piloto`
--

DROP TABLE IF EXISTS `Piloto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Piloto` (
  `nome` varchar(100) NOT NULL,
  `SVA` decimal(2,1) NOT NULL,
  `CTS` decimal(2,1) NOT NULL,
  PRIMARY KEY (`nome`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Piloto`
--

LOCK TABLES `Piloto` WRITE;
/*!40000 ALTER TABLE `Piloto` DISABLE KEYS */;
/*!40000 ALTER TABLE `Piloto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Utilizador`
--

DROP TABLE IF EXISTS `Utilizador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Utilizador` (
  `nomeU` varchar(100) NOT NULL,
  `password` varchar(20) NOT NULL,
  `classificacao` int(11) NOT NULL,
  `premium` tinyint(1) NOT NULL,
  `admin` tinyint(1) NOT NULL,
  PRIMARY KEY (`nomeU`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Utilizador`
--

LOCK TABLES `Utilizador` WRITE;
/*!40000 ALTER TABLE `Utilizador` DISABLE KEYS */;
/*!40000 ALTER TABLE `Utilizador` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-12-19 16:00:29
