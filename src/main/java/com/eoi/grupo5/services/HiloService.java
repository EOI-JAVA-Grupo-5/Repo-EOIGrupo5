package com.eoi.grupo5.services;

import com.eoi.grupo5.entities.EntidadHilo;
import com.eoi.grupo5.repositories.HiloRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HiloService {
    private final HiloRepository hiloRepo;

    public HiloService(HiloRepository hiloRepo) {
        this.hiloRepo = hiloRepo;
    }
    public List<EntidadHilo> obtenerHilos(){
        return hiloRepo.findAll();
    }

    public EntidadHilo obtenerHiloPorId(Long id) {
        Optional<EntidadHilo> hilo = hiloRepo.findById(id);

        if (hilo.isPresent()) {
            return hilo.get();
        } else {
            throw new RuntimeException("Hilo no encontrado");
        }
    }

    public EntidadHilo guardarHilo(EntidadHilo hilo){
        return hiloRepo.save(hilo);
    }
}
