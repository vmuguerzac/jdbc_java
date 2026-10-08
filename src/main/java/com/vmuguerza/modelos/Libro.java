package com.vmuguerza.modelos;

public class Libro {
    private String isbn;
    private String titulo;
    private String autor;
    private double precio;
    private int stock;
    // estado

    public Libro(){}

    public Libro(String isbn, String titulo, String autor, double precio, int stock) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.precio = precio;
        this.stock = stock;
    }

    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        if(isbn == null || isbn.isEmpty()){
            throw new IllegalArgumentException("El ISBN no puede estar vacio");
        }
        if(isbn.length() != 13){
            throw new IllegalArgumentException("El ISBN debe tener 13 caracteres");
        }
        this.isbn = isbn;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getAutor() {
        return autor;
    }
    public void setAutor(String autor) {
        this.autor = autor;
    }
    public double getPrecio() {
        return precio;
    }
    public void setPrecio(double precio) {
        if(precio <= 0){
            throw new IllegalArgumentException("El precio es inválido");
        }
        this.precio = precio;
    }
    public int getStock() {
        return stock;
    }
    public void setStock(int stock) {
        // Validaciones simples
        this.stock = stock;
    }

}
