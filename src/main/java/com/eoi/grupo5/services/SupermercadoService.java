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
        // Add debug logging
        List<Supermercado> result = supermercadoRepository.findAll();
        System.out.println("SupermercadoService.findAll() - Resultados: " +
                (result != null ? result.size() : "null"));
        return result;

        //return supermercadoRepository.findAll();
    }

    public Supermercado findById(Long id) {
        // Add debug logging
        Optional<Supermercado> result = supermercadoRepository.findById(id);
        System.out.println("SupermercadoService.findById(" + id + ") - Encontrado: " +
                result.isPresent());
        return result.orElse(null);

        //return supermercadoRepository.findById(id);
    }

    public Supermercado save(Supermercado supermercado) {
        return supermercadoRepository.save(supermercado);
    }

    public void deleteById(Long id) {
        supermercadoRepository.deleteById(id);
    }
}
