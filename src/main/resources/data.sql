-- Usuarios
INSERT INTO public.usuarios (nick) VALUES ('neo12');
INSERT INTO public.usuarios (nick) VALUES ('maria_88');
INSERT INTO public.usuarios (nick) VALUES ('xXjohnXx');
INSERT INTO public.usuarios (nick) VALUES ('lucia_dev');
INSERT INTO public.usuarios (nick) VALUES ('anon123');
INSERT INTO public.usuarios (nick) VALUES ('eva.luna');
INSERT INTO public.usuarios (nick) VALUES ('pedro01');
INSERT INTO public.usuarios (nick) VALUES ('angelica');
INSERT INTO public.usuarios (nick) VALUES ('joel_r');
INSERT INTO public.usuarios (nick) VALUES ('daniCoder');

-- Hilos
INSERT INTO hilos (id, titulo, descripcion, favoritos, visitas, fecha_creacion, autor_id) VALUES
(1, '¿Cuál es tu framework favorito?', 'Comparte tus experiencias con Spring, Django, etc.', 5, 40, now(), 1),
(2, 'Problema con Thymeleaf', 'Me está dando un error de binding', 2, 25, now(), 2),
(3, 'Recomendaciones de libros de Java', '¿Qué libros os ayudaron más?', 4, 15, now(), 3),
(4, '¿Frontend o Backend?', 'Debate sano, por favor.', 6, 50, now(), 4),
(5, 'Dudas con JPA', '¿Cómo hago joins en entidades?', 1, 18, now(), 5),
(6, '¿Qué es un DTO?', 'No entiendo bien la diferencia con Entity', 3, 22, now(), 6),
(7, 'Spring Security básico', '¿Cómo empiezo con roles?', 2, 33, now(), 7),
(8, 'Errores comunes en Postgres', 'Comparte los tuyos 😅', 5, 60, now(), 8),
(9, 'Inyecciones de dependencia', '¿Por qué se usan tanto?', 4, 28, now(), 9),
(10, 'Buenas prácticas en Java', 'Consejos para código limpio', 7, 70, now(), 10);

-- Mensajes
INSERT INTO mensajes ( contenido, fecha_publicacion, hilo_id, autor_id) VALUES
( 'Me encanta Spring Boot, lo uso en todos mis proyectos.', now(), 1, 2),
( 'Revisa los th:field, suelen ser la causa.', now(), 2, 3),
( 'Effective Java es top 🔥', now(), 3, 4),
( 'Backend, sin duda 💪', now(), 4, 5),
( 'Puedes usar @JoinColumn para especificar la FK.', now(), 5, 6),
( 'DTO = Data Transfer Object. Útil para separar lógica.', now(), 6, 7),
( 'Empieza por configurar el WebSecurityConfigurerAdapter.', now(), 7, 8),
( 'Una vez me equivoqué con las comillas en una query 😅', now(), 8, 9),
( 'Para mí, la inyección te da más testabilidad.', now(), 9, 10),
( 'Clean Code de Uncle Bob es una joya.', now(), 10, 1);
