package com.eoi.grupo5.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordModDTO {

    /**
     * Atributos para modificar la contraseña del usuario
     */

    private String claveActual;
    private String claveNueva;
    private String claveNuevaRepe;
}
