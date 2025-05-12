package com.eoi.grupo5.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class EntidadMensaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String contenido;
    private LocalDateTime fechaPublicacion;

    @ManyToOne
    @JoinColumn(name = "hilo_id")
    private EntidadHilo hilo;

    // TODO: Reemplazar con la clase Usuario de Jose Angel cuando esté lista
    @ManyToOne
    private Usuario autor;
}
