package com.eoi.grupo5.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "hilos")
public class EntidadHilo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descripcion;
    private Long votos;
    private Long visitas;
    private LocalDateTime fechaCreacion;
    private Long mensajeCount;

    @ManyToOne
    @JoinColumn(name = "id_creador")
    private Usuario autor;

    @OneToMany(mappedBy = "hilo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<EntidadMensaje> mensajes = new ArrayList<>();
}
