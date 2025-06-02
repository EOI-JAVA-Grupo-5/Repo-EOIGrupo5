package com.eoi.grupo5.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carritos")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String usuarioId; // O puedes usar una relación @ManyToOne con entidad Usuario si tienes

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarritoItem> items = new ArrayList<>();

    // Constructor vacío
    public Carrito() {
    }

    public Carrito(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    // Getters y setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public List<CarritoItem> getItems() { return items; }
    public void setItems(List<CarritoItem> items) { this.items = items; }

    // Métodos de utilidad para agregar y quitar items

    public void agregarItem(CarritoItem item) {
        items.add(item);
        item.setCarrito(this);
    }

    public void removerItem(CarritoItem item) {
        items.remove(item);
        item.setCarrito(null);
    }

    public Double calcularTotal() {
        return items.stream()
                    .mapToDouble(CarritoItem::getPrecioTotal)
                    .sum();
    }
}
