package com.eoi.grupo5.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

// Clase temporal para pruebas — será reemplazada por Jose Angel
@Getter
@Setter
@Entity
public class Usuario {
    @Id
    private Long id;
    private String nombreUsuario;
}
