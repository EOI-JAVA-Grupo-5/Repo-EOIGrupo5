package com.eoi.grupo5.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "mensajes")
public class EntidadMensaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "texto")
    private String contenido;
    @Column(name = "instantepost")
    private LocalDateTime fechaPublicacion;
    @Column(name = "votos")
    private Long votos;

    @ManyToOne
    @JoinColumn(name = "hilo_id")
    private EntidadHilo hilo;

    // TODO: Reemplazar con la clase Usuario de Jose Angel cuando esté lista
    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Usuario autor;
}
