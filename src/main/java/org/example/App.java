package org.example;

import java.sql.*;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {   //llamado al metodo de conexion
        Connection conexion = getConnection();
//llamado al metodo buscar clientes
        buscarClientes(conexion);
        System.out.println("Ingrese el codigo del cliente: ");
        int codigo = new Scanner(System.in).nextInt();
        buscarClientePorCodigo(conexion, codigo);
        System.out.println("Ingrese el codigo del empleado: ");
        int codigoEmpleado = new Scanner(System.in).nextInt();
        buscarClientesPorEmpleado(conexion, codigoEmpleado);
        cerrarConexion(conexion);
    }

    public static Connection getConnection() {
        String host = "127.0.0.1"; // localhost
        String port = "3306"; // por defecto es el puerto que utiliza
        String name = "sofi"; // usuario de la base de datos
        String password = "Toor*+"; // password de la base de datos
        String database = "vivero"; // nombre de la base de datos recien creada, en este caso vivero.
// Esto especifica una zona horaria, no es obligatorio de utilizar, pero en
// algunas zonas genera conflictos de conexión si no existiera
        String zona = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + zona;
// esto indica la ruta de conexion, que es la combinacion de
// host,usuario,puerto, nombre de la base de datos a la cual queremos
// conectarnos y la zona horaria (si se precisara).

        Connection conexion = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(url, name, password);
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (ClassNotFoundException e) {
            System.out.println("Error al cargar el conector JDBC: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
        return conexion;
    }


    public static void cerrarConexion(Connection conexion) {
        if (conexion != null) {
            try {
                conexion.close();
                System.out.println("La conexión a la base de datos fue cerrada de manera exitosa");
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión:" + e.getMessage());
            }
        }
    }

    public static void buscarClientes(Connection conexion) {
        String sql = "SELECT nombre_contacto, apellido_contacto, telefono FROM cliente";

        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            int count = 0;
            System.out.println("Lista de clientes ");
            System.out.println("id" + " nombre" + " apellido" + " telefono" + "   ");

            while (rs.next()) {
                String nombre = rs.getString("nombre_contacto");
                String apellido = rs.getString("apellido_contacto");
                String telefono = rs.getString("telefono");

                count++;
                System.out.println(count + ". " + nombre + " " + apellido + " " + telefono);

            }
//Cerrar ResultSet y Statement dentro del bloque try-catch-finally
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error al buscar el cliente: " + e.getMessage());
        }
    }

    public static void buscarClientePorCodigo(Connection conexion, int codigo) {
        String sql = "SELECT * FROM cliente WHERE codigo_cliente =" + codigo;
        boolean found = false;

        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String nombre = rs.getString("nombre_contacto");
                String apellido = rs.getString("apellido_contacto");
                String ciudad = rs.getString("ciudad");
                String pais = rs.getString("pais");
                double limite_credito = rs.getDouble("limite_credito");
                // Imprimir los detalles del cliente en una sola llamada a println con saltos de línea manuales
                System.out.println("Cliente con código " + codigo + ":\n"
                        + "Nombre: " + nombre + ", Apellido: " + apellido + ", País: " +

                        pais + ", Ciudad: " + ciudad + "\n"

                        + "Límite de Crédito: " + limite_credito + "\n"
                        + "---------------------");
                found = true; // Se encontraron datos para el código especificado
            }
            if (!found) {
                System.out.println("No se encontraron datos en la base con el código " + codigo);
            }
            //Cerrar ResultSet y Statement dentro del bloque try-catch-finally
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error al buscar el cliente: " + e.getMessage());
        }
    }

    public static void buscarClientesPorEmpleado(Connection conexion, int codigoEmpleado) {
        String sql = "SELECT c.* " +
                "FROM cliente c " +
                "JOIN empleado e ON e.id_empleado = c.id_empleado " +
                "WHERE e.codigo_empleado = " + codigoEmpleado;

        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            int count = 0;
            while (rs.next()) {
                int codigoCliente = rs.getInt("codigo_cliente");
                String nombre = rs.getString("nombre_contacto");
                String apellido = rs.getString("apellido_contacto");
                String telefono = rs.getString("telefono");
                String ciudad = rs.getString("ciudad");
                String pais = rs.getString("pais");
                double limiteCredito = rs.getDouble("limite_credito");
                count++;
                System.out.println("Cliente " + count + ":");

                System.out.println("Código Cliente: " + codigoCliente);
                System.out.println("Nombre: " + nombre + " " + apellido);
                System.out.println("Teléfono: " + telefono);
                System.out.println("Ciudad: " + ciudad);
                System.out.println("País: " + pais);
                System.out.println("Límite de Crédito: " + limiteCredito);
                System.out.println("---------------------");
            }
            if (count == 0) {
                System.out.println("No se encontraron clientes asociados al empleado con código " + codigoEmpleado);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error en la consulta: " + e.getMessage());
        }
    }
}
