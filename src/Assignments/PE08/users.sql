-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema travel
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema travel
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `travel` DEFAULT CHARACTER SET utf8 ;
USE `travel` ;

-- -----------------------------------------------------
-- Table `travel`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `travel`.`users` (
  `userId` INT NOT NULL,
  `UserName` VARCHAR(45) NULL,
  `Password` VARCHAR(45) NULL,
  `Name` VARCHAR(45) NULL,
  `Access` VARCHAR(45) NULL,
  PRIMARY KEY (`userId`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `travel`.`users`
-- -----------------------------------------------------
START TRANSACTION;
USE `travel`;
INSERT INTO `travel`.`users` (`userId`, `UserName`, `Password`, `Name`, `Access`) VALUES (1, 'admin1', 'admin', 'Adin', 'Admin');
INSERT INTO `travel`.`users` (`userId`, `UserName`, `Password`, `Name`, `Access`) VALUES (2, 'admin2', 'admin', 'Adam', 'Admin');
INSERT INTO `travel`.`users` (`userId`, `UserName`, `Password`, `Name`, `Access`) VALUES (3, 'editor1', 'iedit', 'Ed', 'Editor');
INSERT INTO `travel`.`users` (`userId`, `UserName`, `Password`, `Name`, `Access`) VALUES (4, 'editor2', 'iedit', 'Eddy', 'Editor');
INSERT INTO `travel`.`users` (`userId`, `UserName`, `Password`, `Name`, `Access`) VALUES (5, 'general1', 'justread', 'Jen', 'General');
INSERT INTO `travel`.`users` (`userId`, `UserName`, `Password`, `Name`, `Access`) VALUES (6, 'general2', 'justread', 'Jenny', 'General');

COMMIT;

