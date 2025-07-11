package com.eoi.grupo5.services;


import com.eoi.grupo5.entities.Supermercado;
import com.eoi.grupo5.repositories.SupermercadoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupermercadoService {
    private final SupermercadoRepository supermercadoRepository;

    public SupermercadoService(SupermercadoRepository supermercadoRepository) { this.supermercadoRepository = supermercadoRepository; }

    public List<Supermercado> findAll() {

        List<Supermercado> result = supermercadoRepository.findAll();
        return result;
    }

    public Supermercado findById(Long id) {
        Optional<Supermercado> result = supermercadoRepository.findById(id);

        return result.orElse(null);
    }

    public Supermercado save(Supermercado supermercado) {
        return supermercadoRepository.save(supermercado);
    }

    public void deleteById(Long id) {
        supermercadoRepository.deleteById(id);
    }
}
