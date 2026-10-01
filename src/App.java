import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.vmuguerza.modelos.Libro;

public class App {
    // String de conexion
    private static String url = "jdbc:hsqldb:file:data/pageturner;shutdown=true";
    //private static String url = "jdbc:mysql://localhost:3306/pageturner";
    public static void main(String[] args) throws Exception {
        // Prueba conexion y creacion de tabla Libro
        probarConexion();
        //Libro libro = obtenerInformacionLibro(); // Obtener info del libro por parte cliente
        // Patron CRUD - Create (Insert), Read, Update y Delete
        // Insert
        //boolean resultado = insertarLibro(libro);
        //if(resultado){
        //    System.out.println("Se ha insertado los datos con exito");
        //}else{
        //    System.out.println("No se ha insertado la informacion");
        //}
        // Read
        System.out.println("\n======= Obtener todos los Libros ======= ");
        List<Libro> libros = obtenerTodosLibros();
        libros.forEach(l-> 
            System.out.println("ISBN: " + l.getIsbn() + 
                                " Autor: " + l.getAutor() + 
                                " Precio: " + l.getPrecio())
        );
        System.out.println("\n======= Buscar por ISBN ======= ");
        String isbn = "9786124262781";
        Libro libroBuscado = buscarLibroPorISBN(isbn);
        System.out.println("ISBN: " + libroBuscado.getIsbn() + 
                            " Autor: " + libroBuscado.getAutor() + 
                            " Precio: " + libroBuscado.getPrecio());
    }
    
    public static Libro obtenerInformacionLibro(){
        // INSERTAR
        // Crear nuestro objeto
        Libro libro = new Libro();
        try(Scanner scanner = new Scanner(System.in)){
            System.out.println("Ingrese ISBN: ");
            String isbn = scanner.nextLine();
            libro.setIsbn(isbn);

            System.out.println("Ingrese Titulo del Libro: ");
            String titulo = scanner.nextLine();
            libro.setTitulo(titulo);

            System.out.println("Ingrese Nombre del Autor: ");
            String autor = scanner.nextLine();
            libro.setAutor(autor);

            System.out.println("Ingrese precio del libro: ");
            double precio = scanner.nextDouble();
            libro.setPrecio(precio);

            System.out.println("Ingrese stock del libro");
            int stock = scanner.nextInt();
            libro.setStock(stock);
        }catch(IllegalArgumentException ex){
            System.out.println("Error: " + ex.getMessage());
        }catch(Exception ex){
            System.out.println("Error General: " + ex.getMessage());
        }
        return libro;
    }
    
    public static void probarConexion(){
        // Conexion
        try(Connection c = DriverManager.getConnection(url, "SA", "")){
            // Obtener metadatos del gestor de base de datos
            String motor = c.getMetaData().getDatabaseProductName();
            System.out.println("Conexion OK: " + motor);
            // Crear primera tabla
            Statement st = c.createStatement();
            String query = """
                    CREATE TABLE IF NOT EXISTS Libros(
                        isbn VARCHAR(13) PRIMARY KEY,
                        titulo VARCHAR(200),
                        autor VARCHAR(100),
                        precio DOUBLE,
                        stock INT
                    )
                    """;
            boolean resultado = st.execute(query);
            if(resultado == true){
                System.out.println("Tabla de Libros creada");    
            }else{
                System.out.println("Tabla de Libros no creada");
            }
        }catch(SQLException ex){
            System.err.println("Error SQL: " + ex.getMessage());
        }catch(Exception ex){
            System.err.println("Error General: " + ex.getMessage());
        }
    }

    public static boolean insertarLibro(Libro libro){
        int filasAfectadas = 0;
        String query = """
                INSERT INTO Libros (isbn, titulo, autor, precio, stock)
                VALUES(?, ?, ?, ?, ?)
                """;
        try (Connection c = DriverManager.getConnection(url, "SA", "")) {
            PreparedStatement ps = c.prepareStatement(query);
            ps.setString(1, libro.getIsbn());
            ps.setString(2, libro.getTitulo());
            ps.setString(3, libro.getAutor());
            ps.setDouble(4, libro.getPrecio());
            ps.setInt(5, libro.getStock());
            // Ejecutar nuestro query
            filasAfectadas = ps.executeUpdate();
            System.out.println(filasAfectadas + " - Filas afectadas"); 
        } catch (SQLException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
        return filasAfectadas==1?true:false; // if inline
    }

    public static List<Libro> obtenerTodosLibros(){
        List<Libro> resultado = new ArrayList<>();
        String query = """
                SELECT isbn, titulo, autor, precio, stock 
                FROM Libros
            """;
        try(Connection c = DriverManager.getConnection(url, "SA", "")){
            PreparedStatement ps = c.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){ // navegar en los resultados de la BD
                Libro libro = new Libro();
                libro.setIsbn(rs.getString("isbn"));
                libro.setTitulo(rs.getString("titulo"));
                libro.setAutor(rs.getString("autor"));
                libro.setPrecio(rs.getDouble("precio"));
                libro.setStock(rs.getInt("stock"));
                resultado.add(libro);
            }
        }catch(SQLException ex){
            System.err.println("Error de SQL: " + ex.getMessage());
        }catch(IllegalArgumentException ex){
            System.err.println("Error en Argumento: " + ex.getMessage());
        }catch(Exception ex){
            System.err.println("Error: " + ex.getMessage());
        }
        return resultado;
    }

    public static Libro buscarLibroPorISBN(String isbn){
        Libro resultado = new Libro();
        String query = """
                SELECT isbn, titulo, autor, precio, stock 
                FROM Libros
                WHERE isbn = ?
            """;
        try(Connection c = DriverManager.getConnection(url, "SA", "")){
            PreparedStatement ps = c.prepareStatement(query);
            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                resultado.setIsbn(rs.getString("isbn"));
                resultado.setTitulo(rs.getString("titulo"));
                resultado.setAutor(rs.getString("autor"));
                resultado.setPrecio(rs.getDouble("precio"));
                resultado.setStock(rs.getInt("stock"));
            }
        }catch(SQLException ex){
            System.err.println("Error de SQL: " + ex.getMessage());
        }catch(IllegalArgumentException ex){
            System.err.println("Error en Argumento: " + ex.getMessage());
        }catch(Exception ex){
            System.err.println("Error: " + ex.getMessage());
        }
        return resultado != null?resultado: null;
    }


}
