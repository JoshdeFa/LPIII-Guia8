package Ejercicio_1;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Database.inicializar();
        Scanner scanner = new Scanner(System.in);
        EmpleadoManager manager = new EmpleadoManager();
        int opcion = 0;

        do {
            System.out.println("\n========== MENÚ ==========");
            System.out.println("1. Mostrar empleados");
            System.out.println("2. Ingresar empleado");
            System.out.println("3. Actualizar empleado");
            System.out.println("4. Borrar empleado");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida.");
                continue;
            }

            switch (opcion) {
                case 1: manager.mostrarEmpleados(); break;
                case 2: manager.ingresarEmpleado(scanner); break;
                case 3: manager.actualizarEmpleado(scanner); break;
                case 4: manager.borrarEmpleado(scanner); break;
                case 5: System.out.println("Saliendo..."); break;
                default: System.out.println("Opción no válida.");
            }
        } while (opcion != 5);

        scanner.close();
    }
}