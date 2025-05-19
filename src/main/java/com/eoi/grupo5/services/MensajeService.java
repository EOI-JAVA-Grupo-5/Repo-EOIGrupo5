package com.eoi.grupo5.services;

import com.eoi.grupo5.entities.EntidadMensaje;
import com.eoi.grupo5.repositories.MensajeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MensajeService {

    private final MensajeRepository mensajeRepo;

    public MensajeService(MensajeRepository mensajeRepo) {
        this.mensajeRepo = mensajeRepo;
    }

    public List<EntidadMensaje> findMessagesByHiloId(Long hiloId) {
        // Write logic to populate the List of messages

        // Return populated List of messages
        return null;
    }

    public EntidadMensaje guardarMensaje(EntidadMensaje mensaje) {
        return mensajeRepo.save(mensaje);
    }

    public Optional<EntidadMensaje> obtenerMensajePorId(Long id) {
        return mensajeRepo.findById(id);
    }

    public void borrarMensaje(Long id) {
        mensajeRepo.deleteById(id);
    }
}
