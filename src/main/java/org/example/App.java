package org.example;

import org.example.classes.ProductoGama;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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

        System.out.println("Ingrese el punto de reposicion: ");
        int puntoReposicion = new Scanner(System.in).nextInt();
        getProductosParaReponer(conexion, puntoReposicion);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el nombre de la gama del producto: ");
        String gamaProducto = scanner.nextLine(); // Leer el nombre de la gama

        List<ProductoGama> productos = getProductosGama(conexion, gamaProducto);

        if (productos.isEmpty()) {
            System.out.println("No se encontraron productos para la gama: " + gamaProducto);
        } else {
            System.out.println("Productos de la gama " + gamaProducto + ":");
            for (ProductoGama producto : productos) {
                System.out.println(producto); // Imprime la información del producto
            }
        }

        System.out.println("Digite el id del cliente para ver sus pedidos:");
        int idCliente = scanner.nextInt();
        getPedidosPorCliente(conexion, idCliente);

        cerrarConexion(conexion);
        scanner.close();
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
        // esto indica la ruta de conexion, que es la combinacion de host,usuario,puerto,
        // nombre de la base de datos a la cual queremos conectarnos y la zona horaria (si se precisara).

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

    public static void getProductosParaReponer(Connection conexion, int puntoReposicion) {
        String sql = "SELECT id_producto, codigo_producto, nombre, cantidad_en_stock " +
                "FROM producto " +
                "WHERE cantidad_en_stock < " + puntoReposicion;

        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            int count = 0;
            System.out.println("Lista de productos por debajo del punto de reposición: ");
            while (rs.next()) {
                int idProducto = rs.getInt("id_producto");
                String codigoProducto = rs.getString("codigo_producto");
                String nombre = rs.getString("nombre");
                int cantidadEnStock = rs.getInt("cantidad_en_stock");
                System.out.println("ID: " + idProducto);
                System.out.println("Código: " + codigoProducto);
                System.out.println("Nombre: " + nombre);
                System.out.println("Cantidad en Stock: " + cantidadEnStock);

                System.out.println("---------------------------");
                count++;
            }
            if (count == 0) {
                System.out.println("No se encontraron productos para reponer con stock menor a " + puntoReposicion);
            }
            // Cerrar ResultSet y Statement dentro del bloque try-catch-finally
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error en la consulta: " + e.getMessage());
        }
    }

    public static List<ProductoGama> getProductosGama(Connection conexion, String gama) {
        List<ProductoGama> productos = new ArrayList<>();
        String query = "SELECT p.codigo_producto, p.nombre, g.id_gama, g.gama " +
                "FROM producto p " +
                "INNER JOIN gama_producto g ON p.id_gama = g.id_gama " +
                "WHERE g.gama = ?"; // Usamos un parámetro para evitar inyección SQL

        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setString(1, gama); // Asignamos el valor de la gama al parámetro
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    String codigoProducto = rs.getString("codigo_producto");
                    String nombreProducto = rs.getString("nombre");
                    int idGama = rs.getInt("id_gama");
                    String nombreGama = rs.getString("gama");

                    ProductoGama producto = new ProductoGama(codigoProducto, nombreProducto, idGama, nombreGama);
                    productos.add(producto);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener productos por gama: " + e.getMessage());
        }
        return productos;
    }

    public static void getPedidosPorCliente(Connection conexion, int idCliente) {
        String query = "SELECT pedido.id_pedido, pedido.fecha_pedido, pedido.id_cliente "
                +
                "FROM pedido " +
                "WHERE pedido.id_cliente = ?";

        try {
            PreparedStatement statement = conexion.prepareStatement(query);
            statement.setInt(1, idCliente);
            ResultSet rs = statement.executeQuery();
            int count = 0;
            while (rs.next()) {
                int idPedido = rs.getInt("id_pedido");
                Date fechaPedido = rs.getDate("fecha_pedido");
                int pedidoIdCliente = rs.getInt("id_cliente");
                if (count == 0) {
                    System.out.println("Pedidos del cliente con ID " + idCliente + ":");
                }
                count++;
                System.out.println("Pedido " + count + ":");
                System.out.println("ID Pedido: " + idPedido);
                System.out.println("Fecha Pedido: " + fechaPedido);

                System.out.println("ID Cliente: " + pedidoIdCliente);
                System.out.println("---------------------------");
            }
            if (count == 0) {
                System.out.println("No se encontraron pedidos para el cliente con ID " + idCliente);
            }else{
                System.out.println("Total de pedidos del cliente es: " + count);
            }

            rs.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error en la consulta: " + e.getMessage());
        }
    }


}
