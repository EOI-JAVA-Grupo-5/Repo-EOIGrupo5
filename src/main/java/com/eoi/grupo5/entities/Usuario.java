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
@Table(name = "usuarios")
public class Usuario implements Serializable {

    /**
     * SerialVersion
     * */

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Atributos de Usuario
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    private String nick;

    @NonNull
    private String password;

    @NonNull
    private String  correo;

    private String  imagenURL;

    private String  nombre;

    private String  apellidos;

    private String  telefono;

    @NonNull
    private Tipo tipo;


    /**
     * Inserción de enum "Tipo"
     */

    public static enum Tipo{
        ADMIN, USER
    }

}
