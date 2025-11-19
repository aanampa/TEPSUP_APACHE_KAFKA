-- Eliminar tablas si existen (para desarrollo)
DROP TABLE IF EXISTS log_proceso CASCADE;
DROP TABLE IF EXISTS estudiante CASCADE;

-- Crear tabla de estudiantes
CREATE TABLE estudiante (
    estudiante_id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    dni VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    telefono VARCHAR(20),
    fecha_nacimiento DATE NOT NULL,
    direccion VARCHAR(200),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE
);

-- Crear índices para estudiante
CREATE INDEX idx_estudiante_dni ON estudiante(dni);
CREATE INDEX idx_estudiante_email ON estudiante(email);
CREATE INDEX idx_estudiante_nombre ON estudiante(nombre);
CREATE INDEX idx_estudiante_activo ON estudiante(activo);

-- Crear tabla de log de procesos
CREATE TABLE log_proceso (
    id_log_proceso SERIAL PRIMARY KEY,
    contenido TEXT NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear índice para log_proceso
CREATE INDEX idx_log_proceso_fecha ON log_proceso(fecha_registro DESC);

-- Insertar datos de ejemplo en estudiante
INSERT INTO estudiante (nombre, apellido, dni, email, telefono, fecha_nacimiento, direccion, activo) 
VALUES 
    ('Juan', 'Pérez García', '12345678A', 'juan.perez@email.com', '666111222', '2000-05-15', 'Calle Mayor 123, Madrid', TRUE),
    ('María', 'López Martínez', '87654321B', 'maria.lopez@email.com', '666333444', '1999-08-20', 'Avenida Principal 456, Barcelona', TRUE),
    ('Carlos', 'Rodríguez Sánchez', '11223344C', 'carlos.rodriguez@email.com', '666555666', '2001-03-10', 'Plaza España 789, Valencia', TRUE);

-- Insertar datos de ejemplo en log_proceso
INSERT INTO log_proceso (contenido) 
VALUES 
    ('Sistema iniciado correctamente'),
    ('Base de datos inicializada'),
    ('Datos de ejemplo cargados');

-- Mostrar resumen
SELECT 'ESTUDIANTES:' as tabla;
SELECT estudiante_id, nombre, apellido, dni, email, activo FROM estudiante;

SELECT 'LOG_PROCESO:' as tabla;
SELECT id_log_proceso, contenido, fecha_registro FROM log_proceso ORDER BY fecha_registro DESC;
