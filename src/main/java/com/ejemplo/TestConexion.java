package com.ejemplo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestConexion {

    private static final String URL  = "jdbc:h2:./peliculasDB";
    private static final String USER = "sa";
    private static final String PASS = "";

    public static void main(String[] args) {
        System.out.println("🔌 Conectando a: " + URL);

        // Llamamos al inicializador de la base de datos
        DatabaseManager.init();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            System.out.println("✅ Conexión establecida.");
            System.out.println("   Producto : " + conn.getMetaData().getDatabaseProductName());
            System.out.println("   Versión  : " + conn.getMetaData().getDatabaseProductVersion());
            System.out.println("   Driver   : " + conn.getMetaData().getDriverName()
                    + " " + conn.getMetaData().getDriverVersion());

            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT 1")) {
                if (rs.next()) {
                    System.out.println("🧪 SELECT 1 → " + rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error de conexión: " + e.getMessage());
            e.printStackTrace();
        }
    }
}