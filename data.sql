-- MySQL dump 10.13  Distrib 5.7.24, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: goFind
-- ------------------------------------------------------
-- Server version	8.0.37-0ubuntu0.24.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */
;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */
;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */
;
/*!40101 SET NAMES utf8 */
;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */
;
/*!40103 SET TIME_ZONE='+00:00' */
;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */
;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */
;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */
;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */
;

--
-- Table structure for table `DATABASECHANGELOG`
--

/*!40000 ALTER TABLE `jhi_user` DISABLE KEYS */
;

INSERT INTO
    `jhi_user`
VALUES (
        1050,
        'kpihx',
        '$2a$10$QRaqJgwfEDCGppCHOfxfBu57wobcqsc9ayHeMhmn22eArRFLjxwVK',
        NULL,
        NULL,
        'kapoivha@gmail.com',
        NULL,
        1,
        'fr',
        'PrfHSoInvpL55okUR4g8',
        NULL,
        'system',
        '2024-06-08 22:42:22',
        NULL,
        'system',
        '2024-06-08 22:42:22'
    ),
    (
        1051,
        'kpihx-labs',
        '$2a$10$73gCGTedd/olbkJExB9DwOc7sDgrG8.d9WvpwXjLulvTWoisyf4/u',
        NULL,
        NULL,
        'kpihx.labs@gmail.com',
        NULL,
        1,
        'fr',
        NULL,
        NULL,
        'system',
        '2024-06-09 16:41:11',
        NULL,
        'system',
        '2024-06-09 16:41:11'
    );
/*!40000 ALTER TABLE `jhi_user` ENABLE KEYS */
;

--
-- Table structure for table `jhi_user_authority`
--

/*!40000 ALTER TABLE `jhi_user_authority` DISABLE KEYS */
;

INSERT INTO
    `jhi_user_authority`
VALUES (1050, 'ROLE_USER'),
    (1051, 'ROLE_USER');
/*!40000 ALTER TABLE `jhi_user_authority` ENABLE KEYS */
;

INSERT INTO
    `utilisateur`
VALUES (1500, '678936658', 2),
    (1501, '675836168', 1050),
    (1502, '0675836168', 1),
    (1503, '697772316', 1051);
/*!40000 ALTER TABLE `utilisateur` ENABLE KEYS */
;

--
-- Table structure for table `location`
--

/*!40000 ALTER TABLE `maison` DISABLE KEYS */
;

INSERT INTO
    `maison`
VALUES (
        1500,
        'Nkolmesseng',
        'Villa',
        'http://localhost:9000/images/houses/house1/house1.jpeg',
        1502
    ),
    (
        1501,
        'Bastos',
        'Duplex',
        'http://localhost:9000/images/houses/house2/house2.jpeg',
        1502
    );
/*!40000 ALTER TABLE `maison` ENABLE KEYS */
;

--
-- Table structure for table `objet`
--

/*!40000 ALTER TABLE `objet` DISABLE KEYS */
;

INSERT INTO
    `objet`
VALUES (
        1500,
        'Dell Xfire',
        'Laptop gamer',
        'LAPTOP',
        'http://localhost:9000/images/dell.png',
        'laptop001',
        'VOLE',
        1500,
        NULL
    ),
    (
        1501,
        'Iphone 13 Pro MAX',
        'Telephone apple',
        'TELEPHONE',
        'http://localhost:9000/images/iphone.png',
        'telephone000',
        'RETROUVE',
        1500,
        1501
    ),
    (
        1502,
        'Redmi Note 8',
        'Telephone android',
        'TELEPHONE',
        'http://localhost:9000/images/redmi.png',
        'telephone001',
        'VOLE',
        1501,
        NULL
    ),
    (
        1503,
        'HP Pavillon 500',
        'Laptop d\'occasion',
        'LAPTOP',
        'http://localhost:9000/images/hp.png',
        'laptop000',
        'RETROUVE',
        1501,
        1500
    );
/*!40000 ALTER TABLE `objet` ENABLE KEYS */
;

--
-- Table structure for table `piece`
--

-- !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
/*!40000 ALTER TABLE `piece` DISABLE KEYS */
;

INSERT INTO
    `piece`
VALUES (
        1500,
        'Salon',
        'http://localhost:9000/images/houses/house1/living.jpeg',
        'ATTENTE',
        12000,
        1500,
        NULL
    ),
    (
        1501,
        'Chambre',
        'http://localhost:9000/images/houses/house1/bed.jpeg',
        'NONLOUE',
        10000,
        1500,
        NULL
    ),
    (
        1503,
        'Cuisine',
        'http://localhost:9000/images/houses/house2/kitchen.jpeg',
        'NONLOUE',
        8000,
        1501,
        NULL
    ),
    (
        1504,
        'Salon',
        'http://localhost:9000/images/houses/house2/living.jpeg',
        'ATTENTE',
        15000,
        1501,
        NULL
    ),
    (
        1506,
        'Cuisine',
        'http://localhost:9000/images/houses/house1/kitchen.jpeg',
        'NONLOUE',
        9000,
        1500,
        NULL
    );
/*!40000 ALTER TABLE `piece` ENABLE KEYS */
;

--
-- Table structure for table `rel_utilisateur__trajets`
--

INSERT INTO
    `trajet`
VALUES (
        1500,
        'Bafoussam',
        'Yaoundé',
        '2024-06-08 21:00:00.000000',
        3,
        5500,
        1500
    ),
    (
        1502,
        'Douala',
        'Maroua',
        '2024-06-11 11:42:00.000000',
        8,
        1500,
        1501
    );

/*!40000 ALTER TABLE `rel_utilisateur__trajets` DISABLE KEYS */
;

INSERT INTO
    `rel_utilisateur__trajets`
VALUES (1500, 1501),
    (1500, 1503),
    (1502, 1500),
    (1502, 1502),
    (1502, 1503);
/*!40000 ALTER TABLE `rel_utilisateur__trajets` ENABLE KEYS */
;

--
-- Table structure for table `trajet`
--

/*!40000 ALTER TABLE `trajet` DISABLE KEYS */
;

/*!40000 ALTER TABLE `trajet` ENABLE KEYS */
;

--
-- Table structure for table `utilisateur`
--

/*!40000 ALTER TABLE `utilisateur` DISABLE KEYS */
;

--
-- Dumping routines for database 'goFind'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */
;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */
;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */
;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */
;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */
;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */
;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */
;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */
;

-- Dump completed on 2024-06-16 10:54:26