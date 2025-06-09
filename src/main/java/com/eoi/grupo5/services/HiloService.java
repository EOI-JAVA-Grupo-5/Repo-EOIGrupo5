package com.eoi.grupo5.services;

import com.eoi.grupo5.entities.EntidadHilo;
import com.eoi.grupo5.repositories.HiloRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HiloService {
    private final HiloRepository hiloRepository;

    public HiloService(HiloRepository hiloRepository) {
        this.hiloRepository = hiloRepository;
    }

    public List<EntidadHilo> obtenerHilos() {
        return hiloRepository.findAll();
    }

    public EntidadHilo guardarHilo(EntidadHilo hilo) {
        return hiloRepository.save(hilo);
    }

    public Page<EntidadHilo> obtenerHilosRecientesPaginado(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaCreacion").descending());
        return hiloRepository.findAllByOrderByFechaCreacionDesc(pageable);
    }

    public List<EntidadHilo> buscarYOrdenarHilos(String keyword, String sortType) {
        Sort sortCriteria;

        switch (sortType) {
            case "votos":
                sortCriteria = Sort.by(Sort.Direction.DESC, "votos");
                break;
            case "visitas":
                sortCriteria = Sort.by(Sort.Direction.DESC, "visitas");
                break;
            case "mensajes":
                sortCriteria = Sort.by(Sort.Direction.DESC, "mensajeCount");
                break;
            default:
                sortCriteria = Sort.by(Sort.Direction.DESC, "fechaCreacion");
                break;
        }

        if (keyword == null || keyword.trim().isEmpty()) {
            // No keyword: return all, sorted
            return hiloRepository.findAll(sortCriteria);
        } else {
            // Keyword provided: return filtered and sorted
            return hiloRepository.findByTituloContainingIgnoreCase(keyword, sortCriteria);
        }
    }

    public EntidadHilo obtenerHiloPorId(Long id) {
        return hiloRepository.findById(id).orElse(null);
    }
}

