package com.eoi.grupo5.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "listas")
public class Lista implements Serializable {

    /**
     * The serialVersionUID
     */

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Atributos de Lista
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Usuario.class)
    private Usuario usuario;

    private Date fecha;

    private BigDecimal costeTotal;

    private BigDecimal dineroAhorrado;
}
