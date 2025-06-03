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
@Table(name = "mensajes")
public class EntidadMensaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "texto")
    private String contenido;
    @Column(name = "instante_post")
    private LocalDateTime fechaPublicacion;
    @Column(name = "votos")
    private Long votos;

    @ManyToOne
    @JoinColumn(name = "id_hilo")
    private EntidadHilo hilo;

    // TODO: Reemplazar con la clase Usuario de Jose Angel cuando esté lista
//    @ManyToOne
//    @JoinColumn(name = "id_escritor")
//    private Usuario autor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_citado")
    private EntidadMensaje citado;

    @OneToMany(mappedBy = "citado", cascade = CascadeType.ALL)
    private List<EntidadMensaje> citadosPor = new ArrayList<>();
}
