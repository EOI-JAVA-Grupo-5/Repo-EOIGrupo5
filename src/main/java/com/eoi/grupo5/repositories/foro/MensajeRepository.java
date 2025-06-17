package com.eoi.grupo5.repositories.foro;

import com.eoi.grupo5.entities.foro.MensajeHilo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<MensajeHilo, Long> {
    List<MensajeHilo> findByHilo_IdOrderByFechaCreacionAsc(Long hiloId);
}
