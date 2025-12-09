-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema library
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema library
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `library` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `library` ;

-- -----------------------------------------------------
-- Table `library`.`categories`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library`.`categories` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `library`.`books`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library`.`books` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `path` VARCHAR(200) NULL DEFAULT NULL,
  `categories_id` INT NOT NULL,
  `status` ENUM('borrowed', 'returned') NOT NULL DEFAULT 'returned',
  PRIMARY KEY (`id`),
  INDEX `fk_books_categories_idx` (`categories_id` ASC) VISIBLE,
  CONSTRAINT `fk_books_categories`
    FOREIGN KEY (`categories_id`)
    REFERENCES `library`.`categories` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `library`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library`.`user` (
  `oid` VARCHAR(10) NOT NULL,
  `username` VARCHAR(100) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  `role` ENUM('ADMIN', 'USER') NOT NULL,
  PRIMARY KEY (`oid`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `library`.`rent`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library`.`rent` (
  `loan_oid` VARCHAR(10) NOT NULL,
  `books_id` INT NOT NULL,
  `loan_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `due_date` TIMESTAMP NOT NULL,
  `return_date` TIMESTAMP NOT NULL,
  `status` ENUM('borrowed', 'returned') NOT NULL,
  PRIMARY KEY (`loan_oid`, `books_id`),
  INDEX `fk_rent_books_idx` (`books_id` ASC) VISIBLE,
  INDEX `fk_rent_user_idx` (`loan_oid` ASC) VISIBLE,
  CONSTRAINT `fk_rent_books`
    FOREIGN KEY (`books_id`)
    REFERENCES `library`.`books` (`id`),
  CONSTRAINT `fk_rent_user`
    FOREIGN KEY (`loan_oid`)
    REFERENCES `library`.`user` (`oid`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
