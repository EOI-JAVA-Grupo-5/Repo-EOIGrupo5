
INSERT INTO usuarios (id, apellidos, correo, imagenurl, nombre, password, telefono, tipo, username)
VALUES
-- Admin user (tipo = '0')
(900, 'anonymous Admin',     'anonymousAdmin@example.com',  '#',  'anonymous Admin',   'admin123', '600123456', '0', 'anonymousAdmin'),

-- Regular user (tipo = '1')
(901, 'anonymous User',   'anonymousUser@example.com',   '#',   'anonymous User',    'user123',  '611111111', '1', 'anonymousUser');




INSERT INTO public.hilos (id, titulo, descripcion, votos, mensaje_count, visitas, fecha_creacion, id_creador) VALUES
(900, '¿Cuál es tu framework favorito?', 'Comparte tus experiencias con Spring, Django, etc.', 30,1, 1001, now(), 900),
(901, 'Problema con Thymeleaf', 'Me está dando un error de binding', 21, 1, 250, now(), 900),
(902, 'Recomendaciones de libros de Java', '¿Qué libros os ayudaron más?', 4,0, 154, now(), 900),
(903,'¿Frontend o Backend?', 'Debate sano, por favor.', 16, 0, 507, now(), 901),
(904,'Dudas con JPA', '¿Cómo hago joins en entidades?', 11, 0, 185, now(), 901),
(905, '¿Qué es un DTO?', 'No entiendo bien la diferencia con Entity', 5, 0, 228, now(), 901),
(906, 'Spring Security básico', '¿Cómo empiezo con roles?', 2, 0, 339, now(), 901);





INSERT INTO public.mensajes (id, id_escritor, id_hilo, contenido, fecha_creacion) VALUES
(900,900, 900, 'Primer mensaje del hilo ¿Cuál es tu framework favorito?', now() - interval '2 days'),
(901,901, 901, 'Inicio del hilo Problema con Thymeleaf.', now() - interval '1 day');
