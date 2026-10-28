package com.ejemplo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String URL  = "jdbc:h2:./peliculasDB";
    private static final String USER = "sa";
    private static final String PASS = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static void init() {
        String[] sqls = {
                "CREATE TABLE IF NOT EXISTS genero (" +
                        "  id_genero INT AUTO_INCREMENT PRIMARY KEY," +
                        "  nombre VARCHAR(50) NOT NULL UNIQUE)",

                "CREATE TABLE IF NOT EXISTS director (" +
                        "  id_director INT AUTO_INCREMENT PRIMARY KEY," +
                        "  nombre VARCHAR(100) NOT NULL)",

                "CREATE TABLE IF NOT EXISTS actor (" +
                        "  id_actor INT AUTO_INCREMENT PRIMARY KEY," +
                        "  nombre VARCHAR(100) NOT NULL)",

                "CREATE TABLE IF NOT EXISTS pelicula (" +
                        "  id_pelicula INT AUTO_INCREMENT PRIMARY KEY," +
                        "  titulo VARCHAR(150) NOT NULL," +
                        "  anio INT," +
                        "  id_genero INT NOT NULL," +
                        "  id_director INT NOT NULL," +
                        "  CONSTRAINT fk_pelicula_genero FOREIGN KEY (id_genero) REFERENCES genero(id_genero)," +
                        "  CONSTRAINT fk_pelicula_director FOREIGN KEY (id_director) REFERENCES director(id_director))",

                "CREATE TABLE IF NOT EXISTS pelicula_actor (" +
                        "  id_pelicula INT NOT NULL," +
                        "  id_actor INT NOT NULL," +
                        "  PRIMARY KEY (id_pelicula, id_actor)," +
                        "  CONSTRAINT fk_reparto_pelicula FOREIGN KEY (id_pelicula) REFERENCES pelicula(id_pelicula)," +
                        "  CONSTRAINT fk_reparto_actor FOREIGN KEY (id_actor) REFERENCES actor(id_actor))"
        };

        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
            for (String sql : sqls) {
                st.execute(sql);
            }

            if (isEmpty(st, "genero")) {
                st.execute("INSERT INTO genero (nombre) VALUES ('Ciencia Ficción'), ('Drama'), ('Comedia')");
                st.execute("INSERT INTO director (nombre) VALUES ('Christopher Nolan'), ('Quentin Tarantino'), ('Greta Gerwig')");
                st.execute("INSERT INTO actor (nombre) VALUES ('Leonardo DiCaprio'), ('Samuel L. Jackson'), ('Margot Robbie')");
                st.execute("INSERT INTO pelicula (titulo, anio, id_genero, id_director) VALUES ('Inception', 2010, 1, 1), ('Pulp Fiction', 1994, 2, 2), ('Barbie', 2023, 3, 3)");
                st.execute("INSERT INTO pelicula_actor (id_pelicula, id_actor) VALUES (1, 1), (2, 2), (3, 3)");
                System.out.println("🌱 Base de datos inicializada con 5 tablas y datos de ejemplo.");
            }
        } catch (SQLException e) {
            System.err.println("Error inicializando BD: " + e.getMessage());
        }
    }

    private static boolean isEmpty(Statement st, String tabla) throws SQLException {
        var rs = st.executeQuery("SELECT COUNT(*) FROM " + tabla);
        rs.next();
        return rs.getInt(1) == 0;
    }
}