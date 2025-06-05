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
    @Column(name = "id")
    private Long id;
    @Column (name = "titulo")
    private String titulo;
    @Column (name ="descripcion")
    private String descripcion;
    @Column (name = "votos")
    private Long votos;
    @Column (name = "visitas")
    private Long visitas;
    @Column (name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    @Column (name = "mensaje_count")
    private Long mensajeCount;

    @ManyToOne
    @JoinColumn(name = "id_creador") // This is what Hibernate auto-generates anyway
    private Usuario autor;

    @OneToMany(mappedBy = "hilo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<EntidadMensaje> mensajes = new ArrayList<>();
}
