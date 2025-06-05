package com.eoi.grupo5.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails, Serializable {

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
    @Column(unique = true)
    private String username;

    @NonNull
    private String password;

    @NonNull
    @Column(unique = true)
    private String  correo;

    private String  imagenURL;

    private String  nombre;

    private String  apellidos;

    private String  telefono;

    @NonNull
    @Enumerated(EnumType.STRING)
    private Tipo tipo;


    /**
     * Inserción de enum "Tipo"
     */

    public static enum Tipo{
        ADMIN, USER
    }

    /**
     * MÉTODOS DE UserDetails
     */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }


}
