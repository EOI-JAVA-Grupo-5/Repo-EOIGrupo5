package com.eoi.grupo5.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

// Clase temporal para pruebas — será reemplazada por Jose Angel
@Entity
public class Usuario {
    @Id
    private Long id;
    private String nombreUsuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
}
