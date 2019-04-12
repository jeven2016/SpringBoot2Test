-- MySQL Script generated by MySQL Workbench
-- 05/31/18 15:21:13
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `bc`;

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `bc`
  DEFAULT CHARACTER SET utf8
  COLLATE utf8_general_ci;
USE `bc`;

-- -----------------------------------------------------
-- Table `bc`.`website`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bc`.`website`;

CREATE TABLE IF NOT EXISTS `bc`.`website` (
  `id`   BIGINT(20)  NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  `url`  VARCHAR(45) NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `bc`.`page`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bc`.`page`;

CREATE TABLE IF NOT EXISTS `bc`.`page` (
  `id`         BIGINT(20) NOT NULL,
  `pageno`     INT        NULL,
  `website_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_page_website`
  FOREIGN KEY (`website_id`)
  REFERENCES `bc`.`website` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB;

CREATE INDEX `fk_page_website_idx`
  ON `bc`.`page` (`website_id` ASC);

-- -----------------------------------------------------
-- Table `bc`.`book`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bc`.`book`;

CREATE TABLE IF NOT EXISTS `bc`.`book` (
  `id`           BIGINT(20)   NOT NULL,
  `url`          VARCHAR(255) NULL,
  `picpagecount` INT          NULL,
  `name`         VARCHAR(45)  NULL,
  `page_id`      BIGINT(20)   NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_book_page1`
  FOREIGN KEY (`page_id`)
  REFERENCES `bc`.`page` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB;

CREATE INDEX `fk_book_page1_idx`
  ON `bc`.`book` (`page_id` ASC);

-- -----------------------------------------------------
-- Table `bc`.`bookpage`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bc`.`bookpage`;

CREATE TABLE IF NOT EXISTS `bc`.`bookpage` (
  `id`      BIGINT(20)   NOT NULL,
  `url`     VARCHAR(255) NULL,
  `book_id` BIGINT(20)   NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_bookpage_book1`
  FOREIGN KEY (`book_id`)
  REFERENCES `bc`.`book` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB;

CREATE INDEX `fk_bookpage_book1_idx`
  ON `bc`.`bookpage` (`book_id` ASC);

-- -----------------------------------------------------
-- Table `bc`.`picture`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bc`.`picture`;

CREATE TABLE IF NOT EXISTS `bc`.`picture` (
  `id`          BIGINT(20)  NOT NULL,
  `url`         VARCHAR(50) NULL,
  `status`      VARCHAR(45) NULL,
  `bookpage_id` BIGINT(20)  NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_picture_bookpage1`
  FOREIGN KEY (`bookpage_id`)
  REFERENCES `bc`.`bookpage` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB;

CREATE INDEX `fk_picture_bookpage1_idx`
  ON `bc`.`picture` (`bookpage_id` ASC);


SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;