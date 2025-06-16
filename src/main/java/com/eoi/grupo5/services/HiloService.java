package com.eoi.grupo5.services;

import com.eoi.grupo5.entities.EntidadHilo;
import com.eoi.grupo5.entities.EntidadMensaje;
import com.eoi.grupo5.repositories.HiloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HiloService {
    private final HiloRepository hiloRepository;
    private final MensajeService mensajeService;

    @Autowired
    public HiloService(HiloRepository hiloRepository, MensajeService mensajeService) {
        this.hiloRepository = hiloRepository;
        this.mensajeService = mensajeService;
    }

    public EntidadHilo guardarHilo(EntidadHilo hilo) {
        return hiloRepository.save(hilo);
    }

    public Page<EntidadHilo> obtenerHilosRecientesPaginado(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaCreacion").descending());
        return hiloRepository.findAllByOrderByFechaCreacionDesc(pageable);
    }

    public List<EntidadHilo> buscarYOrdenarHilos(String keyword, String sortType,
                                                 UserDetails userDetails,
                                                 boolean misHilos) {
        Sort sortCriteria = buildSortCriteria(sortType);

        if (userDetails == null || !misHilos) {
            // No filtering by user
            if (keyword == null || keyword.trim().isEmpty()) {
                return hiloRepository.findAll(sortCriteria);
            } else {
                return hiloRepository.findByTituloContainingIgnoreCase(keyword, sortCriteria);
            }
        }

        String usuarioUsername = userDetails.getUsername();

        if (keyword == null || keyword.trim().isEmpty()) {
            return hiloRepository.findByAutor_Username(usuarioUsername, sortCriteria);
        } else {
            return hiloRepository.findByAutor_UsernameAndTituloContainingIgnoreCase(usuarioUsername, keyword, sortCriteria);
        }
    }

    public EntidadHilo findById(Long id) {
        return hiloRepository.findById(id).orElse(null);
    }

    public void eliminarHiloYMensajes(Long id) {
        EntidadHilo hilo = hiloRepository.findById(id).orElseThrow(() -> new RuntimeException("Hilo no encontrado"));

        for (EntidadMensaje mensaje : hilo.getMensajes()) {
            mensajeService.deleteMessageById(mensaje.getId());
        }

        hiloRepository.delete(hilo);
    }

    private Sort buildSortCriteria (String sortType) {
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
        return sortCriteria;
    }
}

