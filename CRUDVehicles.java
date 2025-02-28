import java.sql.*;
import java.util.Scanner;

public class CRUDVehicles {
    private static final String URL = "jdbc:mysql://localhost:3306/MP06_UF02_AEA1";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "root";
    private static Connection conexion;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println("Conexión exitosa a la base de datos.");

            while (true) {
                System.out.println("\n--- Menú CRUD Vehicles ---");
                System.out.println("1. Insertar vehículo");
                System.out.println("2. Mostrar vehículos");
                System.out.println("3. Actualizar vehículo");
                System.out.println("4. Eliminar vehículo");
                System.out.println("5. Salir");
                System.out.print("Seleccione una opción: ");

                int opcion = scanner.nextInt();
                scanner.nextLine(); 

                switch (opcion) {
                    case 1 -> insertarVehiculo();
                    case 2 -> mostrarVehiculos();
                    case 3 -> actualizarVehiculo();
                    case 4 -> eliminarVehiculo();
                    case 5 -> {
                        cerrarConexion();
                        System.out.println("Saliendo...");
                        return;
                    }
                    default -> System.out.println("Opción no válida.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertarVehiculo() {
        System.out.print("Ingrese la marca: ");
        String marca = scanner.nextLine();
        System.out.print("Ingrese el modelo: ");
        String modelo = scanner.nextLine();
        System.out.print("Ingrese la capacidad del maletero: ");
        int capacidadMaletero = scanner.nextInt();
        scanner.nextLine();

        String sql = "INSERT INTO vehicles (Marca, Model, CapacitatMaleter) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, marca);
            stmt.setString(2, modelo);
            stmt.setInt(3, capacidadMaletero);
            stmt.executeUpdate();
            System.out.println("Vehículo insertado correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void mostrarVehiculos() {
        String sql = "SELECT * FROM vehicles";

        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\nLista de vehículos:");
            while (rs.next()) {
                System.out.printf("ID: %d | Marca: %s | Modelo: %s | Capacidad Maletero: %dL%n",
                        rs.getInt("Id"), rs.getString("Marca"), rs.getString("Model"), rs.getInt("CapacitatMaleter"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void actualizarVehiculo() {
        System.out.print("Ingrese el ID del vehículo a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Nueva marca: ");
        String marca = scanner.nextLine();
        System.out.print("Nuevo modelo: ");
        String modelo = scanner.nextLine();
        System.out.print("Nueva capacidad del maletero: ");
        int capacidadMaletero = scanner.nextInt();
        scanner.nextLine();

        String sql = "UPDATE vehicles SET Marca = ?, Model = ?, CapacitatMaleter = ? WHERE Id = ?";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, marca);
            stmt.setString(2, modelo);
            stmt.setInt(3, capacidadMaletero);
            stmt.setInt(4, id);
            int filasActualizadas = stmt.executeUpdate();

            if (filasActualizadas > 0) {
                System.out.println("Vehículo actualizado correctamente.");
            } else {
                System.out.println("No se encontró el vehículo con ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void eliminarVehiculo() {
        System.out.print("Ingrese el ID del vehículo a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        String sql = "DELETE FROM vehicles WHERE Id = ?";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int filasEliminadas = stmt.executeUpdate();

            if (filasEliminadas > 0) {
                System.out.println("Vehículo eliminado correctamente.");
            } else {
                System.out.println("No se encontró el vehículo con ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
