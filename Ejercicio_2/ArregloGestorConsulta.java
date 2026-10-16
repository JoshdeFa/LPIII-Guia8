package Ejercicio_2;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ArregloGestorConsulta {
    private Empleado[] arregloEmpleados;
    private int tamano;

    public ArregloGestorConsulta() {
        cargarDesdeBaseDeDatos();
    }

    private void cargarDesdeBaseDeDatos() {
        List<Empleado> listaTemp = new ArrayList<>();
        String sql = "SELECT id, nombre, cargo, salario FROM empleado";
        try (Connection conn = Database.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                listaTemp.add(new Empleado(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("cargo"),
                    rs.getDouble("salario")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar datos: " + e.getMessage());
        }

        arregloEmpleados = listaTemp.toArray(new Empleado[0]);
        tamano = arregloEmpleados.length;
    }

    public void consultar(boolean verId, boolean verNombre, boolean verCargo, boolean verSalario,
                          String campoCondicion, String operador, String valorCondicion,
                          String campoOrden, boolean ascendente, int limite) {
        
        List<Empleado> resultados = new ArrayList<>();

        for (int i = 0; i < tamano; i++) {
            Empleado emp = arregloEmpleados[i];
            if (cumpleCondicion(emp, campoCondicion, operador, valorCondicion)) {
                resultados.add(emp);
            }
        }

        if (campoOrden != null && !campoOrden.isEmpty()) {
            Comparator<Empleado> comparador = obtenerComparador(campoOrden);
            if (comparador != null) {
                if (!ascendente) {
                    comparador = comparador.reversed();
                }
                resultados.sort(comparador);
            }
        }

        if (limite > 0 && resultados.size() > limite) {
            resultados = resultados.subList(0, limite);
        }

        mostrarResultados(resultados, verId, verNombre, verCargo, verSalario);
    }

    private boolean cumpleCondicion(Empleado emp, String campo, String operador, String valor) {
        if (campo == null || campo.isEmpty() || operador == null || valor == null) {
            return true;
        }

        try {
            switch (campo.toLowerCase()) {
                case "id":
                    int valId = Integer.parseInt(valor);
                    return compararNumeros(emp.getId(), valId, operador);
                case "salario":
                    double valSalario = Double.parseDouble(valor);
                    return compararNumeros(emp.getSalario(), valSalario, operador);
                case "nombre":
                    return compararTextos(emp.getNombre(), valor, operador);
                case "cargo":
                    return compararTextos(emp.getCargo(), valor, operador);
                default:
                    return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private boolean compararNumeros(double a, double b, String op) {
        switch (op) {
            case "=": return a == b;
            case ">": return a > b;
            case "<": return a < b;
            case ">=": return a >= b;
            case "<=": return a <= b;
            case "!=": return a != b;
            default: return false;
        }
    }

    private boolean compararTextos(String a, String b, String op) {
        if (op.equals("=")) {
            return a.equalsIgnoreCase(b);
        } else if (op.equalsIgnoreCase("contains")) {
            return a.toLowerCase().contains(b.toLowerCase());
        }
        return false;
    }

    private Comparator<Empleado> obtenerComparador(String campo) {
        switch (campo.toLowerCase()) {
            case "id":
                return Comparator.comparingInt(Empleado::getId);
            case "nombre":
                return Comparator.comparing(Empleado::getNombre, String.CASE_INSENSITIVE_ORDER);
            case "cargo":
                return Comparator.comparing(Empleado::getCargo, String.CASE_INSENSITIVE_ORDER);
            case "salario":
                return Comparator.comparingDouble(Empleado::getSalario);
            default:
                return null;
        }
    }

    private void mostrarResultados(List<Empleado> lista, boolean verId, boolean verNombre, boolean verCargo, boolean verSalario) {
        System.out.println("\n--- RESULTADOS DE LA CONSULTA ---");
        if (lista.isEmpty()) {
            System.out.println("No se encontraron registros que cumplan con los criterios.");
            return;
        }

        for (Empleado e : lista) {
            StringBuilder sb = new StringBuilder();
            if (verId) sb.append("ID: ").append(e.getId()).append(" | ");
            if (verNombre) sb.append("Nombre: ").append(e.getNombre()).append(" | ");
            if (verCargo) sb.append("Cargo: ").append(e.getCargo()).append(" | ");
            if (verSalario) sb.append("Salario: ").append(e.getSalario()).append(" ");
            System.out.println(sb.toString().trim());
        }
    }
}