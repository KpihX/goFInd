-- MySQL dump 10.13  Distrib 5.7.24, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: goFind
-- ------------------------------------------------------
-- Server version	8.0.37-0ubuntu0.24.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `DATABASECHANGELOG`
--

DROP TABLE IF EXISTS `DATABASECHANGELOG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DATABASECHANGELOG` (
  `ID` varchar(255) NOT NULL,
  `AUTHOR` varchar(255) NOT NULL,
  `FILENAME` varchar(255) NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `ORDEREXECUTED` int NOT NULL,
  `EXECTYPE` varchar(10) NOT NULL,
  `MD5SUM` varchar(35) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `COMMENTS` varchar(255) DEFAULT NULL,
  `TAG` varchar(255) DEFAULT NULL,
  `LIQUIBASE` varchar(20) DEFAULT NULL,
  `CONTEXTS` varchar(255) DEFAULT NULL,
  `LABELS` varchar(255) DEFAULT NULL,
  `DEPLOYMENT_ID` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DATABASECHANGELOG`
--

/*!40000 ALTER TABLE `DATABASECHANGELOG` DISABLE KEYS */;
INSERT INTO `DATABASECHANGELOG` VALUES ('00000000000001','jhipster','config/liquibase/changelog/00000000000000_initial_schema.xml','2024-06-09 00:36:15',1,'EXECUTED','9:27550c65ac854b8b465ad94b898a7a20','createTable tableName=jhi_user; createTable tableName=jhi_authority; createTable tableName=jhi_user_authority; addPrimaryKey tableName=jhi_user_authority; addForeignKeyConstraint baseTableName=jhi_user_authority, constraintName=fk_authority_name, ...','',NULL,'4.24.0',NULL,NULL,'7889773508'),('20240523202159-1','jhipster','config/liquibase/changelog/20240523202159_added_entity_Utilisateur.xml','2024-06-09 00:36:16',2,'EXECUTED','9:b3dba3454df6e152224dd363810e3611','createTable tableName=utilisateur','',NULL,'4.24.0',NULL,NULL,'7889773508'),('20240523202159-1-relations','jhipster','config/liquibase/changelog/20240523202159_added_entity_Utilisateur.xml','2024-06-09 00:36:16',3,'EXECUTED','9:f0e9bea273dcef273e733ee7a689b1bc','createTable tableName=rel_utilisateur__trajets; addPrimaryKey tableName=rel_utilisateur__trajets','',NULL,'4.24.0',NULL,NULL,'7889773508'),('20240523202200-1','jhipster','config/liquibase/changelog/20240523202200_added_entity_Objet.xml','2024-06-09 00:36:16',4,'EXECUTED','9:7f6156039b15f904313ab70de8741852','createTable tableName=objet','',NULL,'4.24.0',NULL,NULL,'7889773508'),('20240523202201-1','jhipster','config/liquibase/changelog/20240523202201_added_entity_Trajet.xml','2024-06-09 00:36:16',5,'EXECUTED','9:fbd12c7bb45dabd3727d3447a631ace7','createTable tableName=trajet; dropDefaultValue columnName=date_heure_depart, tableName=trajet','',NULL,'4.24.0',NULL,NULL,'7889773508'),('20240523202202-1','jhipster','config/liquibase/changelog/20240523202202_added_entity_Maison.xml','2024-06-09 00:36:17',6,'EXECUTED','9:318c10c352eac556c95b0c5f8bab7c46','createTable tableName=maison','',NULL,'4.24.0',NULL,NULL,'7889773508'),('20240523202203-1','jhipster','config/liquibase/changelog/20240523202203_added_entity_Piece.xml','2024-06-09 00:36:17',7,'EXECUTED','9:b05f014ccc419ed116b091bc70e5ac57','createTable tableName=piece','',NULL,'4.24.0',NULL,NULL,'7889773508'),('20240523202204-1','jhipster','config/liquibase/changelog/20240523202204_added_entity_Location.xml','2024-06-09 00:36:17',8,'EXECUTED','9:7479fc66ed66c6d63ff33989c688b107','createTable tableName=location','',NULL,'4.24.0',NULL,NULL,'7889773508'),('20240523202159-2','jhipster','config/liquibase/changelog/20240523202159_added_entity_constraints_Utilisateur.xml','2024-06-09 00:36:18',9,'EXECUTED','9:507761c95a5afb39c142e975d903a2ea','addForeignKeyConstraint baseTableName=utilisateur, constraintName=fk_utilisateur__login_id, referencedTableName=jhi_user; addForeignKeyConstraint baseTableName=rel_utilisateur__trajets, constraintName=fk_rel_utilisateur__trajets__utilisateur_id, r...','',NULL,'4.24.0',NULL,NULL,'7889773508'),('20240523202200-2','jhipster','config/liquibase/changelog/20240523202200_added_entity_constraints_Objet.xml','2024-06-09 00:36:18',10,'EXECUTED','9:bf8fee6aefc7cb10291f0e8c3b5bceb0','addForeignKeyConstraint baseTableName=objet, constraintName=fk_objet__proprietaire_id, referencedTableName=utilisateur; addForeignKeyConstraint baseTableName=objet, constraintName=fk_objet__signalant_id, referencedTableName=utilisateur','',NULL,'4.24.0',NULL,NULL,'7889773508'),('20240523202201-2','jhipster','config/liquibase/changelog/20240523202201_added_entity_constraints_Trajet.xml','2024-06-09 00:36:19',11,'EXECUTED','9:955faf05962bbdc04a80f5491bdd5676','addForeignKeyConstraint baseTableName=trajet, constraintName=fk_trajet__proprietaire_id, referencedTableName=utilisateur','',NULL,'4.24.0',NULL,NULL,'7889773508'),('20240523202202-2','jhipster','config/liquibase/changelog/20240523202202_added_entity_constraints_Maison.xml','2024-06-09 00:36:19',12,'EXECUTED','9:6a6ba2723b7dace9dc078ac654af5d7a','addForeignKeyConstraint baseTableName=maison, constraintName=fk_maison__proprietaire_id, referencedTableName=utilisateur','',NULL,'4.24.0',NULL,NULL,'7889773508'),('20240523202203-2','jhipster','config/liquibase/changelog/20240523202203_added_entity_constraints_Piece.xml','2024-06-09 00:36:20',13,'EXECUTED','9:50ab5abbdd5567fb404f11bc53b2f4e8','addForeignKeyConstraint baseTableName=piece, constraintName=fk_piece__maison_id, referencedTableName=maison; addForeignKeyConstraint baseTableName=piece, constraintName=fk_piece__location_id, referencedTableName=location','',NULL,'4.24.0',NULL,NULL,'7889773508'),('20240523202204-2','jhipster','config/liquibase/changelog/20240523202204_added_entity_constraints_Location.xml','2024-06-09 00:36:20',14,'EXECUTED','9:6f4097459b5ab13a96241f9d82c729d0','addForeignKeyConstraint baseTableName=location, constraintName=fk_location__maison_id, referencedTableName=maison','',NULL,'4.24.0',NULL,NULL,'7889773508');
/*!40000 ALTER TABLE `DATABASECHANGELOG` ENABLE KEYS */;

--
-- Table structure for table `DATABASECHANGELOGLOCK`
--

DROP TABLE IF EXISTS `DATABASECHANGELOGLOCK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DATABASECHANGELOGLOCK` (
  `ID` int NOT NULL,
  `LOCKED` tinyint(1) NOT NULL,
  `LOCKGRANTED` datetime DEFAULT NULL,
  `LOCKEDBY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DATABASECHANGELOGLOCK`
--

/*!40000 ALTER TABLE `DATABASECHANGELOGLOCK` DISABLE KEYS */;
INSERT INTO `DATABASECHANGELOGLOCK` VALUES (1,0,NULL,NULL);
/*!40000 ALTER TABLE `DATABASECHANGELOGLOCK` ENABLE KEYS */;

--
-- Table structure for table `jhi_authority`
--

DROP TABLE IF EXISTS `jhi_authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jhi_authority` (
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jhi_authority`
--

/*!40000 ALTER TABLE `jhi_authority` DISABLE KEYS */;
INSERT INTO `jhi_authority` VALUES ('ROLE_ADMIN'),('ROLE_USER');
/*!40000 ALTER TABLE `jhi_authority` ENABLE KEYS */;

--
-- Table structure for table `jhi_user`
--

DROP TABLE IF EXISTS `jhi_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jhi_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `login` varchar(50) NOT NULL,
  `password_hash` varchar(60) NOT NULL,
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `email` varchar(191) DEFAULT NULL,
  `image_url` varchar(256) DEFAULT NULL,
  `activated` tinyint(1) NOT NULL,
  `lang_key` varchar(10) DEFAULT NULL,
  `activation_key` varchar(20) DEFAULT NULL,
  `reset_key` varchar(20) DEFAULT NULL,
  `created_by` varchar(50) NOT NULL,
  `created_date` timestamp NULL,
  `reset_date` timestamp NULL DEFAULT NULL,
  `last_modified_by` varchar(50) DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_user_login` (`login`),
  UNIQUE KEY `ux_user_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=1053 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jhi_user`
--

/*!40000 ALTER TABLE `jhi_user` DISABLE KEYS */;
INSERT INTO `jhi_user` VALUES (1,'admin','$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC','Administrator','Administrator','admin@localhost','',1,'fr',NULL,NULL,'system',NULL,NULL,'system',NULL),(2,'user','$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K','User','User','user@localhost','',1,'fr',NULL,NULL,'system',NULL,NULL,'system',NULL),(1050,'kpihx','$2a$10$QRaqJgwfEDCGppCHOfxfBu57wobcqsc9ayHeMhmn22eArRFLjxwVK',NULL,NULL,'kapoivha@gmail.com',NULL,1,'fr','PrfHSoInvpL55okUR4g8',NULL,'system','2024-06-08 22:42:22',NULL,'system','2024-06-08 22:42:22'),(1051,'kpihx-labs','$2a$10$73gCGTedd/olbkJExB9DwOc7sDgrG8.d9WvpwXjLulvTWoisyf4/u',NULL,NULL,'kpihx.labs@gmail.com',NULL,1,'fr',NULL,NULL,'system','2024-06-09 16:41:11',NULL,'system','2024-06-09 16:41:11');
/*!40000 ALTER TABLE `jhi_user` ENABLE KEYS */;

--
-- Table structure for table `jhi_user_authority`
--

DROP TABLE IF EXISTS `jhi_user_authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jhi_user_authority` (
  `user_id` bigint NOT NULL,
  `authority_name` varchar(50) NOT NULL,
  PRIMARY KEY (`user_id`,`authority_name`),
  KEY `fk_authority_name` (`authority_name`),
  CONSTRAINT `fk_authority_name` FOREIGN KEY (`authority_name`) REFERENCES `jhi_authority` (`name`),
  CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `jhi_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jhi_user_authority`
--

/*!40000 ALTER TABLE `jhi_user_authority` DISABLE KEYS */;
INSERT INTO `jhi_user_authority` VALUES (1,'ROLE_ADMIN'),(1,'ROLE_USER'),(2,'ROLE_USER'),(1050,'ROLE_USER'),(1051,'ROLE_USER');
/*!40000 ALTER TABLE `jhi_user_authority` ENABLE KEYS */;

--
-- Table structure for table `location`
--

DROP TABLE IF EXISTS `location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `location` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `prix` float NOT NULL,
  `maison_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_location__maison_id` (`maison_id`),
  CONSTRAINT `fk_location__maison_id` FOREIGN KEY (`maison_id`) REFERENCES `maison` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1500 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `location`
--

/*!40000 ALTER TABLE `location` DISABLE KEYS */;
/*!40000 ALTER TABLE `location` ENABLE KEYS */;

--
-- Table structure for table `maison`
--

DROP TABLE IF EXISTS `maison`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `maison` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `adresse` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `proprietaire_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_maison__proprietaire_id` (`proprietaire_id`),
  CONSTRAINT `fk_maison__proprietaire_id` FOREIGN KEY (`proprietaire_id`) REFERENCES `utilisateur` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1502 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `maison`
--

/*!40000 ALTER TABLE `maison` DISABLE KEYS */;
INSERT INTO `maison` VALUES (1500,'Nkolmesseng','Villa','http://localhost:9000/images/houses/house1/house1.jpeg',1502),(1501,'Bastos','Duplex','http://localhost:9000/images/houses/house2/house2.jpeg',1502);
/*!40000 ALTER TABLE `maison` ENABLE KEYS */;

--
-- Table structure for table `objet`
--

DROP TABLE IF EXISTS `objet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `objet` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `libelle` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `type` varchar(255) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `identifiant` varchar(255) NOT NULL,
  `etat` varchar(255) NOT NULL,
  `proprietaire_id` bigint DEFAULT NULL,
  `signalant_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_objet__proprietaire_id` (`proprietaire_id`),
  KEY `fk_objet__signalant_id` (`signalant_id`),
  CONSTRAINT `fk_objet__proprietaire_id` FOREIGN KEY (`proprietaire_id`) REFERENCES `utilisateur` (`id`),
  CONSTRAINT `fk_objet__signalant_id` FOREIGN KEY (`signalant_id`) REFERENCES `utilisateur` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1506 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `objet`
--

/*!40000 ALTER TABLE `objet` DISABLE KEYS */;
INSERT INTO `objet` VALUES (1500,'Dell Xfire','Laptop gamer','LAPTOP','http://localhost:9000/images/dell.png','laptop001','VOLE',1500,NULL),(1501,'Iphone 13 Pro MAXX','Telephone apple','TELEPHONE','http://localhost:9000/images/iphone.png','telephone000','RETROUVE',1500,1501),(1502,'Redmi Note 8','Telephone android','TELEPHONE','http://localhost:9000/images/redmi.png','telephone001','VOLE',1501,NULL),(1503,'HP Pavillon 500','Laptop d\'occasion','LAPTOP','http://localhost:9000/images/hp.png','laptop000','RETROUVE',1501,1500);
/*!40000 ALTER TABLE `objet` ENABLE KEYS */;

--
-- Table structure for table `piece`
--

DROP TABLE IF EXISTS `piece`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `piece` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `libelle` varchar(255) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `etat` varchar(255) DEFAULT NULL,
  `maison_id` bigint DEFAULT NULL,
  `location_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_piece__maison_id` (`maison_id`),
  KEY `fk_piece__location_id` (`location_id`),
  CONSTRAINT `fk_piece__location_id` FOREIGN KEY (`location_id`) REFERENCES `location` (`id`),
  CONSTRAINT `fk_piece__maison_id` FOREIGN KEY (`maison_id`) REFERENCES `maison` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1507 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `piece`
--

/*!40000 ALTER TABLE `piece` DISABLE KEYS */;
INSERT INTO `piece` VALUES (1500,'Salon','http://localhost:9000/images/houses/house1/living.jpeg','ATTENTE',1500,NULL),(1501,'Chambre','http://localhost:9000/images/houses/house1/bed.jpeg','NONLOUE',1500,NULL),(1503,'Kitchen','http://localhost:9000/images/houses/house2/kitchen.jpeg','NONLOUE',1501,NULL),(1504,'Salon','http://localhost:9000/images/houses/house2/living.jpeg','ATTENTE',1501,NULL),(1506,'Cuisine','http://localhost:9000/images/houses/house1/kitchen.jpeg','NONLOUE',1500,NULL);
/*!40000 ALTER TABLE `piece` ENABLE KEYS */;

--
-- Table structure for table `rel_utilisateur__trajets`
--

DROP TABLE IF EXISTS `rel_utilisateur__trajets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rel_utilisateur__trajets` (
  `trajets_id` bigint NOT NULL,
  `utilisateur_id` bigint NOT NULL,
  PRIMARY KEY (`utilisateur_id`,`trajets_id`),
  KEY `fk_rel_utilisateur__trajets__trajets_id` (`trajets_id`),
  CONSTRAINT `fk_rel_utilisateur__trajets__trajets_id` FOREIGN KEY (`trajets_id`) REFERENCES `trajet` (`id`),
  CONSTRAINT `fk_rel_utilisateur__trajets__utilisateur_id` FOREIGN KEY (`utilisateur_id`) REFERENCES `utilisateur` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rel_utilisateur__trajets`
--

/*!40000 ALTER TABLE `rel_utilisateur__trajets` DISABLE KEYS */;
INSERT INTO `rel_utilisateur__trajets` VALUES (1500,1501),(1500,1503),(1502,1500),(1502,1502),(1502,1503);
/*!40000 ALTER TABLE `rel_utilisateur__trajets` ENABLE KEYS */;

--
-- Table structure for table `trajet`
--

DROP TABLE IF EXISTS `trajet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `trajet` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `depart` varchar(255) NOT NULL,
  `arrivee` varchar(255) NOT NULL,
  `date_heure_depart` datetime(6) NOT NULL,
  `places` int NOT NULL,
  `prix` float NOT NULL,
  `proprietaire_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_trajet__proprietaire_id` (`proprietaire_id`),
  CONSTRAINT `fk_trajet__proprietaire_id` FOREIGN KEY (`proprietaire_id`) REFERENCES `utilisateur` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1503 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trajet`
--

/*!40000 ALTER TABLE `trajet` DISABLE KEYS */;
INSERT INTO `trajet` VALUES (1500,'Bafoussam','Yaoundé','2024-06-08 21:00:00.000000',3,5500,1500),(1502,'Douala','Maroua','2024-06-11 11:42:00.000000',8,1500,1501);
/*!40000 ALTER TABLE `trajet` ENABLE KEYS */;

--
-- Table structure for table `utilisateur`
--

DROP TABLE IF EXISTS `utilisateur`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `utilisateur` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `telephone` varchar(255) NOT NULL,
  `login_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_utilisateur__login_id` (`login_id`),
  CONSTRAINT `fk_utilisateur__login_id` FOREIGN KEY (`login_id`) REFERENCES `jhi_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1505 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utilisateur`
--

/*!40000 ALTER TABLE `utilisateur` DISABLE KEYS */;
INSERT INTO `utilisateur` VALUES (1500,'678936658',2),(1501,'675836168',1050),(1502,'0675836168',1),(1503,'697772316',1051);
/*!40000 ALTER TABLE `utilisateur` ENABLE KEYS */;

--
-- Dumping routines for database 'goFind'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-06-16 10:54:26
