package com.vmuguerza.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.vmuguerza.modelos.Libro;

public class LibroDAO implements LibroRepositorio  {

    private Connection conn() throws SQLException{
        return ConexionBD.getConexion();
    }

    @Override
    public boolean insertarLibro(Libro libro) {
        int filasAfectadas = 0;
        String query = """
                INSERT INTO Libros (isbn, titulo, autor, precio, stock)
                VALUES(?, ?, ?, ?, ?)
                """;
        try (Connection c = conn()) {
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

    @Override
    public List<Libro> obtenerTodosLibros() {
        List<Libro> resultado = new ArrayList<>();
        String query = """
                SELECT isbn, titulo, autor, precio, stock 
                FROM Libros
            """;
        try(Connection c = conn()){
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

    @Override
    public Optional<Libro> buscarLibroPorISBN(String isbn) {
        Libro resultado;
        String query = """
                SELECT isbn, titulo, autor, precio, stock 
                FROM Libros
                WHERE isbn = ?
            """;
        try(Connection c = conn()){
            PreparedStatement ps = c.prepareStatement(query);
            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                resultado = new Libro();
                resultado.setIsbn(rs.getString("isbn"));
                resultado.setTitulo(rs.getString("titulo"));
                resultado.setAutor(rs.getString("autor"));
                resultado.setPrecio(rs.getDouble("precio"));
                resultado.setStock(rs.getInt("stock"));
                return Optional.of(resultado);
            }
        }catch(SQLException ex){
            System.err.println("Error de SQL: " + ex.getMessage());
        }catch(IllegalArgumentException ex){
            System.err.println("Error en Argumento: " + ex.getMessage());
        }catch(Exception ex){
            System.err.println("Error: " + ex.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean actualizarLibro(Libro libro) {
        int filasAfectadas = 0;
        String query = """
                UPDATE Libros 
                SET titulo = ?, autor = ?, precio = ?, stock = ?
                WHERE isbn = ?
                """;
        try (Connection c = conn()) {
            PreparedStatement ps = c.prepareStatement(query);
            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setDouble(3, libro.getPrecio());
            ps.setInt(4, libro.getStock());
            ps.setString(5, libro.getIsbn());
            // Ejecutar nuestro query
            filasAfectadas = ps.executeUpdate();
            System.out.println(filasAfectadas + " - Filas afectadas"); 
        } catch (SQLException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
        return filasAfectadas==1?true:false; // if inline
    }

    @Override
    public boolean eliminarLibro(String isbn) {
        int filasAfectadas = 0;
        String query = """
                DELETE FROM Libros WHERE isbn = ?
            """;
        try(Connection c = conn()){
            PreparedStatement ps = c.prepareStatement(query);
            ps.setString(1, isbn);
            filasAfectadas = ps.executeUpdate();
        }catch(SQLException ex){
            System.err.println("Error de SQL: " + ex.getMessage());
        }catch(IllegalArgumentException ex){
            System.err.println("Error en Argumento: " + ex.getMessage());
        }catch(Exception ex){
            System.err.println("Error: " + ex.getMessage());
        }
        return filasAfectadas==1?true:false;
    }

    @Override
    public boolean crearTabla() {
        boolean resultado = false;
        // Conexion
        try(Connection c = conn()){
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
            resultado = st.execute(query);
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
        return resultado;
    }
    
}
