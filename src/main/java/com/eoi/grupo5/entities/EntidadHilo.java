package com.eoi.grupo5.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class EntidadHilo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String descripcion;
    private Long favoritos;
    private Long visitas;
    private LocalDateTime fechaCreacion;

    // TODO: Reemplazar con la clase Usuario de Jose Angel cuando esté lista
    @ManyToOne
    private Usuario autor;

    @OneToMany(mappedBy = "hilo", cascade = CascadeType.ALL)
    private List<EntidadMensaje> mensajes;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getFavoritos() {
        return favoritos;
    }

    public void setFavoritos(Long favoritos) {
        this.favoritos = favoritos;
    }

    public Long getVisitas() {
        return visitas;
    }

    public void setVisitas(Long visitas) {
        this.visitas = visitas;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    public List<EntidadMensaje> getMensajes() {
        return mensajes;
    }

    public void setMensajes(List<EntidadMensaje> mensajes) {
        this.mensajes = mensajes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
