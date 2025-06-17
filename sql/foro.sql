INSERT INTO usuarios (id, apellidos, correo, imagenurl, nombre, password, telefono, tipo, username)
VALUES
-- Admin user (tipo = '0')
(900, 'anonymous Admin',     'anonymousAdmin@example.com',  '#',  'anonymous Admin',   '$2a$10$dy5dVxMg6PkaF2ZQXa6anukzOgcEBa65Fi.QBlJ1aTdpn1wRNveVq', '600123456', '0', 'anonymousAdmin'),

-- Regular user (tipo = '1')
(901, 'anonymous User',      'anonymousUser@example.com',   '#',  'anonymous User',    '$2a$10$QQOwO6XSF5oi2iV6IoVrweFVNpOHtRwbTA4M0oAIX6RQzQ4jjfyDi', '611111111', '1', 'anonymousUser'),
(902, 'Martínez López',      'laura.martinez@example.com',  '#',  'Laura',             '$2a$10$QQOwO6XSF5oi2iV6IoVrweFVNpOHtRwbTA4M0oAIX6RQzQ4jjfyDi', '611222333', '1', 'lauraM'),
(903, 'García Fernández',    'carlos.garcia@example.com',   '#',  'Carlos',            '$2a$10$QQOwO6XSF5oi2iV6IoVrweFVNpOHtRwbTA4M0oAIX6RQzQ4jjfyDi', '622333444', '1', 'carlosG'),
(904, 'Sánchez Rodríguez',   'ana.sanchez@example.com',     '#',  'Ana',               '$2a$10$QQOwO6XSF5oi2iV6IoVrweFVNpOHtRwbTA4M0oAIX6RQzQ4jjfyDi', '633444555', '1', 'anaS'),
(905, 'Pérez Gómez',         'jose.perez@example.com',      '#',  'José',              '$2a$10$QQOwO6XSF5oi2iV6IoVrweFVNpOHtRwbTA4M0oAIX6RQzQ4jjfyDi', '644555666', '1', 'joseP'),
(906, 'Ruiz Torres',         'marta.ruiz@example.com',      '#',  'Marta',             '$2a$10$QQOwO6XSF5oi2iV6IoVrweFVNpOHtRwbTA4M0oAIX6RQzQ4jjfyDi', '655666777', '1', 'martaR');




INSERT INTO public.hilos (id, titulo, descripcion, votos, mensaje_count, visitas, fecha_creacion, id_creador) VALUES
(900, '¿Dónde encuentras las mejores ofertas en supermercados?', 'Comparte tus experiencias con Mercadona, Lidl, Carrefour, etc.', 30, 1, 1001, now() - (random() * interval '30 days'), 902),
(901, 'Problemas al usar apps de cupones', 'Me está dando errores una app para escanear cupones en tienda', 21, 1, 250, now() - (random() * interval '30 days'), 903),
(902, 'Recomendaciones de webs para chollos de comida', '¿Qué páginas os han ayudado más a ahorrar en la compra?', 4, 0, 154, now() - (random() * interval '30 days'), 904),
(903, '¿Prefieres comprar online o en tienda física?', 'Hablemos sobre comodidad, precios y calidad en ambas opciones.', 16, 0, 507, now() - (random() * interval '30 days'), 905),
(904, 'Dudas con tarjetas de fidelidad', '¿Merece la pena usarlas en supermercados como Día o Carrefour?', 11, 0, 185, now() - (random() * interval '30 days'), 906),
(905, '¿Qué es una oferta flash?', 'No entiendo bien cómo funcionan estas promociones limitadas', 5, 0, 228, now() - (random() * interval '30 days'), 901),
(906, 'Comparativa de precios entre supermercados', '¿Cuál tiene los mejores precios esta semana?', 2, 0, 339, now() - (random() * interval '30 days'), 900);





INSERT INTO public.mensajes (id, id_escritor, id_hilo, contenido, fecha_creacion) VALUES
-- Hilo 900 - 3 mensajes
(900, 902, 900, 'Primer mensaje del hilo ¿Dónde encuentras las mejores ofertas en supermercados?', now() - (random() * interval '30 days')),
(901, 903, 900, 'Yo suelo ir a Mercadona, aunque últimamente Carrefour tiene buenas ofertas.', now() - (random() * interval '30 days')),
(902, 904, 900, '¿Alguien ha probado Lidl Plus?', now() - (random() * interval '30 days')),

-- Hilo 901 - 1 mensaje
(903, 905, 901, 'Inicio del hilo Problemas al usar apps de cupones.', now() - (random() * interval '30 days')),

-- Hilo 902 - 2 mensajes
(904, 906, 902, 'Yo uso chollometro y soy fan del canal de Telegram de ofertas.', now() - (random() * interval '30 days')),
(905, 902, 902, 'Hay una app llamada Tiendeo que está bastante bien.', now() - (random() * interval '30 days')),

-- Hilo 903 - 5 mensajes
(906, 903, 903, 'En tienda física puedes revisar bien el producto.', now() - (random() * interval '30 days')),
(907, 904, 903, 'Pero online suele haber más promociones.', now() - (random() * interval '30 days')),
(908, 905, 903, 'Depende mucho del supermercado.', now() - (random() * interval '30 days')),
(909, 906, 903, 'Online es mejor si compras mucho de golpe.', now() - (random() * interval '30 days')),
(910, 902, 903, 'Nunca he comprado online, ¿cómo es el servicio?', now() - (random() * interval '30 days')),

-- Hilo 904 - 2 mensajes
(911, 903, 904, 'Las tarjetas de Carrefour me han dado muchos descuentos.', now() - (random() * interval '30 days')),
(912, 904, 904, 'En Día hay una pero no me convence tanto.', now() - (random() * interval '30 days')),

-- Hilo 905 - 1 mensaje
(913, 905, 905, 'Las ofertas flash suelen ser por tiempo muy limitado, tipo 24h.', now() - (random() * interval '30 days'))

-- Hilo 906 intentionally has no messages.
;

