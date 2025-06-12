package com.eoi.grupo5.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Producto {

    @Id
    private Long id;

    private String name;
    private String supermarket;
    private Integer zipCode;
    private Double price;
    private String url;
    private String category;

    // Constructor vacío obligatorio para JPA
    public Producto() {
    }

    // Constructor completo (opcional, útil para pruebas o carga manual)
    public Producto(Long id, String name, String supermarket, Integer zipCode, Double price, String url, String category) {
        this.id = id;
        this.name = name;
        this.supermarket = supermarket;
        this.zipCode = zipCode;
        this.price = price;
        this.url = url;
        this.category = category;
    }
}

