package com.ejemplo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Clase principal de la aplicación.
 * Se encarga de mostrar el menú por consola y gestionar la interacción con el usuario,
 * ejecutando las consultas SQL en la base de datos H2.
 */
public class GestionPeliculas {

    // Scanner global para leer lo que el usuario escribe por teclado
    private static final Scanner sc = new Scanner(System.in);

    // Códigos de escape ANSI para dar color a la consola y que se vea más profesional
    public static final String RESET    = "\u001B[0m";  // Restablece el color por defecto
    public static final String VERDE    = "\u001B[32m"; // Color verde para éxitos
    public static final String AMARILLO = "\u001B[33m"; // Color amarillo para títulos
    public static final String CIAN     = "\u001B[36m"; // Color cian para el menú
    public static final String ROJO     = "\u001B[31m"; // Color rojo para errores

    public static void main(String[] args) {
        // 1. Inicializamos la base de datos (crea las tablas y datos si no existen)
        DatabaseManager.init();

        int opcion;
        // Bucle principal: se repite hasta que el usuario pulse 0
        do {
            limpiarPantalla(); // Borra la consola antes de mostrar el menú
            mostrarMenu();     // Dibuja el menú

            // Pedimos la opción al usuario
            opcion = leerInt(CIAN + "👉 Elige una opción: " + RESET);

            // Bloque try-catch para capturar posibles errores SQL (ej. si intentan borrar algo protegido por una FK)
            try {
                switch (opcion) {
                    case 1 -> { listarPeliculas(); pausar(); }      // Consulta simple
                    case 2 -> { listarPeliculasConJoin(); pausar(); } // Consulta JOIN
                    case 3 -> { contarPorGenero(); pausar(); }      // Consulta con agregación
                    case 4 -> { insertarPelicula(); pausar(); }     // Insertar (CRUD)
                    case 5 -> { borrarPelicula(); pausar(); }       // Borrar (CRUD)
                    case 6 -> { buscarPelicula(); pausar(); }       // Buscador dinámico
                    case 0 -> System.out.println(AMARILLO + "👋 Cerrando la aplicación..." + RESET);
                    default -> {
                        System.out.println(ROJO + "⚠️ Opción no válida." + RESET);
                        pausar();
                    }
                }
            } catch (SQLException e) {
                // Si hay un error en la base de datos, lo mostramos en rojo y pausamos para que el usuario pueda leerlo
                System.out.println(ROJO + "❌ Error en la base de datos: " + e.getMessage() + RESET);
                pausar();
            }
        } while (opcion != 0);
    }

    /**
     * Muestra el diseño del menú principal por pantalla.
     */
    private static void mostrarMenu() {
        System.out.println(CIAN + "╔══════════════════════════════════════════════╗");
        System.out.println("║         🎬  GESTIÓN DE PELÍCULAS (H2)        ║");
        System.out.println("╠══════════════════════════════════════════════╣");
        System.out.println("║ " + RESET + "1. " + VERDE + "Listar películas (Consulta Simple)   " + CIAN + "║");
        System.out.println("║ " + RESET + "2. " + VERDE + "Ver reparto (Consulta JOIN)          " + CIAN + "║");
        System.out.println("║ " + RESET + "3. " + VERDE + "Estadísticas (Agregación)            " + CIAN + "║");
        System.out.println("║ " + RESET + "6. " + VERDE + "Buscador (Título/Actor/Director)     " + CIAN + "║");
        System.out.println("╠══════════════════════════════════════════════╣");
        System.out.println("║ " + RESET + "4. " + AMARILLO + "Añadir una nueva película            " + CIAN + "║");
        System.out.println("║ " + RESET + "5. " + ROJO + "Borrar una película por ID           " + CIAN + "║");
        System.out.println("╠══════════════════════════════════════════════╣");
        System.out.println("║ " + RESET + "0. " + ROJO + "Salir del sistema                    " + CIAN + "║");
        System.out.println("╚══════════════════════════════════════════════╝" + RESET);
    }

    // =====================================================================
    // 1. CONSULTA SIMPLE
    // =====================================================================
    private static void listarPeliculas() throws SQLException {
        // Sentencia SQL básica: selecciona todo de la tabla pelicula
        String sql = "SELECT * FROM pelicula";

        // try-with-resources: Cierra automáticamente la conexión, el statement y el resultSet al terminar
        try (Connection c = DatabaseManager.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\n" + AMARILLO + "--- 📽️  LISTA DE PELÍCULAS ---" + RESET);
            // Imprimimos las cabeceras de la tabla con formato
            System.out.printf("%-5s | %-25s | %-6s%n", "ID", "TÍTULO", "AÑO");
            System.out.println("--------------------------------------------------");

            // Bucle para recorrer los resultados de la consulta fila a fila
            while (rs.next()) {
                System.out.printf("%-5d | %-25s | %-6d%n",
                        rs.getInt("id_pelicula"),
                        rs.getString("titulo"),
                        rs.getInt("anio"));
            }
        }
    }

    // =====================================================================
    // 2. CONSULTA CON JOIN
    // =====================================================================
    private static void listarPeliculasConJoin() throws SQLException {
        // JOIN: Cruza 4 tablas (pelicula, director, genero, actor) usando las Claves Foráneas
        String sql = "SELECT p.titulo, p.anio, d.nombre AS director, g.nombre AS genero, a.nombre AS actor " +
                "FROM pelicula p " +
                "INNER JOIN director d ON p.id_director = d.id_director " +
                "INNER JOIN genero   g ON p.id_genero   = g.id_genero " +
                "INNER JOIN pelicula_actor pa ON p.id_pelicula = pa.id_pelicula " +
                "INNER JOIN actor    a ON pa.id_actor = a.id_actor " +
                "ORDER BY p.titulo";

        try (Connection c = DatabaseManager.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\n" + AMARILLO + "--- 🔗 PELÍCULAS Y SU REPARTO ---" + RESET);
            while (rs.next()) {
                // Imprime el título en verde y debajo los detalles
                System.out.printf(VERDE + "• %s (%d)%n" + RESET, rs.getString("titulo"), rs.getInt("anio"));
                System.out.printf("  Dir: %-20s | Género: %-15s | Actor: %s%n",
                        rs.getString("director"),
                        rs.getString("genero"),
                        rs.getString("actor"));
                System.out.println("  --------------------------------------------------");
            }
        }
    }

    // =====================================================================
    // 3. CONSULTA CON AGREGACIÓN (COUNT + GROUP BY)
    // =====================================================================
    private static void contarPorGenero() throws SQLException {
        // Cuenta cuántas películas hay por cada género
        String sql = "SELECT g.nombre AS genero, COUNT(p.id_pelicula) AS total " +
                "FROM genero g LEFT JOIN pelicula p ON g.id_genero = p.id_genero " +
                "GROUP BY g.nombre ORDER BY total DESC";

        try (Connection c = DatabaseManager.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\n" + AMARILLO + "--- 📊 CANTIDAD DE PELÍCULAS POR GÉNERO ---" + RESET);
            while (rs.next()) {
                System.out.printf("%-20s → " + VERDE + "%d película(s)%n" + RESET,
                        rs.getString("genero"),
                        rs.getInt("total"));
            }
        }
    }

    // =====================================================================
    // 4. AÑADIR PELÍCULA
    // =====================================================================
    private static void insertarPelicula() throws SQLException {
        System.out.println("\n" + AMARILLO + "--- ➕ AÑADIR NUEVA PELÍCULA ---" + RESET);

        try (Connection c = DatabaseManager.getConnection()) {
            // Llamamos a nuestro método auxiliar para que el usuario elija de una lista
            int idGenero = seleccionarDeLista(c, "genero", "id_genero", "GÉNEROS DISPONIBLES", "Género");
            int idDirector = seleccionarDeLista(c, "director", "id_director", "DIRECTORES DISPONIBLES", "Director");
            int idActor = seleccionarDeLista(c, "actor", "id_actor", "ACTORES DISPONIBLES", "Actor");

            // Pedimos los datos propios de la película
            System.out.print(CIAN + "\nTítulo de la película: " + RESET);
            String titulo = sc.nextLine().trim();
            int anio = leerInt("Año: ");

            // Usamos PreparedStatement para evitar inyección SQL (buenas prácticas)
            String sqlPeli = "INSERT INTO pelicula (titulo, anio, id_genero, id_director) VALUES (?, ?, ?, ?)";

            // RETURN_GENERATED_KEYS nos permite obtener el ID que H2 le asigna automáticamente a la nueva película
            try (PreparedStatement ps = c.prepareStatement(sqlPeli, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, titulo);
                ps.setInt(2, anio);
                ps.setInt(3, idGenero);
                ps.setInt(4, idDirector);
                ps.executeUpdate(); // Ejecuta el INSERT

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    // Si se ha insertado bien, recuperamos su ID
                    int idNuevaPelicula = rs.getInt(1);

                    // Ahora insertamos el reparto en la tabla intermedia (pelicula_actor)
                    String sqlReparto = "INSERT INTO pelicula_actor (id_pelicula, id_actor) VALUES (?, ?)";
                    try (PreparedStatement psRep = c.prepareStatement(sqlReparto)) {
                        psRep.setInt(1, idNuevaPelicula);
                        psRep.setInt(2, idActor);
                        psRep.executeUpdate();
                    }
                }
                System.out.println(VERDE + "\n✅ ¡Película '" + titulo + "' añadida con éxito!" + RESET);
            }
        }
    }

    /**
     * Método auxiliar mágico. Muestra una lista numerada de cualquier tabla (genero, director, actor).
     * Si el usuario elige 0, le permite añadir uno nuevo sobre la marcha.
     */
    private static int seleccionarDeLista(Connection c, String tabla, String idColumn, String nombreLista, String singular) throws SQLException {
        List<Integer> ids = new ArrayList<>();
        List<String> nombres = new ArrayList<>();

        // 1. Consultamos todos los registros de esa tabla para mostrarlos
        try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM " + tabla + " ORDER BY " + idColumn)) {
            while (rs.next()) {
                ids.add(rs.getInt(idColumn));
                nombres.add(rs.getString("nombre"));
            }
        }

        // 2. Mostramos el menú de la lista
        System.out.println("\n" + AMARILLO + "--- " + nombreLista + " ---" + RESET);
        for (int i = 0; i < nombres.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + nombres.get(i));
        }
        System.out.println("  0. " + VERDE + "+ Añadir nuevo " + singular + RESET);

        // 3. Pedimos al usuario que elija una opción válida
        int opcion = -1;
        while (opcion < 0 || opcion > nombres.size()) {
            opcion = leerInt(CIAN + "Selecciona una opción: " + RESET);
            if (opcion < 0 || opcion > nombres.size()) System.out.println(ROJO + "⚠️ Opción no válida." + RESET);
        }

        // 4. Si elige 0, insertamos uno nuevo en la base de datos
        if (opcion == 0) {
            System.out.print("Nombre del nuevo " + singular + ": ");
            String nuevoNombre = sc.nextLine().trim();
            try (PreparedStatement ps = c.prepareStatement("INSERT INTO " + tabla + " (nombre) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, nuevoNombre);
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    System.out.println(VERDE + "✅ " + singular + " añadido/a con éxito." + RESET);
                    return rs.getInt(1); // Devolvemos el ID del nuevo registro
                }
            }
        }
        // 5. Si elige una opción existente, devolvemos el ID real de la base de datos
        return ids.get(opcion - 1);
    }

    // =====================================================================
    // 5. BORRAR PELÍCULA
    // =====================================================================
    private static void borrarPelicula() throws SQLException {
        System.out.println("\n" + ROJO + "--- 🗑️ BORRAR PELÍCULA ---" + RESET);
        listarPeliculas(); // Mostramos la lista para que el usuario vea los IDs
        int id = leerInt("\nIntroduce el ID de la película que quieres borrar (o 0 para cancelar): ");

        if (id == 0) {
            System.out.println(AMARILLO + "Operación cancelada." + RESET);
            return;
        }

        try (Connection c = DatabaseManager.getConnection()) {
            // IMPORTANTE (Integridad Referencial):
            // Antes de borrar la película, hay que borrar su relación en la tabla pelicula_actor.
            // Si no lo hacemos, H2 dará error porque el actor está "atado" a esa película (FK).
            try (PreparedStatement ps1 = c.prepareStatement("DELETE FROM pelicula_actor WHERE id_pelicula = ?")) {
                ps1.setInt(1, id);
                ps1.executeUpdate();
            }

            // Ahora sí, borramos la película
            try (PreparedStatement ps2 = c.prepareStatement("DELETE FROM pelicula WHERE id_pelicula = ?")) {
                ps2.setInt(1, id);
                int filas = ps2.executeUpdate(); // Devuelve el número de filas afectadas
                if (filas > 0) {
                    System.out.println(VERDE + "✅ Película borrada correctamente." + RESET);
                } else {
                    System.out.println(ROJO + "⚠️ No existe ninguna película con ese ID." + RESET);
                }
            }
        }
    }

    // =====================================================================
    // 6. BUSCADOR DINÁMICO (Título, Actor o Director)
    // =====================================================================
    private static void buscarPelicula() throws SQLException {
        System.out.println("\n" + AMARILLO + "--- 🔍 BUSCADOR DE PELÍCULAS ---" + RESET);
        System.out.println("1. Buscar por Título");
        System.out.println("2. Buscar por Actor");
        System.out.println("3. Buscar por Director");
        int tipo = leerInt(CIAN + "Elige una opción: " + RESET);

        if (tipo < 1 || tipo > 3) {
            System.out.println(ROJO + "⚠️ Opción no válida." + RESET);
            return;
        }

        System.out.print("Introduce el texto a buscar: ");
        String nombre = sc.nextLine().trim();

        // El símbolo % es un comodín en SQL.
        // Si buscamos "cep", se convierte en "%cep%", lo que significa "que contenga 'cep'".
        // LOWER() se usa para que la búsqueda no distinga entre mayúsculas y minúsculas.
        String busqueda = "%" + nombre.toLowerCase() + "%";

        // Construimos la consulta SQL dependiendo de lo que haya elegido buscar
        String sql = "";
        if (tipo == 1) {
            sql = "SELECT p.titulo, p.anio FROM pelicula p WHERE LOWER(p.titulo) LIKE ?";
        } else if (tipo == 2) {
            sql = "SELECT p.titulo, p.anio, a.nombre AS actor FROM pelicula p " +
                    "INNER JOIN pelicula_actor pa ON p.id_pelicula = pa.id_pelicula " +
                    "INNER JOIN actor a ON pa.id_actor = a.id_actor " +
                    "WHERE LOWER(a.nombre) LIKE ?";
        } else {
            sql = "SELECT p.titulo, p.anio, d.nombre AS director FROM pelicula p " +
                    "INNER JOIN director d ON p.id_director = d.id_director " +
                    "WHERE LOWER(d.nombre) LIKE ?";
        }

        // Ejecutamos la búsqueda
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, busqueda); // Sustituimos la ? por el texto de búsqueda
            ResultSet rs = ps.executeQuery();

            boolean encontrado = false;
            System.out.println("\n" + VERDE + "Resultados encontrados:" + RESET);
            while (rs.next()) {
                encontrado = true;
                // Imprimimos según el tipo de búsqueda
                if (tipo == 1) {
                    System.out.printf("• %s (%d)%n", rs.getString("titulo"), rs.getInt("anio"));
                } else if (tipo == 2) {
                    System.out.printf("• %s (%d) - Actor: %s%n", rs.getString("titulo"), rs.getInt("anio"), rs.getString("actor"));
                } else {
                    System.out.printf("• %s (%d) - Director: %s%n", rs.getString("titulo"), rs.getInt("anio"), rs.getString("director"));
                }
            }
            // Si el bucle no ha encontrado nada, avisamos al usuario
            if (!encontrado) {
                System.out.println(ROJO + "No se han encontrado resultados para: '" + nombre + "'" + RESET);
            }
        }
    }

    // =====================================================================
    // MÉTODOS AUXILIARES DE LA INTERFAZ
    // =====================================================================

    /**
     * Imprime 30 saltos de línea para simular que se "limpia" la consola.
     */
    private static void limpiarPantalla() {
        for (int i = 0; i < 30; i++) System.out.println();
    }

    /**
     * Detiene la ejecución hasta que el usuario pulse la tecla ENTER.
     * Sirve para que los resultados no desaparezcan de golpe.
     */
    private static void pausar() {
        System.out.print("\n" + AMARILLO + "Presiona ENTER para volver al menú..." + RESET);
        sc.nextLine();
    }

    /**
     * Lee un número entero por teclado de forma segura.
     * Si el usuario escribe una letra, captura el error y vuelve a preguntar.
     */
    private static int leerInt(String msg) {
        while (true) {
            System.out.print(msg);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println(ROJO + "⚠️ Introduce un número válido." + RESET);
            }
        }
    }
}