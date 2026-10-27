CREATE TABLE genero (
                        id_genero INT AUTO_INCREMENT PRIMARY KEY,
                        nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE director (
                          id_director INT AUTO_INCREMENT PRIMARY KEY,
                          nombre VARCHAR(100) NOT NULL
);

CREATE TABLE actor (
                       id_actor INT AUTO_INCREMENT PRIMARY KEY,
                       nombre VARCHAR(100) NOT NULL
);

CREATE TABLE pelicula (
                          id_pelicula INT AUTO_INCREMENT PRIMARY KEY,
                          titulo VARCHAR(150) NOT NULL,
                          anio INT,
                          id_genero INT NOT NULL,
                          id_director INT NOT NULL,
                          CONSTRAINT fk_pelicula_genero FOREIGN KEY (id_genero) REFERENCES genero(id_genero),
                          CONSTRAINT fk_pelicula_director FOREIGN KEY (id_director) REFERENCES director(id_director)
);

CREATE TABLE pelicula_actor (
                                id_pelicula INT NOT NULL,
                                id_actor INT NOT NULL,
                                PRIMARY KEY (id_pelicula, id_actor),
                                CONSTRAINT fk_reparto_pelicula FOREIGN KEY (id_pelicula) REFERENCES pelicula(id_pelicula),
                                CONSTRAINT fk_reparto_actor FOREIGN KEY (id_actor) REFERENCES actor(id_actor)
);