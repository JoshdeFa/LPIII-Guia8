package Actividad;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ActividadJDBC {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:practica08.db";
        Connection con = null;

        try {
            //Registra la clase del controlador
            Class.forName("org.sqlite.JDBC");
            
            // 2. Establece la conexión
            con = DriverManager.getConnection(url);
            System.out.println("Conexión establecida exitosamente con SQLite.");

            // 3. Crea la tabla con 3 campos
            Statement stmt = con.createStatement();
            String sqlCrear = "CREATE TABLE IF NOT EXISTS Producto (id INTEGER PRIMARY KEY, nombre TEXT, precio REAL)";
            stmt.execute(sqlCrear);
            System.out.println("Tabla 'Producto' lista (Campos: id, nombre, precio).");

            // INICIO DEL MANEJO DE TRANSACCIONES
            con.setAutoCommit(false); // Desactivamos el guardado automático
            System.out.println("\nAutoCommit desactivado. Iniciando transacción...");

            //OPERACIÓN: INSERTAR
            String sqlInsert = "INSERT INTO Producto (id, nombre, precio) VALUES (?, ?, ?)";
            PreparedStatement pstmtInsert = con.prepareStatement(sqlInsert);
            
            // Producto 1
            pstmtInsert.setInt(1, 1);
            pstmtInsert.setString(2, "Laptop Gamer");
            pstmtInsert.setDouble(3, 1500.50);
            pstmtInsert.executeUpdate();

            // Producto 2
            pstmtInsert.setInt(1, 2);
            pstmtInsert.setString(2, "Mouse Inalámbrico");
            pstmtInsert.setDouble(3, 25.00);
            pstmtInsert.executeUpdate();
            System.out.println("-> Registros insertados correctamente.");

            //OPERACIÓN: ACTUALIZAR
            String sqlUpdate = "UPDATE Producto SET precio = ? WHERE id = ?";
            PreparedStatement pstmtUpdate = con.prepareStatement(sqlUpdate);
            pstmtUpdate.setDouble(1, 1450.00);
            pstmtUpdate.setInt(2, 1);          // ID del producto a modificar
            pstmtUpdate.executeUpdate();
            System.out.println("-> Registro actualizado correctamente (ID 1).");

            //OPERACIÓN: BORRAR
            String sqlDelete = "DELETE FROM Producto WHERE id = ?";
            PreparedStatement pstmtDelete = con.prepareStatement(sqlDelete);
            pstmtDelete.setInt(1, 2); // Borramos el Mouse (ID 2)
            pstmtDelete.executeUpdate();
            System.out.println("-> Registro borrado correctamente (ID 2).");

            // CONFIRMAR TRANSACCIÓN
            con.commit();
            System.out.println("\n*** Transacción confirmada (COMMIT ejecutado). ***");

            //OPERACIÓN: RECUPERAR (MOSTRAR)
            String sqlSelect = "SELECT * FROM Producto";
            PreparedStatement pstmtSelect = con.prepareStatement(sqlSelect);
            ResultSet rs = pstmtSelect.executeQuery();
            
            System.out.println("\n--- Datos Finales en la Tabla Producto ---");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + 
                                   " | Nombre: " + rs.getString("nombre") + 
                                   " | Precio: $" + rs.getDouble("precio"));
            }

        } catch (Exception e) {
            System.out.println("\n[ERROR] Ocurrió una excepción: " + e.getMessage());
            
            // REVERTIR TRANSACCIÓN EN CASO DE ERROR
            if (con != null) {
                try {
                    System.out.println("*** Haciendo ROLLBACK de la transacción para proteger la base de datos... ***");
                    con.rollback(); // Deshace cualquier cambio incompleto
                } catch (SQLException ex) {
                    System.out.println("Error al intentar hacer el rollback: " + ex.getMessage());
                }
            }
        } finally {
            // Cerrar la conexión
            if (con != null) {
                try {
                    con.close();
                    System.out.println("\nConexión cerrada de forma segura.");
                } catch (SQLException ex) {
                    System.out.println("Error al cerrar conexión: " + ex.getMessage());
                }
            }
        }
    }
}