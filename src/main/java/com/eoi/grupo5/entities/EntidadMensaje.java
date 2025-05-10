package com.eoi.grupo5.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public EntidadHilo getHilo() {
        return hilo;
    }

    public void setHilo(EntidadHilo hilo) {
        this.hilo = hilo;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }
}
