-- Esquema de la base tp_final (MySQL).
-- Para crear todo desde cero:  mysql -u root -p < sql/schema.sql
-- Si ya habían creado las tablas a mano, lo más simple (mientras no haya datos
-- importantes) es borrarlas y recrearlas con este script.
CREATE DATABASE IF NOT EXISTS tp_final;
USE tp_final;

CREATE TABLE IF NOT EXISTS habitaciones (
    numero       INT PRIMARY KEY,
    tipo         VARCHAR(20)   NOT NULL,   -- INDIVIDUAL, DOBLE, SUITE
    precio_base  DECIMAL(10,2) NOT NULL,
    extra        BOOLEAN       NOT NULL,   -- escritorio / cama matrimonial / jacuzzi
    estado       VARCHAR(20)   NOT NULL,   -- DISPONIBLE, OCUPADA, MANTENIMIENTO
    motivo       VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS inquilinos (
    dni        VARCHAR(15) PRIMARY KEY,
    nombre     VARCHAR(80) NOT NULL,
    origen     VARCHAR(80),
    domicilio  VARCHAR(120)
);

CREATE TABLE IF NOT EXISTS reservas (
    id                 INT AUTO_INCREMENT PRIMARY KEY,
    dni_inquilino      VARCHAR(15) NOT NULL,
    numero_habitacion  INT         NOT NULL,
    fecha_inicio       DATE        NOT NULL,
    fecha_fin          DATE        NOT NULL,
    estado             VARCHAR(20) NOT NULL,   -- ACTIVA, CANCELADA, COMPLETADA
    FOREIGN KEY (dni_inquilino)     REFERENCES inquilinos(dni),
    FOREIGN KEY (numero_habitacion) REFERENCES habitaciones(numero)
);

CREATE TABLE IF NOT EXISTS estadias (
    id                 INT AUTO_INCREMENT PRIMARY KEY,
    dni_inquilino      VARCHAR(15) NOT NULL,
    numero_habitacion  INT         NOT NULL,
    fecha_entrada      DATE        NOT NULL,
    fecha_salida       DATE        NULL,       -- NULL = estadía abierta
    FOREIGN KEY (dni_inquilino)     REFERENCES inquilinos(dni),
    FOREIGN KEY (numero_habitacion) REFERENCES habitaciones(numero)
);

CREATE TABLE IF NOT EXISTS usuarios (
    nombre_usuario  VARCHAR(50)  PRIMARY KEY,
    contrasena      VARCHAR(100) NOT NULL,
    tipo            VARCHAR(20)  NOT NULL   -- ADMINISTRADOR, RECEPCIONISTA
);

-- Usuario de MySQL que usa la aplicación (ver src/JDBC/Conexion.java).
-- Ejecutar como root si todavía no existe:
-- CREATE USER IF NOT EXISTS 'progra2'@'localhost' IDENTIFIED BY 'santosypia';
-- GRANT ALL PRIVILEGES ON tp_final.* TO 'progra2'@'localhost';
-- FLUSH PRIVILEGES;
