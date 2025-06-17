package com.eoi.grupo5.entities.foro;

import com.eoi.grupo5.entities.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "mensajes")
@NoArgsConstructor
@AllArgsConstructor
public class MensajeHilo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String contenido;
    private LocalDateTime fechaCreacion;
//    private Long votos;

    @ManyToOne
    @JoinColumn(name = "id_hilo")
    private Hilo hilo;

    @ManyToOne
    @JoinColumn(name = "id_escritor")
    private Usuario autor;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "id_citado")
//    private MensajeHilo citado;
//
//    @OneToMany(mappedBy = "citado", cascade = CascadeType.ALL)
//    private List<MensajeHilo> citadosPor = new ArrayList<>();
}
