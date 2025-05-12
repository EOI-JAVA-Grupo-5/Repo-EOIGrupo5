package com.eoi.grupo5.services;

import com.eoi.grupo5.entities.EntidadHilo;
import com.eoi.grupo5.repositories.HiloRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HiloService {
    private final HiloRepository hiloRepo;

    public HiloService(HiloRepository hiloRepo) {
        this.hiloRepo = hiloRepo;
    }
    public List<EntidadHilo> obtenerHilos(){
        return hiloRepo.findAll();
    }

    public EntidadHilo guardarHilo(EntidadHilo hilo){
        return hiloRepo.save(hilo);
    }
}
