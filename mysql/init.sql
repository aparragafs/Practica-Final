SET NAMES utf8mb4;

DROP DATABASE IF EXISTS PRACTICA;

CREATE DATABASE PRACTICA
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE PRACTICA;

CREATE TABLE PRACTICA.EM_EMPLEADOS
  (
   ID_EMPLEADO 				INT(7) PRIMARY KEY NOT NULL,	
   TX_NIF 					VARCHAR(9),									
   TX_NOMBRE 				VARCHAR(30) NOT NULL,			
   TX_APELLIDO1 			VARCHAR(40) NOT NULL,			
   TX_APELLIDO2 			VARCHAR(40) NOT NULL,			
   F_NACIMIENTO				DATE  NOT NULL,								
   N_TELEFONO1 				VARCHAR(12) NOT NULL,						
   N_TELEFONO2 				VARCHAR(12) NOT NULL,						
   TX_EMAIL 				VARCHAR(40) NOT NULL,						
   F_ALTA 					DATE  NOT NULL,		
   F_BAJA					DATE,
   CX_EDOCIVIL 				CHAR(1) NOT NULL,							
   B_FORMACIONU 			CHAR(1) NOT NULL	                      
);

INSERT INTO EM_EMPLEADOS (
    ID_EMPLEADO,
    TX_NIF,
    TX_NOMBRE,
    TX_APELLIDO1,
    TX_APELLIDO2,
    F_NACIMIENTO,
    N_TELEFONO1,
    N_TELEFONO2,
    TX_EMAIL,
    F_ALTA,
    F_BAJA,
    CX_EDOCIVIL,
    B_FORMACIONU
) VALUES
(1, '12345678A', 'Juan', 'Pérez', 'García', '1995-03-10', '600111111', '600111112', 'juan.perez@empresa.com', '2020-01-15', NULL, 'S', 'S'),
(2, '23456789B', 'María', 'López', 'Martín', '1990-07-21', '600222222', '600222223', 'maria.lopez@empresa.com', '2018-05-03', NULL, 'C', 'S'),
(3, '34567890C', 'Carlos', 'Ruiz', 'Sánchez', '1988-11-12', '600333333', '600333334', 'carlos.ruiz@empresa.com', '2016-09-18', NULL, 'C', 'N'),
(4, '45678901D', 'Lucía', 'Fernández', 'Moreno', '1998-02-05', '600444444', '600444445', 'lucia.fernandez@empresa.com', '2023-04-10', NULL, 'S', 'S'),
(5, '56789012E', 'Hugo', 'Martínez', 'López', '1999-02-16', '600555555', '600555556', 'hugo.martinez@empresa.com', '2022-08-01', NULL, 'S', 'S'),
(6, '67890123F', 'Ana', 'Gómez', 'Navarro', '1993-06-28', '600666666', '600666667', 'ana.gomez@empresa.com', '2021-03-14', NULL, 'C', 'N'),
(7, '78901234G', 'Pedro', 'Jiménez', 'Torres', '1985-09-09', '600777777', '600777778', 'pedro.jimenez@empresa.com', '2015-11-20', NULL, 'C', 'S'),
(8, '89012345H', 'Elena', 'Castro', 'Vega', '1997-12-01', '600888888', '600888889', 'elena.castro@empresa.com', '2024-01-08', NULL, 'S', 'S'),
(9, '90123456J', 'Sergio', 'Ortega', 'Díaz', '1991-04-18', '600999999', '601000000', 'sergio.ortega@empresa.com', '2019-06-25', NULL, 'S', 'N'),
(10, '11223344K', 'Laura', 'Romero', 'Gil', '1994-10-30', '601111111', '601111112', 'laura.romero@empresa.com', '2021-12-05', NULL, 'C', 'S'),
(11, '22334455L', 'David', 'Santos', 'Prieto', '1987-08-14', '601222222', '601222223', 'david.santos@empresa.com', '2014-03-01', NULL, 'C', 'S'),
(12, '33445566M', 'Paula', 'Herrera', 'Núñez', '1996-05-19', '601333333', '601333334', 'paula.herrera@empresa.com', '2022-11-12', NULL, 'S', 'S'),
(13, '44556677N', 'Raúl', 'Domínguez', 'Mora', '1984-01-09', '601444444', '601444445', 'raul.dominguez@empresa.com', '2012-06-18', NULL, 'C', 'N'),
(14, '55667788P', 'Carmen', 'Iglesias', 'Rey', '1992-09-23', '601555555', '601555556', 'carmen.iglesias@empresa.com', '2019-02-11', NULL, 'S', 'S'),
(15, '66778899R', 'Javier', 'Molina', 'Serrano', '1989-12-02', '601666666', '601666667', 'javier.molina@empresa.com', '2017-10-04', NULL, 'C', 'S'),
(16, '77889900S', 'Natalia', 'Vargas', 'León', '1997-07-15', '601777777', '601777778', 'natalia.vargas@empresa.com', '2024-02-20', NULL, 'S', 'N'),
(17, '88990011T', 'Álvaro', 'Fuentes', 'Blanco', '1991-03-27', '601888888', '601888889', 'alvaro.fuentes@empresa.com', '2020-09-14', NULL, 'C', 'S'),
(18, '99001122V', 'Patricia', 'Cano', 'Ramos', '1995-11-08', '601999999', '602000000', 'patricia.cano@empresa.com', '2023-06-01', NULL, 'S', 'S');

CREATE TABLE PRACTICA.PR_PROYECTOS
(  
   ID_PROYECTO 				INT(5) PRIMARY KEY NOT NULL,
   TX_DESCRIPCION 			VARCHAR(125) NOT NULL,
   F_INICIO 				DATE NOT NULL,
   F_FIN 					DATE,
   F_BAJA					DATE,
   TX_LUGAR 				VARCHAR(30),
   TX_OBSERVACIONES 		VARCHAR(300)
);

ALTER TABLE PR_PROYECTOS
MODIFY ID_PROYECTO INT(5) NOT NULL AUTO_INCREMENT;

CREATE TABLE PRACTICA.PR_EMPLEADOS_PROYECTO
(
   ID_PROYECTO 				INT(5) NOT NULL,
   ID_EMPLEADO 				INT(7) NOT NULL,
   F_ALTA					DATE,
   CONSTRAINT PK_PR_EMPLEADOS_PROYECTO PRIMARY KEY (ID_PROYECTO, ID_EMPLEADO)
);

ALTER TABLE PRACTICA.PR_EMPLEADOS_PROYECTO ADD CONSTRAINT FK_PR_EMPLEADOS_PROYECTO_EM_EMPLEADOS_01 FOREIGN KEY (ID_EMPLEADO) REFERENCES PRACTICA.EM_EMPLEADOS (ID_EMPLEADO);
ALTER TABLE PRACTICA.PR_EMPLEADOS_PROYECTO ADD CONSTRAINT FK_PR_EMPLEADOS_PROYECTO_PR_PROYECTOS_02 FOREIGN KEY (ID_PROYECTO) REFERENCES PRACTICA.PR_PROYECTOS (ID_PROYECTO);



INSERT INTO PR_PROYECTOS
(TX_DESCRIPCION, F_INICIO, F_FIN, F_BAJA, TX_LUGAR, TX_OBSERVACIONES)
VALUES
('Migración SAP', '2024-01-10', '2024-12-31', NULL, 'Madrid', 'Migración completa del ERP'),
('Portal RRHH', '2025-02-01', '2025-11-30', NULL, 'Barcelona', 'Nuevo portal interno de recursos humanos'),
('Aplicación Comercial', '2025-03-15', '2025-12-15', NULL, 'Madrid', 'Aplicación para el equipo comercial'),
('Cuadro de Mando', '2023-04-01', '2024-02-15', NULL, 'Sevilla', 'Dashboard de indicadores'),
('Renovación Web Corporativa', '2024-05-20', '2025-04-30', NULL, 'Valencia', 'Nueva web corporativa'),
('Implantación CRM', '2022-01-15', '2023-09-30', NULL, 'Madrid', 'Implantación de CRM'),
('Automatización Financiera', '2025-01-10', '2025-12-20', NULL, 'Bilbao', 'Automatización de procesos financieros'),
('App Inventario', '2024-08-01', '2025-06-30', NULL, 'Barcelona', 'Aplicación móvil de inventario'),
('Proyecto IA', '2025-03-01', '2026-03-01', NULL, 'Madrid', 'Pruebas de inteligencia artificial'),
('Auditoría Interna', '2025-04-01', '2025-10-30', NULL, 'Madrid', 'Proyecto sin recursos asignados'),
('Oficina Sin Papel', '2023-09-01', '2024-06-01', '2024-06-01', 'Zaragoza', 'Proyecto ya dado de baja');

INSERT INTO PR_EMPLEADOS_PROYECTO
(ID_PROYECTO, ID_EMPLEADO, F_ALTA)
VALUES
(1, 1, '2024-01-10'),
(1, 2, '2024-01-10'),
(1, 11, '2024-01-10'),

(2, 4, '2025-02-01'),
(2, 12, '2025-02-01'),
(2, 14, '2025-02-01'),

(3, 5, '2025-03-15'),
(3, 6, '2025-03-15'),
(3, 16, '2025-03-15'),

(4, 3, '2023-04-01'),
(4, 7, '2023-04-01'),
(4, 13, '2023-04-01'),

(5, 8, '2024-05-20'),
(5, 9, '2024-05-20'),
(5, 17, '2024-05-20'),

(6, 2, '2022-01-15'),
(6, 13, '2022-01-15'),

(7, 10, '2025-01-10'),
(7, 15, '2025-01-10'),

(8, 5, '2024-08-01'),
(8, 18, '2024-08-01'),

(9, 1, '2025-03-01'),
(9, 14, '2025-03-01');