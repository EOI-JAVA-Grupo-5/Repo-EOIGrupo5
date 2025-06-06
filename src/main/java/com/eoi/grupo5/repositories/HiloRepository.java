package com.eoi.grupo5.repositories;

import com.eoi.grupo5.entities.EntidadHilo;
import com.eoi.grupo5.entities.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HiloRepository extends JpaRepository<EntidadHilo, Long> {
    Page<EntidadHilo> findAllByOrderByFechaCreacionDesc(Pageable pageable);

    List<EntidadHilo> findByAutor(Usuario autor);

    List<EntidadHilo> findByTituloContainingIgnoreCase(String keyword, Sort sort);
}
