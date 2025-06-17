package com.eoi.grupo5.entities.foro;

import com.eoi.grupo5.entities.Usuario;
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
public class Hilo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private String descripcion;

    private long votos = 0;

    private long visitas = 0;

    private LocalDateTime fechaCreacion;

    private long mensajeCount = 0;

    @ManyToOne
    @JoinColumn(name = "id_creador")
    private Usuario autor;

    @OneToMany(mappedBy = "hilo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<MensajeHilo> mensajes = new ArrayList<>();
}
