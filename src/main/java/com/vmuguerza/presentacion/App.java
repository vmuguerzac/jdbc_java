package com.vmuguerza.presentacion;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.vmuguerza.dao.LibroDAO;
import com.vmuguerza.modelos.Libro;
import com.vmuguerza.servicios.LibroServicio;

public class App {    
    public static void main(String[] args) throws Exception {
        LibroServicio libroServicio = new LibroServicio();

        //Libro libro = obtenerInformacionLibro(); // Obtener info del libro por parte cliente
        // Patron CRUD - Create (Insert), Read, Update y Delete
        // Insert
        //boolean resultado = libroDAO.insertarLibro(libro);
        //if(resultado){
        //    System.out.println("Se ha insertado los datos con exito");
        //}else{
        //    System.out.println("No se ha insertado la informacion");
        //}
        // Read
        try {
            System.out.println("\n======= Libros antes de descuento ======= ");
            List<Libro> libros = libroServicio.obtenerTodosLosLibros();
            libros.forEach(l-> 
                System.out.println("ISBN: " + l.getIsbn() + 
                                    " Titulo: " + l.getTitulo() + 
                                    " Precio: " + l.getPrecio())
            );
            libroServicio.aplicarDescuentoALibro(90);
        }catch(IllegalArgumentException ex){
            System.out.println("No se aplico descuento: " + ex.getMessage());
        }catch(SQLException ex){
            System.out.println("Error en base de datos intente luego");
        }
        System.out.println("\n======= Libros despues de descuento ======= ");
        List<Libro> librosTodos = libroServicio.obtenerTodosLosLibros();
        librosTodos.forEach(l-> 
            System.out.println("ISBN: " + l.getIsbn() + 
                                " Titulo: " + l.getTitulo() + 
                                " Precio: " + l.getPrecio())
        );
        /* 
        System.out.println("\n======= Buscar por ISBN ======= ");
        String isbn = "9786124262784";
        Optional<Libro> libroBuscado = libroDAO.buscarLibroPorISBN(isbn);
        if(libroBuscado.isEmpty()){
            System.out.println("No existe el libro para el ISBN buscado");
        }else{
            System.out.println("ISBN: " + libroBuscado.get().getIsbn() + 
                            " Autor: " + libroBuscado.get().getAutor() + 
                            " Precio: " + libroBuscado.get().getPrecio());
        }
        System.out.println("\n======= Actualizar ======= ");
        libroBuscado.get().setPrecio(200.00); // actualizar el precio
        resultado = libroDAO.actualizarLibro(libroBuscado.get()); // ejecutar la act contra la BD
        libros = libroDAO.obtenerTodosLibros();
        libros.forEach(l-> 
            System.out.println("ISBN: " + l.getIsbn() + 
                                " Autor: " + l.getAutor() + 
                                " Precio: " + l.getPrecio())
        );
        System.out.println("\n======= Eliminar ======= ");
        isbn = "9786124262784";
        resultado = libroDAO.eliminarLibro(isbn);
        if(resultado){
            System.out.println("Libro eliminado con exito");
        }else{
            System.out.println("Libro no eliminado");
        }
        libros = libroDAO.obtenerTodosLibros();
        libros.forEach(l-> 
            System.out.println("ISBN: " + l.getIsbn() + 
                                " Autor: " + l.getAutor() + 
                                " Precio: " + l.getPrecio())
        );
        */
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
    
    
}
