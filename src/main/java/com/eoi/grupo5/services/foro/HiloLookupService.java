package com.eoi.grupo5.services.foro;

import com.eoi.grupo5.entities.foro.Hilo;
import com.eoi.grupo5.repositories.foro.HiloRepository;
import org.springframework.stereotype.Service;

/**
 * Decision por tener una tercera service, Spring no me dejaba tener un bucle de inyecciones entre Mensaje y Hilo service.
 * Entonces he tenido que crear este service extra para los metodos que hacen falta en el MensajeService.
 */

@Service
public class HiloLookupService {
    private final HiloRepository hiloRepository;

    public HiloLookupService(HiloRepository hiloRepository) {
        this.hiloRepository = hiloRepository;
    }

    public Hilo findById(Long id) {
        return hiloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hilo no encontrado"));
    }

    public void saveHilo(Hilo hilo) {
        hiloRepository.save(hilo);
    }
}