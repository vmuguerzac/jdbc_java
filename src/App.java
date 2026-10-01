import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.vmuguerza.modelos.Libro;

public class App {
    // String de conexion
    private static String url = "jdbc:hsqldb:file:data/pageturner;shutdown=true";
    //private static String url = "jdbc:mysql://localhost:3306/pageturner";
    public static void main(String[] args) throws Exception {
        // Prueba conexion y creacion de tabla Libro
        probarConexion();
        Libro libro = obtenerInformacionLibro();
        // Patron CRUD - Create (Insert), Read, Update y Delete
        boolean resultado = insertarLibro(libro);
        if(resultado){
            System.out.println("Se ha insertado los datos con exito");
        }else{
            System.out.println("No se ha insertado la informacion");
        }
    }
    
    public static Libro obtenerInformacionLibro(){
        // INSERTAR
        // Crear nuestro objeto
        Libro libro = new Libro();
        libro.setIsbn("9786124262784");
        libro.setTitulo("Cien años de soledad");
        libro.setAutor("Gabriel Garcia Marquez");
        libro.setPrecio(150.00);
        libro.setStock(10);
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
}
