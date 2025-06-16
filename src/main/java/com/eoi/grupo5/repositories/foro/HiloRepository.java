package com.eoi.grupo5.repositories.foro;

import com.eoi.grupo5.entities.foro.EntidadHilo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HiloRepository extends JpaRepository<EntidadHilo, Long> {
    Page<EntidadHilo> findAllByOrderByFechaCreacionDesc(Pageable pageable);

    List<EntidadHilo> findByTituloContainingIgnoreCase(String keyword, Sort sort);

    List<EntidadHilo> findByAutor_UsernameAndTituloContainingIgnoreCase(String username, String keyword, Sort sort);

    List<EntidadHilo> findByAutor_Username(String username, Sort sort);
}
