package com.vmuguerza.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.vmuguerza.modelos.Libro;

public class LibroDAO implements LibroRepositorio  {
    private final Connection conexion; // declarar la conexion

    public LibroDAO(Connection conexion){
        this.conexion = conexion;
    }

    @Override
    public boolean insertarLibro(Libro libro) throws SQLException{
        int filasAfectadas = 0;
        String query = """
                INSERT INTO Libros (isbn, titulo, autor, precio, stock)
                VALUES(?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setString(1, libro.getIsbn());
            ps.setString(2, libro.getTitulo());
            ps.setString(3, libro.getAutor());
            ps.setDouble(4, libro.getPrecio());
            ps.setInt(5, libro.getStock());
            // Ejecutar nuestro query
            filasAfectadas = ps.executeUpdate();
            System.out.println(filasAfectadas + " - Filas afectadas"); 
        }
        return filasAfectadas==1; // if inline
    }

    @Override
    public List<Libro> obtenerTodosLibros() throws SQLException {
        List<Libro> resultado = new ArrayList<>();
        String query = """
                SELECT isbn, titulo, autor, precio, stock 
                FROM Libros
            """;
        try(PreparedStatement ps = conexion.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
            ){
            while(rs.next()){ // navegar en los resultados de la BD
                Libro libro = mapearLibro(rs);
                resultado.add(libro);
            }
        }
        return resultado;
    }

    @Override
    public Optional<Libro> buscarLibroPorISBN(String isbn) throws SQLException {
        Libro resultado;
        String query = """
                SELECT isbn, titulo, autor, precio, stock 
                FROM Libros
                WHERE isbn = ?
            """;
        try(PreparedStatement ps = conexion.prepareStatement(query)){
            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                resultado = mapearLibro(rs);
                return Optional.of(resultado);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean actualizarLibro(Libro libro) throws SQLException {
        int filasAfectadas = 0;
        String query = """
                UPDATE Libros 
                SET titulo = ?, autor = ?, precio = ?, stock = ?
                WHERE isbn = ?
                """;
        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setDouble(3, libro.getPrecio());
            ps.setInt(4, libro.getStock());
            ps.setString(5, libro.getIsbn());
            // Ejecutar nuestro query
            filasAfectadas = ps.executeUpdate();
            System.out.println(filasAfectadas + " - Filas afectadas"); 
        } 
        return filasAfectadas==1; // if inline
    }

    @Override
    public boolean eliminarLibro(String isbn) throws SQLException {
        int filasAfectadas = 0;
        String query = """
                DELETE FROM Libros WHERE isbn = ?
            """;
        try(PreparedStatement ps = conexion.prepareStatement(query)){
            ps.setString(1, isbn);
            filasAfectadas = ps.executeUpdate();
        }
        return filasAfectadas==1;
    }

    @Override
    public void crearTabla() throws SQLException { 
        // Conexion
        try(Statement st = conexion.createStatement();){
            // Crear primera tabla
            String query = """
                    CREATE TABLE IF NOT EXISTS Libros(
                        isbn VARCHAR(13) PRIMARY KEY,
                        titulo VARCHAR(200),
                        autor VARCHAR(100),
                        precio DOUBLE,
                        stock INT
                    )
                    """;
            st.execute(query);
        }
        System.out.println("Creacion de Tabla");
    }

    private Libro mapearLibro(ResultSet rs) throws SQLException {
        Libro libro = new Libro();
        libro.setIsbn(rs.getString("isbn"));
        libro.setTitulo(rs.getString("titulo"));
        libro.setAutor(rs.getString("autor"));
        libro.setPrecio(rs.getDouble("precio"));
        libro.setStock(rs.getInt("stock"));
        return libro;
    }
    
}
