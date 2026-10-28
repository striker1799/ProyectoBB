INSERT INTO genero (nombre) VALUES ('Ciencia Ficción'), ('Drama'), ('Comedia');
INSERT INTO director (nombre) VALUES ('Christopher Nolan'), ('Quentin Tarantino'), ('Greta Gerwig');
INSERT INTO actor (nombre) VALUES ('Leonardo DiCaprio'), ('Samuel L. Jackson'), ('Margot Robbie');

INSERT INTO pelicula (titulo, anio, id_genero, id_director) VALUES
                                                                ('Inception', 2010, 1, 1),
                                                                ('Pulp Fiction', 1994, 2, 2),
                                                                ('Barbie', 2023, 3, 3);

INSERT INTO pelicula_actor (id_pelicula, id_actor) VALUES
                                                       (1, 1), (2, 2), (3, 3);