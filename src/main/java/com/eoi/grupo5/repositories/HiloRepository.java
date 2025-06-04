package com.eoi.grupo5.repositories;

import com.eoi.grupo5.entities.EntidadHilo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HiloRepository extends JpaRepository<EntidadHilo, Long> {
}
