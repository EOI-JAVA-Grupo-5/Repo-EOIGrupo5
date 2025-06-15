package com.eoi.grupo5.repositories;

import com.eoi.grupo5.entities.Lista;
import com.eoi.grupo5.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ListaRepository extends JpaRepository<Lista, Integer> {
    Optional<List<Lista>> findAllByUsuario(Usuario usuario);

    Optional<Lista> findListaById(Long id);

}
