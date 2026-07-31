-- Crear tabla de Usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS bodegas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    ubicacion VARCHAR(150),
    capacidad INT NOT NULL,
    encargado VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS productos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    categoria VARCHAR(50),
    stock INT NOT NULL DEFAULT 0,
    precio DECIMAL(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS movimientos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha DATETIME NOT NULL,
    tipo_movimiento VARCHAR(20) NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INT NOT NULL,
    bodega_origen_id BIGINT,
    bodega_destino_id BIGINT,
    usuario_responsable VARCHAR(50),
    FOREIGN KEY (producto_id) REFERENCES productos(id),
    FOREIGN KEY (bodega_origen_id) REFERENCES bodegas(id),
    FOREIGN KEY (bodega_destino_id) REFERENCES bodegas(id)
);

CREATE TABLE IF NOT EXISTS auditorias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_operacion VARCHAR(20),
    fecha_hora DATETIME,
    usuario VARCHAR(50),
    entidad_afectada VARCHAR(50),
    valores_anteriores TEXT,
    valores_nuevos TEXT
);