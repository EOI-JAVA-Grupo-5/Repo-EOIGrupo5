package com.eoi.grupo5.repositories.foro;

import com.eoi.grupo5.entities.foro.EntidadMensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<EntidadMensaje, Long> {
    List<EntidadMensaje> findByHilo_IdOrderByFechaCreacionAsc(Long hiloId);
}
