package com.eoi.grupo5.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

    // Getters y Setters

}

