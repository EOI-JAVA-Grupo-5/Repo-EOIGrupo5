package com.eoi.grupo5.entities;



public class Carrito {

    private Long productoId; // O el tipo de ID que uses para tus productos
    private String nombre;
    private double precio_unidad;
    private int cantidad;
    private String imagen_url; // Para la imagen del producto en el carrito

    // Constructor vacío (necesario para muchos frameworks como Spring, JPA)
    public Carrito() {
    }

    // Constructor con todos los campos
    public Carrito(Long productoId, String nombre, double precio_unidad, int cantidad, String imagen_url) {
        this.productoId = productoId;
        this.nombre = nombre;
        this.precio_unidad = precio_unidad;
        this.cantidad = cantidad;
        this.imagen_url = imagen_url;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio_unidad(double precio_unidad) {
        this.precio_unidad = precio_unidad;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getImagen_url() {
        return imagen_url;
    }

    public void setImagen_url(String imagen_url) {
        this.imagen_url = imagen_url;
    }

    // Método para calcular el total del producto en el carrito
    public double getPrecio_unidad() {
        return this.precio_unidad * this.cantidad;
    }

    @Override
    public String toString() {
        return "ProductInCart{" +
                "productId=" + productoId +
                ", name='" + nombre + '\'' +
                ", pricePerUnit=" + precio_unidad +
                ", quantity=" + cantidad +
                ", imageUrl='" + imagen_url + '\'' +
                '}';
    }
}
