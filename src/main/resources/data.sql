-- Datos iniciales de LogiTrack
-- Se ejecuta automaticamente en cada arranque (spring.sql.init.mode=always),
-- por eso cada INSERT esta protegido para no duplicar filas.

-- Usuarios por defecto (contrasena para ambos: admin123)
-- Usa ON CONFLICT para que, si ya existian con el hash incorrecto de una
-- corrida anterior, se actualicen con el hash correcto.
INSERT INTO usuarios (username, password, role)
VALUES ('admin', '$2a$10$e6yd5DLPu/h/ZgPUAdGfCOIErscrc95WIWVu0OemDKg4JSZZiNiMe', 'ROLE_ADMIN')
ON CONFLICT (username) DO UPDATE SET password = EXCLUDED.password, role = EXCLUDED.role;

INSERT INTO usuarios (username, password, role)
VALUES ('empleado', '$2a$10$e6yd5DLPu/h/ZgPUAdGfCOIErscrc95WIWVu0OemDKg4JSZZiNiMe', 'ROLE_EMPLEADO')
ON CONFLICT (username) DO UPDATE SET password = EXCLUDED.password, role = EXCLUDED.role;

-- Bodegas de ejemplo (solo se insertan si la tabla esta vacia)
INSERT INTO bodegas (nombre, ubicacion, capacidad, encargado)
SELECT * FROM (VALUES
    ('Bodega Principal', 'Bogota D.C.', 10000, 'Carlos Mendoza'),
    ('Bodega Norte', 'Medellin', 5000, 'Ana Gomez'),
    ('Bodega Occidente', 'Cali', 3500, 'Roberto Silva')
) AS v(nombre, ubicacion, capacidad, encargado)
WHERE NOT EXISTS (SELECT 1 FROM bodegas);

-- Productos de ejemplo (solo se insertan si la tabla esta vacia)
INSERT INTO productos (nombre, categoria, stock, precio)
SELECT * FROM (VALUES
    ('Portatil Lenovo ThinkPad', 'Electronica', 15, 1200.00),
    ('Teclado Mecanico RGB', 'Accesorios', 4, 45.50),
    ('Monitor 27 Pulgadas 4K', 'Pantallas', 8, 320.00),
    ('Silla Ergonomica Oficina', 'Mobiliario', 25, 180.00)
) AS v(nombre, categoria, stock, precio)
WHERE NOT EXISTS (SELECT 1 FROM productos);
