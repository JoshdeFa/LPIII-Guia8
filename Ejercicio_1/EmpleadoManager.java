package Ejercicio_1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class EmpleadoManager {
    private static final String CLAVE_CONFIRMACION = "admin123";

    public void mostrarEmpleados() {
        String sql = "SELECT id, nombre, cargo, salario FROM empleado";
        try (Connection conn = Database.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n--- LISTA DE EMPLEADOS ---");
            boolean hayDatos = false;
            while (rs.next()) {
                hayDatos = true;
                System.out.printf("ID: %d | Nombre: %s | Cargo: %s | Salario: %.2f%n",
                        rs.getInt("id"), rs.getString("nombre"), 
                        rs.getString("cargo"), rs.getDouble("salario"));
            }
            if (!hayDatos) {
                System.out.println("No hay empleados registrados.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void ingresarEmpleado(Scanner scanner) {
        System.out.print("Ingrese nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese cargo: ");
        String cargo = scanner.nextLine();
        System.out.print("Ingrese salario: ");
        double salario = Double.parseDouble(scanner.nextLine());

        String sql = "INSERT INTO empleado(nombre, cargo, salario) VALUES(?, ?, ?)";
        ejecutarTransaccion(scanner, conn -> {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nombre);
                pstmt.setString(2, cargo);
                pstmt.setDouble(3, salario);
                pstmt.executeUpdate();
            }
        });
    }

    public void actualizarEmpleado(Scanner scanner) {
        System.out.print("Ingrese ID del empleado a actualizar: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Ingrese nuevo nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese nuevo cargo: ");
        String cargo = scanner.nextLine();
        System.out.print("Ingrese nuevo salario: ");
        double salario = Double.parseDouble(scanner.nextLine());

        String sql = "UPDATE empleado SET nombre = ?, cargo = ?, salario = ? WHERE id = ?";
        ejecutarTransaccion(scanner, conn -> {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nombre);
                pstmt.setString(2, cargo);
                pstmt.setDouble(3, salario);
                pstmt.setInt(4, id);
                if (pstmt.executeUpdate() == 0) {
                    System.out.println("No se encontró el ID ingresado.");
                }
            }
        });
    }

    public void borrarEmpleado(Scanner scanner) {
        System.out.print("Ingrese ID del empleado a eliminar: ");
        int id = Integer.parseInt(scanner.nextLine());

        String sql = "DELETE FROM empleado WHERE id = ?";
        ejecutarTransaccion(scanner, conn -> {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                if (pstmt.executeUpdate() == 0) {
                    System.out.println("No se encontró el ID ingresado.");
                }
            }
        });
    }

    @FunctionalInterface
    private interface TransaccionSQL {
        void ejecutar(Connection conn) throws SQLException;
    }

    private void ejecutarTransaccion(Scanner scanner, TransaccionSQL transaccion) {
        Connection conn = null;
        try {
            conn = Database.conectar();
            conn.setAutoCommit(false);
            transaccion.ejecutar(conn);

            System.out.print("\nIngrese la clave de confirmación: ");
            String clave = scanner.nextLine();

            if (CLAVE_CONFIRMACION.equals(clave)) {
                conn.commit();
                System.out.println("Operación CONFIRMADA (COMMIT).");
            } else {
                conn.rollback();
                System.out.println("Operación REVERTIDA (ROLLBACK).");
            }
        } catch (SQLException e) {
            System.out.println("Error en transacción: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.out.println("Error en rollback: " + ex.getMessage());
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Error al cerrar conexión: " + e.getMessage());
                }
            }
        }
    }
}