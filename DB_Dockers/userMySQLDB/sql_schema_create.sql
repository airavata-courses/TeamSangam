create database weatherApp;
use weatherApp;
CREATE TABLE weatherApp.users (
Id INT NOT NULL AUTO_INCREMENT,
FirstName VARCHAR(45) NOT NULL,
LastName VARCHAR(45) NOT NULL,
Email VARCHAR(255) NOT NULL,
Password VARCHAR(255) NOT NULL,
Role VARCHAR(10) NOT NULL,
PRIMARY KEY (Id),
UNIQUE INDEX Id_UNIQUE (Id ASC));
