--
-- Database Schema:  uatTest
-- Automatically generated sql script for the service uat-testing-microservice-music-2, created by the CAE.
-- --------------------------------------------------------

--
-- Table structure for table uatMusic.
--
CREATE TABLE uatTest.uatMusic (
  musicId INT(11) ,
  musicName VARCHAR(255) ,
  imageId INT(11) ,
  musicUrl VARCHAR(255) ,
CONSTRAINT musicId_PK PRIMARY KEY (musicId)
 
);



