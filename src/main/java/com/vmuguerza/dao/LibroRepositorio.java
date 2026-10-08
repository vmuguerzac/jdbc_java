package com.vmuguerza.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.vmuguerza.modelos.Libro;

public interface LibroRepositorio {
    public void crearTabla() throws SQLException;
    public boolean insertarLibro(Libro libro) throws SQLException;
    public List<Libro> obtenerTodosLibros() throws SQLException;
    public Optional<Libro> buscarLibroPorISBN(String isbn) throws SQLException;
    public boolean actualizarLibro(Libro libro) throws SQLException;
    public boolean eliminarLibro(String isbn) throws SQLException;
}
