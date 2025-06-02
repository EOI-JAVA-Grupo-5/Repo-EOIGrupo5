package com.eoi.grupo5.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "carrito_items")
public class CarritoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreProducto;

    private Integer cantidad;

    private Double precioUnitario;

    private Double precioTotal;

    private LocalDateTime fechaCreacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrito_id")
    private Carrito carrito;

    public CarritoItem() {
        this.fechaCreacion = LocalDateTime.now();
    }

    public CarritoItem(String nombreProducto, Integer cantidad, Double precioUnitario) {
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.precioTotal = precioUnitario * cantidad;
        this.fechaCreacion = LocalDateTime.now();
    }

    // Getters y setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
        actualizarPrecioTotal();
    }

    public Double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
        actualizarPrecioTotal();
    }

    public Double getPrecioTotal() { return precioTotal; }
    private void actualizarPrecioTotal() {
        if(precioUnitario != null && cantidad != null) {
            this.precioTotal = precioUnitario * cantidad;
        } else {
            this.precioTotal = 0.0;
        }
    }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Carrito getCarrito() { return carrito; }
    public void setCarrito(Carrito carrito) { this.carrito = carrito; }
}
