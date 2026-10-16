package Ejercicio_1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String URL = "jdbc:sqlite:empresa.db";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void inicializar() {
        String sql = "CREATE TABLE IF NOT EXISTS empleado ("
                   + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                   + " nombre TEXT NOT NULL,"
                   + " cargo TEXT NOT NULL,"
                   + " salario REAL NOT NULL"
                   + ");";
        try (Connection conn = conectar();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}