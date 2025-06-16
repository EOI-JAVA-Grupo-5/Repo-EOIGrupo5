package com.eoi.grupo5.services;

import com.eoi.grupo5.entities.EntidadHilo;
import com.eoi.grupo5.entities.EntidadMensaje;
import com.eoi.grupo5.repositories.MensajeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MensajeService {

    private final MensajeRepository mensajeRepository;

    public MensajeService(MensajeRepository mensajeRepo) {
        this.mensajeRepository = mensajeRepo;
    }

    public List<EntidadMensaje> findMessagesByHiloId(Long hiloId) {
        return mensajeRepository.findByHilo_IdOrderByFechaCreacionAsc(hiloId);
    }

    public EntidadMensaje saveMessage(EntidadMensaje mensaje) {
        return mensajeRepository.save(mensaje);
    }

    public EntidadMensaje findMessageById(Long id) {

        EntidadMensaje mensaje = mensajeRepository.findById(id).get();

        if (mensaje == null) {
            throw new EntityNotFoundException("Mensaje no encontrado");
        }

        return mensaje;
    }

    @Transactional
    public void deleteMessageById(Long id) {
        EntidadMensaje mensaje = mensajeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mensaje no encontrado"));

        EntidadHilo hilo = mensaje.getHilo();
        if (hilo != null) {
            hilo.getMensajes().remove(mensaje);
        }

        mensajeRepository.delete(mensaje);
    }
}
