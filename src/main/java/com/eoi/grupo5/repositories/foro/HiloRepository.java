package com.eoi.grupo5.repositories.foro;

import com.eoi.grupo5.entities.foro.Hilo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HiloRepository extends JpaRepository<Hilo, Long> {
    Page<Hilo> findAllByOrderByFechaCreacionDesc(Pageable pageable);

    List<Hilo> findByTituloContainingIgnoreCase(String keyword, Sort sort);

    List<Hilo> findByAutor_UsernameAndTituloContainingIgnoreCase(String username, String keyword, Sort sort);

    List<Hilo> findByAutor_Username(String username, Sort sort);
}
