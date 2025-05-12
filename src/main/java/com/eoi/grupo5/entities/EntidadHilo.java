package com.eoi.grupo5.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
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
}
