package com.vmuguerza.servicios;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.vmuguerza.dao.ConexionBD;
import com.vmuguerza.dao.LibroDAO;
import com.vmuguerza.dao.LibroRepositorio;
import com.vmuguerza.modelos.Libro;

public class LibroServicio {

    private static final BigDecimal PRECIO_MINIMO = new BigDecimal(5);

    public List<Libro> obtenerTodosLosLibros() throws SQLException {
        try(Connection conexion = ConexionBD.getConexion()){
            LibroRepositorio libroDAO = new LibroDAO(conexion);
            return libroDAO.obtenerTodosLibros();
        }
    }

    public boolean insertarLibro(Libro libro) throws SQLException{
        try(Connection conexion = ConexionBD.getConexion()){
            LibroRepositorio libroDAO = new LibroDAO(conexion);
            return libroDAO.insertarLibro(libro);
        }        
    }

    // Transaccion y Rollback
    public void aplicarDescuentoALibro(int porcentaje) throws SQLException{
        // fail check
        if(porcentaje < 1 || porcentaje > 99){
            throw new IllegalArgumentException("El porcentaje debe estar entre 1 y 99");
        }
        try(Connection conexion = ConexionBD.getConexion()){
            conexion.setAutoCommit(false); // BEGIN Transaction
            try{ // manejar errores del dao
                LibroRepositorio libroDAO = new LibroDAO(conexion);
                List<Libro> libros = libroDAO.obtenerTodosLibros();
                BigDecimal factor = BigDecimal.valueOf(100 - porcentaje).divide(BigDecimal.valueOf(100));        
                for(Libro libro : libros){
                    BigDecimal nuevoPrecio = 
                        BigDecimal.valueOf(libro.getPrecio()).multiply(factor).setScale(2, RoundingMode.HALF_UP);
                    if(nuevoPrecio.compareTo(PRECIO_MINIMO) < 0){
                        throw new IllegalArgumentException(
                            "El precio de "+ libro.getTitulo() + " quedaria por debajo del precio minimo");
                    }
                    libro.setPrecio(nuevoPrecio.doubleValue());
                    boolean resultado = libroDAO.actualizarLibro(libro);
                    if(!resultado){
                        throw new SQLException("No se pudo actuzalizar: " + libro.getIsbn());
                    }
                }
                conexion.commit(); // COMMIT 
            }catch(SQLException | RuntimeException ex){
                conexion.rollback(); // ROLLBACK
                throw ex;
            }
        }
        
    }
    
}
