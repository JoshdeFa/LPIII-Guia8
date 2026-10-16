package Ejercicio_2;

import java.util.Scanner;

public class MainConsulta {
    public static void main(String[] args) {
        Database.inicializar();
        Scanner scanner = new Scanner(System.in);
        ArregloGestorConsulta gestor = new ArregloGestorConsulta();
        int opcion = 0;

        do {
            System.out.println("\n========== MENÚ CONSULTA EN ARREGLO ==========");
            System.out.println("1. Ejecutar consulta personalizada");
            System.out.println("2. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida.");
                continue;
            }

            if (opcion == 1) {
                System.out.println("\n--- a) Campos a mostrar ---");
                System.out.print("¿Mostrar ID? (s/n): ");
                boolean verId = scanner.nextLine().equalsIgnoreCase("s");
                System.out.print("¿Mostrar Nombre? (s/n): ");
                boolean verNombre = scanner.nextLine().equalsIgnoreCase("s");
                System.out.print("¿Mostrar Cargo? (s/n): ");
                boolean verCargo = scanner.nextLine().equalsIgnoreCase("s");
                System.out.print("¿Mostrar Salario? (s/n): ");
                boolean verSalario = scanner.nextLine().equalsIgnoreCase("s");

                System.out.println("\n--- b) Condición opcional ---");
                System.out.print("¿Desea aplicar condición? (s/n): ");
                String aplicarCondicion = scanner.nextLine();
                String campoCond = "", operador = "", valCond = "";
                if (aplicarCondicion.equalsIgnoreCase("s")) {
                    System.out.print("Campo (id, nombre, cargo, salario): ");
                    campoCond = scanner.nextLine();
                    System.out.print("Operador (=, >, <, >=, <=, !=, contains): ");
                    operador = scanner.nextLine();
                    System.out.print("Valor de condición: ");
                    valCond = scanner.nextLine();
                }

                System.out.println("\n--- c) Ordenamiento ---");
                System.out.print("¿Desea ordenar los resultados? (s/n): ");
                String aplicarOrden = scanner.nextLine();
                String campoOrden = "";
                boolean ascendente = true;
                if (aplicarOrden.equalsIgnoreCase("s")) {
                    System.out.print("Campo para ordenar (id, nombre, cargo, salario): ");
                    campoOrden = scanner.nextLine();
                    System.out.print("¿Orden ascendente? (s/n): ");
                    ascendente = scanner.nextLine().equalsIgnoreCase("s");
                }

                System.out.println("\n--- d) Límite ---");
                System.out.print("Ingrese límite de registros a mostrar (0 para sin límite): ");
                int limite = 0;
                try {
                    limite = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    limite = 0;
                }

                gestor.consultar(verId, verNombre, verCargo, verSalario,
                                 campoCond, operador, valCond,
                                 campoOrden, ascendente, limite);
            } else if (opcion == 2) {
                System.out.println("Saliendo...");
            } else {
                System.out.println("Opción no válida.");
            }
        } while (opcion != 2);

        scanner.close();
    }
}