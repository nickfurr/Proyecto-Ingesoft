SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM `pago`;
DELETE FROM `reserva`;
DELETE FROM `disponibilidad_diaria`;
DELETE FROM `paquete_alquiler`;
DELETE FROM `baño`;
DELETE FROM `cocina`;
DELETE FROM `habitacion`;
DELETE FROM `casaFotos`;
DELETE FROM `casa_rural`;
DELETE FROM `cliente`;
DELETE FROM `propietario`;
DELETE FROM `usuario`;

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO `usuario` (`id`, `username`, `password`, `email`) VALUES
    (1, 'juan.propietario', '123456', 'juan.montoya@ruralstay.com'),
    (2, 'ana.propietaria', '123456', 'ana.perez@ruralstay.com'),
    (3, 'carlos.cliente', '123456', 'carlos.rojas@gmail.com'),
    (4, 'laura.cliente', '123456', 'laura.gomez@gmail.com');

INSERT INTO `propietario` (`id`, `nombre_completo`, `telefono`, `numero_cuenta_bancaria`, `activo`) VALUES
    (1, 'Juan Montoya', '3001234567', '1234567890', TRUE),
    (2, 'Ana Perez', '3007654321', '9876543210', TRUE);

INSERT INTO `cliente` (`id`, `telefono_contacto`) VALUES
    (3, '3105551212'),
    (4, '3114448899');

INSERT INTO `casa_rural` (`codigo`, `poblacion`, `descripcion_general`, `numero_dormitorios`, `numero_banos`, `numero_cocinas`, `numero_comedores`, `plazas_garaje`, `activa`, `precio`, `ciudad`, `propietario_id`) VALUES
    (1, 'Vereda La Esperanza', 'Casa campestre con vista a las montanas, zona BBQ y jardin amplio.', 3, 2, 1, 1, 2, TRUE, 450000.00, 'Salento', 1),
    (2, 'Sector El Mirador', 'Alojamiento amplio con terrazas y areas sociales para grupos familiares.', 4, 3, 2, 2, 1, TRUE, 580000.00, 'Filandia', 1),
    (3, 'Vereda San Pedro', 'Casa acogedora para parejas y familias pequenas, rodeada de naturaleza.', 2, 1, 1, 1, 1, TRUE, 320000.00, 'Montenegro', 2);

INSERT INTO `habitacion` (`codigo`, `numero_camas`, `tipo_cama`, `tiene_bano`, `casa_rural_id`) VALUES
    (1, 2, 'SENCILLA', FALSE, 1),
    (2, 1, 'DOBLE', TRUE, 1),
    (3, 2, 'DOBLE', TRUE, 1),
    (4, 1, 'DOBLE', TRUE, 2),
    (5, 2, 'DOBLE', TRUE, 2),
    (6, 1, 'SENCILLA', FALSE, 3),
    (7, 1, 'DOBLE', TRUE, 3);

INSERT INTO `baño` (`id`, `compartido`, `casa_rural_id`) VALUES
    (1, TRUE, 1),
    (2, FALSE, 1),
    (3, TRUE, 2),
    (4, FALSE, 2),
    (5, FALSE, 3);

INSERT INTO `cocina` (`id`, `tiene_lavavajillas`, `tiene_lavadora`, `casa_rural_id`) VALUES
    (1, TRUE, FALSE, 1),
    (2, TRUE, TRUE, 2),
    (3, FALSE, TRUE, 2),
    (4, FALSE, FALSE, 3);

INSERT INTO `paquete_alquiler` (`id`, `fecha_inicio`, `fecha_fin`, `modalidad`, `precio_casa_entera`, `precio_por_habitacion`, `vigente`, `casa_rural_id`) VALUES
    (1, '2026-01-01', '2026-12-31', 'CASA_ENTERA', 450000.00, 120000.00, TRUE, 1),
    (2, '2026-01-01', '2026-12-31', 'AMBAS', 580000.00, 180000.00, TRUE, 2),
    (3, '2026-01-01', '2026-12-31', 'POR_HABITACIONES', NULL, 115000.00, TRUE, 3);

INSERT INTO `disponibilidad_diaria` (`id`, `fecha`, `estado_casa`, `observacion`, `casa_rural_id`) VALUES
    (1, '2026-07-08', 'RESERVADA', 'Reserva confirmada #1', 1),
    (2, '2026-07-09', 'LIBRE', 'Disponible', 1),
    (3, '2026-07-15', 'RESERVADA', 'Reserva pendiente #2', 2),
    (4, '2026-07-16', 'LIBRE', 'Disponible', 2),
    (5, '2026-08-01', 'RESERVADA', 'Reserva parcial #3', 3),
    (6, '2026-08-02', 'NO_DISPONIBLE', 'Mantenimiento preventivo', 3);

INSERT INTO `reserva` (`numero_reserva`, `fecha_reserva`, `fecha_entrada`, `numero_noches`, `modalidad`, `importe_total`, `importe_anticipo`, `fecha_limite_pago`, `estado`, `casa_rural_id`, `cliente_id`, `propietario_id`) VALUES
    (1, '2026-05-10', '2026-07-08', 3, 'CASA_ENTERA', 1350000.00, 450000.00, '2026-06-01', 'CONFIRMADA', 1, 3, 1),
    (2, '2026-05-11', '2026-07-15', 2, 'POR_HABITACIONES', 320000.00, 128000.00, '2026-06-10', 'PENDIENTE_PAGO', 2, 4, 1),
    (3, '2026-05-12', '2026-08-01', 4, 'AMBAS', 640000.00, 250000.00, '2026-06-20', 'PAGADA_PARCIAL', 3, 3, 2);

INSERT INTO `pago` (`id`, `fecha_pago`, `monto`, `concepto`, `verificado`, `tipo`, `reserva_id`) VALUES
    (1, '2026-05-13', 450000.00, 'Anticipo reserva 1', TRUE, 'TRANSFERENCIA', 1),
    (2, '2026-05-14', 250000.00, 'Pago parcial reserva 3', TRUE, 'NEQUI', 3);

INSERT INTO `casaFotos` (`casaId`, `fotoUrl`) VALUES
    (1, 'https://images.example.com/ruralstay/casa-1-foto-1.jpg'),
    (1, 'https://images.example.com/ruralstay/casa-1-foto-2.jpg'),
    (2, 'https://images.example.com/ruralstay/casa-2-foto-1.jpg'),
    (2, 'https://images.example.com/ruralstay/casa-2-foto-2.jpg'),
    (3, 'https://images.example.com/ruralstay/casa-3-foto-1.jpg');