-- Profe había un error en el script, este es prácticamente el mismo, pero con un ajuste al final
DROP DATABASE IF EXISTS medicare;
CREATE DATABASE medicare
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
USE medicare;

-- Tabla roles
CREATE TABLE rol (
  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL UNIQUE
);

-- Tabla usuarios
CREATE TABLE usuario (
  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(150),
  email VARCHAR(200) UNIQUE,
  password VARCHAR(255),
  rol_id BIGINT UNSIGNED,
  activo BOOLEAN DEFAULT TRUE,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (rol_id) REFERENCES rol(id)
);

-- Tabla citas medicas
CREATE TABLE cita_medica (
  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  paciente_nombre VARCHAR(150),
  especialidad VARCHAR(100),
  fecha DATE,
  costo DOUBLE,
  activa BOOLEAN DEFAULT TRUE
);

-- Datos de prueba
INSERT INTO rol (nombre) VALUES ('ADMIN'), ('MEDICO'), ('PACIENTE');
INSERT INTO usuario (nombre, email, password, rol_id) VALUES
('Admin General', 'admin@medicare.com', '$2a$10$.54Izy8PNTW1mMISg8ocHudQaILKu9xKhCHoxtJhmafvG8ZtGwb.e', 1),
('Dr. Roberto', 'medico@medicare.com', '$2a$10$.54Izy8PNTW1mMISg8ocHudQaILKu9xKhCHoxtJhmafvG8ZtGwb.e', 2),
('Paciente Maria', 'paciente@medicare.com', '$2a$10$.54Izy8PNTW1mMISg8ocHudQaILKu9xKhCHoxtJhmafvG8ZtGwb.e', 3);

INSERT INTO cita_medica (paciente_nombre, especialidad, fecha, costo, activa) VALUES
('Carlos Gomez', 'Cardiologia', '2026-05-10', 45000.0, TRUE),
('Ana Martinez', 'Dermatologia', '2026-06-15', 35000.0, TRUE);
