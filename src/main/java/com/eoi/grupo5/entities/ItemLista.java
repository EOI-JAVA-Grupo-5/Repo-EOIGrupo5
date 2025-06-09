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
@Table(name = "items_de_listas")
public class ItemLista implements Serializable {

    /**
     * The serialVersionUID
     */

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Atributos de ItemLista
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Lista.class)
    private Lista lista;

    private int cantidadComprada;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;
}
