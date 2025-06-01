package com.eoi.grupo5.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "relaciones_seguidores")
public class RelacionUsuarios implements Serializable {

    /**
     * The serialVersionUID
     */

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Atributos de RelacionesUsuarios
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @NonNull
    @OneToOne(targetEntity = Usuario.class, fetch = FetchType.LAZY)
    private Usuario usuarioQueSigue;


    @NonNull
    @OneToOne(targetEntity = Usuario.class, fetch = FetchType.LAZY)
    private Usuario usuarioSeguido;
}
