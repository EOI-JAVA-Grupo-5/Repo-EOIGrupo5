package com.eoi.grupo5.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "mensajes")
@NoArgsConstructor
@AllArgsConstructor
public class EntidadMensaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String contenido;
    private LocalDateTime fechaCreacion;
    private Long votos;

    @ManyToOne
    @JoinColumn(name = "id_hilo")
    private EntidadHilo hilo;

    @ManyToOne
    @JoinColumn(name = "id_escritor")
    private Usuario autor;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "id_citado")
//    private EntidadMensaje citado;
//
//    @OneToMany(mappedBy = "citado", cascade = CascadeType.ALL)
//    private List<EntidadMensaje> citadosPor = new ArrayList<>();
}
