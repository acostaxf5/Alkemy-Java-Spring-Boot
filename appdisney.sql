DROP SCHEMA IF EXISTS `appdisney`;

CREATE SCHEMA IF NOT EXISTS `appdisney`;
USE `appdisney`;

CREATE TABLE IF NOT EXISTS `appdisney`.`imagen` (
  `id_imagen` INT NOT NULL AUTO_INCREMENT,
  `datos` LONGBLOB NOT NULL,
  `nombre` VARCHAR(255) NOT NULL,
  `tipo` VARCHAR(80) NOT NULL,
  PRIMARY KEY (`id_imagen`));

CREATE TABLE IF NOT EXISTS `appdisney`.`genero` (
  `id_genero` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(80) NOT NULL,
  `id_imagen` INT NOT NULL,
  PRIMARY KEY (`id_genero`),
  INDEX `fk_imagen_genero` (`id_imagen` ASC) VISIBLE,
  CONSTRAINT `fk_imagen_genero`
    FOREIGN KEY (`id_imagen`)
    REFERENCES `appdisney`.`imagen` (`id_imagen`));

CREATE TABLE IF NOT EXISTS `appdisney`.`pelicula` (
  `id_pelicula` INT NOT NULL AUTO_INCREMENT,
  `id_imagen` INT NOT NULL,
  `titulo` VARCHAR(80) NOT NULL,
  `fecha_creacion` DATETIME NOT NULL,
  `calificacion` INT NOT NULL,
  `id_genero` INT NOT NULL,
  PRIMARY KEY (`id_pelicula`),
  INDEX `fk_imagen_pelicula_idx` (`id_imagen` ASC) VISIBLE,
  INDEX `fk_genero_pelicula_idx` (`id_genero` ASC) VISIBLE,
  CONSTRAINT `fk_genero_pelicula`
    FOREIGN KEY (`id_genero`)
    REFERENCES `appdisney`.`genero` (`id_genero`),
  CONSTRAINT `fk_imagen_pelicula`
    FOREIGN KEY (`id_imagen`)
    REFERENCES `appdisney`.`imagen` (`id_imagen`));

CREATE TABLE IF NOT EXISTS `appdisney`.`personaje` (
  `id_personaje` INT NOT NULL AUTO_INCREMENT,
  `id_imagen` INT NOT NULL,
  `nombre` VARCHAR(80) NOT NULL,
  `edad` INT NOT NULL,
  `peso` DOUBLE NOT NULL,
  `historia` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id_personaje`),
  INDEX `fk_imagen_personaje_idx` (`id_imagen` ASC) VISIBLE,
  CONSTRAINT `fk_imagen_personaje`
    FOREIGN KEY (`id_imagen`)
    REFERENCES `appdisney`.`imagen` (`id_imagen`));

CREATE TABLE IF NOT EXISTS `appdisney`.`personaje_pelicula` (
  `id_personaje` INT NOT NULL,
  `id_pelicula` INT NOT NULL,
  INDEX `fk_personaje_conexion_idx` (`id_personaje` ASC) VISIBLE,
  INDEX `fk_pelicula_conexion_idx` (`id_pelicula` ASC) VISIBLE,
  CONSTRAINT `fk_pelicula_conexion`
    FOREIGN KEY (`id_pelicula`)
    REFERENCES `appdisney`.`pelicula` (`id_pelicula`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_personaje_conexion`
    FOREIGN KEY (`id_personaje`)
    REFERENCES `appdisney`.`personaje` (`id_personaje`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

CREATE TABLE IF NOT EXISTS `appdisney`.`usuario` (
  `id_usuario` INT NOT NULL AUTO_INCREMENT,
  `correo` VARCHAR(255) NOT NULL,
  `clave` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE INDEX `nombre_UNIQUE` (`correo` ASC) VISIBLE);