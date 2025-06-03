package com.eoi.grupo5.repositories;

import com.eoi.grupo5.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findUsuarioById(Long id);

    Optional<Usuario> findUsuarioByNombreUsuario(String nombreUsuario);
}
