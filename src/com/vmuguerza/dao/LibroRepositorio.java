package com.vmuguerza.dao;

import java.util.List;
import java.util.Optional;

import com.vmuguerza.modelos.Libro;

public interface LibroRepositorio {
    public boolean crearTabla();
    public boolean insertarLibro(Libro libro);
    public List<Libro> obtenerTodosLibros();
    public Optional<Libro> buscarLibroPorISBN(String isbn);
    public boolean actualizarLibro(Libro libro);
    public boolean eliminarLibro(String isbn);
}
