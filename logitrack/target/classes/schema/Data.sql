
INSERT INTO usuarios (username, password, role) 
VALUES ('admin', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymY05zg7XYM/c6Jd5mGgmy', 'ROLE_ADMIN'),
       ('empleado', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymY05zg7XYM/c6Jd5mGgmy', 'ROLE_EMPLEADO');

INSERT INTO bodegas (nombre, ubicacion, capacidad, encargado)
VALUES ('Bodega Principal', 'Bogotá D.C.', 10000, 'Carlos Mendoza'),
       ('Bodega Norte', 'Medellín', 5000, 'Ana Gómez'),
       ('Bodega Occidente', 'Cali', 3500, 'Roberto Silva');

INSERT INTO productos (nombre, categoria, stock, precio)
VALUES ('Portátil Lenovo ThinkPad', 'Electrónica', 15, 1200.00),
       ('Teclado Mecánico RGB', 'Accesorios', 4, 45.50),
       ('Monitor 27 Pulgadas 4K', 'Pantallas', 8, 320.00),
       ('Silla Ergonómica Oficina', 'Mobiliario', 25, 180.00);