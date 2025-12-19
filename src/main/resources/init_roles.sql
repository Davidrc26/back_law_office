-- Script SQL para inicializar roles básicos del sistema

-- Crear roles si no existen
INSERT INTO roles (name, spanish_name) 
SELECT 'ROLE_STUDENT', 'Estudiante'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_STUDENT');

INSERT INTO roles (name, spanish_name) 
SELECT 'ROLE_PROFESSOR', 'Profesor'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_PROFESSOR');

INSERT INTO roles (name, spanish_name) 
SELECT 'ROLE_ADMIN', 'Administrador'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_ADMIN');

INSERT INTO roles (name, spanish_name) 
SELECT 'ROLE_ASSISTANT', 'Asistente'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_ASSISTANT');

-- Verificar roles creados
SELECT * FROM roles;
